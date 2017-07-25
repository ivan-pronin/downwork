package com.facebook.javatest;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import com.facebook.javatest.model.Group;
import com.facebook.javatest.page.FacebookPage;
import com.facebook.javatest.page.GroupsPage;
import com.facebook.javatest.util.PrintUtils;
import com.facebook.javatest.util.PropertiesLoader;
import com.facebook.javatest.util.WaitUtils;
import com.facebook.javatest.webdriver.WebDriverProvider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

public class FacebookTestApplication
{
    private static final Logger LOGGER = LogManager.getLogger(FacebookTestApplication.class);
    private static final int GROUPS_JOIN_COUNT_OVER_SPECIFIED_MEMBERS = 2;
    private static final int GROUPS_JOIN_WITH_ANY_MEMBERS_INDEX = 6;

    private String login;
    private String password;
    private Properties props;
    private String searchQuery;
    private int membersRequired;

    public static void main(String[] args)
    {
        LOGGER.info("App started ...");

        FacebookTestApplication app = new FacebookTestApplication();
        app.initVariables();
        app.run();
        
        LOGGER.info("App ended ...");
    }

    private void initVariables()
    {
        props = PropertiesLoader.getProperties();
        login = props.getProperty("login");
        password = props.getProperty("password");
        searchQuery = props.getProperty("searchQuery", "Food");
        membersRequired = Integer.parseInt(props.getProperty("membersRequired", "5000"));
    }

    private void run()
    {
        WebDriver driver = WebDriverProvider.getDriverInstance();
        driver.navigate().to("https://www.facebook.com");
        FacebookPage facebookPage = new FacebookPage(driver);
        facebookPage.login(login, password);
        facebookPage.search(searchQuery);
        facebookPage.openGroupsTab();
        GroupsPage groupsPage = new GroupsPage(driver);
        List<Group> groups = groupsPage.grabVisibleGroups();
        PrintUtils.printGroupsToConsole(groups);

        joinGroupsWithMembersOver(filterGroupsByMembersRequired(groups, membersRequired), groupsPage);
        joinGroupWithMembersByIndex(filterGroupsByMembersRequired(groups, 0), groupsPage);

        // sleep for demonstrating results
        LOGGER.info("Sleeping for 60 seconds to demo the results ... ");
        WaitUtils.sleepSeconds(this, 60);
        WebDriverProvider.stopDriver();
    }

    private void joinGroupWithMembersByIndex(List<Group> groups, GroupsPage groupsPage)
    {
        while (groups.size() <= GROUPS_JOIN_WITH_ANY_MEMBERS_INDEX)
        {
            groups.addAll(groupsPage.scrollForNewGroups());
            groups = filterGroupsByMembersRequired(groups, 0);
        }
        Group groupByIndex = groups.get(GROUPS_JOIN_WITH_ANY_MEMBERS_INDEX - 1);
        if (groupsPage.joinGroupByButtonId(groupByIndex.getButtonId()))
        {
            LOGGER.info("Join request has been sent for a Group with any members by index: {} - {}", groupByIndex,
                    GROUPS_JOIN_WITH_ANY_MEMBERS_INDEX);
        }
    }

    private void joinGroupsWithMembersOver(List<Group> groups, GroupsPage groupsPage)
    {
        while (groups.size() <= GROUPS_JOIN_COUNT_OVER_SPECIFIED_MEMBERS)
        {
            groups.addAll(groupsPage.scrollForNewGroups());
            groups = filterGroupsByMembersRequired(groups, membersRequired);
        }

        groups.stream().limit(GROUPS_JOIN_COUNT_OVER_SPECIFIED_MEMBERS).forEach(g ->
        {
            if (groupsPage.joinGroupByButtonId(g.getButtonId()))
            {
                LOGGER.info("Join request has been sent for a Group with members over: {} - {}", g, membersRequired);
            }
        });
    }

    private List<Group> filterGroupsByMembersRequired(List<Group> groups, int membersOver)
    {
        return groups.stream().filter(g -> g.getMembers() > membersOver & g.getButtonId() != null)
                .collect(Collectors.toList());
    }
}
