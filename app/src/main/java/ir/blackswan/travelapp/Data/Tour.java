package ir.blackswan.travelapp.Data;

import java.io.Serializable;
import java.util.List;

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
    private User creatorT;
    private String tour_name;
    private int tour_capacity;
    private int registerCount;
    private String destination;
    private String start_date, end_date;
    private Place[] places;
    private long price;
    private String residence ;
    private boolean has_breakfast , has_lunch ,has_dinner ;
    private String has_transportation;
    private List<User> requestedUsers;
    private List<User> registeredUses;

    public Tour(int tour_id, User creatorT, String tour_name, int tour_capacity, String destination,
                String start_date, String end_date, List<Place> places, String residence,
                boolean has_breakfast, boolean has_lunch, boolean has_dinner, String has_transportation) {
        this.tour_id = tour_id;
        this.creatorT = creatorT;
        this.tour_name = tour_name;
        this.tour_capacity = tour_capacity;
        this.destination = destination;
        this.start_date = start_date;
        this.end_date = end_date;
        this.places = places;
        this.residence = residence;
        this.has_breakfast = has_breakfast;
        this.has_lunch = has_lunch;
        this.has_dinner = has_dinner;
        this.has_transportation = has_transportation;
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

    public String getEnd_date() {
        return end_date;
    }

    public void setRegisteredUses(List<User> registeredUses) {
        this.registeredUses = registeredUses;
    }

    public User getCreatorT() {
        return creatorT;
    }

    public String getTour_name() {
        return tour_name;
    }

    public int getTour_capacity() {
        return tour_capacity;
    }

    public int getRegisterCount() {
        return registerCount;
    }

    public String getDestination() {
        return destination;
    }


    public Place[] getPlaces() {
        return places;
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

    public long getPrice() {
        return price;
    }

    public List<User> getRequestedUsers() {
        return requestedUsers;
    }

    public List<User> getRegisteredUses() {
        return registeredUses;
    }

    @Override
    public String toString() {
        return "Tour{" +
                "id=" + tour_id +
                ", creatorT=" + creatorT +
                ", tour_name='" + tour_name + '\'' +
                ", tour_capacity=" + tour_capacity +
                ", registerCount=" + registerCount +
                ", destination='" + destination + '\'' +
                ", start_date='" + start_date + '\'' +
                ", end_date='" + end_date + '\'' +
                ", places=" + places +
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
