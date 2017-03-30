package com.crunchbase.scraper.webdriver.proxy;

public interface IProxyProvider
{
    ProxyAdapter getNextWorkingProxy();
}
