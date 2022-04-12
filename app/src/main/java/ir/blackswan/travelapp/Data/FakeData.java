package ir.blackswan.travelapp.Data;


import java.util.ArrayList;
import java.util.List;

import ir.blackswan.travelapp.Utils.MyPersianCalender;

public class FakeData {
    private static List<Place> fakePlaces = new ArrayList<>();
    private static List<Tour> fakeTours = new ArrayList<>();
    private static User fakeUser = new User("سید محمد حسین" , "حسینی نسب" , "seyed@gmail.com");


    static {
        MyPersianCalender startDate = new MyPersianCalender();
        startDate.setDate(1401, 10, 12);
        MyPersianCalender finalDate = new MyPersianCalender();
        finalDate.setDate(1401, 10, 16);
        for (int i = 0; i < 100; i++) {
            fakePlaces.add(new Place("مکان‍‍۱", "شهر۱", "توضیحات",
                    null,
                    "https://panel.safarmarket.com/storage/img/tours/1649138747448.jpg"));
            fakeTours.add(new Tour(fakeUser , "لورم ایپسوم" , 17 , "آذربایجان غربی" ,
                    startDate.getTimestamp() , finalDate.getTimestamp() , fakePlaces , 12000));
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
}