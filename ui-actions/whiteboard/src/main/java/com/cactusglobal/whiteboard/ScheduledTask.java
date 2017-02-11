package com.cactusglobal.whiteboard;

import com.cactusglobal.whiteboard.action.UiActions;
import com.cactusglobal.whiteboard.calculator.CapacityCalculator;
import com.cactusglobal.whiteboard.model.Job;
import com.cactusglobal.whiteboard.model.JobField;
import com.cactusglobal.whiteboard.model.WorkInProgress;
import com.cactusglobal.whiteboard.util.DateTimeUtil;
import com.cactusglobal.whiteboard.util.PropertiesLoader;
import com.cactusglobal.whiteboard.util.WebDriverUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;

public class ScheduledTask extends TimerTask
{
    private static final Logger LOGGER = LogManager.getLogger(Application.class);
    private static final String CLASS = "class";
    private static final String RETURN = "\n";

    private Map<String, String> cookies = new HashMap<>();
    private Set<WorkInProgress> workInProgress = new HashSet<>();
    private boolean isOngoingJobsInitialized;

    @Override
    public void run()
    {
        LOGGER.info("Starting to poll the site ...");
        Document doc = getDashboardDocument();
        LOGGER.info("Polling the site finished ... ");

        workInProgress = initWorkInProgress(doc);

        Element allocatedJobs = doc.getElementById("fl_job_allocation");
        Elements allocatedJobsContent = allocatedJobs.getElementsByClass("dashboardBlockContent");
        if (!allocatedJobsContent.text().contains("No new jobs assigned"))
        {
            System.out.println();
            System.out.println("=======================================");
            LOGGER.info("!!! Found Allocated Jobs! Processing...");
            WebDriverUtil.takeScreenShot();
            List<Job> jobs = getAllocatedJobs(allocatedJobs);
            writeTextToFile(doc.html());
            int dailyCapacity = Integer
                    .parseInt(PropertiesLoader.getProperties().getProperty("dailyWordsCapacity", "2660"));
            CapacityCalculator calculator = new CapacityCalculator(dailyCapacity);
            calculator.setWorkInProgress(workInProgress);
            for (Job job : jobs)
            {
                if (calculator.canTakeJob(job))
                {
                    LOGGER.info("Trying to accept the job: {}", job);
                    UiActions uiActions = new UiActions(WebDriverProvider.getDriverInstance());
                    if (uiActions.acceptJob(job))
                    {
                        workInProgress.add(new WorkInProgress(job));
                        LOGGER.info("Added new job to Ongoing list: {}", job);
                        LOGGER.info("Current ongoing jobs count: {}", workInProgress.size());
                    }
                }
            }
        }
    }

    private Set<WorkInProgress> initWorkInProgress(Document document)
    {
        if (!isOngoingJobsInitialized && workInProgress.isEmpty())
        {
            LOGGER.info("Retrieving Ongoing Jobs list from the page...");
            Set<WorkInProgress> result = new HashSet<>();
            List<Job> jobs = getOngoingJobs(document);
            jobs.forEach(e -> result.add(new WorkInProgress(e)));
            LOGGER.info("Printing Ongoing jobs:");
            jobs.forEach(LOGGER::info);
            isOngoingJobsInitialized = true;
            return result;
        }
        return workInProgress;
    }

    private Document getDashboardDocument()
    {
        try
        {
            return Jsoup.connect(Application.MAIN_PAGE + "/dashboard").userAgent("chrome").cookies(cookies)
                    .timeout(60000).get();
        }
        catch (IOException e)
        {
            LOGGER.error("Error while reading data from dashboard: {}", e);
        }
        return null;
    }

    public List<Job> getOngoingJobs(Document document)
    {
        Element divWrapper = document.getElementById("eteStatusTable");
        List<Element> table = divWrapper.getElementsByAttributeValueContaining("class", "onGoingJobTable ");
        List<Element> tbody = table.get(0).getElementsByTag("tbody");
        List<Element> rows = tbody.get(0).getElementsByTag("tr");
        List<Job> jobs = new ArrayList<>();
        for (Element row : rows)
        {
            List<Element> columns = row.getElementsByTag("td");
            String codeRaw = columns.get(0).text();
            String unitsRaw = columns.get(3).text().split(" ")[0];
            String deadlineRaw = columns.get(4).text();
            LocalDateTime deadline = DateTimeUtil.parseFromGMTString(deadlineRaw);
            jobs.add(new Job(codeRaw, deadline, parseUnits(unitsRaw)));
        }
        return jobs;
    }

    private int parseUnits(String unitsRaw)
    {
        return Integer.parseInt(unitsRaw.replaceAll(",", ""));
    }

    public List<Job> getAllocatedJobs(Element allocatedJobs)
    {
        List<Element> divContents = allocatedJobs.getElementsByClass("view-content");
        if (!divContents.isEmpty())
        {
            List<Job> jobs = new ArrayList<>();
            Element div = divContents.get(0);
            List<Element> table = div.getElementsByTag("table");
            List<Element> tbody = table.get(0).getElementsByTag("tbody");
            List<Element> rows = tbody.get(0).getElementsByTag("tr");
            for (Element row : rows)
            {
                String nameValue = findAndProcessElementByClassName(row, JobField.NAME,
                        "views-field views-field-title");

                String deadlineValue = findAndProcessElementByClassName(row, JobField.DEADLINE,
                        "views-field views-field-phpcode-4");

                String unitsValue = findAndProcessElementByClassName(row, JobField.UNITS,
                        "views-field views-field-phpcode-5");

                LocalDateTime deadline = DateTimeUtil.parseFromGMTString(deadlineValue);
                jobs.add(new Job(nameValue, deadline, parseUnits(unitsValue)));
            }
            LOGGER.info("Printing Allocated Jobs...");
            jobs.forEach(LOGGER::info);
            return jobs;
        }
        return Collections.emptyList();
    }

    private String findAndProcessElementByClassName(Element row, JobField field, String className)
    {
        List<Element> elements = row.getElementsByAttributeValue(CLASS, className);
        return processElements(field, elements);
    }

    private String processElements(JobField field, List<Element> elements)
    {
        if (elements.isEmpty())
        {
            LOGGER.error("Empty elements to parse field type: {} / {}", field, elements);
            return null;
        }
        String result = null;
        switch (field)
        {
            case DEADLINE:
                result = elements.get(0).text().split("GMT")[0];
                break;
            case UNITS:
                result = elements.get(0).text().split(" ")[0];
                break;
            default:
                return elements.get(0).text();
        }
        return result;
    }

    private void writeTextToFile(String text)
    {
        LOGGER.info("Appending HTML code at file ...");
        StringBuilder builder = new StringBuilder();
        builder.append(RETURN).append("======== Appending info ========").append(RETURN)
                .append(DateTimeUtil.getTimeStamp()).append(RETURN).append(text);
        try
        {
            Files.write(FileSystems.getDefault().getPath("logs", "html_src.txt"), builder.toString().getBytes(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }
        catch (IOException e)
        {
            LOGGER.error("Error while writing to file {}", e);
        }
    }

    public void setCookies(Map<String, String> cookies)
    {
        this.cookies = cookies;
    }
}
