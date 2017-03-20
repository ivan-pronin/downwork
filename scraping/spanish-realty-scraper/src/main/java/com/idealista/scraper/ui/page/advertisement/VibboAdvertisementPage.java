package com.idealista.scraper.ui.page.advertisement;

import com.idealista.scraper.ui.page.BasePage;
import com.idealista.scraper.util.RegexUtils;

import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class VibboAdvertisementPage extends BasePage
{
    public String getAddress()
    {
        String[] parts = getAddressParts();
        return parts.length > 0 ? parts[0] : null;
    }

    public String getAge()
    {
        return INFO_NOT_PRESENT;
    }

    public String getAgentEmail()
    {
        return INFO_NOT_PRESENT;
    }

    public String getAgentPhone()
    {
        return TODO;
    }

    public int getBathrooms()
    {
        String text = getDescription();
        String match = RegexUtils.getDigitStringOccurenceInText("baños", text);
        int bathrooms = RegexUtils.extractDigit(match);
        return bathrooms > 0 ? bathrooms : 0;
    }

    public int getBedrooms()
    {
        String text = findBedroomsTechInfo();
        int bedrooms = RegexUtils.extractDigit(text);
        return bedrooms > 0 ? bedrooms : 0;
    }

    public String getCity()
    {
        return getAddress();
    }

    public String getDescription()
    {
        return searchActions.getElementText(searchActions.findElementsById("descriptionText"));
    }

    public String getEnergyCertification()
    {
        return INFO_NOT_PRESENT;
    }

    public String getListingAgent()
    {
        String professionalAgent = searchActions.getElementTextByXpath("//div[@class='StoreName']");
        if (professionalAgent == null)
        {
            return searchActions.getElementTextByXpath("//div[@class='sellerBox__info__name']");
        }
        return professionalAgent;
    }

    public String getListingDate()
    {
        String text = searchActions.getElementTextByXpath("//div[@class='ad_date']");
        String[] parts = text.split(": ");
        return parts.length > 1 ? parts[1] : null;
    }

    public String getNumberOfViews()
    {
        return "" + RegexUtils.extractNumber(searchActions.getElementTextByXpath("//p[@class='TimesSeen']"));
    }

    public String getPostalCode()
    {
        String[] parts = getAddressParts();
        if (parts.length > 1)
        {
            return RegexUtils.replaceAllNonDigitCharacters(parts[1]);
        }
        return null;
    }

    public int getPrice()
    {
        String priceText = searchActions.getElementTextByXpath("//span[@class='price']");
        return RegexUtils.extractBigNumber(priceText);
    }

    public String getProfessional()
    {
        String text = searchActions.getElementTextByXpath("//div[@class='user_type_label  ']");
        String professional = isProfessional(text);
        if (professional == null)
        {
            return PRIVATE;
        }
        return professional;
    }

    public String getProvince()
    {
        String[] breadCrumbParts = extractBreadcrumbParts();
        return breadCrumbParts.length > 6 ? breadCrumbParts[5] + " > " + breadCrumbParts[5] : INFO_NOT_FOUND;
    }

    public String getSize()
    {
        String sizeText = findSizeTechInfo();
        int size = RegexUtils.extractNumber(sizeText);
        return size > 0 ? "" + size : INFO_NOT_FOUND;
    }

    public String getState()
    {
        return INFO_NOT_PRESENT;
    }

    public String getSubType()
    {
        String[] breadCrumbParts = extractBreadcrumbParts();
        return breadCrumbParts.length > 3 ? breadCrumbParts[3] : INFO_NOT_FOUND;
    }

    public List<String> getTags()
    {
        List<WebElement> container = searchActions.findElementsByXpath("//div[@class='adview_extrasInmo ']");
        List<String> results = new ArrayList<>();
        for (WebElement item : searchActions.findElementsByTagName(container, "li"))
        {
            results.add(item.getText());
        }
        return results;
    }

    public String getTitle()
    {
        List<WebElement> breadcrumb = searchActions.findElementsById("breadcrumbsContainer");
        if (!breadcrumb.isEmpty())
        {
            return searchActions
                    .getElementText(searchActions.findElementsByXpath(breadcrumb, "//p[@class='breadcrumbs']//strong"));
        }
        return null;
    }

    public String getType()
    {
        String[] breadCrumbParts = extractBreadcrumbParts();
        return breadCrumbParts.length > 4 ? breadCrumbParts[4] : INFO_NOT_FOUND;
    }

    public boolean hasImages()
    {
        return searchActions.findElementsByXpath("//div[@class='no_photo_ad']").isEmpty();
    }

    private String[] extractBreadcrumbParts()
    {
        String breadCrumbText = searchActions.getElementTextByXpath("//p[@class='breadcrumbs']");
        return breadCrumbText == null ? new String[0] : breadCrumbText.split(" > ");
    }

    private String findBedroomsTechInfo()
    {
        return findTechInfo(1);
    }

    private String findSizeTechInfo()
    {
        return findTechInfo(2);
    }

    private String findTechInfo(int index)
    {
        return searchActions.getElementTextByXpath(String.format("(//div[@class='techInfo']//span)[%s]", index));
    }

    private String[] getAddressParts()
    {
        String text = searchActions.getElementTextByXpath("//div[@class='map-ad-location__name']");
        return text.split(" ");
    }
}
