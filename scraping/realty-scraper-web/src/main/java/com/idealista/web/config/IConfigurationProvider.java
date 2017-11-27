package com.idealista.web.config;

public interface IConfigurationProvider
{
    BaseSourceConfiguration getSourceConfiguration();

    BaseScraperConfiguration getScraperCofiguration();
}
