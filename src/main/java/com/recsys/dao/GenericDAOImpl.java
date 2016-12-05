package com.recsys.dao;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * Created by alimert on 5.12.2016.
 */
@Repository
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class GenericDAOImpl<E, K extends Serializable> extends AbstractDAOImpl <E,K> implements GenericDAO<E,K>
{

}
