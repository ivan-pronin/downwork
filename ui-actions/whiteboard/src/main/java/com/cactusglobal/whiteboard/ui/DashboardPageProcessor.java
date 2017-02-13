package com.cactusglobal.whiteboard.ui;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.cactusglobal.whiteboard.action.DocumentSearchActions;
import com.cactusglobal.whiteboard.model.Job;
import com.cactusglobal.whiteboard.model.JobField;
import com.cactusglobal.whiteboard.util.DateTimeUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class DashboardPageProcessor
{
    private static final Logger LOGGER = LogManager.getLogger(DashboardPageProcessor.class);

    private DocumentSearchActions documentSearchActions;

    public DashboardPageProcessor(Document dashBoardDocument)
    {
        documentSearchActions = new DocumentSearchActions(dashBoardDocument);
    }

    public List<Job> getAllocatedJobs()
    {
        Element allocatedJobs = getAllocatedJobsContainer();
        List<Element> divContents = documentSearchActions.getElementsByClassName(allocatedJobs, "view-content");
        if (!divContents.isEmpty())
        {
            List<Job> jobs = new ArrayList<>();
            List<Element> table = documentSearchActions.getElementsByTag(divContents, "table");
            List<Element> rows = getTableRows(table);
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

    public List<Job> getOngoingJobs()
    {
        Element divWrapper = documentSearchActions.getElementById("eteStatusTable");
        List<Element> table = documentSearchActions.getElementsByClassContainingValue(Arrays.asList(divWrapper),
                "onGoingJobTable ");
        List<Element> rows = getTableRows(table);
        List<Job> jobs = new ArrayList<>();
        for (Element row : rows)
        {
            List<Element> columns = documentSearchActions.getElementsByTag(Arrays.asList(row), "td");
            String codeRaw = documentSearchActions.getElementText(columns, 0);
            String unitsRaw = extractUnits(documentSearchActions.getElementText(columns, 3));
            String deadlineRaw = documentSearchActions.getElementText(columns, 4);
            LocalDateTime deadline = DateTimeUtil.parseFromGMTString(deadlineRaw);
            jobs.add(new Job(codeRaw, deadline, parseUnits(unitsRaw)));
        }
        return jobs;
    }

    public boolean isNewAllocatedJobAppeared()
    {
        Element allocatedJobs = getAllocatedJobsContainer();
        List<Element> allocatedJobsContent = documentSearchActions.getElementsByClassName(allocatedJobs,
                "dashboardBlockContent");
        if (!allocatedJobsContent.isEmpty())
        {
            return !allocatedJobsContent.get(0).text().contains("No new jobs assigned");
        }
        return false;
    }

    private String extractUnits(String text)
    {
        if (StringUtils.isEmpty(text))
        {
            return StringUtils.EMPTY;
        }
        return text.split(" ")[0];
    }

    private String findAndProcessElementByClassName(Element row, JobField field, String className)
    {
        List<Element> element = documentSearchActions.getElementsByClassName(row, className);
        return getElementTextByField(field, element);
    }

    private Element getAllocatedJobsContainer()
    {
        return documentSearchActions.getElementById("fl_job_allocation");
    }

    private String getElementTextByField(JobField field, List<Element> rootElement)
    {
        if (rootElement.isEmpty())
        {
            return null;
        }
        Element element = rootElement.get(0);
        switch (field)
        {
            case DEADLINE:
                return element.text().split("GMT")[0];
            case UNITS:
                return element.text().split(" ")[0];
            default:
                return element.text();
        }
    }

    private List<Element> getTableRows(List<Element> table)
    {
        return documentSearchActions.getElementsByTag(documentSearchActions.getElementsByTag(table, "tbody"), "tr");
    }

    private int parseUnits(String unitsRaw)
    {
        return Integer.parseInt(unitsRaw.replaceAll(",", ""));
    }
}
