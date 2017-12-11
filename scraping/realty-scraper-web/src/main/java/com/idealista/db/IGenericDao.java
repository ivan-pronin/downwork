package com.idealista.db;

public interface IGenericDao<T>
{
    long save(T item);

    T getById(long id);
}
