package com.idealista.scraper.util;

import java.util.logging.Level;

import org.apache.commons.logging.LogFactory;

public final class LoggingUtils
{
    public static void turnOffHtmlUnitWarnings()
    {
        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log",
                "org.apache.commons.logging.impl.NoOpLog");
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit.DefaultCssErrorHandler").setLevel(Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
    }
}
