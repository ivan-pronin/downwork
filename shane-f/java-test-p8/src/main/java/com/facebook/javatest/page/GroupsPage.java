package com.facebook.javatest.page;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.facebook.javatest.actions.ClickActions;
import com.facebook.javatest.actions.ElementActions;
import com.facebook.javatest.actions.SearchActions;
import com.facebook.javatest.actions.WaitActions;
import com.facebook.javatest.model.Group;
import com.facebook.javatest.util.RegexUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class GroupsPage
{
    private static final Logger LOGGER = LogManager.getLogger(GroupsPage.class);
    private static final String MEMBERS = "members";

    private ClickActions clickActions = new ClickActions();
    private ElementActions elementActions = new ElementActions();
    private SearchActions searchActions = new SearchActions();
    private WaitActions waitActions = new WaitActions();

    public GroupsPage(WebDriver driver)
    {
        searchActions.setWebDriver(driver);
        clickActions.setWebDriver(driver);
        waitActions.setWebDriver(driver);
    }

    public List<Group> grabVisibleGroups()
    {
        LOGGER.info("Grabbing visible groups from page ...");
        List<WebElement> resultElements = waitActions.waitForElements(By.xpath("//div[@class='_3u1 _gli _uvb _5und']"),
                10);
        List<Group> results = new ArrayList<>();
        resultElements.forEach(e -> results.add(parseResult(e)));
        results.forEach(LOGGER::info);
        return results;
    }

    public boolean joinGroupByButtonId(String buttonId)
    {
        LOGGER.info("Joining group by buttonId {}", buttonId);
        clickActions.click(isButtonByIdExist(buttonId));
        if (isButtonByIdExist(buttonId).isEmpty())
        {
            return true;
        }
        return false;
    }

    public Collection<? extends Group> scrollForNewGroups()
    {
        LOGGER.info("Scrolling page down for new groups ... ");
        clickActions.scrollToTheBottom();
        return grabVisibleGroups();
    }

    private List<WebElement> isButtonByIdExist(String buttonId)
    {
        return searchActions
                .findElementsByXpath(String.format("//div[@class='_glk rfloat _ohf']//a[@id='%s']", buttonId));
    }

    private Group parseResult(WebElement element)
    {
        long id = RegexUtils.extractId(elementActions
                .getAttribute(searchActions.findElementsByXpath(element, "//div[@class='_401d']"), "data-gt"));
        List<WebElement> title = searchActions.findElementsByXpath(element, "//div[@class='_2mch _gll']//a");
        if (title.isEmpty() || id < 0)
        {
            LOGGER.error("Could not parse group result of element {}", element);
            return null;
        }
        String name = title.get(0).getText();
        String membersText = searchActions
                .getElementText(searchActions.findElementsByXpath(element, "//div[@class='_pac']"));
        long membersCount = 0;
        if (!StringUtils.isEmpty(membersText) && membersText.contains(MEMBERS))
        {
            membersCount = RegexUtils.extractNumber(membersText.split(MEMBERS)[0]);
        }
        List<WebElement> joinButton = searchActions.findElementsByXpath(element, "//div[@class='_glk rfloat _ohf']//a");
        String buttonId = elementActions.getAttribute(joinButton, "id");
        return new Group(id, name, membersCount, buttonId);
    }
}
