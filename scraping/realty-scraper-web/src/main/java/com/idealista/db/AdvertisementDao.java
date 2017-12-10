package com.idealista.db;

import java.sql.Timestamp;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
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
        Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("title", item.getTitle());
        namedParameters.addValue("operationTypeId", 1);
        namedParameters.addValue("realtyTypeId", 1);
        namedParameters.addValue("province", item.getProvince());
        namedParameters.addValue("listingDate", item.getDateOfListing());
        namedParameters.addValue("viewsCount", item.getNumberOfViews());
        namedParameters.addValue("address", item.getAddress());
        namedParameters.addValue("stateId", 1);
        namedParameters.addValue("cityId", 1);
        namedParameters.addValue("postalCode", item.getPostalCode());
        namedParameters.addValue("description", item.getDescription());
        namedParameters.addValue("bedrooms", item.getBedRooms());
        namedParameters.addValue("bathrooms", item.getBathRooms());
        namedParameters.addValue("m2Size", item.getSize());
        namedParameters.addValue("price", item.getPrice());
        namedParameters.addValue("energyCertificationId", 1);
        namedParameters.addValue("publicityId", 1);
        namedParameters.addValue("sellerId", 1);
        namedParameters.addValue("url", item.getUrl().toExternalForm());
        namedParameters.addValue("hasImages", item.hasImages());
        namedParameters.addValue("timestampScraped", timeStamp);
        namedParameters.addValue("scrapSourceId", 1);
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
