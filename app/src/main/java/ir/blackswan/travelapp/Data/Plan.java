package ir.blackswan.travelapp.Data;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ir.blackswan.travelapp.Utils.MyPersianCalender;

public class Plan implements Serializable {
    private User plan_creator;
    @Expose
    private String destination;
    @Expose
    private String start_date, end_date;
    @Expose
    private String wanted_list;
    private List<User> requestedGuides;
    private User registeredUser;
    @Expose
    private Place[] places;
    private int travel_plan_id;


    public int getTravel_plan_id() {
        return travel_plan_id;
    }

    public Plan(String destination, String start_date, String end_date, List<String> requestedThings,
                Place[] places) {
        this.destination = destination;
        this.start_date = start_date;
        this.end_date = end_date;
        this.wanted_list = new Gson().toJson(requestedThings);
        this.places = places;
    }

    public User getPlan_creator() {
        return plan_creator;
    }

    public String getDestination() {
        return destination;
    }

    public String getStart_date() {
        return start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public List<String> getWanted_list() {
        try {
            return new Gson().fromJson(wanted_list, List.class);
        } catch (JsonSyntaxException e) {
            return new ArrayList<>(); //todo: remove this
        }
    }

    public List<User> getRequestedGuides() {
        return requestedGuides;
    }

    public User getRegisteredUser() {
        return registeredUser;
    }

    public MyPersianCalender getPersianStartDate() {
        try {
            MyPersianCalender persianDate = new MyPersianCalender();
            persianDate.setDate(convertStringToDate(getStart_date()));
            return persianDate;
        } catch (ParseException ignored) {
        }
        return null;
    }

    public MyPersianCalender getPersianEndDate() {
        try {
            MyPersianCalender persianDate = new MyPersianCalender();
            persianDate.setDate(convertStringToDate(getEnd_date()));
            return persianDate;
        } catch (ParseException ignored) {
        }
        return null;
    }

    public Place[] getPlaces() {
        return places;
    }

    private Date convertStringToDate(String date) throws ParseException {

        return new SimpleDateFormat("yyyy-MM-dd").parse(date);

    }
}
