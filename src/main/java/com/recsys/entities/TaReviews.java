package com.recsys.entities;

import javax.persistence.*;

/**
 * Created by alimert on 3.12.2016.
 */
@Entity
@Table(name="ta_reviews")
public class TaReviews {

    @Id
    @Column(name="id")
    private String id ;

    @Column(name = "title")
    private String title ;

    @Column(name = "text")
    private String text ;

    @Column(name = "date")
    private String date ;

    @Column(name= "rate")
    private double rate ;

    @ManyToOne
    @JoinColumn(name = "placeId")
    private TaPlace placeId ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public TaPlace getPlaceId() {
        return placeId;
    }

    public void setPlaceId(TaPlace placeId) {
        this.placeId = placeId;
    }
}
