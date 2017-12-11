package com.idealista.db;

import org.springframework.stereotype.Component;

import com.idealista.db.model.DbTable;

@Component
public class TagDao extends AbstractDao implements IGenericDao<String>
{

    @Override
    public long save(String item)
    {
        return updateTableByNameValue(item, DbTable.TAG);
    }

    @Override
    public String getById(long id)
    {
        // TODO Auto-generated method stub
        return null;
    }
}
