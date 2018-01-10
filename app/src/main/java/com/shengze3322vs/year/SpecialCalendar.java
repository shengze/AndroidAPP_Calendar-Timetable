package com.shengze3322vs.year;

import java.util.Calendar;

/**
 * Created by Administrator on 2017/9/12 0012.
 */

public class SpecialCalendar {

    private int daysInMonth = 0;
    private int dayInWeek = 0;

    public boolean isLeapYear(int year){
        if(year % 400 == 0 || (year % 4 == 0 && year % 100 != 0))
            return true;
        return false;
    }

    public int getDaysInMonth(boolean isLeapyear, int month){
        switch (month){
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                daysInMonth = 31;
                return daysInMonth;
            case 4:
            case 6:
            case 9:
            case 11:
                daysInMonth = 30;
                return daysInMonth;
            case 2:
                if(isLeapyear){
                    daysInMonth = 29;
                }else {
                    daysInMonth = 28;
                }
                return daysInMonth;
        }
        return daysInMonth;
    }

    public int getDayInWeek(int year, int month){
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, 1);
        dayInWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
        return dayInWeek;
    }

    public int getEasterMonth(int year){
        int n = year-1900;
        int a = n % 19;
        int q = n / 4;
        int b = (7*a + 1) / 19;
        int m = (11*a + 4 - b) % 29;
        int w = (n+q+31-m) % 7;
        int d = 25 - m - w;
        return d > 0 ? 4 : 3;
    }
    public int getEasterDay(int year){
        int n = year-1900;
        int a = n % 19;
        int q = n / 4;
        int b = (7*a + 1) / 19;
        int m = (11*a + 4 - b) % 29;
        int w = (n+q+31-m) % 7;
        int d = 25 - m - w;
        return d > 0 ? d : (d == 0 ? 31 : -d);
    }

}
