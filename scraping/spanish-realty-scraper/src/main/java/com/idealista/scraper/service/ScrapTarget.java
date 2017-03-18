package com.idealista.scraper.service;

public enum ScrapTarget
{
    VIBBO("http://www.vibbo.com/pisos-y-casas/")
    {
        @Override
        public String getMainPageLocalizedUrl(String language)
        {
            throw new UnsupportedOperationException("Property <language> is not applicable to VIBBO");
        }
    },
    IDEALISTA("https://www.idealista.com/")
    {
        @Override
        public String getMainPageLocalizedUrl(String language)
        {
            String localizationSuffix = "";
            if (language.equalsIgnoreCase("en"))
            {
                localizationSuffix = "en/";
            }
            return getMainPageUrl() + localizationSuffix;
        }
    };

    private String mainPageUrl;

    private ScrapTarget(String mainPageUrl)
    {
        this.mainPageUrl = mainPageUrl;
    }

    public String getMainPageUrl()
    {
        return mainPageUrl;
    }

    public abstract String getMainPageLocalizedUrl(String language);
}