package com.recsys.service.Implementations;

import com.recsys.dao.GenericDAO;
import com.recsys.entities.TaReviews;
import com.recsys.service.Interfaces.TaReviewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by alimert on 4.12.2016.
 */

@Service
@Configurable
public class TaReviewsServiceImpl extends GenericServiceImpl<TaReviews, String> implements TaReviewsService {

    private GenericDAO taReviewsDao;

    public TaReviewsServiceImpl(){

    }

    @Autowired
    public TaReviewsServiceImpl(@Qualifier("taReviewsDaoImpl") GenericDAO<TaReviews, String> genericDao) {
        super(genericDao);
        this.taReviewsDao = genericDao;
    }

}