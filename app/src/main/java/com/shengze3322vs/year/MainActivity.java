package com.shengze3322vs.year;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static com.shengze3322vs.year.EventDataBase.*;

public class MainActivity extends AppCompatActivity {

    private CalendarAdapter calV;
    private TextView topText;
    private GridView gridView;

    private EventDataBase DBHandler;
    private SQLiteDatabase myEventDB;

    private String currentDate = "";
    private int year_c = 0;
    private int month_c = 0;
    private int day_c = 0;
    private int dayInWeek;
    private int daysInMonth;
    private SpecialCalendar sc;
    private static int jumpMonth = 0;
    private static int jumpYear = 0;

    public MainActivity(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        currentDate = sdf.format(date);
        year_c = Integer.parseInt(currentDate.split("-")[0]);
        month_c = Integer.parseInt(currentDate.split("-")[1]);
        day_c = Integer.parseInt(currentDate.split("-")[2]);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.add_icon:
                    Intent i = new Intent(MainActivity.this, MainActivity_AddEvents.class);
                    startActivity(i);
                    return true;
                case R.id.list_icon:
/*
                    EventDataBase eDb = EventDataBase.getInstance(MainActivity.this);
                    //eDb.deleteEvent("yyyy-mm-dd", " ", "", "", MainActivity.this);
                    eDb.emptyDatabase(MainActivity.this);*/
                    Toast.makeText(MainActivity.this, "Have a Nice Day!", Toast.LENGTH_SHORT).show();

                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //House Keeping -- actionBar
        /*ActionBar ab = getSupportActionBar();
        if(ab != null){
            ab.setDisplayHomeAsUpEnabled(true);
        }*/

        //House Keeping -- navigationBar
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Initialize Calendar -- add dates in month, add title, add contents in month.
        sc = new SpecialCalendar();

        calV = new CalendarAdapter(this, getResources(), jumpMonth, jumpYear, year_c, month_c, day_c);
        addGridView();
        gridView.setAdapter(calV);

        topText = (TextView) findViewById(R.id.tv_month);
        addTextToTopTextView(topText, calV);

        //Get Database for Future Use
        DBHandler = EventDataBase.getInstance(this);

        //Set Event Listening on Slipping to Next Month or Last Month
        TextView lastm = (TextView) findViewById(R.id.left_img);
        TextView nextm = (TextView) findViewById(R.id.right_img);
        lastm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                jumpMonth--;
                jumpYear = (12 - month_c - jumpMonth)%12;
                calV = new CalendarAdapter(MainActivity.this, getResources(), jumpMonth, jumpYear, year_c, month_c, day_c);
                addGridView();
                gridView.setAdapter(calV);
                addTextToTopTextView(topText, calV);
            }
        });
        nextm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                jumpMonth++;
                jumpYear = (jumpMonth + month_c)%12;
                calV = new CalendarAdapter(MainActivity.this, getResources(), jumpMonth, jumpYear, year_c, month_c, day_c);
                addGridView();
                gridView.setAdapter(calV);
                addTextToTopTextView(topText, calV);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        //Thank God!
        //Initialize Calendar Again On Resume -- add dates in month, add title, update contents in month.
        jumpMonth = 0;
        jumpYear = 0;
        calV = new CalendarAdapter(this, getResources(), jumpMonth, jumpYear, year_c, month_c, day_c);
        addGridView();
        gridView.setAdapter(calV);
        addTextToTopTextView(topText, calV);

        //Toast.makeText(MainActivity.this, Integer.toString(sc.getEasterMonth(year_c)), Toast.LENGTH_SHORT).show();

        //Toast.makeText(MainActivity.this, Integer.toString(SmartStackClass.amList.size()), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        /*if(item.getItemId() == android.R.id.home){

        }*/
        if(item.getItemId() == R.id.switchmode1){
            //Go to Timetable View
            Intent i = new Intent(this, MainActivity_TimeTable.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addTextToTopTextView(TextView view, CalendarAdapter calV_current){
        StringBuffer textDate = new StringBuffer();
        textDate.append(calV_current.getHeadYear()).append("-").append(calV_current.getHeadMonth()).append("\t");
        view.setText(textDate);
        view.setTextColor(Color.BLACK);
        view.setTypeface(Typeface.DEFAULT_BOLD);
    }

    public void addGridView(){
        gridView = (GridView) findViewById(R.id.gridview);

        gridView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        //Listener On Click Each Date of Calendar
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //find out the date that you are clicking
                int stepYear = 0;
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
                dayInWeek = sc.getDayInWeek(stepYear, stepMonth);
                daysInMonth = sc.getDaysInMonth(sc.isLeapYear(stepYear), stepMonth);
                int stepDay = position - dayInWeek + 1;
                if(stepDay > daysInMonth || stepDay <= 0)    return;
                String sendDayInfo = Integer.toString(stepYear) + "-" + Integer.toString(stepMonth) + "-" + Integer.toString(stepDay);

                //Go to addEvent Activity
                SmartStackClass ssc = new SmartStackClass();
                ssc.smartstack.push(sendDayInfo);
                Intent i = new Intent(MainActivity.this, MainActivity_DayEventList.class);
                startActivity(i);
                //Toast.makeText(MainActivity.this, sendDayInfo, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
