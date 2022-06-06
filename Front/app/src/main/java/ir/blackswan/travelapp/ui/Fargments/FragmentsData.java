package ir.blackswan.travelapp.ui.Fargments;

import android.content.Context;

import com.google.gson.Gson;

import java.lang.reflect.GenericSignatureFormatError;
import java.util.List;

import ir.blackswan.travelapp.Data.Place;
import ir.blackswan.travelapp.Utils.SharedPrefManager;

public class FragmentsData {
    String name , capacity , price , destination , startDate , finalDate;
    Place[] selectedPlace;
    boolean[] groupPlace , groupFood , groupVehicle;
    String[] wantedList;
    static Gson gson = new Gson();

    public FragmentsData() {

    }

    public FragmentsData(String name, String capacity, String price, String destination,
                         String startDate, String finalDate, Place[] selectedPlace,
                         boolean[] groupPlace, boolean[] groupFood, boolean[] groupVehicle) {
        this.name = name;
        this.capacity = capacity;
        this.price = price;
        this.destination = destination;
        this.startDate = startDate;
        this.finalDate = finalDate;
        this.selectedPlace = selectedPlace;
        this.groupPlace = groupPlace;
        this.groupFood = groupFood;
        this.groupVehicle = groupVehicle;
    }

    public FragmentsData(String destination, String startDate, String finalDate, Place[] selectedPlace, String[] wantedList) {
        this.destination = destination;
        this.startDate = startDate;
        this.finalDate = finalDate;
        this.selectedPlace = selectedPlace;
        this.wantedList = wantedList;
    }

}
