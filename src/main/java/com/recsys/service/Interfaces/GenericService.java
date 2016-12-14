package com.recsys.service.Interfaces;

import com.recsys.dao.GenericDAO;

import java.util.List;

/**
 * Created by alimert on 4.12.2016.
 */
public interface GenericService<E,K> {

    void saveOrUpdate(E entity);

    List<E> getAll();

    E get(K id);

    void add(E entity);

    void update(E entity);

    void remove(E entity);

    void flush();

    void clear() ;

    E getItemByAttr(String attrName, K attrValue) ;

}
