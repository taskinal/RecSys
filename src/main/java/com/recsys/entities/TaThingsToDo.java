package com.recsys.entities;

import javax.persistence.*;

/**
 * Created by alimert on 3.12.2016.
 */

@Entity
@Table(name="ta_things_to_do")
public class TaThingsToDo {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column(name="id")
    private long id ;

    @OneToOne
    @JoinColumn(name="placeId")
    private TaPlace placeId ;

    @ManyToOne
    @JoinColumn (name="ttdId")
    private TaThingsToDoSubCategory ttdId ;

    @Column(name = "extraCategories")
    private String extraCategories ;

    @Column(name = "suggestedVisitingHour")
    private String suggestedVisitingHour ;

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

    public TaThingsToDoSubCategory getTtdId() {
        return ttdId;
    }

    public void setTtdId(TaThingsToDoSubCategory ttdId) {
        this.ttdId = ttdId;
    }

    public String getExtraCategories() {
        return extraCategories;
    }

    public void setExtraCategories(String extraCategories) {
        this.extraCategories = extraCategories;
    }

    public String getSuggestedVisitingHour() {
        return suggestedVisitingHour;
    }

    public void setSuggestedVisitingHour(String suggestedVisitingHour) {
        this.suggestedVisitingHour = suggestedVisitingHour;
    }
}
