package com.idealista.db;

import java.sql.Timestamp;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.idealista.scraper.model.Advertisement;
import com.idealista.web.config.BaseScraperConfiguration;

@Repository
public class AdvertisementDao implements IGenericDao<Advertisement>
{

    private static final Logger LOGGER = LogManager.getLogger(AdvertisementDao.class);

    @Autowired
    private BaseScraperConfiguration scraperConfiguration;

    @Value("${insert.advertisement}")
    private String insertAdvertisementSql;

    @Value("${insert.genericTableName.name}")
    private String insertGenericTableName;

    @Value("${select.genericNameTable.id}")
    private String selectIdFromGenericNameTableByName;

    private NamedParameterJdbcTemplate namedJdbcTemplate;

    @Override
    public int save(Advertisement item)
    {
        Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();

        int operationTypeId = updateTableByNameValue(item.getType(), DbTable.OPERATION_TYPE);
        int realtyTypeId = updateTableByNameValue(item.getSubType(), DbTable.REALTY_TYPE);
        int stateId = updateTableByNameValue(item.getState(), DbTable.STATE);
        int cityId = updateTableByNameValue(item.getCity(), DbTable.CITY);
        int energyCertificationId = updateTableByNameValue(item.getEnergyCertification(), DbTable.ENERGY_CERTIFICATION);
        int publicityId = updateTableByNameValue(item.getProfessional(), DbTable.PUBLICITY);
        int sellerId = updateTableByNameValue(item.getAgent(), DbTable.SELLER);
        int scrapSourceId = updateTableByNameValue(scraperConfiguration.getScrapTarget().name().toLowerCase(),
                DbTable.SCRAP_SOURCE);

        namedParameters.addValue("title", item.getTitle());
        namedParameters.addValue("operationTypeId", operationTypeId);
        namedParameters.addValue("realtyTypeId", realtyTypeId);
        namedParameters.addValue("province", item.getProvince());
        namedParameters.addValue("listingDate", item.getDateOfListing());
        namedParameters.addValue("viewsCount", item.getNumberOfViews());
        namedParameters.addValue("address", item.getAddress());
        namedParameters.addValue("stateId", stateId);
        namedParameters.addValue("cityId", cityId);
        namedParameters.addValue("postalCode", item.getPostalCode());
        namedParameters.addValue("description", item.getDescription());
        namedParameters.addValue("bedrooms", item.getBedRooms());
        namedParameters.addValue("bathrooms", item.getBathRooms());
        namedParameters.addValue("m2Size", item.getSize());
        namedParameters.addValue("price", item.getPrice());
        namedParameters.addValue("energyCertificationId", energyCertificationId);
        namedParameters.addValue("publicityId", publicityId);
        namedParameters.addValue("sellerId", sellerId);
        namedParameters.addValue("url", item.getUrl().toExternalForm());
        namedParameters.addValue("hasImages", item.hasImages());
        namedParameters.addValue("timestampScraped", timeStamp);
        namedParameters.addValue("scrapSourceId", scrapSourceId);
        return namedJdbcTemplate.update(insertAdvertisementSql, namedParameters);
    }

    private int updateTableByNameValue(String nameValue, DbTable tableName)
    {
        String selectSql = String.format(selectIdFromGenericNameTableByName, tableName);
        MapSqlParameterSource params = new MapSqlParameterSource(DbColumn.NAME.name(), nameValue);
        Integer id = null;
        try
        {
            id = namedJdbcTemplate.queryForObject(selectSql, params, Integer.class);
        }
        catch (EmptyResultDataAccessException e)
        {
            String insertSql = String.format(insertGenericTableName, tableName);
            int rowsAffected = namedJdbcTemplate.update(insertSql, params);
            if (0 == rowsAffected)
            {
                LOGGER.error("Failed to insert new row into table: {}, value: {}", tableName, nameValue);
            }
            id = namedJdbcTemplate.queryForObject(selectSql, params, Integer.class);
            debugSelectIdMsg(nameValue, tableName, id);
        }
        return id;
    }

    private static void debugSelectIdMsg(String value, DbTable tableName, Integer foundId)
    {
        LOGGER.debug("Got correct ID {} from table: {} by value: {}", foundId, tableName, value);
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
