package com.recsys.dao;

import com.recsys.entities.TaUser;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by alimert on 4.12.2016.
 */

public abstract class AbstractDAOImpl<E,K extends Serializable> implements GenericDAO<E,K> {

    @Autowired
    private SessionFactory sessionFactory ;

    protected Class<E> daoType;

    public void setDaoType (Class <E> daoToSet){

        this.daoType = daoToSet;

    }

    protected Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public void add (E entity) {
        currentSession().save(entity);
    }


    public void saveOrUpdate (E entity) {
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

    public E getItemByAttr(String attrName, K attrValue){

        Criteria criteria = currentSession().createCriteria(daoType);
        criteria.add(Restrictions.eq(attrName,attrValue));
        return (E) criteria.uniqueResult();

    }

    public void flush(){
        currentSession().flush();
    }

    public void clear(){
        currentSession().clear();
    }

}
