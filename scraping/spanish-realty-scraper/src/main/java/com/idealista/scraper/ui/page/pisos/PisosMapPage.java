package com.idealista.scraper.ui.page.pisos;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import com.idealista.scraper.ui.page.BasePage;
import com.idealista.scraper.ui.page.IMapPage;

@Component
public class PisosMapPage extends BasePage implements IMapPage
{
    private static final int WAIT_FOR_ELEMENT_TIMEOUT_SECONDS = 5;

    public void clickShowAll()
    {
        WebElement showAllLink = waitActions.waitForElement(By.xpath("//h1/a"), WAIT_FOR_ELEMENT_TIMEOUT_SECONDS);
        if (showAllLink != null)
        {
            clickActions.click(showAllLink);
        }
    }
}
