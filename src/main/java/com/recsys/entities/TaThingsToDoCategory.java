package com.recsys.entities;

import javax.persistence.*;

/**
 * Created by alimert on 3.12.2016.
 */

@Entity
@Table(name="ta_things_to_do_category")
public class TaThingsToDoCategory {

    @Id
    @Column(name="id")
    private String id ;

    @Column (name = "name")
    private String name ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
