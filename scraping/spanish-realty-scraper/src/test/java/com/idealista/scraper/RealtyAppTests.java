package com.idealista.scraper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, TestContextConfig.class})
public class RealtyAppTests
{

    @Value("${driverType}")
    private String driverType;

    @Value("${publicationDateFilter}")
    private String publicationDateFilter;

    @Value("${vibboProperty}")
    private String vibboProperty;
    
    @Value("#{ '${testEnum1}'.toUpperCase() }")
    private TestEnum tEnum1;
    
    @Value("#{ '${testEnum2}'.toUpperCase()}")
    private TestEnum tEnum2;
    
//    @Value("${testEnum3}")
//    private TestEnum tEnum3;

    @Test
    public void testProperties() throws Exception
    {
        System.out.println(driverType);
        System.out.println(publicationDateFilter);
        System.out.println(vibboProperty);
        System.out.println(tEnum1);
        System.out.println(tEnum2);
//        System.out.println(tEnum3);
    }
    
    public enum TestEnum
    {
        VIBBO,IDEALISTA;
    }
}
