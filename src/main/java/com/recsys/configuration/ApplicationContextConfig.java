package com.recsys.configuration;

import com.recsys.dao.AbstractDAOImpl;
import com.recsys.dao.GenericDAO;
import com.recsys.dao.GenericDAOImpl;
import com.recsys.datacollector.tadatacollector.PlaceCollector.IPlaceCollector;
import com.recsys.datacollector.tadatacollector.PlaceCollector.PlaceCollectorImpl;
import com.recsys.datacollector.tadatacollector.RestaurantCollector.IRestaurantCollector;
import com.recsys.datacollector.tadatacollector.RestaurantCollector.RestaurantCollectorImpl;
import com.recsys.entities.TaPlace;
import com.recsys.entities.TaRestaurants;
import com.recsys.entities.TaThingsToDoCategory;
import com.recsys.service.Implementations.*;
import com.recsys.service.Interfaces.*;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by alimert on 4.12.2016.
 */

@Configuration
@EnableTransactionManagement
@ComponentScan({ "com.recsys" })
@PropertySource(value = { "classpath:application.properties" })
public class ApplicationContextConfig {

    @Autowired
    private Environment environment;

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(new String[] {"com.recsys.entities"});
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
        dataSource.setUrl(environment.getRequiredProperty("jdbc.url"));
        dataSource.setUsername(environment.getRequiredProperty("jdbc.username"));
        dataSource.setPassword(environment.getRequiredProperty("jdbc.password"));
        return dataSource;
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", environment.getRequiredProperty("hibernate.show_sql"));
        properties.put("hibernate.format_sql", environment.getRequiredProperty("hibernate.format_sql"));
        properties.put("hibernate.batch_size", environment.getRequiredProperty("hibernate.batch_size")) ;
        return properties;
    }

    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory s) {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(s);
        return txManager;
    }
/*
    @Bean(name="daoType")
    public GenericDAO daoType(){
        return new GenericDAOImpl() ;
    }


    @Bean(name="tripAdvisorPlaceCollector")
    public IPlaceCollector tripAdvisorPlaceCollector(){
        return new PlaceCollectorImpl();
    }

    @Bean(name="tripAdvisorRestaurantCollector")
    public IRestaurantCollector tripAdvisorRestaurantCollector(){
        return new RestaurantCollectorImpl();
    }
*/
/*
    @Bean(name="taRestaurantsService")
    @Autowired
    public TaRestaurantsService taRestaurantsService(GenericDAO dao){
        return new TaRestaurantsServiceImpl(dao);
    }

    @Bean(name="taPlaceService")
    @Autowired
    public TaPlaceService taPlaceService(GenericDAO dao){
        return new TaPlaceServiceImpl(dao);
    }


    @Bean(name="taRestaurantCategoryService")
    @Autowired
    public TaRestaurantCategoryService taRestaurantCategoryService(GenericDAO dao){
        return new TaRestaurantCategoryServiceImpl(dao);
    }

    @Bean(name="taReviewsService")
    @Autowired
    public TaReviewsService taReviewsService(GenericDAO dao){
        return new TaReviewsServiceImpl(dao);
    }

    @Bean(name="taThingsToDoCategoryService")
    @Autowired
    public TaThingsToDoCategoryService taThingsToDoCategoryService(GenericDAO dao){
        return new TaThingsToDoCategoryServiceImpl(dao);
    }

    @Bean(name="taThingsToDoService")
    @Autowired
    public TaThingsToDoService taThingsToDoService(GenericDAO dao){
        return new TaThingsToDoServiceImpl(dao);
    }

    @Bean(name="taThingsToDoSubCategoryService")
    @Autowired
    public TaThingsToDoSubCategoryService taThingsToDoSubCategoryService(GenericDAO dao){
        return new TaThingsToDoSubCategoryServiceImpl(dao);
    }

    @Bean(name="taUserService")
    @Autowired
    public TaUserService taUserService(GenericDAO dao){
        return new TaUserServiceImpl(dao);
    }

*/


}
