package ir.blackswan.travelapp.Data;

import java.io.Serializable;

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
}
