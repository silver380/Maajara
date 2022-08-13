package ir.blackswan.travelapp.Data;

import static ir.blackswan.travelapp.Utils.Utils.convertStringToDate;
import static ir.blackswan.travelapp.Utils.Utils.priceToString;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import ir.blackswan.travelapp.Utils.MyPersianCalender;
import ir.blackswan.travelapp.Utils.Utils;

public class Tour implements Serializable {
    /*
     "id": 1,
        "tour_name": "Number1",
        "tour_capacity": 10,
        "destination": "a",
        "residence": "HS",
        "start_date": "1900-10-10",
        "end_date": "1900-10-11",
        "has_breakfast": true,
        "has_lunch": false,
        "has_dinner": true,
        "has_transportation": "MB",
        "creator": 3
     */
    private int tour_id;
    private User creator;
    @Expose
    private String tour_name;
    @Expose
    private int tour_capacity;
    private int current_confirmed;
    @Expose
    private String destination;
    @Expose
    private String start_date, end_date;
    private Place[] places;
    @Expose
    private int[] places_ids;
    @Expose
    private int price;
    @Expose
    private String residence;
    @Expose
    private boolean has_breakfast, has_lunch, has_dinner;
    @Expose
    private String has_transportation;
    private transient List<User> requestedUsers;
    private transient List<User> registeredUses;

    public Tour(String tour_name, int tour_capacity, int price, String destination,
                String start_date, String end_date, Place[] places, String residence,
                boolean has_breakfast, boolean has_lunch, boolean has_dinner, String has_transportation) {
        this.tour_name = tour_name;
        this.tour_capacity = tour_capacity;
        this.price = price;
        this.destination = destination;
        this.start_date = start_date;
        this.end_date = end_date;
        this.places = places;
        this.residence = residence;
        this.has_breakfast = has_breakfast;
        this.has_lunch = has_lunch;
        this.has_dinner = has_dinner;
        this.has_transportation = has_transportation;
        places_ids = new int[places.length];
        for (int i = 0; i < places.length; i++) {
            places_ids[i] = places[i].getPlace_id();
        }
    }

    public void setRequestedUsers(List<User> requestedUsers) {
        this.requestedUsers = requestedUsers;
    }

    public int getTour_id() {
        return tour_id;
    }

    public String getStart_date() {
        return start_date;
    }

    public boolean isFull(){
        return current_confirmed >= tour_capacity;
    }


    public String getPriceString(){
        return Utils.getPriceString(price);
    }

    public String getShortPriceString() {
        return priceToString(price) + "ت";
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

    public String getEnd_date() {
        return end_date;
    }

    public void setRegisteredUses(List<User> registeredUses) {
        this.registeredUses = registeredUses;
    }

    public User getCreator() {
        return creator;
    }

    public String getTour_name() {
        return tour_name;
    }

    public int getTour_capacity() {
        return tour_capacity;
    }

    public String getDestination() {
        return destination;
    }


    public Place[] getPlaces() {
        return places;
    }

    public int[] getPlaceIds() {
        int[] ids = new int[places.length];
        for (int i = 0; i < places.length; i++) {
            ids[i] = places[i].getPlace_id();
        }
        return ids;
    }

    public List<Integer> getPlaceIdsList() {
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < places.length; i++) {
            ids.add(places[i].getPlace_id());
        }
        return ids;
    }

    public String getResidence() {
        return residence;
    }

    public String getHas_transportation() {
        return has_transportation;
    }

    public boolean isHas_breakfast() {
        return has_breakfast;
    }

    public boolean isHas_lunch() {
        return has_lunch;
    }

    public boolean isHas_dinner() {
        return has_dinner;
    }

    public int getPrice() {
        return price;
    }

    public List<User> getRequestedUsers() {
        return requestedUsers;
    }

    public List<User> getRegisteredUses() {
        return registeredUses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tour tour = (Tour) o;
        return tour_id == tour.tour_id;
    }

    @Override
    public int hashCode() {

        return Objects.hash(tour_id);
    }

    public int getConfirmed_count() {
        return current_confirmed;
    }

    @Override
    public String toString() {
        return "Tour{" +
                "tour_id=" + tour_id +
                ", creator=" + creator +
                ", tour_name='" + tour_name + '\'' +
                ", tour_capacity=" + tour_capacity +
                ", registered_count=" + current_confirmed +
                ", destination='" + destination + '\'' +
                ", start_date='" + start_date + '\'' +
                ", end_date='" + end_date + '\'' +
                ", places=" + Arrays.toString(places) +
                ", places_ids=" + Arrays.toString(places_ids) +
                ", price=" + price +
                ", residence='" + residence + '\'' +
                ", has_breakfast=" + has_breakfast +
                ", has_lunch=" + has_lunch +
                ", has_dinner=" + has_dinner +
                ", has_transportation='" + has_transportation + '\'' +
                ", requestedUsers=" + requestedUsers +
                ", registeredUses=" + registeredUses +
                '}';
    }
}
