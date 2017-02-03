package com.cactusglobal.whiteboard;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

public class ScheduledTask extends TimerTask
{
    private static final String RETURN = "\n";

    @Override
    public void run()
    {
        try
        {
            Map<String, String> cookies = new LinkedHashMap<>();
            cookies.put("SESS0b06fabaa8f517c01e97a2b76e6d47ff", "3ns61l2oj5bfk74tjnep5bh442");
            cookies.put("SESS0ce3aec17f2ffdff1b7e12a6c3bc45ba", "3ns61l2oj5bfk74tjnep5bh442");
            cookies.put("has_js", "1");
            cookies.put("AWSALB",
                    "OF39XKDnGcHfkiFrz0qfr7/YT2OhSdyJ8t/87T7Y5Kw4hH2dIgkv8MAWC5jr/2CLI22/ppidp9pKq8u0qAJFfZc7PBQPWbrbxzhrV+gYc1xh1uT8yiGSaPVJzp18");
            System.out.println("Starting to poll the site at: " + getTimeStamp());
            Document doc = Jsoup.connect("http://whiteboard.cactusglobal.com/dashboard").userAgent("chrome")
                    .cookies(cookies).timeout(60000).get();
            System.out.println("Polled site at:               " + getTimeStamp());
            Element allocatedJobs = doc.getElementById("fl_job_allocation");
            Elements allocatedJobsContent = allocatedJobs.getElementsByClass("dashboardBlockContent");
            if (allocatedJobsContent.isEmpty() || !allocatedJobsContent.text().contains("No new jobs assigned"))
            {
//                List<Job> jobs = getJobs(allocatedJobs);
                writeTextToFile(allocatedJobs.html());
//                acceptJobIfMatchCriteria();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void writeTextToFile(String text)
    {
        Timestamp timestamp = getTimeStamp();
        System.out.println("Appending new info at: " + timestamp);
        StringBuilder builder = new StringBuilder();
        builder.append(RETURN).append("========Appending info========").append(RETURN).append(timestamp).append(RETURN)
                .append(text);
        try
        {
            Files.write(FileSystems.getDefault().getPath("logs", "log.txt"), builder.toString().getBytes(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static Timestamp getTimeStamp()
    {
        java.util.Date date = new java.util.Date();
        return new Timestamp(date.getTime());
    }

}
