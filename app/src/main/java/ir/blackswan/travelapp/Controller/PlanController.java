package ir.blackswan.travelapp.Controller;

import ir.blackswan.travelapp.Data.Plan;
import ir.blackswan.travelapp.ui.AuthActivity;

public class PlanController extends Controller {

    static Plan[] allPlans;
    static Plan[] createdPlans;

    static {
        //todo: remove these (FAKE PLANS!)
//        allPlans = new Plan[]{
//                new Plan("اردکان" , "2022-05-10" , "2022-05-10"),
//                new Plan("شهرکرد" , "2022-07-12" , "2022-08-17"),
//                new Plan("اصفهان" , "2022-01-12" , "2022-05-16"),
//                new Plan("کاشان" , "2022-04-13" , "2022-05-15"),
//                new Plan("اردستان" , "2022-03-14" , "2022-05-14")
//        };
    }

    public PlanController(AuthActivity authActivity) { super(authActivity); }

    public void addPlanToServer(Plan plan, OnResponse onResponse){
//        todo
//        api.addPlan(AuthController.getTokenString(), ?? )
    }

    public static Plan[] getAllPlans(){ return allPlans; }

    public static Plan[] getCreatedPlans(){ return createdPlans; }

}
