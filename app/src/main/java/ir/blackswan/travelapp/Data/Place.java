package ir.blackswan.travelapp.Data;

import java.io.Serializable;
import java.util.Objects;

public class Place implements Serializable {
    private int place_id;
    private String name , city, description;
    private String picture;
    private Tag tag;

    public Place(String name, String city, String description, String localPicturePath , String serverPicturePath) {
        this.name = name;
        this.city = city;
        this.description = description;
        picture = serverPicturePath;
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
