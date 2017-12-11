package com.idealista.db;

public interface IGenericLinkedDao<K, V>
{
    long save(K firstItem, V secondItem);
}
