package com.idealista.db;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.idealista.scraper.model.Advertisement;

@Repository
public class AdvertisementDao implements IGenericDao<Advertisement>
{

    @Value("${insert.advertisement}")
    private String insertAdvertisementSql;

    private NamedParameterJdbcTemplate namedJdbcTemplate;

    @Override
    public int save(Advertisement item)
    {
        SqlParameterSource namedParameters = new MapSqlParameterSource("t1", "title t1 t1").addValue("a1",
                "aaa ddddress 123");
        return namedJdbcTemplate.update(insertAdvertisementSql, namedParameters);
    }

    @Override
    public Advertisement getById(long id)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Autowired
    public void setDataSource(DataSource dataSource)
    {
        namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }
}
