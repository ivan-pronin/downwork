package com.idealista.db;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.idealista.scraper.AppConfig;
import com.idealista.web.Boot;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Boot.class, AppConfig.class})
@TestPropertySource(locations = "classpath:test.properties")
public class DBTest
{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    @Test
    public void testConnection() throws SQLException
    {
        System.out.println("Metadata: " + dataSource.getConnection().getMetaData());
        int rowCount = jdbcTemplate.queryForObject("select count(*) from scraper.agency", Integer.class);
        System.out.println("Rowcount: " + rowCount);
    }
}
