package com.recsys.service.Implementations;

import com.recsys.dao.GenericDAO;
import com.recsys.entities.TaUser;
import com.recsys.service.Interfaces.TaUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

/**
 * Created by alimert on 13.12.2016.
 */

@Service
@Configurable
public class TaUserServiceImpl extends GenericServiceImpl<TaUser, String> implements TaUserService  {

    private GenericDAO taUserDAO;

    @Autowired
    public TaUserServiceImpl( GenericDAO<TaUser, String> genericDao) {
        super(genericDao);
        this.taUserDAO = genericDao;
        this.taUserDAO.setDaoType(TaUser.class);

    }



}
