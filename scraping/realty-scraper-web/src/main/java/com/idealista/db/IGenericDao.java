package com.idealista.db;

public interface IGenericDao<T>
{
    int save(T item);

    T getById(long id);
}
