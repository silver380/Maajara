package ir.blackswan.travelapp.Data;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class PlanRequest implements Serializable {
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

    public Plan getPlan() {
        return plan;
    }

    public int getPrice() {
        return suggested_price;
    }
}
