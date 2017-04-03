package com.idealista.scraper.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public final class DateTimeUtils
{
    public static String getTimestamp()
    {
        return getFormattedTimestamp(new SimpleDateFormat("(dd-MM-yyyy)_(HH-mm-ss)_SSS"));
    }

    public static String getFilenameTimestamp()
    {
        return getFormattedTimestamp(new SimpleDateFormat("(dd-MM-yyyy)"));
    }
    
    public static String getScrapTimestamp()
    {
        return getFormattedTimestamp(new SimpleDateFormat("HH:mm dd/MM/yy"));
    }

    private static String getFormattedTimestamp(SimpleDateFormat sdf)
    {
        return sdf.format(new Timestamp(new java.util.Date().getTime()));
    }
}
