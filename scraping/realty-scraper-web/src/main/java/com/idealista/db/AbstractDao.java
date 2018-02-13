package com.idealista.db;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.idealista.db.model.DbColumn;
import com.idealista.db.model.DbTable;

@Component
public class AbstractDao
{

    private static final Logger LOGGER = LogManager.getLogger(AbstractDao.class);

    protected NamedParameterJdbcTemplate namedJdbcTemplate;

    @Value("${insert.genericTableName.name}")
    private String insertGenericTableName;

    @Value("${select.genericNameTable.id}")
    private String selectIdFromGenericNameTableByName;

    @Autowired
    public void setDataSource(DataSource dataSource)
    {
        namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    protected long updateTableByNameValue(String nameValue, DbTable tableName)
    {
        String selectSql = String.format(selectIdFromGenericNameTableByName, tableName);
        MapSqlParameterSource params = new MapSqlParameterSource(DbColumn.NAME.name(), nameValue);
        Long id = null;
        try
        {
            id = namedJdbcTemplate.queryForObject(selectSql, params, Long.class);
        }
        catch (EmptyResultDataAccessException e)
        {
            String insertSql = String.format(insertGenericTableName, tableName);
            KeyHolder keyHolder = new GeneratedKeyHolder();
            int rowsAffected = namedJdbcTemplate.update(insertSql, params, keyHolder);
            if (0 == rowsAffected)
            {
                LOGGER.error("Failed to insert new row into table: {}, value: {}", tableName, nameValue);
            }
            id = (Long) keyHolder.getKey();
            debugSelectIdMsg(nameValue, tableName, id);
        }
        return id;
    }

    protected static void debugSelectIdMsg(String value, DbTable tableName, long foundId)
    {
        LOGGER.debug("Got correct ID {} from table: {} by value: {}", foundId, tableName, value);
    }

}
