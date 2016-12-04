package com.recsys.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by alimert on 4.12.2016.
 */

@Repository
public abstract class GenericDAOImpl <E,K extends Serializable> implements GenericDAO<E, K>  {

    @Autowired
    private SessionFactory sessionFactory ;

    protected Class<? extends E> daoType;

    public GenericDAOImpl() {

        Type t = getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) t;
        daoType = (Class) pt.getActualTypeArguments()[0];

    }

    protected Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public void add(E entity) {
        currentSession().save(entity);
    }


    public void saveOrUpdate(E entity) {
        currentSession().saveOrUpdate(entity);
    }


    public void update(E entity) {
        currentSession().saveOrUpdate(entity);
    }


    public void remove(E entity) {
        currentSession().delete(entity);
    }


    public E find(K key) {
        return (E) currentSession().get(daoType, key);
    }


    public List<E> getAll() {
        return currentSession().createCriteria(daoType).list();
    }

    public void flush(){
        currentSession().flush();
    }

    public void clear(){
        currentSession().clear();
    }

}
