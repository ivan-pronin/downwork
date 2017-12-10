package com.idealista.db;

import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.FileCopyUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.idealista.scraper.model.Advertisement;
import com.idealista.web.Boot;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Boot.class)
@TestPropertySource(locations = "classpath:test.properties")
public class JsonTest
{

    @Value("classpath:json/advertisement.json")
    private Resource jsonFile;

    @Test
    public void testReadFromJson() throws IOException
    {
        System.out.println(jsonFile.getFilename());
        String copyToString = FileCopyUtils.copyToString(new FileReader(jsonFile.getFile()));
        Advertisement readValue = new ObjectMapper().readValue(copyToString, Advertisement.class);
        System.out.println(readValue);
        // System.out.println(jsonParser.parseList(copyToString));
        // jsonParser.parseMap(jsonFile.getInputStream().)
    }
}
