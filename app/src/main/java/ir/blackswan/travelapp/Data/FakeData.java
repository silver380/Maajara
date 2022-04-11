package ir.blackswan.travelapp.Data;


import java.util.Arrays;
import java.util.List;

public class FakeData {
    private static List<Place> fakePlaces = Arrays.asList();

    static {
        for (int i = 0; i < 100; i++) {
            fakePlaces.add(new Place("مکان‍‍۱", "شهر۱", "توضیحات",
                    null,
                    "https://photokade.com/wp-content/uploads/lovegraphy-photokade-com-12.jpg"));
        }
    }

    public static List<Place> getFakePlaces() {
        return fakePlaces;
    }

}
