package ir.blackswan.travelapp.Data;

import static ir.blackswan.travelapp.Utils.Utils.getPriceString;
import static ir.blackswan.travelapp.Utils.Utils.priceToString;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class PlanRequest implements Serializable {
    private int id;
    private User tour_leader;
    private Plan travel_plan;
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

    public Plan getTravel_plan() {
        return travel_plan;
    }

    public String getSuggested_price() {
        return getPriceString(suggested_price);
    }

    public String getShortPriceString() {
        return priceToString(suggested_price) + "ت";
    }

    @Override
    public String toString() {
        return "PlanRequest{" +
                "id='" + id + '\'' +
                ", tour_leader=" + tour_leader +
                ", plan=" + travel_plan +
                ", travel_plan_id=" + travel_plan_id +
                ", suggested_price=" + suggested_price +
                '}';
    }
}
