package com.shengze3322vs.year;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.text.SimpleDateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.shengze3322vs.year.views.CustomView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import static com.shengze3322vs.year.EventDataBase.COLUMN_COLOR;
import static com.shengze3322vs.year.EventDataBase.COLUMN_EDATE;
import static com.shengze3322vs.year.EventDataBase.COLUMN_REPEAT;
import static com.shengze3322vs.year.EventDataBase.COLUMN_SDATE;
import static com.shengze3322vs.year.EventDataBase.TABLE_EVENTS;

/**
 * Created by Administrator on 2017/9/12 0012.
 */

public class CalendarAdapter extends BaseAdapter {

    private String headYear = "";
    private String headMonth = "";

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
    private String sysDate = "";
    private String sys_year = "";
    private String sys_month = "";
    private String sys_day = "";

    private int currentDayFlag = -1;
    private String currentYear = "";
    private String currentMonth = "";
    private String currentDay = "";

    private int daysInMonth = 0;
    private int dayInWeek = 0;
    private int daysInLastMonth = 0;
    private boolean isLeapyear = false;

    private Context context;
    private Resources res;
    private SpecialCalendar sc;
    private String[] dayNumber = new String[42];

    //private HashMap<Integer, HashSet<Integer>> db_color_draw;
    private HashSet<Integer>[] color_to_draw;

    public CalendarAdapter(){
        Date date = new Date();
        sysDate = sdf.format(date);
        sys_year = sysDate.split("-")[0];
        sys_month = sysDate.split("-")[1];
        sys_day = sysDate.split("-")[2];
    }

    public CalendarAdapter(Context context, Resources rs, int jumpMonth, int jumpYear, int year_c, int month_c, int day_c){

        this();
        this.context = context;
        sc = new SpecialCalendar();
        this.res = rs;

        int stepYear = year_c + jumpYear;
        int stepMonth = month_c + jumpMonth;
        if(stepMonth > 0){
            if(stepMonth % 12 == 0){
                stepYear = year_c + stepMonth / 12 - 1;
                stepMonth = 12;
            }else{
                stepYear = year_c + stepMonth / 12;
                stepMonth = stepMonth % 12;
            }
        } else {
            stepYear = year_c - 1 + stepMonth / 12;
            stepMonth = stepMonth % 12 + 12;
        }

        currentYear = String.valueOf(stepYear);
        currentMonth = String.valueOf(stepMonth);
        currentDay = String.valueOf(day_c);

        getCalendar(Integer.parseInt(currentYear), Integer.parseInt(currentMonth));

        //db_color_draw = new HashMap<>();
        color_to_draw = new HashSet[42];
        for(int i = 0; i < 42; i++){
            HashSet<Integer> hs = new HashSet<>();
            color_to_draw[i] = hs;
        }
        paintColor(Integer.parseInt(currentYear), Integer.parseInt(currentMonth));

    }

