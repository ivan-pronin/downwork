package com.idealista.db;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.idealista.scraper.model.Advertisement;

@Repository
public class AdvertisementDao implements IGenericDao<Advertisement>
{

    private JdbcTemplate jdbcTemplate;

    @Override
    public int save(Advertisement item)
    {
        String query = "INSERT INTO ADVERTISEMENT (TITLE,OPERATION_TYPE_ID,REALTY_TYPE_ID,"
                + "PROVINCE,LISTING_DATE,VIEWS_COUNT,ADDRESS,STATE_ID,CITY_ID,POSTAL_CODE,DESCRIPTION,"
                + "BEDROOMS,BATHROOMS,M2_SIZE,PRICE,ENERGY_CERTIFICATION_ID,PUBLICITY_ID,SELLER_ID,URL,"
                + "HAS_IMAGES,TIMESTAMP_SCRAPED,SCRAP_SOURCE_ID) "
                + "VALUES ('title 444',2,2,'province4 444',CURRENT_DATE(),22,'address',2,2,'postal_code','description',3,3,44,55.5,2,2,2,'http://idelaista.com/ad=54',false,CURRENT_TIMESTAMP(),1);";
        return jdbcTemplate.update(query);
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
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
}
