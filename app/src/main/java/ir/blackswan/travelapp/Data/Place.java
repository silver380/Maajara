package ir.blackswan.travelapp.Data;

public class Place {
    private String cityName , details;
    private Path picturePath;
    private Tag tag;

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
