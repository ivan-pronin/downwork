package com.idealista.db;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.idealista.db.model.DbColumn;

@Component
public class TagLinkedDao implements IGenericLinkedDao<Long, Long>
{

    private static final Logger LOGGER = LogManager.getLogger(TagLinkedDao.class);

    protected NamedParameterJdbcTemplate namedJdbcTemplate;

    @Value("${insert.tagReference}")
    private String insertTagReference;

    @Override
    public long save(Long tagId, Long advertisementId)
    {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(DbColumn.TAG_ID.name(), tagId);
        namedParameters.addValue(DbColumn.ADVERTISEMENT_ID.name(), advertisementId);
        return namedJdbcTemplate.update(insertTagReference, namedParameters);
    }

    @Autowired
    public void setDataSource(DataSource dataSource)
    {
        namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }
}
