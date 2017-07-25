package com.facebook.javatest.util;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import com.facebook.javatest.model.Group;

public final class PrintUtils
{
    private PrintUtils()
    {
    }
    
    public static void printGroupsToConsole(List<Group> groups)
    {
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.getDefault());
        String outputFormat = "| %-25.25s | %-25.25s | %10.10s | %-25.25s |%n";
        System.out.format(
                "+---------------------------+---------------------------+------------+---------------------------+%n");
        System.out.format(
                "| Name                      | ID                        | Members    | Button ID                 |%n");
        System.out.format(
                "+---------------------------+---------------------------+------------+---------------------------+%n");
        groups.stream().forEach(group -> System.out.format(outputFormat, group.getName(), group.getId(),
                numberFormat.format(group.getMembers()), group.getButtonId()));
        System.out.format(
                "+---------------------------+---------------------------+------------+---------------------------+%n");

    }

}
