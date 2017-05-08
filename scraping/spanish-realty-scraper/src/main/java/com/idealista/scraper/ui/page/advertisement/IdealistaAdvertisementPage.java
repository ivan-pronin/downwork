package com.idealista.scraper.ui.page.advertisement;

import com.idealista.scraper.ui.page.BasePage;
import com.idealista.scraper.util.RegexUtils;

import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IdealistaAdvertisementPage extends BasePage
{
    private static final String SECTION_H2_TEXT_LOCATOR = "//h2[contains(.,'%s')]/..//ul//li";
    private static final String M_2 = "m²";
    private static final String EN = "en";
    private static final String ES = "es";
    private String language;

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

    public String getAge()
    {
        return NA_FOR_THIS_SITE;
    }

    public String getAgentEmail()
    {
        return NA_FOR_THIS_SITE;
    }

    public String getAgentPhone()
    {
        return searchActions.getElementText(searchActions.findElementsByXpath("//div[@class='phone first-phone']"));
    }

    public int getBathrooms()
    {
        String bathroom = getLocalizedBathroom();
        if (extractSpecificCharacteristics(bathroom).isEmpty())
        {
            return 0;
        }
        int bathrooms = Integer.parseInt(extractSpecificCharacteristics(bathroom).get(0));
        return bathrooms > 0 ? bathrooms : 0;
    }

    public int getBedrooms()
    {
        String bedroom = getLocalizedBedroom();
        if (extractSpecificCharacteristics(bedroom).isEmpty())
        {
            return 0;
        }
        int bedrooms = Integer.parseInt(extractSpecificCharacteristics(bedroom).get(0));
        return bedrooms > 0 ? bedrooms : 0;
    }

    public String getCity()
    {
        return RegexUtils.extractTextBetweenTwoNumbers(getAdvertizerText());
    }

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

    public String getEnergyCertification()
    {
        String xpath = SECTION_H2_TEXT_LOCATOR;
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

    public String getListingAgent()
    {
        return searchActions
                .getElementText(searchActions.findElementsByXpath("//div[contains(@class,'advertiser-data')]"));
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

    public String getNumberOfViews()
    {
        return NA_FOR_THIS_SITE;
    }

    public String getPostalCode()
    {
        return RegexUtils.extractPostalCode(getAdvertizerText());
    }

    public String getPrice()
    {
        String text = searchActions.getElementText(
                searchActions.findElementsByXpath("//div[@class='info-data']//span[@class='txt-big txt-bold']"));
        return "" + RegexUtils.extractBigNumber(text);
    }

    public String getProfessional()
    {
        String listingAgent = getListingAgent();
        return isProfessional(listingAgent);
    }

    public String getSize()
    {
        String text = searchActions.getElementText(
                searchActions.findElementsByXpath("//div[@class='info-data']//span[@class='txt-big']/.."));
        if (text == null || !text.contains(M_2))
        {
            return extractSpecificCharacteristics(M_2).toString();
        }
        int size = RegexUtils.extractBigNumber(text);
        return size > 0 ? "" + size : INFO_NOT_FOUND;
    }

    public List<String> getTags()
    {
        List<WebElement> ulElements = findSpecificCharacteristicsList();
        ulElements.addAll(findConstructionCharactefisticsList());
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

    public boolean hasImages()
    {
        return !searchActions.findElementsByXpath("//div[@id='main-multimedia']//img").isEmpty();
    }

    public void setLanguage(String language)
    {
        this.language = language;
    }

    private List<String> extractSpecificCharacteristics(String type)
    {
        List<WebElement> ulElements = findSpecificCharacteristicsList();
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

    private List<WebElement> findContainer()
    {
        return searchActions.findElementsByXpath("//div[@class='container']");
    }

    private List<WebElement> findConstructionCharactefisticsList()
    {
        return searchActions.findElementsByXpath(String.format(SECTION_H2_TEXT_LOCATOR, getLocalizedConstruction()));
    }

    private List<WebElement> findSpecificCharacteristicsList()
    {
        return searchActions
                .findElementsByXpath(String.format(SECTION_H2_TEXT_LOCATOR, getLocalizedSpecificCharacteristics()));
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

    private String getLanguage()
    {
        return language;
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
}
