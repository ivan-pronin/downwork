package com.idealista.scraper.ui.page.advertisement;

import com.idealista.scraper.ui.page.BasePage;
import com.idealista.scraper.util.RegexUtils;

import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FotocasaAdvertisementPage extends BasePage
{
    private static final String[] BREADCRUMB_KEYS = {"Province", "State", "City1", "City2"};
    private Map<String, String> breadCrumbsData;
    
    public String getAddress()
    {
        List<WebElement> address = searchActions.findElementsByXpath(
                "//section[@class='section section--noBorder' and contains(.,'Ubicación del inmueble')]"
                + "//div[@class='detail-section-content']");
        return getTextOrNotFound(address);
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
        return TODO;
    }

    public int getBathrooms()
    {
        return getBathBedrooms("litBaths");
    }

    public int getBedrooms()
    {
        return getBathBedrooms("litRooms");
    }

    public String getCity()
    {
        String city1 = extractBreadcrumbParts().get(BREADCRUMB_KEYS[2]);
        String city2 = extractBreadcrumbParts().get(BREADCRUMB_KEYS[3]);
        if (city1 == null)
        {
            return INFO_NOT_FOUND;
        }
        return city1 + " > " + city2 == null ? "" : city2;
    }

    public String getDescription()
    {
        List<WebElement> description = searchActions.findElementsByXpath("//div[@class='detail-section-content']/p");
        return getTextOrNotFound(description);
    }

    public String getEnergyCertification()
    {
        List<WebElement> energy = searchActions.findElementsByXpath("//div[@id='energySection']//p");
        return getTextOrNotFound(energy);
    }

    public String getListingAgent()
    {
        List<WebElement> dataCell = searchActions.findElementsById("lnkMoreBuildings");
        if (!dataCell.isEmpty())
        {
            return getOwnerText(searchActions.getElementText(dataCell));
        }
        dataCell = searchActions.findElementsById("ctl00_ucInfoRequestGeneric_divAdvertisement");
        if (!dataCell.isEmpty())
        {
            return getOwnerText(searchActions.getElementText(dataCell));
        }
        return INFO_NOT_FOUND;
    }

    public String getListingDate()
    {
        return NA_FOR_THIS_SITE;
    }

    public String getNumberOfViews()
    {
        return NA_FOR_THIS_SITE;
    }

    public String getPostalCode()
    {
        return NA_FOR_THIS_SITE;
    }

    public String getPrice()
    {
        String text = searchActions.getElementText(searchActions.findElementsById("priceContainer"));
        if (text != null && text.contains("Precio"))
        {
            return text;
        }
        return "" + RegexUtils.extractBigNumber(text);
    }

    public String getProfessional()
    {
        List<WebElement> moreLinks = searchActions.findElementsById("lnkMoreBuildings");
        return moreLinks.isEmpty() ? PRIVATE : PROFESSIONAL;
    }

    public String getProvince()
    {
        return textOrNotFound(extractBreadcrumbParts().get(BREADCRUMB_KEYS[0]));
    }

    private String textOrNotFound(String value)
    {
        return value != null ? value : INFO_NOT_FOUND;
    }

    public String getSize()
    {
        String text = searchActions.getElementText(searchActions.findElementsById("ctl00_liSurface"));
        int size = RegexUtils.extractNumber(text);
        return size > 0 ? "" + size : INFO_NOT_FOUND;
    }

    public String getState()
    {
        return textOrNotFound(extractBreadcrumbParts().get(BREADCRUMB_KEYS[1]));
    }

    public String getSubType()
    {
        return NA_FOR_THIS_SITE;
    }

    public List<String> getTags()
    {
        List<WebElement> container = searchActions.findElementsById("divFeatures");
        List<String> results = new ArrayList<>();
        for (WebElement item : searchActions.findElementsByXpath(container, "//ul[@class='detail-extras']//li"))
        {
            results.add(item.getText());
        }
        return results;
    }

    public String getTitle()
    {
        List<WebElement> h1Title = searchActions.findElementsByXpath("//h1[@class='property-title']");
        if (!h1Title.isEmpty())
        {
            return searchActions.getElementText(h1Title);
        }
        return null;
    }

    public String getType()
    {
        List<WebElement> element = searchActions.findElementsById("ctl00_promotionName");
        if (!element.isEmpty())
        {
            return searchActions.getElementText(element);
        }
        List<WebElement> characteristics = searchActions
                .findElementsById("ctl00_rptMainFeaturesLeft_ctl01_litFeatures");
        String text = searchActions.getElementText(characteristics);
        if (text != null)
        {
            return text.replace("Tipo de inmueble : ", "");
        }
        return INFO_NOT_FOUND;
    }

    public boolean hasImages()
    {
        return !searchActions.findElementsByXpath("//ul[@id='containerSlider']//img").isEmpty();
    }

    public void scrollToTheBottom()
    {
        clickActions.scrollToTheBottom();
    }

    private Map<String, String> extractBreadcrumbParts()
    {
        if (breadCrumbsData == null)
        {
            breadCrumbsData = new HashMap<>();
            List<WebElement> breadCrumbs = searchActions.findElementsByXpath("//ul[@class='breadcrumb-classic']//li");
            int size = breadCrumbs.size();
            if (!breadCrumbs.isEmpty() && size > 3)
            {
                for (int i = 4; i < size; i++)
                {
                    breadCrumbsData.put(BREADCRUMB_KEYS[i-4], breadCrumbs.get(i).getText());
                }
            }
        }
        return breadCrumbsData;
    }

    private int getBathBedrooms(String id)
    {
        String text = searchActions.getElementText(searchActions.findElementsById(id));
        int bedrooms = RegexUtils.extractDigit(text);
        return bedrooms > 0 ? bedrooms : 0;
    }

    private String getOwnerText(String text)
    {
        if (text != null)
        {
            return text.replaceAll("Contacto: ", "");
        }
        return INFO_NOT_FOUND;
    }

    private String getTextOrNotFound(List<WebElement> element)
    {
        String text = searchActions.getElementText(element);
        return text == null ? INFO_NOT_FOUND : text;
    }
}
