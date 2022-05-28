package ir.blackswan.travelapp.Data;

import static ir.blackswan.travelapp.Utils.Utils.getPriceString;
import static ir.blackswan.travelapp.Utils.Utils.priceToString;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.Objects;

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

    public PlanRequest(User tour_leader, Plan travel_plan) {
        this.tour_leader = tour_leader;
        this.travel_plan = travel_plan;
    }

    public User getTour_leader() {
        return tour_leader;
    }

    public int getId() { return travel_plan_id; }

    public Plan getTravel_plan() {
        return travel_plan;
    }

    public String getSuggested_priceString() {
        return getPriceString(suggested_price);
    }

    public int getSuggested_price() {
        return suggested_price;
    }

    public String getShortPriceString() {
        return priceToString(suggested_price) + "ت";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlanRequest that = (PlanRequest) o;
        return travel_plan_id == that.travel_plan_id && tour_leader.equals(that.tour_leader);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tour_leader.getUser_id(), travel_plan_id);
    }

    @Override
    public String toString() {
        return "PlanRequest{" +
                "travel_plan_id=" + travel_plan_id +
                ", suggested_price=" + suggested_price +
                '}';
    }
}
