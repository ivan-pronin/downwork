package com.idealista.web.config;

import org.springframework.stereotype.Component;

@Component
public class ScraperConfiguration extends BaseScraperConfiguration
{

    public ScraperConfiguration()
    {
    }

    @Override
    public String toString()
    {
        return "ScraperConfiguration [scrapTarget=" + scrapTarget + ", maxAdsToProcess=" + maxAdsToProcess
                + ", newestAdsCount=" + newestAdsCount + ", useProxy=" + useProxy + ", proxySources=" + proxySources
                + ", maxProxyResponseTime=" + maxProxyResponseTime + ", appendXlsTimestamp=" + appendXlsTimestamp
                + ", xlsFileName=" + xlsFileName + ", privateAdsFiltering=" + privateAdsFiltering + ", maxThreads="
                + maxThreads + ", proxy1=" + proxy1 + ", proxy2=" + proxy2 + "]";
    }

}
