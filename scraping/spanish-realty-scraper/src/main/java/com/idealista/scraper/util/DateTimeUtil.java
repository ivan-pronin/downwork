package com.idealista.scraper.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public final class DateTimeUtil
{
    public static String getTimeStamp()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd_HH-mm-ss_SSS");
        return sdf.format(new Timestamp(new java.util.Date().getTime()));
    }
}
