package com.recsys.service.Implementations;

import com.recsys.dao.GenericDAO;
import com.recsys.entities.TaThingsToDoCategory;
import com.recsys.service.Interfaces.TaThingsToDoCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by alimert on 4.12.2016.
 */

@Service
@Configurable
public class TaThingsToDoCategoryServiceImpl extends GenericServiceImpl<TaThingsToDoCategory, String> implements TaThingsToDoCategoryService {

    private GenericDAO taThingsToDoCategoryDao;

    public TaThingsToDoCategoryServiceImpl(){

    }

    @Autowired
    //@Qualifier("taThingsToDoCategoryDaoImpl")
    public TaThingsToDoCategoryServiceImpl(GenericDAO<TaThingsToDoCategory, String> genericDao) {
        super(genericDao);
        this.taThingsToDoCategoryDao = genericDao;
        this.taThingsToDoCategoryDao.setDaoType(TaThingsToDoCategory.class);
    }

}