package com.recsys.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by alimert on 3.12.2016.
 */

@Entity
@Table(name="ta_restaurant_category")
public class TaRestaurantCategory {

    @Id
    @Column(name="id")
    private String id ;


    @Column(name = "name")
    private String name ;

}
