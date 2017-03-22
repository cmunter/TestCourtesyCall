package com.munternet.app.courtesycall.alarm;

/**
 * Created by chrtistianmunter on 2/16/17.
 */

public enum Day{
    SUNDAY,
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY;

    @Override
    public String toString() {
        switch(this.ordinal()){
            case 0:
                return "Sunday";
            case 1:
                return "Monday";
            case 2:
                return "Tuesday";
            case 3:
                return "Wednesday";
            case 4:
                return "Thursday";
            case 5:
                return "Friday";
            case 6:
                return "Saturday";
        }
        return super.toString();
    }

}