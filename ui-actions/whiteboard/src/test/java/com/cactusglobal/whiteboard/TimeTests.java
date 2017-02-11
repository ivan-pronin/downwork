package com.cactusglobal.whiteboard;

import com.cactusglobal.whiteboard.util.DateTimeUtil;

import org.junit.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeTests
{

    @Test
    public void testTime()
    {
        String rawDeadlineTime = "Fri, 10 Feb 2017 22:30 GMT";
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime parsedDeadlineTime = LocalDateTime.parse(rawDeadlineTime, DateTimeFormatter.RFC_1123_DATE_TIME);
        System.out.println(parsedDeadlineTime);
        Duration duration = Duration.between(startTime, parsedDeadlineTime);
        System.out.println(duration.toHours());

    }

    @Test
    public void testOngoingDeadline()
    {
        String timeText = "Mon, 2017-02-13 01:30";
        String gmtText = "Fri, 10 Feb 2017 22:30 GMT";
        System.out.println(DateTimeUtil.parseFromGMTString(timeText));
        System.out.println(DateTimeUtil.parseFromGMTString(gmtText));
    }
    
    @Test
    public void testName() throws Exception
    {
        System.out.println("Timestamp: " + DateTimeUtil.getTimeStamp());
    }
}
