package com.idealista.db;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.FileCopyUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.idealista.scraper.model.Advertisement;
import com.idealista.web.Boot;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Boot.class)
@TestPropertySource(locations = "classpath:test.properties")
public class DBTest
{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private IGenericDao<Advertisement> dao;

    @Autowired
    private DataSource dataSource;

    @Value("classpath:json/advertisement.json")
    private Resource jsonFile;

    // @Test
    public void testConnection() throws SQLException
    {
        System.out.println("Metadata: " + jdbcTemplate.getDataSource().getConnection().getMetaData());
        int rowCount = jdbcTemplate.queryForObject("select count(*) from scraper.agency", Integer.class);
        System.out.println("Rowcount: " + rowCount);
        Assert.assertThat(rowCount, Matchers.is(Matchers.greaterThanOrEqualTo(1)));
    }

    // @Test
    public void testSelectNamedParameterJdbcTemplate()
    {
        String query = "select id from advertisement where title= :t1 AND address= :a1";
        NamedParameterJdbcTemplate named = new NamedParameterJdbcTemplate(dataSource);
        Map<String, String> namedParameters = new HashMap<>();
        namedParameters.put("t1", "title333");
        namedParameters.put("a1", "address");
        int result = named.queryForObject(query, namedParameters, Integer.class);
        Assert.assertEquals(6, result);
    }

    // @Test
    public void testSelectSqlParameterSource()
    {
        String query = "select id from advertisement where title= :t1 AND address= :a1";
        NamedParameterJdbcTemplate named = new NamedParameterJdbcTemplate(dataSource);
        SqlParameterSource namedParameters = new MapSqlParameterSource("t1", "title333").addValue("a1", "address");
        int result = named.queryForObject(query, namedParameters, Integer.class);
        Assert.assertEquals(6, result);
    }

    @Test
    public void testSaveAdvertisement() throws FileNotFoundException, IOException
    {
        String copyToString = FileCopyUtils.copyToString(new FileReader(jsonFile.getFile()));
        Advertisement ad = new ObjectMapper().readValue(copyToString, Advertisement.class);
        int rowsAffected = dao.save(ad);
        Assert.assertEquals(1, rowsAffected);
    }
}
