package com.idealista.scraper.util;

import org.apache.commons.logging.LogFactory;

import java.util.logging.Level;

public class LoggingUtils
{
    public static void turnOffHtmlUnitWarnings()
    {
        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log",
                "org.apache.commons.logging.impl.NoOpLog");
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit.DefaultCssErrorHandler").setLevel(Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
    }
}
