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
        return String.format("%2d/%2d/%2d" , getPersianYear() , getPersianMonth() , getPersianDay());
    }
}
