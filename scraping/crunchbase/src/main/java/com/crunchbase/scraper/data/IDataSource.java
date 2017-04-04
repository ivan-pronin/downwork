package com.crunchbase.scraper.data;

import java.util.Set;

public interface IDataSource
{
    void writeProxiesToFile(Set<String> proxies);
}
