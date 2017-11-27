package com.idealista.scraper.ui.page.advertisement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import com.idealista.scraper.ui.page.BasePage;
import com.idealista.scraper.ui.page.IAdvertisementPage;
import com.idealista.scraper.util.RegexUtils;

@Component
public class PisosAdvertisementPage extends BasePage implements IAdvertisementPage
{
    public String getAddress()
    {
        String breadCrumbText = searchActions.getElementTextByXpath("//div[@class='breadcrumb']");
        if (breadCrumbText == null)
        {
            return INFO_NOT_FOUND;
        }
        List<String> parts = new ArrayList<>(Arrays.asList(breadCrumbText.split("\\n")));
        parts.remove(parts.size() - 1);
        parts.remove(0);
        return parts.stream().collect(Collectors.joining(">"));
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
        return searchActions.getElementText(searchActions.findElementsByXpath("//span[@class='number one']"));
    }

    public int getBathrooms()
    {
        List<String> data = getMainCharacteristics();
        if (data.size() > 2)
        {
            int number = RegexUtils.extractDigit(data.get(2));
            return number > 0 ? number : 0;
        }
        return 0;
    }

    public int getBedrooms()
    {
        List<String> data = getMainCharacteristics();
        if (data.size() > 1)
        {
            int number = RegexUtils.extractDigit(data.get(1));
            return number > 0 ? number : 0;
        }
        return 0;
    }

    public String getCity()
    {
        String h2Text = getH2Text();
        return h2Text == null ? null : h2Text.split("\\(")[0];
    }

    public String getDescription()
    {
        return searchActions.getElementTextByXpath("//div[@class='description']");
    }

    public String getDistrict()
    {
        return RegexUtils.extractTextBetweenParenthesis(getH2Text());
    }

    public String getEnergyCertification()
    {
        String text = searchActions.getElementText(searchActions
                .findElementsByXpath("//div[@class='block precioCertificado']//div[contains(.,'Clasificación: ')]"));
        return text == null ? INFO_NOT_FOUND : text.replace("Clasificación: ", "");
    }

    public String getListingAgent()
    {
        return searchActions.getElementText(
                searchActions.findElementsByXpath(getContactsContainer(), "//div[@class='tag left fullWidth']"));
    }

    public String getListingDate()
    {
        String contactsText = searchActions.getElementText(
                searchActions.findElementsByXpath(getContactsContainer(), "//div[@class='main darkGray multiLine']"));
        if (contactsText == null)
        {
            return INFO_NOT_FOUND;
        }
        String[] text = contactsText.split("Actualizado el ");
        return text.length > 1 ? text[1] : INFO_NOT_FOUND;
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
        String text = searchActions
                .getElementText(searchActions.findElementsByXpath("//div[@class='rightBox ']//div[@class='price']"));
        return "" + RegexUtils.extractBigNumber(text);
    }

    public String getProfessional()
    {
        String listingAgent = getListingAgent();
        return listingAgent == null ? PRIVATE : PROFESSIONAL;
    }

    public String getProvince()
    {
        return NA_FOR_THIS_SITE;
    }

    public String getSize()
    {
        List<String> data = getMainCharacteristics();
        String size = INFO_NOT_FOUND;
        if (data.size() > 0)
        {
            int number = RegexUtils.extractNumber(data.get(0));
            size = number > 0 ? "" + number : INFO_NOT_FOUND;
        }
        return size;
    }

    public String getState()
    {
        return NA_FOR_THIS_SITE;
    }

    public List<String> getTags()
    {
        return getGeneralCharacteristics();
    }

    public String getTitle()
    {
        List<WebElement> textElement = searchActions.findElementsByXpath("//h1");
        if (!textElement.isEmpty())
        {
            return textElement.get(0).getText();
        }
        return null;
    }

    public boolean hasImages()
    {
        return !searchActions.findElementsById("basic").isEmpty();
    }

    private List<WebElement> getContactsContainer()
    {
        return searchActions.findElementsByXpath("//div[@class='contact']");
    }

    private List<String> getGeneralCharacteristics()
    {
        return searchActions
                .findElementsByXpath("//div[@class='characteristics']//div[contains(@class,'block') and "
                        + "not(contains(.,'energético '))]//div[@class='line']")
                .stream().map(WebElement::getText).collect(Collectors.toList());
    }

    private String getH2Text()
    {
        return searchActions.getElementText(searchActions.findElementsByXpath("//h2[@class='position']"));
    }

    private List<String> getMainCharacteristics()
    {
        return searchActions.findElementsByXpath("//div[@class='leftBox ']//div[@class='characteristics']").stream()
                .map(WebElement::getText).map(e -> e.split("\\n")).map(Arrays::asList).flatMap(List::stream)
                .collect(Collectors.toList());
    }
}
