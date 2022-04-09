package ir.blackswan.travelapp.Data;

public class Place {
    private String name ,cityName , details;
    private Path picturePath;
    private Tag tag;

    public Place(String name, String cityName, String details, String localPicturePath , String serverPicturePath) {
        this.name = name;
        this.cityName = cityName;
        this.details = details;
        this.picturePath = new Path(serverPicturePath , localPicturePath);
    }

    public String getName() {
        return name;
    }

    public String getCityName() {
        return cityName;
    }

    public String getDetails() {
        return details;
    }

    public Path getPicturePath() {
        return picturePath;
    }

    public Tag getTag() {
        return tag;
    }
}
