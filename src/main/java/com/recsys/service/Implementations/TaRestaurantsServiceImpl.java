package com.recsys.service.Implementations;

import com.recsys.dao.GenericDAO;
import com.recsys.entities.TaRestaurants;
import com.recsys.service.Interfaces.TaRestaurantsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by alimert on 4.12.2016.
 */

@Service
@Configurable
public class TaRestaurantsServiceImpl extends GenericServiceImpl<TaRestaurants, String> implements TaRestaurantsService {

    private GenericDAO taRestaurantsDao;

    public TaRestaurantsServiceImpl(){

    }

    @Autowired
    public TaRestaurantsServiceImpl(@Qualifier("taRestaurantsDaoImpl") GenericDAO<TaRestaurants, String> genericDao) {
        super(genericDao);
        this.taRestaurantsDao = genericDao;
    }

}