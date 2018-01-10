package com.shengze3322vs.year;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.shengze3322vs.year.EventDataBase.*;

public class MainActivity_TimeTable extends AppCompatActivity {

    public ArrayList<Map<String, String>> dataList = new ArrayList<>();
    public ArrayList<Map<String, String>> timeList = new ArrayList<>();
    public ArrayList<Map<String, String>> selectedList = new ArrayList<>();
    private MyHorizontalScrollView sc_title;
    private MyHorizontalScrollView sc_data;
    private LinearLayout ll_left;
    private LinearLayout ll_data;

    private int TitleBarLength = 465;

    private int year_s = 0;
    private int month_s = 0;
    private int day_s = 0;
    private int year_e = 0;
    private int month_e = 0;
    private int day_e = 0;
    private int year_c = 0;
    private int month_c = 0;
    private int day_c = 0;
    private int dayInWeek = 0;
    private SpecialCalendar sc;

    private RelativeLayout ll_col0;
    private RelativeLayout ll_col1;
    private RelativeLayout ll_col2;
    private RelativeLayout ll_col3;
    private RelativeLayout ll_col4;
    private RelativeLayout ll_col5;
    private RelativeLayout ll_col6;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.add_icon:
                    Intent i = new Intent(MainActivity_TimeTable.this, MainActivity_AddEvents.class);
                    startActivity(i);
                    return true;
                case R.id.list_icon:
                    Toast.makeText(MainActivity_TimeTable.this, "Have a Nice Day!", Toast.LENGTH_SHORT).show();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__time_table);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        String currentDate = sdf.format(date);
        year_c = Integer.parseInt(currentDate.split("-")[0]);
        month_c = Integer.parseInt(currentDate.split("-")[1]);
        day_c = Integer.parseInt(currentDate.split("-")[2]);

        sc = new SpecialCalendar();

        Calendar cal = Calendar.getInstance();
        cal.set(year_c, month_c - 1, day_c);
        dayInWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;

        setStartDate();
        setEndDate();
        addTextToTopTextView();

        initData();

        ll_data = (LinearLayout)findViewById(R.id.ll_data);
        ll_left = (LinearLayout)findViewById(R.id.ll_left);
        ll_col0 = (RelativeLayout)findViewById(R.id.time_table_col0);
        ll_col1 = (RelativeLayout)findViewById(R.id.time_table_col1);
        ll_col2 = (RelativeLayout)findViewById(R.id.time_table_col2);
        ll_col3 = (RelativeLayout)findViewById(R.id.time_table_col3);
        ll_col4 = (RelativeLayout)findViewById(R.id.time_table_col4);
        ll_col5 = (RelativeLayout)findViewById(R.id.time_table_col5);
        ll_col6 = (RelativeLayout)findViewById(R.id.time_table_col6);
        sc_title = (MyHorizontalScrollView)findViewById(R.id.sc_title);
        sc_data = (MyHorizontalScrollView)findViewById(R.id.sc_data);


        sc_data.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener(){
            @Override
            public void onScrollChanged() {
                sc_data.scrollTo(sc_data.getScrollX(), 0);
                sc_title.scrollTo(sc_data.getScrollX(), 0);
            }
        });

        renderTable();

        TextView bt_nweek = (TextView) findViewById(R.id.right_img);
        TextView bt_lweek = (TextView) findViewById(R.id.left_img);
        bt_lweek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int k = getMaxDayOfMonth(month_s-1);
                day_c = day_s-1 > 0 ? day_s-1 : k;
                month_c = (day_c == k && day_s-1 <= 0) ? (month_s-1 > 0 ? month_s-1 : 12) : month_s;
                year_c = (month_c == 12 && day_c == 31) ? year_s - 1 : year_s;
                dayInWeek = 6;
                setStartDate();
                setEndDate();
                addTextToTopTextView();

                ll_col0.removeAllViews();
                ll_col1.removeAllViews();
                ll_col2.removeAllViews();
                ll_col3.removeAllViews();
                ll_col4.removeAllViews();
                ll_col5.removeAllViews();
                ll_col6.removeAllViews();
                getDataColumn();

                setEventListenerForCol();
            }
        });
        bt_nweek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int k = getMaxDayOfMonth(month_e);
                day_c = day_e+1 > k ? 1 : day_e+1;
                month_c = day_c == 1 ? (month_e+1 > 12 ? 1 : month_e+1) : month_e;
                year_c = (month_c == 1 && day_c == 1) ? year_e + 1 : year_e;
                dayInWeek = 0;
                setStartDate();
                setEndDate();
                addTextToTopTextView();

                ll_col0.removeAllViews();
                ll_col1.removeAllViews();
                ll_col2.removeAllViews();
                ll_col3.removeAllViews();
                ll_col4.removeAllViews();
                ll_col5.removeAllViews();
                ll_col6.removeAllViews();
                getDataColumn();

                setEventListenerForCol();
            }
        });

        setEventListenerForCol();


    }

    @Override
    protected void onResume() {
        super.onResume();

        ll_col0.removeAllViews();
        ll_col1.removeAllViews();
        ll_col2.removeAllViews();
        ll_col3.removeAllViews();
        ll_col4.removeAllViews();
        ll_col5.removeAllViews();
        ll_col6.removeAllViews();
        getDataColumn();

        setEventListenerForCol();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_main_timetable, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.switchmode2){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void initData() {
        //title content
        for (int i = 0; i < 7; i++) {
            Map<String, String> map = new HashMap<>();
            switch (i){
                case 0:
                    map.put("text", "Sun");
                    break;
                case 1:
                    map.put("text", "Mon");
                    break;
                case 2:
                    map.put("text", "Tue");
                    break;
                case 3:
                    map.put("text", "Wed");
                    break;
                case 4:
                    map.put("text", "Thu");
                    break;
                case 5:
                    map.put("text", "Fri");
                    break;
                case 6:
                    map.put("text", "Sat");
                    break;
            }
            map.put("pid", String.valueOf(i));
            dataList.add(map);
        }

        //time bar content
        for (int i = 0; i < 24; i++) {
            Map<String, String> map = new HashMap<>();
            switch (i){
                case 0:
                    map.put("text", "06:00");
                    break;
                case 1:
                    map.put("text", "07:00");
                    break;
                case 2:
                    map.put("text", "08:00");
                    break;
                case 3:
                    map.put("text", "09:00");
                    break;
                case 4:
                    map.put("text", "10:00");
                    break;
                case 5:
                    map.put("text", "11:00");
                    break;
                case 6:
                    map.put("text", "12:00");
                    break;
                case 7:
                    map.put("text", "13:00");
                    break;
                case 8:
                    map.put("text", "14:00");
                    break;
                case 9:
                    map.put("text", "15:00");
                    break;
                case 10:
                    map.put("text", "16:00");
                    break;
                case 11:
                    map.put("text", "17:00");
                    break;
                case 12:
                    map.put("text", "18:00");
                    break;
                case 13:
                    map.put("text", "19:00");
                    break;
                case 14:
                    map.put("text", "20:00");
                    break;
                case 15:
                    map.put("text", "21:00");
                    break;
                case 16:
                    map.put("text", "22:00");
                    break;
                case 17:
                    map.put("text", "23:00");
                    break;
                case 18:
                    map.put("text", "00:00");
                    break;
                case 19:
                    map.put("text", "01:00");
                    break;
                case 20:
                    map.put("text", "02:00");
                    break;
                case 21:
                    map.put("text", "03:00");
                    break;
                case 22:
                    map.put("text", "04:00");
                    break;
                case 23:
                    map.put("text", "05:00");
                    break;
            }
            map.put("tid", String.valueOf(i));
            timeList.add(map);
        }

        //合并区域
        Map<String, String> map1 = new HashMap<>();
        map1.put("pid", String.valueOf(1));
        map1.put("start_tid", String.valueOf(4));
        map1.put("end_tid", String.valueOf(5));
        //map1.put("status", String.valueOf(STATUS_ONE));
        selectedList.add(map1);

        //合并了3行
        Map<String, String> map2 = new HashMap<>();
        map2.put("pid", String.valueOf(0));
        map2.put("start_tid", String.valueOf(4));
        map2.put("end_tid", String.valueOf(6));
        //map2.put("status", String.valueOf(STATUS_TWO));
        selectedList.add(map2);

    }

    private void renderTable() {
        //initialize time
        int timeCount = timeList.size();
        for (int i = 0; i < timeCount; i++) {
            ll_left.addView(getTimeRow(i));

        }

        //initialize title
        sc_title.addView(getTitleRow());

        //initialize events
        getDataColumn();

    }

    private View getTimeRow(int i){
        String returnV = timeList.get(i).get("text");
        TextView tv = new TextView(this);
        tv.setText(returnV);
        Resources r = getResources();
        float px_h = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) 90, r.getDisplayMetrics());
        tv.setHeight((int) px_h);
        tv.setBackgroundColor(Color.rgb(211, 211, 211));
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        return tv;
    }

    private View getTitleRow(){
        String returnV = "";
        for(int i = 0; i < dataList.size(); i++){
            returnV += "                " + dataList.get(i).get("text") + "                ";
        }
        TextView tv = new TextView(this);
        tv.setText(returnV);
        TitleBarLength = tv.getWidth();
        return tv;
    }

    private void getDataColumn(){
        EventDataBase edb = EventDataBase.getInstance(MainActivity_TimeTable.this);
        SQLiteDatabase dbThisWeek = edb.getMyWritableDataBase();
        Cursor cc = dbThisWeek.rawQuery("SELECT " + COLUMN_TITLE + ", " + COLUMN_STIME + ", " + COLUMN_ETIME + ", " + COLUMN_SDATE + ", " + COLUMN_EDATE + ", " + COLUMN_COLOR + ", " + COLUMN_REPEAT + " FROM " + TABLE_EVENTS + ";", null);

        boolean[] safty = new boolean[7];
        int year_cur = year_s;
        int month_cur = month_s;
        int day_cur = day_s;
        for(int i = 0; i < 7; i++){
            if(cc != null) {
                if (cc.moveToFirst()) {
                    do {
                        String startD = cc.getString(cc.getColumnIndex(COLUMN_SDATE));
                        int year_start = Integer.parseInt(startD.split("-")[0]);
                        int month_start = Integer.parseInt(startD.split("-")[1]);
                        int day_start = Integer.parseInt(startD.split("-")[2]);
                        String endD = cc.getString(cc.getColumnIndex(COLUMN_EDATE));
                        int year_end = Integer.parseInt(endD.split("-")[0]);
                        int month_end = Integer.parseInt(endD.split("-")[1]);
                        int day_end = Integer.parseInt(endD.split("-")[2]);

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

                        int weekN = (sc.getDayInWeek(year_start, month_start) + day_start - 1) % 7;

                        boolean inbetween = false;
                        if(inBetween(year_start,month_start,day_start,year_end,month_end,day_end,year_cur,month_cur,day_cur)){
                            inbetween = true;
                        }

                        if((inbetween && step == 1) ||
                                (step == Integer.MAX_VALUE && year_start == year_cur && month_start == month_cur && day_start == day_cur) ||
                                (step == 7 && weekN == ((sc.getDayInWeek(year_cur, month_cur) + day_cur -1) % 7) && inbetween)){
                            safty[i] = true;
                            String title = cc.getString(cc.getColumnIndex(COLUMN_TITLE));
                            String startT = cc.getString(cc.getColumnIndex(COLUMN_STIME));
                            int hour_s = Integer.parseInt(startT.split(" : ")[0]);
                            int minute_s = Integer.parseInt(startT.split(" : ")[1]);
                            String endT = cc.getString(cc.getColumnIndex(COLUMN_ETIME));
                            int hour_e = Integer.parseInt(endT.split(" : ")[0]);
                            int minute_e = Integer.parseInt(endT.split(" : ")[1]);

                            double topM, botM;
                            float tv_h;
                            if(hour_e >= 6){
                                botM = (hour_e - 6) * 90 + minute_e * 1.5;
                            }else{
                                botM = 1080 + hour_e * 90 + minute_e * 1.5;
                            }
                            if(hour_s >= 6){
                                topM = (hour_s - 6) * 90 + minute_s * 1.5;
                            }else{
                                topM = 1080 + hour_s * 90 + minute_s * 1.5;
                            }
                            Resources r = getResources();
                            float px_topM = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) topM, r.getDisplayMetrics());
                            float px_botM = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) botM, r.getDisplayMetrics());
                            float tv_w = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float)134, r.getDisplayMetrics());
                            tv_h = px_botM - px_topM;

                            int colorSet = Integer.parseInt(cc.getString(cc.getColumnIndex(COLUMN_COLOR)));
                            TextView tv = new TextView(this);
                            tv.setText(title + "\n\nFrom\n" + startT + "\nTo\n" + endT);
                            tv.setBackgroundColor(colorSet);
                            tv.setGravity(Gravity.CENTER);

                            RelativeLayout.LayoutParams params;
                            params = new RelativeLayout.LayoutParams((int) tv_w, (int) tv_h);
                            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                            params.topMargin = (int)px_topM;
                            switch (i){
                                case 0:
                                    //Toast.makeText(MainActivity_TimeTable.this, Integer.toString((int)tv_h), Toast.LENGTH_SHORT).show();
                                    ll_col0.addView(tv, params);
                                    break;
                                case 1:
                                    ll_col1.addView(tv, params);
                                    break;
                                case 2:
                                    ll_col2.addView(tv, params);
                                    break;
                                case 3:
                                    ll_col3.addView(tv, params);
                                    break;
                                case 4:
                                    ll_col4.addView(tv, params);
                                    break;
                                case 5:
                                    ll_col5.addView(tv, params);
                                    break;
                                case 6:
                                    ll_col6.addView(tv, params);
                                    break;
                            }
                        }

                    } while (cc.moveToNext());
                }
            }
            if(!safty[i]) {
                TextView tv = new TextView(this);
                Resources r = getResources();
                float topM = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float)2160, r.getDisplayMetrics());
                float tv_w = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float)134, r.getDisplayMetrics());
                RelativeLayout.LayoutParams params;
                params = new RelativeLayout.LayoutParams((int) tv_w, 0);
                params.topMargin = (int)topM;
                switch (i) {
                    case 0:
                        ll_col0.addView(tv, params);
                        break;
                    case 1:
                        ll_col1.addView(tv, params);
                        break;
                    case 2:
                        ll_col2.addView(tv, params);
                        break;
                    case 3:
                        ll_col3.addView(tv, params);
                        break;
                    case 4:
                        ll_col4.addView(tv, params);
                        break;
                    case 5:
                        ll_col5.addView(tv, params);
                        break;
                    case 6:
                        ll_col6.addView(tv, params);
                        break;
                }
            }
            int k = 0;
            switch (month_cur){
                case 1:
                    k = 31;
                    break;
                case 2:
                    k = sc.isLeapYear(year_cur) ? 29 : 28;
                    break;
                case 3:
                    k = 31;
                    break;
                case 4:
                    k = 30;
                    break;
                case 5:
                    k = 31;
                    break;
                case 6:
                    k = 30;
                    break;
                case 7:
                    k = 31;
                    break;
                case 8:
                    k = 31;
                    break;
                case 9:
                    k = 30;
                    break;
                case 10:
                    k = 31;
                    break;
                case 11:
                    k = 30;
                    break;
                case 12:
                    k = 31;
                    break;
            }
            day_cur = (day_cur + 1) > k ? 1 : day_cur + 1;
            month_cur = day_cur == 1 ? ((month_cur+1) > 12 ? 1 : month_cur+1) : month_cur;
            year_cur = (month_cur == 1 && day_cur == 1) ? year_cur + 1 : year_cur;
        }
        if(cc != null)
            cc.close();

    }

    public void addTextToTopTextView(){
        TextView view = (TextView) findViewById(R.id.tv_markDateOfTheWeek);
        StringBuffer textDate = new StringBuffer();
        textDate.append(Integer.toString(year_s)).append("-").append(Integer.toString(month_s)).append("-").append(Integer.toString(day_s)).append("\n").append("to\n").append(Integer.toString(year_e)).append("-").append(Integer.toString(month_e)).append("-").append(Integer.toString(day_e));
        view.setText(textDate);
        view.setTextColor(Color.BLACK);
        view.setTypeface(Typeface.DEFAULT_BOLD);
    }

    public void setStartDate(){
        if(day_c - dayInWeek > 0){
            day_s = day_c - dayInWeek;
            month_s = month_c;
            year_s = year_c;
        }else if(month_c - 1 <= 0){
            day_s = 31 + day_c - dayInWeek;
            month_s = 12;
            year_s = year_c - 1;
        }else{
            year_s = year_c;
            month_s = month_c - 1;
            switch (month_s){
                case 1:
                    day_s = 31 + day_c - dayInWeek;
                    break;
                case 2:
                    if(sc.isLeapYear(year_s))
                        day_s = 29 + day_c - dayInWeek;
                    else
                        day_s = 28 + day_c - dayInWeek;
                    break;
                case 3:
                    day_s = 31 + day_c - dayInWeek;
                    break;
                case 4:
                    day_s = 30 + day_c - dayInWeek;
                    break;
                case 5:
                    day_s = 31 + day_c - dayInWeek;
                    break;
                case 6:
                    day_s = 30 + day_c - dayInWeek;
                    break;
                case 7:
                    day_s = 31 + day_c - dayInWeek;
                    break;
                case 8:
                    day_s = 31 + day_c - dayInWeek;
                    break;
                case 9:
                    day_s = 30 + day_c - dayInWeek;
                    break;
                case 10:
                    day_s = 31 + day_c - dayInWeek;
                    break;
                case 11:
                    day_s = 30 + day_c - dayInWeek;
                    break;
                case 12:
                    day_s = 31 + day_c - dayInWeek;
                    break;
            }
        }
    }

    public void setEndDate(){
        int yy = getMaxDayOfMonth(month_s);

        if(day_s + 6 <= yy) {
            day_e = day_s + 6;
            month_e = month_s;
            year_e = year_s;
        }
        else if(month_s < 12){
            day_e = day_s + 6 - yy;
            month_e = month_s + 1;
            year_e = year_s;
        }
        else {
            day_e = day_s + 6 - yy;
            month_e = 1;
            year_e = year_s + 1;
        }

    }

    public boolean inBetween(int y_s, int m_s, int d_s, int y_e, int m_e, int d_e, int y_c, int m_c, int d_c){
        if(y_s <= y_c && y_e >= y_c){
            if(y_s == y_c && m_s <= m_c){
                if(m_s == m_c && d_s <= d_c){
                    if(y_e > y_c || (y_e == y_c && m_e > m_c) || (y_e == y_c && m_e == m_c && d_e >= d_c))
                        return true;
                }else if(m_s < m_c){
                    if(y_e > y_c || (y_e == y_c && m_e > m_c) || (y_e == y_c && m_e == m_c && d_e >= d_c))
                        return true;
                }
            }else if(y_s < y_c){
                if(y_e == y_c && m_e >= m_c){
                    if((m_e == m_c && d_e >= d_c) || m_e > m_c)
                        return true;
                }else if(y_e > y_c){
                    return true;
                }
            }
        }
        return false;
    }

    public int getMaxDayOfMonth(int mm){
        switch (mm){
            case 1:
                return 31;
            case 2:
                return sc.isLeapYear(year_s) ? 29 : 28;
            case 3:
                return 31;
            case 4:
                return 30;
            case 5:
                return 31;
            case 6:
                return 30;
            case 7:
                return 31;
            case 8:
                return 31;
            case 9:
                return 30;
            case 10:
                return 31;
            case 11:
                return 30;
            case 0:
                return 31;
        }
        return 31;
    }

    public void setEventListenerForCol(){
        for (int i = 0; i < ll_col0.getChildCount(); i++) {
            View v = ll_col0.getChildAt(i);
            if (v instanceof TextView) {
                TextView vv = (TextView) v;
                setOnClickListenerForEvents(vv,0);
            }
        }
        for (int i = 0; i < ll_col1.getChildCount(); i++) {
            View v = ll_col1.getChildAt(i);
            if (v instanceof TextView) {
                TextView vv = (TextView) v;
                setOnClickListenerForEvents(vv,1);
            }
        }
        for (int i = 0; i < ll_col2.getChildCount(); i++) {
            View v = ll_col2.getChildAt(i);
            if (v instanceof TextView) {
                TextView vv = (TextView) v;
                setOnClickListenerForEvents(vv,2);
            }
        }
        for (int i = 0; i < ll_col3.getChildCount(); i++) {
            View v = ll_col3.getChildAt(i);
            if (v instanceof TextView) {
                TextView vv = (TextView) v;
                setOnClickListenerForEvents(vv,3);
            }
        }
        for (int i = 0; i < ll_col4.getChildCount(); i++) {
            View v = ll_col4.getChildAt(i);
            if (v instanceof TextView) {
                TextView vv = (TextView) v;
                setOnClickListenerForEvents(vv,4);
            }
        }
        for (int i = 0; i < ll_col5.getChildCount(); i++) {
            View v = ll_col5.getChildAt(i);
            if (v instanceof TextView) {
                TextView vv = (TextView) v;
                setOnClickListenerForEvents(vv,5);
            }
        }
        for (int i = 0; i < ll_col6.getChildCount(); i++) {
            View v = ll_col6.getChildAt(i);
            if (v instanceof TextView) {
                TextView vv = (TextView) v;
                setOnClickListenerForEvents(vv,6);
            }
        }

    }

    public void setOnClickListenerForEvents(TextView v, final int day){
        if(v.getText().toString().equals(""))    return;
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mark = ((TextView) v).getText().toString().split("\nTo\n")[1];
                mark += "#2";
                int k = getMaxDayOfMonth(month_s);
                int day_send = (day_s+day) > k ? (day_s+day)%k : (day_s+day);
                int month_send = (day_send-day_s) < 0 ? (month_s+1 > 12 ? 1 : month_s+1) : month_s;
                int year_send = (month_send == 1 && day_send-day_s < 0) ? year_s+1 : year_s;
                String mark_day = Integer.toString(year_send) + "-" + Integer.toString(month_send) + "-" + Integer.toString(day_send);

                SmartStackClass ssc = new SmartStackClass();
                ssc.smartstack.push(mark);
                ssc.smartstack2.push(mark_day);
                Intent i = new Intent(MainActivity_TimeTable.this, MainActivity_UpdateEvents.class);
                startActivity(i);

                //Toast.makeText(MainActivity_TimeTable.this, mark, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
