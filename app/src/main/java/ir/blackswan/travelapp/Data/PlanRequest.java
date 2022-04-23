package ir.blackswan.travelapp.Data;

import java.io.Serializable;

public class PlanRequest implements Serializable {
    private User guide;
    private Plan plan;
    private int price;

    public User getGuide() {
        return guide;
    }

    public Plan getPlan() {
        return plan;
    }

    public int getPrice() {
        return price;
    }
}
