package com.idealista.scraper.ui.page.idealista;

import com.idealista.scraper.ui.actions.ClickActions;
import com.idealista.scraper.ui.page.BasePage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

@Component
public class IdealistaMapPage extends BasePage
{
    private static final int WAIT_FOR_ELEMENT_TIMEOUT_SECONDS = 5;
    private static final Logger LOGGER = LogManager.getLogger(IdealistaMapPage.class);

    public void clickIdealistaLink()
    {
        WebElement idealistaLink = waitActions
                .waitForElement(By.xpath("//div[@class='breadcrumb-geo wrapper clearfix']//a[text()='idealista']"), 5);
        if (idealistaLink == null)
        {
            LOGGER.warn("Failed to find <idealista> link");
            return;
        }
        clickActions.click(idealistaLink);
    }

    public void clickShowAll()
    {
        WebElement showAllLink = waitActions.waitForElement(By.id("showAllLink"), WAIT_FOR_ELEMENT_TIMEOUT_SECONDS);
        if (showAllLink != null)
        {
            clickActions.click(showAllLink);
        }
    }

    public void setClickActions(ClickActions clickActions)
    {
        this.clickActions = clickActions;
    }
}
