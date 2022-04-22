package ir.blackswan.travelapp.Data;

public class Place {
    private int place_id;
    private String name , city, description;
    private Path picturePath;
    private Tag tag;

    public Place(String name, String city, String description, String localPicturePath , String serverPicturePath) {
        this.name = name;
        this.city = city;
        this.description = description;
        this.picturePath = new Path(serverPicturePath , localPicturePath);
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getDescription() {
        return description;
    }

    public Path getPicturePath() {
        return picturePath;
    }

    public Tag getTag() {
        return tag;
    }
}
