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

        @Override
        public String getNormalPageElementXpath()
        {
            return "//*[@id='generalContent']";
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

        @Override
        public String getNormalPageElementXpath()
        {
            return "//*[@id='no-login-user-bar']";
        }
    },
    PISOS("https://www.pisos.com/")
    {
        @Override
        public String getMainPageLocalizedUrl(String language)
        {
            throw new UnsupportedOperationException("Property <language> is not applicable to PISOS");
        }

        @Override
        public String getNormalPageElementXpath()
        {
            return "//*[@id='headerUser']";
        }
    },
    FOTOCASA("http://www.fotocasa.es/es/")
    {
        @Override
        public String getMainPageLocalizedUrl(String language)
        {
            throw new UnsupportedOperationException("Property <language> is not applicable to FOTOCASA");
        }

        @Override
        public String getNormalPageElementXpath()
        {
            return "//div[@class='item-Background']";
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

    public abstract String getNormalPageElementXpath();
}
