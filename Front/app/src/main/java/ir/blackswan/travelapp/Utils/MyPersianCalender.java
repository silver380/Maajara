package ir.blackswan.travelapp.Utils;

import ir.hamsaa.persiandatepicker.date.PersianDateImpl;

public class MyPersianCalender extends PersianDateImpl {

    public MyPersianCalender(){
        super();
    }
    public MyPersianCalender(long timeInMillis){
        super();
        setDate(timeInMillis);
    }
    public String getShortDate(){
        return String.format("%d/%d/%d" , getPersianYear() , getPersianMonth() , getPersianDay());
    }

}
