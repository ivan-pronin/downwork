package com.idealista.web.config.cloner;

import com.idealista.web.config.BaseScraperConfiguration;

public final class BaseScraperConfigCloner
{
    public static void cloneScraperConfiguration(BaseScraperConfiguration src, BaseScraperConfiguration dest)
    {
        dest.setAppendXlsTimestamp(src.isAppendXlsTimestamp());
        dest.setMaxAdsToProcess("" + src.getMaxAdsToProcess());
        dest.setMaxProxyResponseTime("" + src.getMaxProxyResponseTime());
        dest.setMaxThreads(src.getMaxThreads());
        dest.setNewestAdsCount("" + src.getNewestAdsCount());
        dest.setPrivateAdsFiltering(src.isPrivateAdsFiltering());
        dest.setProxy1(src.isProxy1());
        dest.setProxy2(src.isProxy2());
        dest.setUseProxy(src.isUseProxy());
        dest.setProxySources(src.getProxySources());
        dest.setScrapTarget(src.getScrapTarget());
        dest.setXlsFileName(src.getXlsFileName());
    }
}
