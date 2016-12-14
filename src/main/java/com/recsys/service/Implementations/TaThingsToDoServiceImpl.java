package com.recsys.service.Implementations;

import com.recsys.dao.GenericDAO;
import com.recsys.entities.TaThingsToDo;
import com.recsys.service.Interfaces.TaThingsToDoService;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by alimert on 5.12.2016.
 */

@Service
@Configurable
public class TaThingsToDoServiceImpl extends GenericServiceImpl<TaThingsToDo, Long> implements TaThingsToDoService {

    private GenericDAO taThingsToDoDAO;

    public TaThingsToDoServiceImpl(){

    }

    @Autowired
    //@Qualifier("taThingsToDoImpl")
    public TaThingsToDoServiceImpl(GenericDAO<TaThingsToDo, Long> genericDao) {
        super(genericDao);
        this.taThingsToDoDAO = genericDao;
        this.taThingsToDoDAO.setDaoType(TaThingsToDo.class);
    }

}
