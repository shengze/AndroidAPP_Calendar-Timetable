package com.shengze3322vs.year;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.shengze3322vs.year.EventDataBase.*;

public class MainActivity_AddEvents extends AppCompatActivity {

    private static final int DIALOG_TIME_START = 0;
    private static final int DIALOG_TIME_END = 1;
    private static final int DIALOG_DATE_START = 2;
    private static final int DIALOG_DATE_END = 3;
    //private final Dialog colorPickerDialog = new Dialog(MainActivity_AddEvents.this);
    private int hour_set;
    private int min_set;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
    private String sysDate = "";
    private String sys_year = "";
    private String sys_month = "";
    private String sys_day = "";
    private int year_set;
    private int month_set;
    private int day_set;

    private TextView startTime;
    private TextView endTime;
    private TextView startDate;
    private TextView endDate;
    private TextView colorPicker;
    private TextView notiPicker;
    private TextView repeatPicker;
    private CheckBox countDay;

    private String title = "";
    private String description = "";
    private String starttime = "";
    private String endtime = "";
    private String startdate = "";
    private String enddate = "";
    private String notification = "";
    private String repeatway = "";
    private String eventcolor = "";
    private String countday = "";

    private int int_stime_h = 0;
    private int int_stime_m = 0;
    private int int_etime_h = 0;
    private int int_etime_m = 0;
    private int int_sdate_y = 0;
    private int int_sdate_m = 0;
    private int int_sdate_d = 0;
    private int int_edate_y = 0;
    private int int_edate_m = 0;
    private int int_edate_d = 0;
    private SpecialCalendar sc;
    private MainActivity_UpdateEvents callinbetween;

    public EventDataBase dataBaseHandler;
    public SQLiteDatabase newevent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__add_events);

        ActionBar ab = getSupportActionBar();
        if(ab != null){
            ab.setDisplayHomeAsUpEnabled(true);
        }

        dataBaseHandler = EventDataBase.getInstance(MainActivity_AddEvents.this);
        sc = new SpecialCalendar();
        callinbetween = new MainActivity_UpdateEvents();

        Date date = new Date();
        sysDate = sdf.format(date);
        sys_year = sysDate.split("-")[0];
        sys_month = sysDate.split("-")[1];
        sys_day = sysDate.split("-")[2];
        year_set = Integer.parseInt(sys_year);
        month_set = Integer.parseInt(sys_month) - 1;
        day_set = Integer.parseInt(sys_day);

        startTime = (TextView) findViewById(R.id.start_time);
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_TIME_START);
            }
        });
        endTime = (TextView) findViewById(R.id.end_time);
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_TIME_END);
            }
        });
        startDate = (TextView) findViewById(R.id.start_date);
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_DATE_START);
            }
        });
        endDate = (TextView) findViewById(R.id.end_date);
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_DATE_END);
            }
        });

        colorPicker = (TextView) findViewById(R.id.pick_event_color);
        colorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorPickerRadioDialog();
            }
        });
