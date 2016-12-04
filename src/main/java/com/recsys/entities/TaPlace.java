package com.recsys.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by alimert on 3.12.2016.
 */

@Entity
@Table(name="ta_place")
public class TaPlace {

    @Id
    @Column(name="id")
    private String id ;

    @Column (name = "name")
    private String name ;

    @Column (name = "rating")
    private double rating ;

    @Column (name = "neighborhood")
    private String neighborhood ;

    @Column (name = "ranking")
    private int ranking ;

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

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }
}
