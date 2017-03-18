package com.idealista.scraper.model.parser;

import com.idealista.scraper.model.search.IGenericSearchAttributes;
import com.idealista.scraper.model.search.IdealistaSearchAttributes;
import com.idealista.scraper.model.search.SearchAttributes;
import com.idealista.scraper.ui.page.IdealistaStartPage;
import com.idealista.scraper.webdriver.WebDriverProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
public class IdealistaSearchAttributesParser implements ISearchAttributesParser
{
    @Autowired
    private WebDriverProvider webDriverProvider;
    
    private IdealistaStartPage startPage;

    @Override
    public SearchAttributes parseSearchAttributes(Map<IGenericSearchAttributes, Set<String>> attributes)
    {
        Set<String> operations = attributes.get(IdealistaSearchAttributes.OPERATION);
        Set<String> typologies = attributes.get(IdealistaSearchAttributes.TYPOLOGY);
        Set<String> locations = attributes.get(IdealistaSearchAttributes.LOCATION);
        startPage = new IdealistaStartPage();
        startPage.setWebDriver(webDriverProvider.get());
        if (operations.isEmpty())
        {
            operations = startPage.getAvailableOperations();
        }
        if (typologies.isEmpty())
        {
            typologies = startPage.getAvailableTypologies();
        }
        if (locations.isEmpty())
        {
            locations = startPage.getAvailableLocations();
        }
        return new SearchAttributes(operations, typologies, locations);
    }
}
