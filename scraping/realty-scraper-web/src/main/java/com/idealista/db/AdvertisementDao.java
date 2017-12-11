package com.idealista.db;

import java.sql.Timestamp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.idealista.db.factory.SellerFactory;
import com.idealista.db.model.DbColumn;
import com.idealista.db.model.DbTable;
import com.idealista.db.model.Seller;
import com.idealista.db.parser.SqlDateParser;
import com.idealista.scraper.model.Advertisement;
import com.idealista.web.config.BaseScraperConfiguration;

@Repository
public class AdvertisementDao extends AbstractDao implements IGenericDao<Advertisement>
{

    private static final Logger LOGGER = LogManager.getLogger(AbstractDao.class);

    @Autowired
    private TagDao tagDao;

    @Autowired
    private TagLinkedDao tagLinkedDao;

    @Autowired
    private BaseScraperConfiguration scraperConfiguration;

    @Value("${insert.advertisement}")
    private String insertAdvertisementSql;

    @Value("${insert.seller}")
    private String insertSeller;

    @Value("${select.seller.id}")
    private String selectIdFromSellerByEmailAndPhone;

    @Override
    public long save(Advertisement item)
    {
        LOGGER.info("Saving Advertisement to DB: {}", item);
        if (null == item.getTitle())
        {
            LOGGER.info("Skip saving Advertisement to DB since title is null: {}", item);
            return -1;
        }
        Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();

        long operationTypeId = updateTableByNameValue(item.getType(), DbTable.OPERATION_TYPE);
        long realtyTypeId = updateTableByNameValue(item.getSubType(), DbTable.REALTY_TYPE);
        long stateId = updateTableByNameValue(item.getState(), DbTable.STATE);
        long cityId = updateTableByNameValue(item.getCity(), DbTable.CITY);
        long energyCertificationId = updateTableByNameValue(item.getEnergyCertification(),
                DbTable.ENERGY_CERTIFICATION);
        long publicityId = updateTableByNameValue(item.getProfessional(), DbTable.PUBLICITY);

        Seller seller = SellerFactory.create(item.getAgent(), item.getAgentPhone(), item.getAgentEmail());
        long sellerId = updateSellerTable(seller);

        long scrapSourceId = updateTableByNameValue(scraperConfiguration.getScrapTarget().name().toLowerCase(),
                DbTable.SCRAP_SOURCE);

        java.sql.Date date = SqlDateParser.parse(item.getDateOfListing());

        namedParameters.addValue("title", item.getTitle());
        namedParameters.addValue("operationTypeId", operationTypeId);
        namedParameters.addValue("realtyTypeId", realtyTypeId);
        namedParameters.addValue("province", item.getProvince());
        namedParameters.addValue("listingDate", date);
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
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = namedJdbcTemplate.update(insertAdvertisementSql, namedParameters, keyHolder);
        long generatedId = (long) keyHolder.getKey();
        System.out.println("KEY: " + generatedId);

        for (String tag : item.getTags())
        {
            long tagId = tagDao.save(tag);
            tagLinkedDao.save(tagId, generatedId);
        }
        if (rowsAffected > 0)
        {
            LOGGER.info("Successuflly saved to DB with ID: {}", generatedId);
        }
        else
        {
            LOGGER.warn("Failed to save to DB!");
        }
        return rowsAffected;
    }

    private long updateSellerTable(Seller seller)
    {
        LOGGER.debug("Saving seller: {}", seller);
        MapSqlParameterSource params = new MapSqlParameterSource(DbColumn.EMAIL.name(), seller.getEmail())
                .addValue(DbColumn.PHONE.name(), seller.getPhone());
        Long id = null;
        try
        {
            id = namedJdbcTemplate.queryForObject(selectIdFromSellerByEmailAndPhone, params, Long.class);
        }
        catch (EmptyResultDataAccessException e)
        {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            int rowsAffected = namedJdbcTemplate.update(insertSeller,
                    new MapSqlParameterSource(params.getValues()).addValue(DbColumn.NAME.name(), seller.getName())
                            .addValue(DbColumn.LAST_NAME.name(), seller.getEmail()),
                    keyHolder);
            if (0 == rowsAffected)
            {
                LOGGER.error("Failed to insert new row into table: {}, value: {}", DbTable.SELLER, seller);
            }
            id = (Long) keyHolder.getKey();
            debugSelectIdMsg(seller.toString(), DbTable.SELLER, id);
        }
        return id;
    }

    @Override
    public Advertisement getById(long id)
    {
        // TODO Auto-generated method stub
        return null;
    }
}
