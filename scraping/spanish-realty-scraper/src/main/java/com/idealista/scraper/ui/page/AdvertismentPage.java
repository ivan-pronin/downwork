package com.idealista.scraper.ui.page;

import com.idealista.scraper.ui.SearchActions;
import com.idealista.scraper.util.RegexUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdvertismentPage
{
    private static final String M_2 = "m²";
    private static final String BATHROOM = "bathroom";
    private static final String BEDROOM = "bedroom";
    private static final String ENERGY_CERTIFICATION = "Energy certification:";
    private SearchActions searchActions;

    public AdvertismentPage(WebDriver driver)
    {
        searchActions = new SearchActions(driver);
    }

    public String getDescription()
    {
        List<WebElement> container = findContainer();
        if (!container.isEmpty())
        {
            List<WebElement> textElement = searchActions.findElementsByXpath(container,
                    "//div[@class='adCommentsLanguage expandable']");
            return searchActions.getElementText(textElement);
        }
        return null;
    }

    public int getBedrooms()
    {
        return extractSpecificCharacteristics(BEDROOM).isEmpty() ? -1
                : Integer.parseInt(extractSpecificCharacteristics(BEDROOM).get(0));
    }

    public int getBathrooms()
    {
        return extractSpecificCharacteristics(BATHROOM).isEmpty() ? -1
                : Integer.parseInt(extractSpecificCharacteristics(BATHROOM).get(0));
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
        List<WebElement> ulElements = searchActions
                .findElementsByXpath("//h2[contains(.,'Specific characteristics')]/..//ul//li");
        List<String> results = new ArrayList<>();
        for (WebElement item : ulElements)
        {
            String text = item.getText().toLowerCase();
            if (!text.contains(BEDROOM) && !text.contains(BATHROOM) && !text.contains(M_2))
            {
                results.add(text);
            }
        }
        return results;
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
        List<WebElement> ulElements = searchActions
                .findElementsByXpath("//h2[contains(.,'Specific characteristics')]/..//ul//li");
        List<String> results = new ArrayList<>();
        for (WebElement item : ulElements)
        {
            String text = item.getText().toLowerCase();
            if (text.contains(type))
            {
                if (type.equals(BEDROOM) || type.equals(BATHROOM))
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
                    return text.replaceAll("Listing updated on ", "");
                }
            }
        }
        return null;
    }

    public String getEnergyCertification()
    {
        List<WebElement> ulElements = searchActions.findElementsByXpath("//h2[contains(.,'Construction')]/..//ul//li");
        for (WebElement item : ulElements)
        {
            String text = item.getText();
            if (text.contains(ENERGY_CERTIFICATION))
            {
                return RegexUtils.extractTextAfterAnchor(text, ENERGY_CERTIFICATION);
            }
        }
        return null;
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
