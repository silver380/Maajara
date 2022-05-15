package ir.blackswan.travelapp.Data;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class PlanRequest implements Serializable {
    private String id;
    private User tour_leader;
    private Plan plan;
    @Expose
    private int travel_plan_id;
    @Expose
    private int suggested_price;

    public PlanRequest(int travel_plan_id, int suggested_price) {
        this.travel_plan_id = travel_plan_id;
        this.suggested_price = suggested_price;
    }

    public User getTour_leader() {
        return tour_leader;
    }

    public int getId() { return travel_plan_id; }

    public Plan getPlan() {
        return plan;
    }

    public int getSuggested_price() {
        return suggested_price;
    }
}
