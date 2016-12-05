package com.recsys.dao;

import java.util.List;

/**
 * Created by alimert on 4.12.2016.
 */
public interface GenericDAO<E, K> {
    
    void add(E entity) ;

    void saveOrUpdate(E entity) ;

    void update(E entity) ;

    void remove(E entity);

    E find(K key);

    List<E> getAll() ;

    void flush() ;

    void clear() ;



}
