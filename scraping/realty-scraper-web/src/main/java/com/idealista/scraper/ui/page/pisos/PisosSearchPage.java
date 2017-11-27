package com.idealista.scraper.ui.page.pisos;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import com.idealista.scraper.ui.page.BasePage;
import com.idealista.scraper.ui.page.ISearchPage;

@Component
public class PisosSearchPage extends BasePage implements ISearchPage
{
    private static final Logger LOGGER = LogManager.getLogger(PisosSearchPage.class);

    private static final int WAIT_FOR_ELEMENTS_TIMEOUT = 5;
    private static final String ZONA_ID = "dd_ddZonaComarca";
    private static final String MUNICIPIOS_ID = "dd_ddZonaMunicipios";
    private static final String DISTROS_ID = "dd_ddZonaPoblacionDistrito";
    private static final String DIV_ID_PATTERN = "//div[@id='%s']";
    private static final String TODAS_LOCATOR_PATTERN = DIV_ID_PATTERN + "//div[@class='box ']"
            + "//span[contains(.,'Todas')]";
    private static final String OPTIONS_LOCATOR_PATTERN = DIV_ID_PATTERN + "//div[@class='ms-drop']//li//span";
    private static final String ULTIMA_SEMANA = "Última semana";
    private static final String ULTIMA_MES = "Último mes";

    public void selectZone(String zone)
    {
        selectDropDown(DropDown.ZONE, zone);
    }

    public void selectMunicipio(String municipio)
    {
        selectDropDown(DropDown.MUNICIPIO, municipio);
        clickSearch();
    }

    public void selectDistrict(String district)
    {
        selectDropDown(DropDown.DISTRICT, district);
        clickSearch();
    }

    private void clickSearch()
    {
        WebElement searchButton = waitActions.waitForElement(By.xpath("//div[@class='filtro']//input[@value='Buscar']"),
                5);
        clickActions.click(searchButton);
    }

    private void selectDropDown(DropDown dropDown, String value)
    {
        String id = dropDown.getId();
        WebElement dropDownElement = waitActions.waitForElement(By.id(id), WAIT_FOR_ELEMENTS_TIMEOUT);
        if (dropDownElement == null)
        {
            LOGGER.error("Could not wait for " + dropDown + " element");
            return;
        }
        clickActions.click(dropDownElement);
        waitActions.waitForElement(By.xpath(String.format(TODAS_LOCATOR_PATTERN, id)), WAIT_FOR_ELEMENTS_TIMEOUT);
        List<WebElement> options = waitActions.waitForElements(By.xpath(dropDown.getXpath()),
                WAIT_FOR_ELEMENTS_TIMEOUT);
        for (WebElement option : options)
        {
            String optionText = option.getText();
            boolean containsValue = optionText.contains(value);
            LOGGER.debug("Current option text: {} contains value: {} = {}", optionText, value, containsValue);
            if (containsValue)
            {
                clickActions.click(option);
                return;
            }
        }
    }

    private enum DropDown
    {
        ZONE(ZONA_ID, String.format(OPTIONS_LOCATOR_PATTERN, ZONA_ID)), MUNICIPIO(MUNICIPIOS_ID,
                String.format(OPTIONS_LOCATOR_PATTERN, MUNICIPIOS_ID)), DISTRICT(DISTROS_ID,
                        String.format(OPTIONS_LOCATOR_PATTERN, DISTROS_ID));

        private String id;
        private String xpath;

        private DropDown(String id, String xpath)
        {
            this.id = id;
            this.xpath = xpath;
        }

        private String getId()
        {
            return id;
        }

        private String getXpath()
        {
            return xpath;
        }
    }

    public void selectExtras(String extras)
    {
        String id = "dvMasFiltros";
        WebElement dropDownElement = waitActions.waitForElement(By.id(id), WAIT_FOR_ELEMENTS_TIMEOUT);
        if (dropDownElement == null)
        {
            LOGGER.error("Could not wait for Extras element");
            return;
        }
        clickActions.click(dropDownElement);
        waitActions.waitForElement(By.xpath("//div[@class='tipoFiltro']"), WAIT_FOR_ELEMENTS_TIMEOUT);
        List<WebElement> options = waitActions.waitForElements(By.xpath("//div[@class='fkCheckbox']"),
                WAIT_FOR_ELEMENTS_TIMEOUT);
        for (WebElement option : options)
        {
            String optionText = option.getText();
            String spanishEquivalent = getSpanishEquivalent(extras);
            boolean containsValue = optionText.contains(spanishEquivalent);
            LOGGER.debug("Current option text: {} contains value: {} = {}", optionText, spanishEquivalent,
                    containsValue);
            if (containsValue)
            {
                clickActions.click(option);
                break;
            }
        }
        WebElement searchButton = waitActions.waitForElement(By.xpath("//div[@class='filtro']//button"), 5);
        clickActions.click(searchButton);
    }

    private String getSpanishEquivalent(String extras)
    {
        switch (extras)
        {
            case "ultimaSemana":
                return ULTIMA_SEMANA;
            case "ultimoMes":
                return ULTIMA_MES;
            default:
                throw new IllegalArgumentException(
                        "Invalid <extras> value: " + extras + ". Valid values are 'ultimaSemana, ultimoMes'");
        }
    }
}
