package com.shengze3322vs.year;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.shengze3322vs.year.EventDataBase.*;

public class MainActivity_DayEventList extends AppCompatActivity {

    private MainActivity_UpdateEvents callBetween;
    private ArrayList<String> titles;
    private ArrayList<String> times;
    private ArrayList<Integer> colors;
    private ArrayList<String> ids;
    private String[] eventTitle;
    private String[] eventTime;
    private Integer[] eventColor;
    private String[] eventid;

    private String dayInfo;
    private int position_limit = 0;
    private SpecialCalendar sc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__day_event_list);

        ActionBar ab = getSupportActionBar();
        if(ab != null){
            ab.setDisplayHomeAsUpEnabled(true);
        }

        callBetween = new MainActivity_UpdateEvents();
        dayInfo = SmartStackClass.smartstack.pop();
        //Toast.makeText(MainActivity_DayEventList.this, dayInfo, Toast.LENGTH_SHORT).show();

        sc = new SpecialCalendar();

        titles = new ArrayList<>();
        times = new ArrayList<>();
        colors = new ArrayList<>();
        ids = new ArrayList<>();
        setEventsList(dayInfo);

        ListAdapter adapter = new CustomListAdapter(this, eventTitle, eventTime, eventColor);
        ListView shengzeCustomList = (ListView)findViewById(R.id.CustomList);
        shengzeCustomList.setAdapter(adapter);

        shengzeCustomList.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(position < position_limit) {
                            SmartStackClass ssc = new SmartStackClass();
                            String id_send = eventid[position] + "#1";
                            ssc.smartstack.push(id_send);
                            Intent i = new Intent(MainActivity_DayEventList.this, MainActivity_UpdateEvents.class);
                            startActivity(i);
                        }
                    }
                }
        );

    }

    @Override
    protected void onResume() {
        super.onResume();

        titles = new ArrayList<>();
        times = new ArrayList<>();
        colors = new ArrayList<>();
        ids = new ArrayList<>();
        setEventsList(dayInfo);

        ListAdapter adapter = new CustomListAdapter(this, eventTitle, eventTime, eventColor);
        ListView shengzeCustomList = (ListView)findViewById(R.id.CustomList);
        shengzeCustomList.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void setEventsList(String dayInfo){
        EventDataBase edb = EventDataBase.getInstance(MainActivity_DayEventList.this);
        SQLiteDatabase dbThisWeek = edb.getMyWritableDataBase();
        Cursor cc = dbThisWeek.rawQuery("SELECT " + COLUMN_ID + ", " + COLUMN_TITLE + ", " + COLUMN_STIME + ", " + COLUMN_ETIME + ", " + COLUMN_SDATE + ", " + COLUMN_EDATE + ", " + COLUMN_COLOR + ", " + COLUMN_REPEAT + " FROM " + TABLE_EVENTS + ";", null);

        if(cc != null){
            if(cc.moveToFirst()){
                do {
                    String repeat = cc.getString(cc.getColumnIndex(COLUMN_REPEAT));
                    String startdate = cc.getString(cc.getColumnIndex(COLUMN_SDATE));
                    String enddate = cc.getString(cc.getColumnIndex(COLUMN_EDATE));
                    if(repeat.equals("Everyday")) {
                        if (callBetween.inBetween(startdate, enddate, dayInfo)) {
                            int color_ = Integer.parseInt(cc.getString(cc.getColumnIndex(COLUMN_COLOR)));
                            String title_ = cc.getString(cc.getColumnIndex(COLUMN_TITLE));
                            String time_ = cc.getString(cc.getColumnIndex(COLUMN_STIME)) + " - " + cc.getString(cc.getColumnIndex(COLUMN_ETIME));
                            String id_ = cc.getString(cc.getColumnIndex(COLUMN_ID));

                            titles.add(title_);
                            times.add(time_);
                            colors.add(color_);
                            ids.add(id_);
                        }
                    }else if(repeat.equals("Never")){
                        if (callBetween.inBetween(startdate, startdate, dayInfo)) {
                            int color_ = Integer.parseInt(cc.getString(cc.getColumnIndex(COLUMN_COLOR)));
                            String title_ = cc.getString(cc.getColumnIndex(COLUMN_TITLE));
                            String time_ = cc.getString(cc.getColumnIndex(COLUMN_STIME)) + " - " + cc.getString(cc.getColumnIndex(COLUMN_ETIME));
                            String id_ = cc.getString(cc.getColumnIndex(COLUMN_ID));

                            titles.add(title_);
                            times.add(time_);
                            colors.add(color_);
                            ids.add(id_);
                        }
                    }else {
                        if (callBetween.inBetween(startdate, enddate, dayInfo)) {
                            int cur_y = Integer.parseInt(dayInfo.split("-")[0]);
                            int cur_m = Integer.parseInt(dayInfo.split("-")[1]);
                            int cur_d = Integer.parseInt(dayInfo.split("-")[2]);
                            int dayInWeek_cur = sc.getDayInWeek(cur_y, cur_m);
                            int start_y = Integer.parseInt(startdate.split("-")[0]);
                            int start_m = Integer.parseInt(startdate.split("-")[1]);
                            int start_d = Integer.parseInt(startdate.split("-")[2]);
                            int dayInWeek_start = sc.getDayInWeek(start_y, start_m);

                            if((cur_d+dayInWeek_cur)%7 == (start_d+dayInWeek_start)%7) {
                                int color_ = Integer.parseInt(cc.getString(cc.getColumnIndex(COLUMN_COLOR)));
                                String title_ = cc.getString(cc.getColumnIndex(COLUMN_TITLE));
                                String time_ = cc.getString(cc.getColumnIndex(COLUMN_STIME)) + " - " + cc.getString(cc.getColumnIndex(COLUMN_ETIME));
                                String id_ = cc.getString(cc.getColumnIndex(COLUMN_ID));

                                titles.add(title_);
                                times.add(time_);
                                colors.add(color_);
                                ids.add(id_);
                            }
                        }
                    }
                }while (cc.moveToNext());
            }
            cc.close();
        }
        position_limit = ids.size();

        int y_c = Integer.parseInt(dayInfo.split("-")[0]);
        int m_c = Integer.parseInt(dayInfo.split("-")[1]);
        int d_c = Integer.parseInt(dayInfo.split("-")[2]);
        int dayInWeek = sc.getDayInWeek(y_c, m_c);
        //new year's day
        if(m_c == 1 && d_c == 1){
            times.add("New Year's Day");
            titles.add("");
            colors.add(Color.GRAY);
            ids.add("");
        }
        //christmas day
        if(m_c == 12 && d_c == 25){
            times.add("Christmas Day");
            titles.add("");
            colors.add(Color.GRAY);
            ids.add("");
        }
        //mother's day
        if(m_c == 5 && d_c == (dayInWeek > 0 ? 14 - dayInWeek + 1 : 7 - dayInWeek + 1)){
            times.add("Mother's Day");
            titles.add("");
            colors.add(Color.GRAY);
            ids.add("");
        }
        //father's day
        if(m_c == 6 && d_c == (dayInWeek > 0 ? 21 - dayInWeek + 1 : 14 - dayInWeek + 1)){
            times.add("Father's Day");
            titles.add("");
            colors.add(Color.GRAY);
            ids.add("");
        }
        //thanksgiving
        if(m_c == 11 && d_c == (dayInWeek > 4 ? 28 - dayInWeek + 5 : 21 - dayInWeek + 5)){
            times.add("Thanksgiving");
            titles.add("");
            colors.add(Color.GRAY);
            ids.add("");
        }
        //valentine's day
        if(m_c == 2 && d_c == 14){
            times.add("Valentine's Day");
            titles.add("");
            colors.add(Color.GRAY);
            ids.add("");
        }
        //halloween
        if(m_c == 10 && d_c == 31){
            times.add("Halloween");
            titles.add("");
            colors.add(Color.GRAY);
            ids.add("");
        }
        //easter
        int easter_m = sc.getEasterMonth(y_c);
        int easter_d = sc.getEasterDay(y_c);
        if(m_c == easter_m && d_c == easter_d){
            times.add("Easter");
            titles.add("");
            colors.add(Color.GRAY);
            ids.add("");
        }
        //independence day
        if(m_c == 7 && d_c == 4){
            times.add("Independence Day");
            titles.add("");
            colors.add(Color.GRAY);
            ids.add("");
        }
        //saint patrick's day
        if(m_c == 3 && d_c == 17){
            times.add("Saint Patrick's Day");
            titles.add("");
            colors.add(Color.GRAY);
            ids.add("");
        }
        //labor day
        if(m_c == 9 && d_c == (dayInWeek > 1 ? 7 - dayInWeek + 2 : 0 - dayInWeek + 2)){
            times.add("Labor Day");
            titles.add("");
            colors.add(Color.GRAY);
            ids.add("");
        }
        //veterans day
        if(m_c == 11 && d_c == 11){
            times.add("Veterans Day");
            titles.add("");
            colors.add(Color.GRAY);
            ids.add("");
        }
        //memorial day
        if(m_c == 5 && d_c == (dayInWeek < 6 ? 31 - dayInWeek -1 : 31)){
            times.add("Memorial Day");
            titles.add("");
            colors.add(Color.GRAY);
            ids.add("");
        }
        //Martin Luther King, Jr. Day
        if(m_c == 1 && d_c == (dayInWeek > 1 ? 21 - dayInWeek + 2 : 14 - dayInWeek + 2)){
            times.add("Martin Luther King, Jr. Day");
            titles.add("");
            colors.add(Color.GRAY);
            ids.add("");
        }
        //presidents' day
        if(m_c == 2 && d_c == (dayInWeek > 1 ? 21 - dayInWeek + 2 : 14 - dayInWeek + 2)){
            times.add("Presidents' Day");
            titles.add("");
            colors.add(Color.GRAY);
            ids.add("");
        }
        //columbus day
        if(m_c == 10 && d_c == (dayInWeek > 1 ? 14 - dayInWeek + 2 : 7 - dayInWeek + 2)){
            times.add("Columbus Day");
            titles.add("");
            colors.add(Color.GRAY);
            ids.add("");
        }
        //election day
        if(m_c == 11 && d_c == (dayInWeek > 2 ? 7 - dayInWeek + 3 : 0 - dayInWeek + 3)){
            times.add("Election Day");
            titles.add("");
            colors.add(Color.GRAY);
            ids.add("");
        }
        //christmas eve
        if(m_c == 12 && d_c == 24){
            times.add("Christmas Eve");
            titles.add("");
            colors.add(Color.GRAY);
            ids.add("");
        }
        //new year's eve
        if(m_c == 12 && d_c == 31){
            times.add("New Year's Eve");
            titles.add("");
            colors.add(Color.GRAY);
            ids.add("");
        }

        eventid = ids.toArray(new String[ids.size()]);
        eventTitle = titles.toArray(new String[titles.size()]);
        eventTime = times.toArray(new String[times.size()]);
        eventColor = colors.toArray(new Integer[colors.size()]);

    }
}
