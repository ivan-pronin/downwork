package com.idealista.db;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

import org.junit.Test;

public class DateTest
{
    @Test
    public void t()
    {
        String actualDate = "2016-03-20";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("MMM yyyy", Locale.ENGLISH);
        LocalDate ld = LocalDate.parse(actualDate, dtf);
        String month_name = dtf2.format(ld);
        System.out.println(month_name); // Mar 2016
    }

    // @Test
    public void t2()
    {
        String actualDate = "7 November";
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("dd MMM", Locale.ENGLISH);
        MonthDay md = MonthDay.parse(actualDate, dtf2);
        System.out.println("MD: " + md);
        // LocalDate ld = LocalDate.parse(actualDate, dtf2).withYear(2017);
        // String month_name = dtf2.format(ld);
        // System.out.println(month_name); // Mar 2016
    }

    @Test
    public void t3()
    {
        String actualDate = "7 November";
        String[] parts = actualDate.split(" ");
        Month m = Month.valueOf(parts[1].toUpperCase());
        MonthDay md = MonthDay.of(m, Integer.parseInt(parts[0]));
        LocalDate date = LocalDate.of(Calendar.getInstance().get(Calendar.YEAR), m, Integer.parseInt(parts[0]));
        System.out.println("MD: " + md);
        System.out.println("дв: " + date);
        java.sql.Date sqlDate = Date.valueOf(date);
        System.out.println("sql: " + sqlDate);
    }
}
