package com.idealista.web.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.idealista.scraper.service.ScrapTarget;

@Component
public class BaseScraperConfiguration
{

    public BaseScraperConfiguration()
    {

    }

    protected ScrapTarget scrapTarget = ScrapTarget.IDEALISTA;
    protected int maxAdsToProcess = 10;
    protected int newestAdsCount;
    protected boolean useProxy;
    protected List<Integer> proxySources = new ArrayList<>();
    protected int maxProxyResponseTime = 8000;
    protected boolean appendXlsTimestamp;
    protected String xlsFileName = "defaultXlsFileName.xls";
    protected boolean privateAdsFiltering;
    protected int maxThreads = 2;
    protected boolean proxy1;
    protected boolean proxy2;
    private String driverType = "chrome";
    private boolean useLightweightedChrome = true;
    private boolean javascriptEnabled = true;
    private boolean useChromeService = true;
    private boolean maximizeBrowserWindow;

    public ScrapTarget getScrapTarget()
    {
        return scrapTarget;
    }

    public void setScrapTarget(ScrapTarget scrapTarget)
    {
        this.scrapTarget = scrapTarget;
    }

    public boolean isUseProxy()
    {
        return useProxy;
    }

    public void setUseProxy(boolean useProxy)
    {
        this.useProxy = useProxy;
    }

    public List<Integer> getProxySources()
    {
        if (proxy1)
        {
            proxySources.add(1);
        }
        if (proxy2)
        {
            proxySources.add(2);
        }
        return proxySources;
    }

    public void setProxySources(List<Integer> proxySources)
    {
        this.proxySources = proxySources;
    }

    public int getMaxProxyResponseTime()
    {
        return maxProxyResponseTime;
    }

    public void setMaxProxyResponseTime(String maxProxyResponseTime)
    {
        if (maxProxyResponseTime.isEmpty())
        {
            return;
        }
        this.maxProxyResponseTime = Integer.parseInt(maxProxyResponseTime);
    }

    public int getMaxAdsToProcess()
    {
        return maxAdsToProcess;
    }

    public void setMaxAdsToProcess(String maxAdsToProcess)
    {
        if (maxAdsToProcess.isEmpty())
        {
            return;
        }
        this.maxAdsToProcess = Integer.parseInt(maxAdsToProcess);
    }

    public int getNewestAdsCount()
    {
        return newestAdsCount;
    }

    public void setNewestAdsCount(String newestAdsCount)
    {
        if (newestAdsCount.isEmpty())
        {
            return;
        }
        this.newestAdsCount = Integer.parseInt(newestAdsCount);
    }

    public String getXlsFileName()
    {
        return xlsFileName;
    }

    public void setXlsFileName(String xlsFileName)
    {
        this.xlsFileName = xlsFileName;
    }

    public boolean isPrivateAdsFiltering()
    {
        return privateAdsFiltering;
    }

    public void setPrivateAdsFiltering(boolean privateAdsFiltering)
    {
        this.privateAdsFiltering = privateAdsFiltering;
    }

    public int getMaxThreads()
    {
        return maxThreads;
    }

    public void setMaxThreads(int maxThreads)
    {
        this.maxThreads = maxThreads;
    }

    public boolean isProxy1()
    {
        return proxy1;
    }

    public void setProxy1(boolean proxy1)
    {
        this.proxy1 = proxy1;
    }

    public boolean isProxy2()
    {
        return proxy2;
    }

    public void setProxy2(boolean proxy2)
    {
        this.proxy2 = proxy2;
    }

    public boolean isAppendXlsTimestamp()
    {
        return appendXlsTimestamp;
    }

    public void setAppendXlsTimestamp(boolean appendXlsTimestamp)
    {
        this.appendXlsTimestamp = appendXlsTimestamp;
    }

    public String getDriverType()
    {
        return driverType;
    }

    public boolean isUseLightweightedChrome()
    {
        return useLightweightedChrome;
    }

    public boolean isJavascriptEnabled()
    {
        return javascriptEnabled;
    }

    public boolean useChromeService()
    {
        return useChromeService;
    }

    public boolean maximizeBrowserWindow()
    {
        return maximizeBrowserWindow;
    }

}
