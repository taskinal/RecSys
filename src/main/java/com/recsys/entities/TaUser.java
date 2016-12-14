package com.recsys.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by alimert on 13.12.2016.
 */

@Entity
@Table(name="ta_user")
public class TaUser {

    @Id
    @Column(name="id")
    private String id ;

    @Column (name = "name")
    private String username ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
