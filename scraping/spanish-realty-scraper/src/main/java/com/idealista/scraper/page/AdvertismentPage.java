package com.idealista.scraper.page;

import java.util.List;

import com.idealista.scraper.search.SearchActions;
import com.idealista.scraper.util.RegexUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class AdvertismentPage
{
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

    public String getBedrooms()
    {
        return extractSpecificCharacteristic("bedroom");
    }

    public String getBathrooms()
    {
        return extractSpecificCharacteristic("bathroom");
    }

    public int getSize()
    {
        String text = searchActions
                .getElementText(searchActions.findElementsByXpath("//div[@class='info-data']//span[@class='txt-big']"));
        return RegexUtils.extractNumber(text);
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

    private String extractSpecificCharacteristic(String type)
    {
        List<WebElement> ulElements = searchActions
                .findElementsByXpath("//h2[contains(.,'Specific characteristics')]/..//ul//li");
        for (WebElement item : ulElements)
        {
            String text = item.getText().toLowerCase();
            if (text.contains(type))
            {
                return RegexUtils.extractDigit(text);
            }
        }
        return null;
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
                return searchActions.getElementText(p);
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
        List<WebElement> container = findContainer();
        if (!container.isEmpty())
        {
            List<WebElement> advertiser = searchActions.findElementsByXpath(container,
                    "//div[@class='advertiser-name']");
            String text = searchActions.getElementText(advertiser);
            return RegexUtils.extractPostalCode(text);
        }
        return null;

    }
}
