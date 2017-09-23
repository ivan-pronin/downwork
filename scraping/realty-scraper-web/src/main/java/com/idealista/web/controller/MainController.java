package com.idealista.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.idealista.scraper.model.LaunchResult;
import com.idealista.web.Boot;
import com.idealista.web.SimpleData;
import com.idealista.web.config.BaseScraperConfiguration;
import com.idealista.web.config.BaseSourceConfiguration;
import com.idealista.web.config.cloner.BaseScraperConfigCloner;

@Controller
public class MainController
{

    @Autowired
    private Boot boot;

    @Autowired
    private BaseScraperConfiguration scraperConfiguration;

    @Autowired
    private BaseSourceConfiguration sourceConfiguration;

    @Autowired
    private LaunchResult launchResult;

    @GetMapping("/")
    public String index(Model model)
    {
        model.addAttribute("data", new SimpleData());
        return "index";
    }

    @GetMapping("/settings")
    public String result(Model model)
    {
        model.addAttribute("scraperConfiguration", scraperConfiguration);
        model.addAttribute("sourceConfiguration", sourceConfiguration);
        return "settings";
    }

    @PostMapping("/resultScraperConfiguration")
    public String postScraperConfiguration(
            @ModelAttribute("scraperConfiguration") BaseScraperConfiguration scraperConfiguration, Model model)
    {
        BaseScraperConfigCloner.cloneScraperConfiguration(scraperConfiguration, this.scraperConfiguration);
        model.addAttribute("scraperConfiguration", this.scraperConfiguration);
        return "index";
    }

    @PostMapping("/resultSourceConfiguration")
    public String postSourceConfiguration(
            @ModelAttribute("sourceConfiguration") BaseSourceConfiguration sourceConfiguration, Model model)
    {
        this.sourceConfiguration = sourceConfiguration;
        model.addAttribute("scraperConfiguration", this.scraperConfiguration);
        model.addAttribute("sourceConfiguration", this.sourceConfiguration);
        return "index";
    }

    @PostMapping("/launch")
    public String postSourceConfiguration(Model model)
    {
        model.addAttribute("scraperConfiguration", this.scraperConfiguration);
        model.addAttribute("sourceConfiguration", this.sourceConfiguration);
        boot.launch();
        model.addAttribute("launchResult", launchResult);
        return "launchResult";
    }
}
