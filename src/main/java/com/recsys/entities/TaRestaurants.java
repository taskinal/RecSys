package com.recsys.entities;

import javax.persistence.*;

/**
 * Created by alimert on 3.12.2016.
 */

@Entity
@Table(name="ta_restaurants")
public class TaRestaurants {


    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column(name = "id")
    private long id ;

    @OneToOne
    @JoinColumn(name="placeId")
    private TaPlace placeId ;

    @ManyToOne
    @JoinColumn(name="rcId")
    private TaRestaurantCategory rcId ;

    @Column(name="ratingSummary")
    private String ratingSummary ;

    @Column(name="details")
    private String details ;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public TaPlace getPlaceId() {
        return placeId;
    }

    public void setPlaceId(TaPlace placeId) {
        this.placeId = placeId;
    }

    public TaRestaurantCategory getRcId() {
        return rcId;
    }

    public void setRcId(TaRestaurantCategory rcId) {
        this.rcId = rcId;
    }

    public String getRatingSummary() {
        return ratingSummary;
    }

    public void setRatingSummary(String ratingSummary) {
        this.ratingSummary = ratingSummary;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }













}
