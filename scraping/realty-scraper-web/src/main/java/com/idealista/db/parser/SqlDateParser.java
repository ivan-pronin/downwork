package com.idealista.db.parser;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;
import java.util.Calendar;

public class SqlDateParser
{

    public static Date parse(String dateOfListing)
    {
        dateOfListing = dateOfListing.replace(" of", "");
        String[] parts = dateOfListing.split(" ");
        Month m = Month.valueOf(parts[1].toUpperCase());
        LocalDate localDate = LocalDate.of(Calendar.getInstance().get(Calendar.YEAR), m, Integer.parseInt(parts[0]));
        return Date.valueOf(localDate);
    }
}
