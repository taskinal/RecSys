package com.recsys.service.Implementations;

import com.recsys.dao.GenericDAO;
import com.recsys.entities.TaThingsToDoSubCategory;
import com.recsys.service.Interfaces.TaThingsToDoSubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by alimert on 4.12.2016.
 */

@Service
@Configurable
public class TaThingsToDoSubCategoryServiceImpl extends GenericServiceImpl<TaThingsToDoSubCategory, Integer> implements TaThingsToDoSubCategoryService {

    private GenericDAO taThingsToDoSubCategoryDao;

    public TaThingsToDoSubCategoryServiceImpl(){

    }
    @Autowired
    public TaThingsToDoSubCategoryServiceImpl(GenericDAO<TaThingsToDoSubCategory, Integer> genericDao) {
        super(genericDao);
        this.taThingsToDoSubCategoryDao = genericDao;
        this.taThingsToDoSubCategoryDao.setDaoType(TaThingsToDoSubCategory.class);
    }

}