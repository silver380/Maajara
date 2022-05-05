package ir.blackswan.travelapp.Data;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ir.blackswan.travelapp.Utils.MyPersianCalender;

public class Plan implements Serializable {
    private User creator;
    @Expose
    private String destination;
    @Expose
    private String start_date, end_date;
    @Expose
    private List<String> requestedThings;
    private List<User> requestedGuides;
    private User registeredUser;
    @Expose
    private Place[] places;


    public Plan(String destination, String start_date, String end_date, List<String> requestedThings,
                Place[] places) {
        this.destination = destination;
        this.start_date = start_date;
        this.end_date = end_date;
        this.requestedThings = requestedThings;
        this.places = places;
    }

    public User getCreator() {
        return creator;
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

    public List<String> getRequestedThings() {
        return requestedThings;
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

    public Place[] getPlaces() { return places; }

    private Date convertStringToDate(String date) throws ParseException {

        return new SimpleDateFormat("yyyy-MM-dd").parse(date);

    }
}
