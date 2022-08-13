package ir.blackswan.travelapp.Data;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.Objects;

public class Place implements Serializable {
    private int place_id;
    @Expose
    private String name , name_en,  city, city_en, description, description_en;
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

    public Place(int place_id) {
        this.place_id = place_id;
    }

    public String getName() {
        return name;
    }

    public int getPlace_id() {
        return place_id;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Place place = (Place) o;
        return place_id == place.place_id;
    }

    @Override
    public String toString() {
        return "Place{" +
                "place_id=" + place_id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(place_id);
    }
}