/*
        notiPicker = (TextView) findViewById(R.id.noti_way);
        notiPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNotiPickerRadioDialog();
            }
        });*/

        repeatPicker = (TextView) findViewById(R.id.pick_repeat_way);
        repeatPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRepeatPickerRadioDialog();
            }
        });

        //countDay = (CheckBox)findViewById(R.id.count_day);
    }

    private void showColorPickerRadioDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.setTitle("Choose the Event Color :");
        dialog.setContentView(R.layout.color_picker_dialog);
        List<String> colorList = new ArrayList<>();
        colorList.add("Green");
        colorList.add("Blue");
        colorList.add("Red");
        colorList.add("Yellow");
        colorList.add("Cyan");

        RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.colorpick_radiodialog);
        for(int i = 0; i < colorList.size(); i++){
            RadioButton rb = new RadioButton(this);
            rb.setText(colorList.get(i));
            rg.addView(rb);
            if(i == 0)
                rb.setChecked(true);
            if(i == 1 && colorPicker.getText().toString().equals("Blue"))
                rb.setChecked(true);
            if(i == 2 && colorPicker.getText().toString().equals("Red"))
                rb.setChecked(true);
            if(i == 3 && colorPicker.getText().toString().equals("Yellow"))
                rb.setChecked(true);
            if(i == 4 && colorPicker.getText().toString().equals("Cyan"))
                rb.setChecked(true);
        }

        Button okay = (Button) dialog.findViewById(R.id.ensurecolor);
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.colorpick_radiodialog);
                int selectedId = rg.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) dialog.findViewById(selectedId);
                switch(radioButton.getText().toString()){
                    case "Green":
                        colorPicker.setBackgroundColor(Color.GREEN);
                        colorPicker.setText("Green");
                        colorPicker.setGravity(Gravity.CENTER);
                        break;
                    case "Blue":
                        colorPicker.setBackgroundColor(Color.BLUE);
                        colorPicker.setText("Blue");
                        colorPicker.setGravity(Gravity.CENTER);
                        break;
                    case "Red":
                        colorPicker.setBackgroundColor(Color.RED);
                        colorPicker.setText("Red");
                        colorPicker.setGravity(Gravity.CENTER);
                        break;
                    case "Yellow":
                        colorPicker.setBackgroundColor(Color.YELLOW);
                        colorPicker.setText("Yellow");
                        colorPicker.setGravity(Gravity.CENTER);
                        break;
                    case "Cyan":
                        colorPicker.setBackgroundColor(Color.CYAN);
                        colorPicker.setText("Cyan");
                        colorPicker.setGravity(Gravity.CENTER);
                        break;
                    default:
                        colorPicker.setBackgroundColor(Color.GREEN);
                        colorPicker.setText("GREEN");
                        colorPicker.setGravity(Gravity.CENTER);
                        break;
                }
                eventcolor = Integer.toString(((ColorDrawable)colorPicker.getBackground()).getColor());
                dialog.dismiss();
            }
        });

        dialog.show();
    }
