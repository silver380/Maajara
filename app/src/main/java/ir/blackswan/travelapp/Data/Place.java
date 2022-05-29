package ir.blackswan.travelapp.Data;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class Place implements Serializable {
    private int place_id;
    @Expose
    private String name , city, description;
    @Expose
    private float latitude , longitude;
    private String picture;
    private Tag tag;

    public Place(String name, String city, String description, float latitude, float longitude) {
        this.name = name;
        this.city = city;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public int getPlace_id() {
        return place_id;
    }

    public String getCity() {
        return city;
    }

    public String getDescription() {
        return description;
    }

    public String getPicture() {
        return picture;
    }

    public Tag getTag() {
        return tag;
    }
}
