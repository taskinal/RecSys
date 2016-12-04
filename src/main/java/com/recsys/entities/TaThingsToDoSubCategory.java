package com.recsys.entities;

import javax.persistence.*;

/**
 * Created by alimert on 3.12.2016.
 */

@Entity
@Table(name="ta_things_to_do_sub_category")
public class TaThingsToDoSubCategory {

    @Id
    @Column(name="id")
    private int id ;

    @Column (name = "name")
    private String name ;

    @ManyToOne
    @JoinColumn(name="ttdId")
    private TaThingsToDoCategory ttdId ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TaThingsToDoCategory getTtdIid() {
        return ttdId;
    }

    public void setTtdId(TaThingsToDoCategory ttdId) {
        this.ttdId = ttdId;
    }
}