/*
    private void showNotiPickerRadioDialog(){
        final Dialog dialog_noti = new Dialog(this);
        dialog_noti.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_noti.setContentView(R.layout.color_picker_dialog);

        List<String> colorList = new ArrayList<>();
        colorList.add("No Notifications");
        colorList.add("At Start of Event");
        colorList.add("10 minutes before Event Starts");
        colorList.add("1 Hour before Event Starts");
        colorList.add("At End of Event");

        RadioGroup rg = (RadioGroup) dialog_noti.findViewById(R.id.colorpick_radiodialog);
        for(int i = 0; i < colorList.size(); i++){
            RadioButton rb = new RadioButton(this);
            rb.setText(colorList.get(i));
            rg.addView(rb);
            if(i == 0)    rb.setChecked(true);
        }

        Button okay = (Button) dialog_noti.findViewById(R.id.ensurecolor);
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioGroup rg = (RadioGroup) dialog_noti.findViewById(R.id.colorpick_radiodialog);
                int selectedId = rg.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) dialog_noti.findViewById(selectedId);
                switch(radioButton.getText().toString()){
                    case "No Notifications":
                        notiPicker.setText(radioButton.getText().toString());
                        break;
                    case "At Start of Event":
                        notiPicker.setText(radioButton.getText().toString());
                        break;
                    case "10 minutes before Event Starts":
                        notiPicker.setText(radioButton.getText().toString());
                        break;
                    case "1 Hour before Event Starts":
                        notiPicker.setText(radioButton.getText().toString());
                        break;
                    case "At End of Event":
                        notiPicker.setText(radioButton.getText().toString());
                        break;
                }
                notification = notiPicker.getText().toString();
                dialog_noti.dismiss();
            }
        });

        dialog_noti.show();
    }*/

    private void showRepeatPickerRadioDialog(){
        final Dialog dialog_re = new Dialog(this);
        dialog_re.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_re.setContentView(R.layout.color_picker_dialog);

        List<String> colorList = new ArrayList<>();
        colorList.add("Never");
        colorList.add("Once Per Week");
        colorList.add("Everyday");

        RadioGroup rg = (RadioGroup) dialog_re.findViewById(R.id.colorpick_radiodialog);
        for(int i = 0; i < colorList.size(); i++){
            RadioButton rb = new RadioButton(this);
            rb.setText(colorList.get(i));
            rg.addView(rb);
            if(i == 0)    rb.setChecked(true);
            if(i == 1 && repeatPicker.getText().toString().equals("Once Per Week"))
                rb.setChecked(true);
            if(i == 2 && repeatPicker.getText().toString().equals("Everyday"))
                rb.setChecked(true);
        }

        Button okay = (Button) dialog_re.findViewById(R.id.ensurecolor);
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioGroup rg = (RadioGroup) dialog_re.findViewById(R.id.colorpick_radiodialog);
                int selectedId = rg.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) dialog_re.findViewById(selectedId);
                switch(radioButton.getText().toString()){
                    case "Never":
                        repeatPicker.setText(radioButton.getText().toString());
                        break;
                    case "Once Per Week":
                        repeatPicker.setText(radioButton.getText().toString());
                        break;
                    case "Everyday":
                        repeatPicker.setText(radioButton.getText().toString());
                        break;
                }
                repeatway = repeatPicker.getText().toString();
                dialog_re.dismiss();
            }
        });

        dialog_re.show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        if(id == DIALOG_TIME_START){
            return new TimePickerDialog(MainActivity_AddEvents.this, kTimePickerListener, hour_set, min_set, false);
        } else if(id == DIALOG_TIME_END){
            return new TimePickerDialog(MainActivity_AddEvents.this, eTimePickerListener, hour_set, min_set, false);
        } else if(id == DIALOG_DATE_START){
            return new DatePickerDialog(this, dpickerListener, year_set, month_set, day_set);
        } else if(id == DIALOG_DATE_END){
            return new DatePickerDialog(this, epickerListener, year_set, month_set, day_set);
        }

        return null;
    }

    protected TimePickerDialog.OnTimeSetListener kTimePickerListener = new TimePickerDialog.OnTimeSetListener(){
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            hour_set = hourOfDay;
            min_set = minute;
            int_stime_h = hour_set;
            int_stime_m = min_set;
            startTime.setText(Integer.toString(hour_set) + " : " + Integer.toString(min_set));
            starttime = startTime.getText().toString();
        }
    };
    protected TimePickerDialog.OnTimeSetListener eTimePickerListener = new TimePickerDialog.OnTimeSetListener(){
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            hour_set = hourOfDay;
            min_set = minute;
            int_etime_h = hour_set;
            int_etime_m = min_set;
            endTime.setText(Integer.toString(hour_set) + " : " + Integer.toString(min_set));
            endtime = endTime.getText().toString();
        }
    };
    protected DatePickerDialog.OnDateSetListener dpickerListener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year_set = year;
            month_set = month;
            day_set = dayOfMonth;
            int_sdate_y = year_set;
            int_sdate_m = month_set;
            int_sdate_d = day_set;
            startDate.setText(Integer.toString(year_set) + "-" + Integer.toString(month_set + 1) + "-" + Integer.toString(day_set));
            startdate = startDate.getText().toString();
        }
    };
    protected DatePickerDialog.OnDateSetListener epickerListener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year_set = year;
            month_set = month;
            day_set = dayOfMonth;
            int_edate_y = year_set;
            int_edate_m = month_set;
            int_edate_d = day_set;
            endDate.setText(Integer.toString(year_set) + "-" + Integer.toString(month_set + 1) + "-" + Integer.toString(day_set));
            enddate = endDate.getText().toString();
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_addevent, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        if(item.getItemId() == R.id.addevent){

            title = ((TextView)findViewById(R.id.event_title)).getText().toString();
            description = ((TextView)findViewById(R.id.Description)).getText().toString();
            if(repeatway.length() == 0)
                repeatway = repeatPicker.getText().toString();
            if(starttime.length() == 0)
                starttime = "0 : 0";
            //countday = countDay.isChecked() ? "1" : "0";
            String verify_st = startTime.getText().toString();
            String verify_et = endTime.getText().toString();
            int st_h = Integer.parseInt(verify_st.split(" : ")[0]);
            int st_m = Integer.parseInt(verify_st.split(" : ")[1]);
            int et_h = Integer.parseInt(verify_et.split(" : ")[0]);
            int et_m = Integer.parseInt(verify_et.split(" : ")[1]);
            if(st_h > et_h || (st_h == et_h && st_m >= et_m)){
                Toast.makeText(MainActivity_AddEvents.this, "Invalid End Time", Toast.LENGTH_LONG).show();
                return true;
            }
            if(st_h < 6 && et_h > 5){
                Toast.makeText(MainActivity_AddEvents.this, "Take a snooze. No need to get up too early", Toast.LENGTH_LONG).show();
                return true;
            }
            if(startDate.getText().toString().equals("yyyy-mm-dd") || endDate.getText().toString().equals("yyyy-mm-dd")) {
                Toast.makeText(MainActivity_AddEvents.this, "Please Set Both Start Date and End Date", Toast.LENGTH_LONG).show();
                return true;
            }
            int sd_y = Integer.parseInt(startdate.split("-")[0]);
            int sd_m = Integer.parseInt(startdate.split("-")[1]);
            int sd_d = Integer.parseInt(startdate.split("-")[2]);
            int ed_y = Integer.parseInt(enddate.split("-")[0]);
            int ed_m = Integer.parseInt(enddate.split("-")[1]);
            int ed_d = Integer.parseInt(enddate.split("-")[2]);
            if(sd_y > ed_y || (sd_y == ed_y && (sd_m > ed_m || (sd_m == ed_m && sd_d > ed_d)))){
                Toast.makeText(MainActivity_AddEvents.this, "Invalid End Date", Toast.LENGTH_LONG).show();
                return true;
            }
            String titi = "";
            SQLiteDatabase db_verify = dataBaseHandler.getMyWritableDataBase();
            Cursor cc = db_verify.rawQuery("SELECT " + COLUMN_TITLE + ", " + COLUMN_STIME + ", " + COLUMN_ETIME + ", " + COLUMN_SDATE + ", " + COLUMN_EDATE + ", " + COLUMN_REPEAT + " FROM " + TABLE_EVENTS + ";", null);
            boolean conflict = false;
            if(cc != null){
                if(cc.moveToFirst()){
                    do {
                        String verify_repeat = cc.getString(cc.getColumnIndex(COLUMN_REPEAT));
                        String verify_sdate = cc.getString(cc.getColumnIndex(COLUMN_SDATE));
                        String verify_edate = cc.getString(cc.getColumnIndex(COLUMN_EDATE));
                        if(verify_repeat.equals("Everyday")){
                            if(repeatway.equals("Never")){
                                if(callinbetween.inBetween(verify_sdate, verify_edate, startdate)){
                                    //time conflict judge
                                    String verify_stime = cc.getString(cc.getColumnIndex(COLUMN_STIME));
                                    String verify_etime = cc.getString(cc.getColumnIndex(COLUMN_ETIME));
                                    int ver_st_h = Integer.parseInt(verify_stime.split(" : ")[0]);
                                    int ver_st_m = Integer.parseInt(verify_stime.split(" : ")[1]);
                                    int ver_et_h = Integer.parseInt(verify_etime.split(" : ")[0]);
                                    int ver_et_m = Integer.parseInt(verify_etime.split(" : ")[1]);
                                    if(callinbetween.timeConflict(st_h, st_m, et_h, et_m, ver_st_h, ver_st_m, ver_et_h, ver_et_m)){
                                        titi = cc.getString(cc.getColumnIndex(COLUMN_TITLE));
                                        conflict = true;
                                        break;
                                    }
                                }
                            }else if(repeatway.equals("Everyday")){
                                if(callinbetween.inBetween(verify_sdate, verify_edate, startdate) || callinbetween.inBetween(startdate, enddate, verify_sdate)){
                                    //time conflict judge
                                    String verify_stime = cc.getString(cc.getColumnIndex(COLUMN_STIME));
                                    String verify_etime = cc.getString(cc.getColumnIndex(COLUMN_ETIME));
                                    int ver_st_h = Integer.parseInt(verify_stime.split(" : ")[0]);
                                    int ver_st_m = Integer.parseInt(verify_stime.split(" : ")[1]);
                                    int ver_et_h = Integer.parseInt(verify_etime.split(" : ")[0]);
                                    int ver_et_m = Integer.parseInt(verify_etime.split(" : ")[1]);
                                    if(callinbetween.timeConflict(st_h, st_m, et_h, et_m, ver_st_h, ver_st_m, ver_et_h, ver_et_m)){
                                        titi = cc.getString(cc.getColumnIndex(COLUMN_TITLE));
                                        conflict = true;
                                        break;
                                    }
                                }
                            }else{
                                if(callinbetween.inBetween(verify_sdate, verify_edate, startdate) || callinbetween.inBetween(startdate, enddate, verify_sdate)){
                                    //time conflict judge
                                    String verify_stime = cc.getString(cc.getColumnIndex(COLUMN_STIME));
                                    String verify_etime = cc.getString(cc.getColumnIndex(COLUMN_ETIME));
                                    int ver_st_h = Integer.parseInt(verify_stime.split(" : ")[0]);
                                    int ver_st_m = Integer.parseInt(verify_stime.split(" : ")[1]);
                                    int ver_et_h = Integer.parseInt(verify_etime.split(" : ")[0]);
                                    int ver_et_m = Integer.parseInt(verify_etime.split(" : ")[1]);
                                    if(callinbetween.timeConflict(st_h, st_m, et_h, et_m, ver_st_h, ver_st_m, ver_et_h, ver_et_m)){
                                        titi = cc.getString(cc.getColumnIndex(COLUMN_TITLE));
                                        conflict = true;
                                        break;
                                    }
                                }
                            }
                        }else if(verify_repeat.equals("Never")){
                            if(repeatway.equals("Never")){
                                if(callinbetween.inBetween(verify_sdate, verify_sdate, startdate)){
                                    //time conflict judge
                                    String verify_stime = cc.getString(cc.getColumnIndex(COLUMN_STIME));
                                    String verify_etime = cc.getString(cc.getColumnIndex(COLUMN_ETIME));
                                    int ver_st_h = Integer.parseInt(verify_stime.split(" : ")[0]);
                                    int ver_st_m = Integer.parseInt(verify_stime.split(" : ")[1]);
                                    int ver_et_h = Integer.parseInt(verify_etime.split(" : ")[0]);
                                    int ver_et_m = Integer.parseInt(verify_etime.split(" : ")[1]);
                                    if(callinbetween.timeConflict(st_h, st_m, et_h, et_m, ver_st_h, ver_st_m, ver_et_h, ver_et_m)){
                                        titi = cc.getString(cc.getColumnIndex(COLUMN_TITLE));
                                        conflict = true;
                                        break;
                                    }
                                }
                            }else if(repeatway.equals("Everyday")){
                                if(callinbetween.inBetween(startdate, enddate, verify_sdate)){
                                    //time conflict judge
                                    String verify_stime = cc.getString(cc.getColumnIndex(COLUMN_STIME));
                                    String verify_etime = cc.getString(cc.getColumnIndex(COLUMN_ETIME));
                                    int ver_st_h = Integer.parseInt(verify_stime.split(" : ")[0]);
                                    int ver_st_m = Integer.parseInt(verify_stime.split(" : ")[1]);
                                    int ver_et_h = Integer.parseInt(verify_etime.split(" : ")[0]);
                                    int ver_et_m = Integer.parseInt(verify_etime.split(" : ")[1]);
                                    if(callinbetween.timeConflict(st_h, st_m, et_h, et_m, ver_st_h, ver_st_m, ver_et_h, ver_et_m)){
                                        titi = cc.getString(cc.getColumnIndex(COLUMN_TITLE));
                                        conflict = true;
                                        break;
                                    }
                                }
                            }else{
                                if(callinbetween.inBetween(startdate, enddate, verify_sdate)){
                                    int verify_sd_y = Integer.parseInt(verify_sdate.split("-")[0]);
                                    int verify_sd_m = Integer.parseInt(verify_sdate.split("-")[1]);
                                    int verify_sd_d = Integer.parseInt(verify_sdate.split("-")[2]);
                                    int ver_dayinweek = sc.getDayInWeek(verify_sd_y, verify_sd_m);
                                    int dayinweek = sc.getDayInWeek(sd_y, sd_m);
                                    if((sd_d + dayinweek)%7 == (verify_sd_d + ver_dayinweek)%7){
                                        //time conflict judge
                                        String verify_stime = cc.getString(cc.getColumnIndex(COLUMN_STIME));
                                        String verify_etime = cc.getString(cc.getColumnIndex(COLUMN_ETIME));
                                        int ver_st_h = Integer.parseInt(verify_stime.split(" : ")[0]);
                                        int ver_st_m = Integer.parseInt(verify_stime.split(" : ")[1]);
                                        int ver_et_h = Integer.parseInt(verify_etime.split(" : ")[0]);
                                        int ver_et_m = Integer.parseInt(verify_etime.split(" : ")[1]);
                                        if(callinbetween.timeConflict(st_h, st_m, et_h, et_m, ver_st_h, ver_st_m, ver_et_h, ver_et_m)){
                                            titi = cc.getString(cc.getColumnIndex(COLUMN_TITLE));
                                            conflict = true;
                                            break;
                                        }
                                    }
                                }
                            }
                        }else{
                            if(repeatway.equals("Never")){
                                if(callinbetween.inBetween(verify_sdate, verify_sdate, startdate)){
                                    int verify_sd_y = Integer.parseInt(verify_sdate.split("-")[0]);
                                    int verify_sd_m = Integer.parseInt(verify_sdate.split("-")[1]);
                                    int verify_sd_d = Integer.parseInt(verify_sdate.split("-")[2]);
                                    int ver_dayinweek = sc.getDayInWeek(verify_sd_y, verify_sd_m);
                                    int dayinweek = sc.getDayInWeek(sd_y, sd_m);
                                    if((sd_d + dayinweek)%7 == (verify_sd_d + ver_dayinweek)%7){
                                        //time conflict judge
                                        String verify_stime = cc.getString(cc.getColumnIndex(COLUMN_STIME));
                                        String verify_etime = cc.getString(cc.getColumnIndex(COLUMN_ETIME));
                                        int ver_st_h = Integer.parseInt(verify_stime.split(" : ")[0]);
                                        int ver_st_m = Integer.parseInt(verify_stime.split(" : ")[1]);
                                        int ver_et_h = Integer.parseInt(verify_etime.split(" : ")[0]);
                                        int ver_et_m = Integer.parseInt(verify_etime.split(" : ")[1]);
                                        if(callinbetween.timeConflict(st_h, st_m, et_h, et_m, ver_st_h, ver_st_m, ver_et_h, ver_et_m)){
                                            titi = cc.getString(cc.getColumnIndex(COLUMN_TITLE));
                                            conflict = true;
                                            break;
                                        }
                                    }
                                }
                            }else if(repeatway.equals("Everyday")){
                                if(callinbetween.inBetween(verify_sdate, verify_edate, startdate) || callinbetween.inBetween(startdate, enddate, verify_sdate)){
                                    //time conflict judge
                                    String verify_stime = cc.getString(cc.getColumnIndex(COLUMN_STIME));
                                    String verify_etime = cc.getString(cc.getColumnIndex(COLUMN_ETIME));
                                    int ver_st_h = Integer.parseInt(verify_stime.split(" : ")[0]);
                                    int ver_st_m = Integer.parseInt(verify_stime.split(" : ")[1]);
                                    int ver_et_h = Integer.parseInt(verify_etime.split(" : ")[0]);
                                    int ver_et_m = Integer.parseInt(verify_etime.split(" : ")[1]);
                                    if(callinbetween.timeConflict(st_h, st_m, et_h, et_m, ver_st_h, ver_st_m, ver_et_h, ver_et_m)){
                                        titi = cc.getString(cc.getColumnIndex(COLUMN_TITLE));
                                        conflict = true;
                                        break;
                                    }
                                }
                            }else{
                                if(callinbetween.inBetween(verify_sdate, verify_edate, startdate) || callinbetween.inBetween(startdate, enddate, verify_sdate)){
                                    int verify_sd_y = Integer.parseInt(verify_sdate.split("-")[0]);
                                    int verify_sd_m = Integer.parseInt(verify_sdate.split("-")[1]);
                                    int verify_sd_d = Integer.parseInt(verify_sdate.split("-")[2]);
                                    int ver_dayinweek = sc.getDayInWeek(verify_sd_y, verify_sd_m);
                                    int dayinweek = sc.getDayInWeek(sd_y, sd_m);
                                    if((sd_d + dayinweek)%7 == (verify_sd_d + ver_dayinweek)%7){
                                        //time conflict judge
                                        String verify_stime = cc.getString(cc.getColumnIndex(COLUMN_STIME));
                                        String verify_etime = cc.getString(cc.getColumnIndex(COLUMN_ETIME));
                                        int ver_st_h = Integer.parseInt(verify_stime.split(" : ")[0]);
                                        int ver_st_m = Integer.parseInt(verify_stime.split(" : ")[1]);
                                        int ver_et_h = Integer.parseInt(verify_etime.split(" : ")[0]);
                                        int ver_et_m = Integer.parseInt(verify_etime.split(" : ")[1]);
                                        if(callinbetween.timeConflict(st_h, st_m, et_h, et_m, ver_st_h, ver_st_m, ver_et_h, ver_et_m)){
                                            titi = cc.getString(cc.getColumnIndex(COLUMN_TITLE));
                                            conflict = true;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }while (cc.moveToNext());
                }
                cc.close();
            }
            if(conflict){
                Toast.makeText(MainActivity_AddEvents.this, "Time Conflict Exist with " + titi, Toast.LENGTH_LONG).show();
                return true;
            }
            if(colorPicker.getText().toString().equals("Select A Color")){
                Toast.makeText(MainActivity_AddEvents.this, "Please Set Event Color", Toast.LENGTH_LONG).show();
                return true;
            }


            newevent = dataBaseHandler.getMyWritableDataBase();
            dataBaseHandler.addEvent(title, description, starttime, endtime, startdate, enddate, notification, repeatway, eventcolor, countday);

            //obtain the event id that we just add
            /*String notif_id = "";
            SQLiteDatabase getId = dataBaseHandler.getMyWritableDataBase();
            Cursor cc = getId.rawQuery("SELECT " + COLUMN_ID + ", " + COLUMN_STIME + ", " + COLUMN_SDATE + " FROM " + TABLE_EVENTS + ";", null);
            if(cc != null){
                if(cc.moveToFirst()){
                    do{
                        String st_f_id = cc.getString(cc.getColumnIndex(COLUMN_STIME));
                        String sd_f_id = cc.getString(cc.getColumnIndex(COLUMN_SDATE));
                        if(st_f_id.equals(starttime) && sd_f_id.equals(startdate)){
                            notif_id = cc.getString(cc.getColumnIndex(COLUMN_ID));
                            break;
                        }
                    }while(cc.moveToNext());
                }
                cc.close();
            }*/

            //set notification function here!
            /*if(!notification.equals("No Notifications")) {

                Intent myIntent = new Intent(MainActivity_AddEvents.this, MyIntentService.class);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                PendingIntent pendingIntent = PendingIntent.getService(MainActivity_AddEvents.this, Integer.parseInt(notif_id), myIntent, PendingIntent.FLAG_ONE_SHOT);

                Calendar calendar = Calendar.getInstance();
                calendar.set(int_sdate_y, int_sdate_m, int_sdate_d);
                switch (notification) {
                    case "At End of Event":
                        calendar.set(Calendar.HOUR_OF_DAY, int_etime_h);
                        calendar.set(Calendar.MINUTE, int_etime_m);
                        calendar.set(Calendar.SECOND, 0);
                        break;
                    case "At Start of Event":
                        calendar.set(Calendar.HOUR_OF_DAY, int_stime_h);
                        calendar.set(Calendar.MINUTE, int_stime_m);
                        calendar.set(Calendar.SECOND, 0);
                        break;
                    case "10 minutes before Event Starts":
                        int_stime_m = int_stime_m - 10 >= 0 ? int_stime_m - 10 : int_stime_m + 50;
                        int_stime_h = int_stime_m > 50 ? (int_stime_h - 1 >= 0 ? int_stime_h - 1 : 23) : int_stime_h;
                        if(int_stime_h == 23 && int_sdate_m > 50){
                            int k = sc.getDaysInMonth(sc.isLeapYear(int_sdate_y), int_sdate_m-1);
                            int sdate_d = int_sdate_d - 1 > 0 ? int_sdate_d - 1 : k;
                            int sdate_m = int_sdate_d - 1 > 0 ? int_sdate_m : (int_sdate_m - 1 > 0 ? int_sdate_m - 1 : 12);
                            int sdate_y = (sdate_m == 12 && sdate_d == 31) ? int_sdate_y - 1 : int_sdate_y;
                            calendar.set(sdate_y, sdate_m, sdate_d);
                        }
                        calendar.set(Calendar.HOUR_OF_DAY, int_stime_h);
                        calendar.set(Calendar.MINUTE, int_stime_m);
                        calendar.set(Calendar.SECOND, 0);
                        break;
                    default:
                        int_stime_h = int_stime_h - 1 >= 0 ? int_stime_h - 1 : 23;
                        if(int_stime_h == 23){
                            int k = sc.getDaysInMonth(sc.isLeapYear(int_sdate_y), int_sdate_m-1);
                            int sdate_d = int_sdate_d - 1 > 0 ? int_sdate_d - 1 : k;
                            int sdate_m = int_sdate_d - 1 > 0 ? int_sdate_m : (int_sdate_m - 1 > 0 ? int_sdate_m - 1 : 12);
                            int sdate_y = (sdate_m == 12 && sdate_d == 31) ? int_sdate_y - 1 : int_sdate_y;
                            calendar.set(sdate_y, sdate_m, sdate_d);
                        }
                        calendar.set(Calendar.HOUR_OF_DAY, int_stime_h);
                        calendar.set(Calendar.MINUTE, int_stime_m);
                        calendar.set(Calendar.SECOND, 0);
                        break;
                }

                if(repeatway.equals("Once Per Week")) {
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24 * 60 * 60 * 1000 * 7, pendingIntent);  //set repeating every 24 hours*7
                }
                else if(repeatway.equals("Everyday")) {
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 2 * 60 * 1000, pendingIntent);  //set repeating every 24 hours

                }
            }*/

            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
