package ir.blackswan.travelapp.Data;

import java.io.Serializable;

public class PlanRequest implements Serializable {
    private String id;
    private User tour_leader;
    private Plan travel_plan;
    private int suggested_price;

    public String getId() { return id; }

    public User getTour_leader() {
        return tour_leader;
    }

    public Plan getTravel_plan() {
        return travel_plan;
    }

    public int getSuggested_price() {
        return suggested_price;
    }
}
