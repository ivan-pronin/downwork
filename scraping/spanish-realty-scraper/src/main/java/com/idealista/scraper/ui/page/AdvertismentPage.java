package com.idealista.scraper.ui.page;

import com.idealista.scraper.RealtyApp;
import com.idealista.scraper.util.RegexUtils;

import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class AdvertismentPage extends BasePage
{
    private static final String M_2 = "m²";
    private static final String EN = "en";
    private static final String ES = "es";

    @Autowired
    private RealtyApp realtyApp;

    public String getDescription()
    {
        List<WebElement> container = findContainer();
        if (!container.isEmpty())
        {
            List<WebElement> textElement = searchActions.findElementsByXpath(container,
                    "//div[@class='adCommentsLanguage expandable']");
            List<WebElement> expander = searchActions.findElementsByXpath(textElement, "//a[@class='expander']");
            clickActions.click(expander);
            return searchActions.getElementText(textElement);
        }
        return null;
    }

    public int getBedrooms()
    {
        String bedroom = getLocalizedBedroom();
        return extractSpecificCharacteristics(bedroom).isEmpty() ? -1
                : Integer.parseInt(extractSpecificCharacteristics(bedroom).get(0));
    }

    public int getBathrooms()
    {
        String bathroom = getLocalizedBathroom();
        return extractSpecificCharacteristics(bathroom).isEmpty() ? -1
                : Integer.parseInt(extractSpecificCharacteristics(bathroom).get(0));
    }

    private String getLocalizedBathroom()
    {
        String language = getLanguage();
        if (language.equalsIgnoreCase(EN))
        {
            return "bathroom";
        }
        if (language.equalsIgnoreCase(ES))
        {
            return "baño";
        }
        return null;
    }

    private String getLocalizedBedroom()
    {
        String language = getLanguage();
        if (language.equalsIgnoreCase(EN))
        {
            return "bedroom";
        }
        if (language.equalsIgnoreCase(ES))
        {
            return "habitacion";
        }
        return null;
    }

    public String getSize()
    {
        String text = searchActions.getElementText(
                searchActions.findElementsByXpath("//div[@class='info-data']//span[@class='txt-big']/.."));
        if (text == null || !text.contains(M_2))
        {
            return extractSpecificCharacteristics(M_2).toString();
        }
        return "" + RegexUtils.extractNumber(text);
    }

    public List<String> getTags()
    {
        List<WebElement> ulElements = findSpecificCharactefisticsList();
        List<String> results = new ArrayList<>();
        for (WebElement item : ulElements)
        {
            String text = item.getText().toLowerCase();
            if (!text.contains(getLocalizedBedroom()) && !text.contains(getLocalizedBathroom()) && !text.contains(M_2))
            {
                results.add(text);
            }
        }
        return results;
    }

    private List<WebElement> findSpecificCharactefisticsList()
    {
        String xpath = "//h2[contains(.,'%s')]/..//ul//li";
        return searchActions.findElementsByXpath(String.format(xpath, getLocalizedSpecificCharacteristics()));
    }

    private String getLocalizedSpecificCharacteristics()
    {
        String language = getLanguage();
        if (language.equalsIgnoreCase(EN))
        {
            return "Specific characteristics";
        }
        if (language.equalsIgnoreCase(ES))
        {
            return "Características básicas";
        }
        return null;
    }

    public int getPrice()
    {
        String text = searchActions.getElementText(
                searchActions.findElementsByXpath("//div[@class='info-data']//span[@class='txt-big txt-bold']"));
        return RegexUtils.extractBigNumber(text);
    }

    public String getListingAgent()
    {
        return searchActions
                .getElementText(searchActions.findElementsByXpath("//div[contains(@class,'advertiser-data')]"));
    }

    public boolean hasImages()
    {
        return searchActions.findElementsByXpath("//div[@id='main-multimedia']//img").size() > 0;
    }

    private List<String> extractSpecificCharacteristics(String type)
    {
        List<WebElement> ulElements = findSpecificCharactefisticsList();
        List<String> results = new ArrayList<>();
        for (WebElement item : ulElements)
        {
            String text = item.getText().toLowerCase();
            if (text.contains(type))
            {
                if (type.equals(getLocalizedBedroom()) || type.equals(getLocalizedBathroom()))
                {
                    return Arrays.asList("" + RegexUtils.extractDigit(text));
                }
                results.add(text);
            }
        }
        return results;
    }

    public String getTitle()
    {
        List<WebElement> container = findContainer();
        if (!container.isEmpty())
        {
            List<WebElement> textElement = searchActions.findElementsByTagName(container, "h1");
            if (!textElement.isEmpty())
            {
                return textElement.get(0).getText();
            }
        }
        return null;
    }

    private List<WebElement> findContainer()
    {
        return searchActions.findElementsByXpath("//div[@class='container']");
    }

    public String getListingDate()
    {
        List<WebElement> container = findContainer();
        if (!container.isEmpty())
        {
            List<WebElement> stats = searchActions.findElementsById(container, "stats");
            if (!stats.isEmpty())
            {
                List<WebElement> p = searchActions.findElementsByTagName(stats, "p");
                String text = searchActions.getElementText(p);
                if (text != null)
                {
                    return text.replaceAll(getLocalizedListingUpdatedText(), "");
                }
            }
        }
        return null;
    }

    private String getLocalizedListingUpdatedText()
    {
        String language = getLanguage();
        if (language.equalsIgnoreCase(EN))
        {
            return "Listing updated on ";
        }
        if (language.equalsIgnoreCase(ES))
        {
            return "Anuncio actualizado el ";
        }
        return null;
    }

    public String getEnergyCertification()
    {
        String xpath = "//h2[contains(.,'%s')]/..//ul//li";
        List<WebElement> ulElements = searchActions
                .findElementsByXpath(String.format(xpath, getLocalizedConstruction()));
        for (WebElement item : ulElements)
        {
            String text = item.getText();
            String energyCertification = getLocalizedEnergyCert();
            if (text.contains(energyCertification))
            {
                return RegexUtils.extractTextAfterAnchor(text, energyCertification);
            }
        }
        return null;
    }

    private String getLocalizedEnergyCert()
    {
        String language = getLanguage();
        if (language.equalsIgnoreCase(EN))
        {
            return "Energy certification:";
        }
        if (language.equalsIgnoreCase(ES))
        {
            return "Certificación energética:";
        }
        return null;
    }

    private String getLocalizedConstruction()
    {
        String language = getLanguage();
        if (language.equalsIgnoreCase(EN))
        {
            return "Construction";
        }
        if (language.equalsIgnoreCase(ES))
        {
            return "Edificio";
        }
        return null;
    }

    private String getLanguage()
    {
        return realtyApp.getLanguage();
    }

    public String getAddress()
    {
        List<WebElement> container = findContainer();
        if (!container.isEmpty())
        {
            List<WebElement> address = searchActions.findElementsById(container, "addressPromo");
            if (!address.isEmpty())
            {
                List<WebElement> ul = searchActions.findElementsByTagName(address, "ul");
                return searchActions.getElementText(ul);
            }
        }
        return null;
    }

    public String getPostalCode()
    {
        return RegexUtils.extractPostalCode(getAdvertizerText());
    }

    private String getAdvertizerText()
    {
        List<WebElement> container = findContainer();
        if (!container.isEmpty())
        {
            return searchActions
                    .getElementText(searchActions.findElementsByXpath(container, "//div[@class='advertiser-name']"));
        }
        return null;
    }

    public String getCity()
    {
        return RegexUtils.extractTextBetweenTwoNumbers(getAdvertizerText());
    }

    public String getProfessional()
    {
        String listingAgent = getListingAgent();
        if (listingAgent != null)
        {
            return listingAgent.toLowerCase().contains("profes") ? "Professional" : "Private";
        }
        return null;
    }

    public String getAgentPhone()
    {
        return searchActions.getElementText(searchActions.findElementsByXpath("//div[@class='phone first-phone']"));
    }
}
