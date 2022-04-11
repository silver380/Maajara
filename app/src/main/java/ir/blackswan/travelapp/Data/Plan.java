package ir.blackswan.travelapp.Data;

import java.util.List;

public class Plan {
    private User creator;
    private String city;
    private long startDate,finalDate;
    private List<String> requestedThings;
    private List<User> requestedGuides;
    private User registeredUser;

    public User getCreator() {
        return creator;
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

    public List<String> getRequestedThings() {
        return requestedThings;
    }

    public List<User> getRequestedGuides() {
        return requestedGuides;
    }

    public User getRegisteredUser() {
        return registeredUser;
    }
}