    @Override
    public int getCount() {
        return dayNumber.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.calendar_item, null);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.tvtext);
        String d = dayNumber[position];

        textView.setText(d);
        textView.setTextColor(Color.GREEN);

        if(position >= daysInMonth + dayInWeek){
            return convertView;
        }


        if(position < daysInMonth + dayInWeek && position >= dayInWeek){
            textView.setTextColor(Color.BLACK);
            /*if(db_color_draw.containsKey(position)){
                for(int readyColor : db_color_draw.get(position))
                    cv.setColor(readyColor);
            }*/
            CustomView cv = (CustomView) convertView.findViewById(R.id.customview_initem);
            if(color_to_draw[position].size() != 0){
                for(int readyColor : color_to_draw[position])
                    cv.setColor(readyColor);
            }
            //new year's day
            if(currentMonth.equals("1") && position-dayInWeek == 0)
                cv.setColor(Color.GRAY);
            //christmas day
            if(currentMonth.equals("12") && position-dayInWeek == 24)
                cv.setColor(Color.GRAY);
            //mother's day
            if(currentMonth.equals("5") && position == 7*(2 - (dayInWeek > 0 ? 0 : 1)))
                cv.setColor(Color.GRAY);
            //father's day
            if(currentMonth.equals("6") && position == 7*(3 - (dayInWeek > 0 ? 0 : 1)))
                cv.setColor(Color.GRAY);
            //thanksgiving
            if(currentMonth.equals("11") && position == 4+7*(4 - (dayInWeek > 4 ? 0 : 1)))
                cv.setColor(Color.GRAY);
            //valentine's day
            if(currentMonth.equals("2") && position-dayInWeek == 13)
                cv.setColor(Color.GRAY);
            //halloween
            if(currentMonth.equals("10") && position-dayInWeek == 30)
                cv.setColor(Color.GRAY);
            //easter
            int easter_m = sc.getEasterMonth(Integer.parseInt(currentYear));
            int easter_d = sc.getEasterDay(Integer.parseInt(currentYear));
            if(currentMonth.equals(Integer.toString(easter_m)) && position-dayInWeek == easter_d-1)
                cv.setColor(Color.GRAY);
            //independence day
            if(currentMonth.equals("7") && position-dayInWeek == 3)
                cv.setColor(Color.GRAY);
            //saint patrick's day
            if(currentMonth.equals("3") && position-dayInWeek == 16)
                cv.setColor(Color.GRAY);
            //labor day
            if(currentMonth.equals("9") && position == 1+7*(1 - (dayInWeek > 1 ? 0 : 1)))
                cv.setColor(Color.GRAY);
            //veterans day
            if(currentMonth.equals("11") && position-dayInWeek == 10)
                cv.setColor(Color.GRAY);
            //memorial day
            if(currentMonth.equals("5") && position == (dayInWeek < 6 ? 1+7*4 : 1+7*5))
                cv.setColor(Color.GRAY);
            //Martin Luther King, Jr. Day
            if(currentMonth.equals("1") && position == 1+7*(3 - (dayInWeek > 1 ? 0 : 1)))
                cv.setColor(Color.GRAY);
            //presidents' day
            if(currentMonth.equals("2") && position == 1+7*(3 - (dayInWeek > 1 ? 0 : 1)))
                cv.setColor(Color.GRAY);
            //columbus day
            if(currentMonth.equals("10") && position == 1+7*(2 - (dayInWeek > 1 ? 0 : 1)))
                cv.setColor(Color.GRAY);
            //election day
            if(currentMonth.equals("11") && position == 2+7*(1 - (dayInWeek > 2 ? 0 : 1)))
                cv.setColor(Color.GRAY);
            //christmas eve
            if(currentMonth.equals("12") && position-dayInWeek == 23)
                cv.setColor(Color.GRAY);
            //new year's eve
            if(currentMonth.equals("12") && position-dayInWeek == 30)
                cv.setColor(Color.GRAY);

        }


        if(currentDayFlag == position){
            textView.setTextColor(Color.RED);
            textView.setTypeface(null, Typeface.BOLD);

            //CustomView cv = (CustomView) convertView.findViewById(R.id.customview_initem);
            //cv.setColor(Color.RED);
        }

        return convertView;
    }

    public void getCalendar(int year, int month){
        isLeapyear = sc.isLeapYear(year);
        daysInMonth = sc.getDaysInMonth(isLeapyear, month);
        dayInWeek = sc.getDayInWeek(year, month);
        daysInLastMonth = sc.getDaysInMonth(isLeapyear, month-1);
        Log.d("DAY", isLeapyear+" ======  "+daysInMonth+"  ============  "+dayInWeek+"  =========   "+daysInLastMonth);
        getWeek(year, month);
    }

    private void getWeek(int year, int month){
        int j = 1;

        setHeadYear(String.valueOf(year));
        setHeadMonth(String.valueOf(month));

        for(int i = 0; i < dayNumber.length; i++){
            if(i < dayInWeek){
                int temp = daysInLastMonth - dayInWeek + 1;
                dayNumber[i] = Integer.toString(temp + i);
            }else if(i < daysInMonth + dayInWeek){
                String day = String.valueOf(i - dayInWeek + 1);
                dayNumber[i] = day;

                if(sys_year.equals(String.valueOf(year)) && sys_month.equals(String.valueOf(month)) && sys_day.equals(day)){
                    currentDayFlag = i;
                }

            }else{
                dayNumber[i] = Integer.toString(j);
                j++;
            }
        }
        String abc = "";
        for(int i = 0; i < dayNumber.length; i++){
            abc = abc+dayNumber[i]+":";
        }
        Log.d("DAYNUMBER",abc);
    }

    public void setHeadYear(String headYear){
        this.headYear = headYear;
    }
    public void setHeadMonth(String headMonth){
        this.headMonth = headMonth;
    }
    public String getHeadYear(){
        return headYear;
    }
    public String getHeadMonth(){
        return headMonth;
    }

    public void paintColor(int year_now, int month_now){
        EventDataBase DBHandler = EventDataBase.getInstance(context);
        SQLiteDatabase myEventDB = DBHandler.getMyWritableDataBase();
        Cursor cc = myEventDB.rawQuery("SELECT " + COLUMN_SDATE + ", " + COLUMN_EDATE + ", " + COLUMN_COLOR + ", " + COLUMN_REPEAT + " FROM " + TABLE_EVENTS + ";", null);
        if(cc != null){
            if(cc.moveToFirst()){
                do{
                    String startD = cc.getString(cc.getColumnIndex(COLUMN_SDATE));
                    int year_s = Integer.parseInt(startD.split("-")[0]);
                    int month_s = Integer.parseInt(startD.split("-")[1]);
                    int day_s = Integer.parseInt(startD.split("-")[2]);
                    String endD = cc.getString(cc.getColumnIndex(COLUMN_EDATE));
                    int year_e = Integer.parseInt(endD.split("-")[0]);
                    int month_e = Integer.parseInt(endD.split("-")[1]);
                    int day_e = Integer.parseInt(endD.split("-")[2]);

                    int color = Integer.parseInt(cc.getString(cc.getColumnIndex(COLUMN_COLOR)));

                    String repeatW = cc.getString(cc.getColumnIndex(COLUMN_REPEAT));
                    int step = 1;
                    switch(repeatW) {
                        case "Never":
                            step = Integer.MAX_VALUE;
                            break;
                        case "Once Per Week":
                            step = 7;
                            break;
                        case "Everyday":
                            step = 1;
                            break;
                    }

                    if(step == 1) {
                        if (year_now >= year_s && month_now >= month_s && year_now <= year_e && month_now <= month_e) {
                            int start = (year_s < year_now || month_s < month_now) ? dayInWeek : dayInWeek + day_s - 1;
                            int end = (year_e > year_now || month_e > month_now) ? dayInWeek + daysInMonth - 1 : dayInWeek + day_e - 1;
                            for (int i = start; i <= end; i += step) {
                                /*HashSet<Integer> addC = new HashSet<>();
                                if (!db_color_draw.containsKey(i)) {
                                    db_color_draw.put(i, addC);
                                }
                                addC = db_color_draw.get(i);
                                if (!addC.contains(color)) {
                                    addC.add(color);
                                    db_color_draw.put(i, addC);
                                }*/
                                HashSet<Integer> addC;
                                addC = color_to_draw[i];
                                if(!color_to_draw[i].contains(color)){
                                    addC.add(color);
                                    color_to_draw[i] = addC;
                                }
                            }
                        }
                    }else if(step == Integer.MAX_VALUE){
                        if(year_now == year_s && month_now == month_s){
                            /*HashSet<Integer> addC = new HashSet<>();
                            if (!db_color_draw.containsKey(dayInWeek+day_s-1)) {
                                db_color_draw.put(dayInWeek+day_s-1, addC);
                            }
                            addC = db_color_draw.get(dayInWeek+day_s-1);
                            if (!addC.contains(color)) {
                                addC.add(color);
                                db_color_draw.put(dayInWeek+day_s-1, addC);
                            }*/
                            HashSet<Integer> addC;
                            addC = color_to_draw[dayInWeek+day_s-1];
                            if(!color_to_draw[dayInWeek+day_s-1].contains(color)){
                                addC.add(color);
                                color_to_draw[dayInWeek+day_s-1] = addC;
                            }
                        }
                    }else{
                        int weekN = (sc.getDayInWeek(year_s, month_s) + day_s - 1) % 7;
                        if (year_now >= year_s && month_now >= month_s && year_now <= year_e && month_now <= month_e) {
                            int start = (year_s < year_now || month_s < month_now) ? dayInWeek : dayInWeek + day_s - 1;
                            int end = (year_e > year_now || month_e > month_now) ? dayInWeek + daysInMonth - 1 : dayInWeek + day_e - 1;
                            for (int i = start; i <= end; i ++) {
                                if(weekN == i % 7) {
                                    /*HashSet<Integer> addC = new HashSet<>();
                                    if (!db_color_draw.containsKey(i)) {
                                        db_color_draw.put(i, addC);
                                    }
                                    addC = db_color_draw.get(i);
                                    if (!addC.contains(color)) {
                                        addC.add(color);
                                        db_color_draw.put(i, addC);
                                    }*/
                                    HashSet<Integer> addC;
                                    addC = color_to_draw[i];
                                    if(!color_to_draw[i].contains(color)){
                                        addC.add(color);
                                        color_to_draw[i] = addC;
                                    }
                                }
                            }
                        }
                    }


                }while (cc.moveToNext());
            }
            cc.close();
        }
    }

}
