package com.recsys.service.Implementations;

import com.recsys.dao.GenericDAO;
import com.recsys.entities.TaPlace;
import com.recsys.service.Interfaces.TaPlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by alimert on 4.12.2016.
 */

@Service
@Configurable
public class TaPlaceServiceImpl extends GenericServiceImpl<TaPlace, String> implements TaPlaceService {


    private GenericDAO taPlaceDao;

    public TaPlaceServiceImpl(){

    }

    @Autowired
    public TaPlaceServiceImpl(@Qualifier("taPlaceDaoImpl") GenericDAO<TaPlace, String> genericDao) {
        super(genericDao);
        this.taPlaceDao = genericDao;
    }

}
