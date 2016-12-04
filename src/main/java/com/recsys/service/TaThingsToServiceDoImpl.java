package com.recsys.service;

import com.recsys.dao.GenericDAO;
import com.recsys.dao.interfaces.TaThingsToDoDAO;
import com.recsys.entities.TaThingsToDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by alimert on 5.12.2016.
 */

@Service
@Configurable
public class TaThingsToServiceDoImpl extends GenericServiceImpl<TaThingsToDo, Long> implements TaThingsToDoService {

    private TaThingsToDoDAO taThingsToDoDAO;

    public TaThingsToServiceDoImpl(){

    }

    @Autowired
    public TaThingsToServiceDoImpl(@Qualifier("taThingsToDoImpl") GenericDAO<TaThingsToDo, Long> genericDao) {
        super(genericDao);
        this.taThingsToDoDAO = (TaThingsToDoDAO) genericDao;
    }
}
