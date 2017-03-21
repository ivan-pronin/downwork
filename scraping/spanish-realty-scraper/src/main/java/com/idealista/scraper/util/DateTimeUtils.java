package com.idealista.scraper.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public final class DateTimeUtils
{
    public static String getTimeStamp()
    {
        return getFormattedTimestamp(new SimpleDateFormat("yyyy.MM.dd_HH-mm-ss_SSS"));
    }

    public static String getTimestampFoXls()
    {
        return getFormattedTimestamp(new SimpleDateFormat("HH:mm dd/MM/yy"));
    }

    private static String getFormattedTimestamp(SimpleDateFormat sdf)
    {
        return sdf.format(new Timestamp(new java.util.Date().getTime()));
    }
}
