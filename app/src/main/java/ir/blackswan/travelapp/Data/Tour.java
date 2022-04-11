package ir.blackswan.travelapp.Data;

import java.util.List;

public class Tour {
    private User creator;
    private String tourName;
    private int capacity;
    private int registerCount;
    private String city;
    private long startDate , finalDate;
    private List<Place> places;
    private boolean hotel , sweet , house , villa;
    private boolean breakfast , lunch , dinner;
    private boolean car , minibus , bus , van;
    private List<User> requestedUsers;
    private List<User> registeredUses;

    public User getCreator() {
        return creator;
    }

    public String getTourName() {
        return tourName;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getRegisterCount() {
        return registerCount;
    }

    public String getCity() {
        return city;
    }

    public long getStartDate() {
        return startDate;
    }

    public long getFinalDate() {
        return finalDate;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public boolean isHotel() {
        return hotel;
    }

    public boolean isSweet() {
        return sweet;
    }

    public boolean isHouse() {
        return house;
    }

    public boolean isVilla() {
        return villa;
    }

    public boolean isBreakfast() {
        return breakfast;
    }

    public boolean isLunch() {
        return lunch;
    }

    public boolean isDinner() {
        return dinner;
    }

    public boolean isCar() {
        return car;
    }

    public boolean isMinibus() {
        return minibus;
    }

    public boolean isBus() {
        return bus;
    }

    public boolean isVan() {
        return van;
    }

    public List<User> getRequestedUsers() {
        return requestedUsers;
    }

    public List<User> getRegisteredUses() {
        return registeredUses;
    }
}