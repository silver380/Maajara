package ir.blackswan.travelapp.Data;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import ir.blackswan.travelapp.Utils.MyPersianCalender;

public class FakeData {
    private static List<Place> fakePlaces = new ArrayList<>();
    private static List<Tour> fakeTours = new ArrayList<>();
    private static List<Path> fakeImagePath = new ArrayList<>();
    private static User fakeUser = new User("سید محمد حسین", "حسینی نسب", "seyed@gmail.com");


    static {
        MyPersianCalender startDate = new MyPersianCalender();
        startDate.setDate(1401, 10, 12);
        MyPersianCalender finalDate = new MyPersianCalender();
        finalDate.setDate(1401, 10, 16);
        for (int i = 0; i < 100; i++) {
            fakePlaces.add(new Place("مکان‍‍۱", "شهر۱", "توضیحات",
                    null,
                    "https://panel.safarmarket.com/storage/img/tours/1649138747448.jpg"));

//            fakeTours = Arrays.asList(new Tour(FakeData.getFakeUser(), "dfdf", 4,
//                            "ssss", "1900-01-01", "1900-01-01", FakeData.getFakePlaces(), 100)
//                    , new Tour(FakeData.getFakeUser(), "dfdf", 4,
//                            "ssss", "1900-01-01", "1900-01-01", FakeData.getFakePlaces(), 100)
//                    ,
//                    new Tour(FakeData.getFakeUser(), "dfdf", 4,
//                            "ssss", "1900-01-01", "1900-01-01", FakeData.getFakePlaces(), 100)
//                    , new Tour(FakeData.getFakeUser(), "dfdf", 4,
//                            "ssss", "1900-01-01", "1900-01-01", FakeData.getFakePlaces(), 100));
//

            fakeImagePath = Arrays.asList(
                    new Path(
                            "https://media.istockphoto.com/photos/hot-air-balloons-flying-over-the-botan-canyon-in-turkey-picture-id1297349747?b=1&k=20&m=1297349747&s=170667a&w=0&h=oH31fJty_4xWl_JQ4OIQWZKP8C6ji9Mz7L4XmEnbqRU="
                            , null
                    ),
                    new Path(
                            "https://images.unsplash.com/photo-1454391304352-2bf4678b1a7a?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxleHBsb3JlLWZlZWR8Mnx8fGVufDB8fHx8&auto=format&fit=crop&w=600&q=60"
                            , null
                    ),
                    new Path(
                            "https://images.unsplash.com/photo-1522878129833-838a904a0e9e?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxleHBsb3JlLWZlZWR8Nnx8fGVufDB8fHx8&auto=format&fit=crop&w=600&q=60"
                            , null
                    ),
                    new Path("https://images.unsplash.com/photo-1495904786722-d2b5a19a8535?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxleHBsb3JlLWZlZWR8MTJ8fHxlbnwwfHx8fA%3D%3D&auto=format&fit=crop&w=600&q=60"
                            , null),
                    new Path(
                            "https://images.unsplash.com/photo-1482192505345-5655af888cc4?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxleHBsb3JlLWZlZWR8MTl8fHxlbnwwfHx8fA%3D%3D&auto=format&fit=crop&w=600&q=60"
                            , null
                    ) , new Path(
                            "https://images.unsplash.com/photo-1508672019048-805c876b67e2?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxleHBsb3JlLWZlZWR8MjB8fHxlbnwwfHx8fA%3D%3D&auto=format&fit=crop&w=600&q=60"
                            , null
                    )
            );
        }
    }

    public static List<Place> getFakePlaces() {
        return fakePlaces;
    }

    public static User getFakeUser() {
        return fakeUser;
    }

    public static List<Tour> getFakeTours() {
        return fakeTours;
    }

    public static Path getRandomImagePath(){
        return fakeImagePath.get(new Random().nextInt(fakeImagePath.size()));
    }
}
