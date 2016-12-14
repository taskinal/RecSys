package com.recsys.service.Implementations;

import com.recsys.dao.GenericDAO;
import com.recsys.entities.TaRestaurantCategory;
import com.recsys.service.Implementations.GenericServiceImpl;
import com.recsys.service.Interfaces.TaRestaurantCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by alimert on 4.12.2016.
 */

@Service
@Configurable
public class TaRestaurantCategoryServiceImpl extends GenericServiceImpl<TaRestaurantCategory, String> implements TaRestaurantCategoryService {

    private GenericDAO taRestaurantCategoryDao;

    public TaRestaurantCategoryServiceImpl(){

    }

    @Autowired
    //@Qualifier("taRestaurantCategoryDaoImpl")
    public TaRestaurantCategoryServiceImpl(GenericDAO<TaRestaurantCategory, String> genericDao) {
        super(genericDao);
        this.taRestaurantCategoryDao = genericDao;
        this.taRestaurantCategoryDao.setDaoType(TaRestaurantCategory.class);
    }

}