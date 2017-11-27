package com.idealista.web.config;

import org.springframework.stereotype.Component;

@Component
public class ConfigurationProvider implements IConfigurationProvider
{

    private BaseSourceConfiguration sourceConfiguration;
    private BaseScraperConfiguration scraperConfiguration;

    @Override
    public BaseSourceConfiguration getSourceConfiguration()
    {
        return sourceConfiguration;
    }

    @Override
    public BaseScraperConfiguration getScraperCofiguration()
    {
        return scraperConfiguration;
    }

    public BaseScraperConfiguration getScraperConfiguration()
    {
        return scraperConfiguration;
    }

    public void setScraperConfiguration(BaseScraperConfiguration scraperConfiguration)
    {
        this.scraperConfiguration = scraperConfiguration;
    }

    public void setSourceConfiguration(BaseSourceConfiguration sourceConfiguration)
    {
        this.sourceConfiguration = sourceConfiguration;
    }
}
