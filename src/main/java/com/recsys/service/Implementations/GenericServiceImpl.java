package com.recsys.service.Implementations;

import com.recsys.dao.GenericDAO;
import com.recsys.service.Interfaces.GenericService;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by alimert on 4.12.2016.
 */

@Service
@Configurable
public abstract class GenericServiceImpl<E,K> implements GenericService<E,K> {

    private GenericDAO<E, K> genericDao;

    public GenericServiceImpl(GenericDAO<E,K> genericDao) {

        this.genericDao=genericDao;
    }

    public GenericServiceImpl() {

    }


    @Transactional(propagation = Propagation.REQUIRED)
    public void saveOrUpdate(E entity) {
        genericDao.saveOrUpdate(entity);
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<E> getAll() {
        return genericDao.getAll();
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public E get(K id) {
        return genericDao.find(id);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public void add(E entity) {
        genericDao.add(entity);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public void update(E entity) {
        genericDao.update(entity);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public void remove(E entity) {
        genericDao.remove(entity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void flush(){genericDao.flush();}

    @Transactional(propagation = Propagation.REQUIRED)
    public void clear(){genericDao.clear();}

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public E getItemByAttr(String attrName, K attrValue){return genericDao.getItemByAttr(attrName,attrValue);}

}
