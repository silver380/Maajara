package ir.blackswan.travelapp.Controller;

import ir.blackswan.travelapp.Data.Plan;
import ir.blackswan.travelapp.ui.AuthActivity;
import okhttp3.RequestBody;

public class PlanController<getAllPlans> extends Controller{

    static Plan[] allPlans;
    static Plan[] createdPlans;

    public PlanController(AuthActivity authActivity) { super(authActivity); }

    public void addPlanToServer(Plan plan, OnResponse onResponse){
//        todo
//        api.addPlan(AuthController.getTokenString(), ?? )
    }

    public static Plan[] getAllPlans(){ return allPlans; }

    public static Plan[] getCreatedPlans(){ return createdPlans; }

}
