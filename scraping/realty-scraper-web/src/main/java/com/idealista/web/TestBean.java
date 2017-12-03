package com.idealista.web;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class TestBean
{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    public void testDb()
    {
        try
        {

            DatabaseMetaData metaData = dataSource.getConnection().getMetaData();
            System.out.println("Metadata: " + metaData);
            System.out.println("Username: " + metaData.getUserName());
            System.out.println("URL: " + metaData.getURL());
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int rowCount = jdbcTemplate.queryForObject("select count(*) from scraper.agency", Integer.class);
        System.out.println("Rowcount: " + rowCount);
    }
}
