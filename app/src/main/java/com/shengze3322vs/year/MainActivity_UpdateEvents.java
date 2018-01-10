package com.shengze3322vs.year;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationManager;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.shengze3322vs.year.EventDataBase.*;

public class MainActivity_UpdateEvents extends AppCompatActivity {

    private static final int DIALOG_TIME_START = 0;
    private static final int DIALOG_TIME_END = 1;
    private static final int DIALOG_DATE_START = 2;
    private static final int DIALOG_DATE_END = 3;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
    private String sysDate = "";
    private String sys_year = "";
    private String sys_month = "";
    private String sys_day = "";
    private int year_set;
    private int month_set;
    private int day_set;
    private int hour_set;
    private int min_set;

    EditText tv_title;
    EditText tv_desc;
    TextView tv_stime;
    TextView tv_etime;
    TextView tv_sdate;
    TextView tv_edate;
    TextView tv_noti;
    TextView tv_repeat;
    TextView tv_color;
    CheckBox count_day;
    private String id = "";
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

    private String id_in_this_view = "";

    //MainActivity_UpdateEvents(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__update_events);

        ActionBar ab = getSupportActionBar();
        if(ab != null){
            ab.setDisplayHomeAsUpEnabled(true);
        }

        sc = new SpecialCalendar();

        tv_title = (EditText)findViewById(R.id.et_tit);
        tv_desc = (EditText)findViewById(R.id.et_desc);
        tv_stime = (TextView)findViewById(R.id.s_time);
        tv_etime = (TextView)findViewById(R.id.e_time);
        tv_sdate = (TextView)findViewById(R.id.s_date);
        tv_edate = (TextView)findViewById(R.id.e_date);
        //tv_noti = (TextView)findViewById(R.id.tv_noti);
        tv_repeat = (TextView)findViewById(R.id.tv_repeat);
        tv_color = (TextView)findViewById(R.id.tv_color);
        //count_day = (CheckBox)findViewById(R.id.cb_count);

        String mark = SmartStackClass.smartstack.pop();
        String info_mark = mark.split("#")[0];
        String tableorcal = mark.split("#")[1];

        if(Integer.parseInt(tableorcal) == 2){
            //Toast.makeText(MainActivity_UpdateEvents.this, info_mark + "\n" + mark_day, Toast.LENGTH_SHORT).show();
            String mark_day = SmartStackClass.smartstack2.pop();
            loadView(info_mark, mark_day);
        }else{
            id_in_this_view = info_mark;
            loadViewForMainActivity(info_mark);
        }

        Date date = new Date();
        sysDate = sdf.format(date);
        sys_year = sysDate.split("-")[0];
        sys_month = sysDate.split("-")[1];
        sys_day = sysDate.split("-")[2];
        year_set = Integer.parseInt(sys_year);
        month_set = Integer.parseInt(sys_month) - 1;
        day_set = Integer.parseInt(sys_day);

        tv_stime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_TIME_START);
            }
        });
        tv_etime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_TIME_END);
            }
        });
        tv_sdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_DATE_START);
            }
        });
        tv_edate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_DATE_END);
            }
        });

        tv_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorPickerRadioDialog();
            }
        });
/*
        tv_noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNotiPickerRadioDialog();
            }
        });*/

        tv_repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRepeatPickerRadioDialog();
            }
        });

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
            if(i == 1 && tv_color.getText().toString().equals("Blue"))
                rb.setChecked(true);
            if(i == 2 && tv_color.getText().toString().equals("Red"))
                rb.setChecked(true);
            if(i == 3 && tv_color.getText().toString().equals("Yellow"))
                rb.setChecked(true);
            if(i == 4 && tv_color.getText().toString().equals("Cyan"))
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
                        tv_color.setBackgroundColor(Color.GREEN);
                        tv_color.setText("Green");
                        tv_color.setGravity(Gravity.CENTER);
                        break;
                    case "Blue":
                        tv_color.setBackgroundColor(Color.BLUE);
                        tv_color.setText("Blue");
                        tv_color.setGravity(Gravity.CENTER);
                        break;
                    case "Red":
                        tv_color.setBackgroundColor(Color.RED);
                        tv_color.setText("Red");
                        tv_color.setGravity(Gravity.CENTER);
                        break;
                    case "Yellow":
                        tv_color.setBackgroundColor(Color.YELLOW);
                        tv_color.setText("Yellow");
                        tv_color.setGravity(Gravity.CENTER);
                        break;
                    case "Cyan":
                        tv_color.setBackgroundColor(Color.CYAN);
                        tv_color.setText("Cyan");
                        tv_color.setGravity(Gravity.CENTER);
                        break;
                }
                eventcolor = Integer.toString(((ColorDrawable)tv_color.getBackground()).getColor());
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
                        tv_noti.setText(radioButton.getText().toString());
                        break;
                    case "At Start of Event":
                        tv_noti.setText(radioButton.getText().toString());
                        break;
                    case "10 minutes before Event Starts":
                        tv_noti.setText(radioButton.getText().toString());
                        break;
                    case "1 Hour before Event Starts":
                        tv_noti.setText(radioButton.getText().toString());
                        break;
                    case "At End of Event":
                        tv_noti.setText(radioButton.getText().toString());
                        break;
                }
                notification = tv_noti.getText().toString();
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
            if(i == 1 && tv_repeat.getText().toString().equals("Once Per Week"))
                rb.setChecked(true);
            if(i == 2 && tv_repeat.getText().toString().equals("Everyday"))
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
                        tv_repeat.setText(radioButton.getText().toString());
                        break;
                    case "Once Per Week":
                        tv_repeat.setText(radioButton.getText().toString());
                        break;
                    case "Everyday":
                        tv_repeat.setText(radioButton.getText().toString());
                        break;
                }
                repeatway = tv_repeat.getText().toString();
                dialog_re.dismiss();
            }
        });

        dialog_re.show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        if(id == DIALOG_TIME_START){
            return new TimePickerDialog(MainActivity_UpdateEvents.this, kTimePickerListener, hour_set, min_set, false);
        } else if(id == DIALOG_TIME_END){
            return new TimePickerDialog(MainActivity_UpdateEvents.this, eTimePickerListener, hour_set, min_set, false);
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
            tv_stime.setText(Integer.toString(hour_set) + " : " + Integer.toString(min_set));
            int_stime_h = hour_set;
            int_stime_m = min_set;
            starttime = tv_stime.getText().toString();
        }
    };
    protected TimePickerDialog.OnTimeSetListener eTimePickerListener = new TimePickerDialog.OnTimeSetListener(){
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            hour_set = hourOfDay;
            min_set = minute;
            tv_etime.setText(Integer.toString(hour_set) + " : " + Integer.toString(min_set));
            int_etime_h = hour_set;
            int_etime_m = min_set;
            endtime = tv_etime.getText().toString();
        }
    };
    protected DatePickerDialog.OnDateSetListener dpickerListener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year_set = year;
            month_set = month;
            day_set = dayOfMonth;
            tv_sdate.setText(Integer.toString(year_set) + "-" + Integer.toString(month_set + 1) + "-" + Integer.toString(day_set));
            int_sdate_y = year_set;
            int_sdate_m = month_set;
            int_sdate_d = day_set;
            startdate = tv_sdate.getText().toString();
        }
    };
    protected DatePickerDialog.OnDateSetListener epickerListener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year_set = year;
            month_set = month;
            day_set = dayOfMonth;
            tv_edate.setText(Integer.toString(year_set) + "-" + Integer.toString(month_set + 1) + "-" + Integer.toString(day_set));
            int_edate_y = year_set;
            int_edate_m = month_set;
            int_edate_d = day_set;
            enddate = tv_edate.getText().toString();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_updateevent, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        if(item.getItemId() == R.id.updateevent){
            title = tv_title.getText().toString();
            description = tv_desc.getText().toString();
            starttime = tv_stime.getText().toString();
            endtime = tv_etime.getText().toString();
            startdate = tv_sdate.getText().toString();
            enddate = tv_edate.getText().toString();
            repeatway = tv_repeat.getText().toString();

            EventDataBase edb = EventDataBase.getInstance(MainActivity_UpdateEvents.this);
            //countday = count_day.isChecked() ? "1" : "0";

            //Add Event Error Handler:
            int st_h = Integer.parseInt(starttime.split(" : ")[0]);
            int st_m = Integer.parseInt(starttime.split(" : ")[1]);
            int et_h = Integer.parseInt(endtime.split(" : ")[0]);
            int et_m = Integer.parseInt(endtime.split(" : ")[1]);
            if(st_h > et_h || (st_h == et_h && st_m >= et_m)){
                Toast.makeText(MainActivity_UpdateEvents.this, "Invalid End Time", Toast.LENGTH_LONG).show();
                return true;
            }
            if(st_h < 6 && et_h > 5){
                Toast.makeText(MainActivity_UpdateEvents.this, "Take a snooze. No need to get up too early", Toast.LENGTH_LONG).show();
                return true;
            }
            int sd_y = Integer.parseInt(startdate.split("-")[0]);
            int sd_m = Integer.parseInt(startdate.split("-")[1]);
            int sd_d = Integer.parseInt(startdate.split("-")[2]);
            int ed_y = Integer.parseInt(enddate.split("-")[0]);
            int ed_m = Integer.parseInt(enddate.split("-")[1]);
            int ed_d = Integer.parseInt(enddate.split("-")[2]);
            if(sd_y > ed_y || (sd_y == ed_y && (sd_m > ed_m || (sd_m == ed_m && sd_d > ed_d)))){
                Toast.makeText(MainActivity_UpdateEvents.this, "Invalid End Date", Toast.LENGTH_LONG).show();
                return true;
            }
            String titi = "";
            SQLiteDatabase db_verify = edb.getMyWritableDataBase();
            Cursor cc = db_verify.rawQuery("SELECT " + COLUMN_ID + ", " + COLUMN_TITLE + ", " + COLUMN_STIME + ", " + COLUMN_ETIME + ", " + COLUMN_SDATE + ", " + COLUMN_EDATE + ", " + COLUMN_REPEAT + " FROM " + TABLE_EVENTS + ";", null);
            boolean conflict = false;
            if(cc != null){
                if(cc.moveToFirst()){
                    do {
                        String cursor_event_id = cc.getString(cc.getColumnIndex(COLUMN_ID));
                        if(!cursor_event_id.equals(id_in_this_view)) {
                            String verify_repeat = cc.getString(cc.getColumnIndex(COLUMN_REPEAT));
                            String verify_sdate = cc.getString(cc.getColumnIndex(COLUMN_SDATE));
                            String verify_edate = cc.getString(cc.getColumnIndex(COLUMN_EDATE));
                            if (verify_repeat.equals("Everyday")) {
                                if (repeatway.equals("Never")) {
                                    if (inBetween(verify_sdate, verify_edate, startdate)) {
                                        //time conflict judge
                                        String verify_stime = cc.getString(cc.getColumnIndex(COLUMN_STIME));
                                        String verify_etime = cc.getString(cc.getColumnIndex(COLUMN_ETIME));
                                        int ver_st_h = Integer.parseInt(verify_stime.split(" : ")[0]);
                                        int ver_st_m = Integer.parseInt(verify_stime.split(" : ")[1]);
                                        int ver_et_h = Integer.parseInt(verify_etime.split(" : ")[0]);
                                        int ver_et_m = Integer.parseInt(verify_etime.split(" : ")[1]);
                                        if (timeConflict(st_h, st_m, et_h, et_m, ver_st_h, ver_st_m, ver_et_h, ver_et_m)) {
                                            titi = cc.getString(cc.getColumnIndex(COLUMN_TITLE));
                                            conflict = true;
                                            break;
                                        }
                                    }
                                } else if (repeatway.equals("Everyday")) {
                                    if (inBetween(verify_sdate, verify_edate, startdate) || inBetween(startdate, enddate, verify_sdate)) {
                                        //time conflict judge
                                        String verify_stime = cc.getString(cc.getColumnIndex(COLUMN_STIME));
                                        String verify_etime = cc.getString(cc.getColumnIndex(COLUMN_ETIME));
                                        int ver_st_h = Integer.parseInt(verify_stime.split(" : ")[0]);
                                        int ver_st_m = Integer.parseInt(verify_stime.split(" : ")[1]);
                                        int ver_et_h = Integer.parseInt(verify_etime.split(" : ")[0]);
                                        int ver_et_m = Integer.parseInt(verify_etime.split(" : ")[1]);
                                        if (timeConflict(st_h, st_m, et_h, et_m, ver_st_h, ver_st_m, ver_et_h, ver_et_m)) {
                                            titi = cc.getString(cc.getColumnIndex(COLUMN_TITLE));
                                            conflict = true;
                                            break;
                                        }
                                    }
                                } else {
                                    if (inBetween(verify_sdate, verify_edate, startdate) || inBetween(startdate, enddate, verify_sdate)) {
                                        //time conflict judge
                                        String verify_stime = cc.getString(cc.getColumnIndex(COLUMN_STIME));
                                        String verify_etime = cc.getString(cc.getColumnIndex(COLUMN_ETIME));
                                        int ver_st_h = Integer.parseInt(verify_stime.split(" : ")[0]);
                                        int ver_st_m = Integer.parseInt(verify_stime.split(" : ")[1]);
                                        int ver_et_h = Integer.parseInt(verify_etime.split(" : ")[0]);
                                        int ver_et_m = Integer.parseInt(verify_etime.split(" : ")[1]);
                                        if (timeConflict(st_h, st_m, et_h, et_m, ver_st_h, ver_st_m, ver_et_h, ver_et_m)) {
                                            titi = cc.getString(cc.getColumnIndex(COLUMN_TITLE));
                                            conflict = true;
                                            break;
                                        }
                                    }
                                }
                            } else if (verify_repeat.equals("Never")) {
                                if (repeatway.equals("Never")) {
                                    if (inBetween(verify_sdate, verify_sdate, startdate)) {
                                        //time conflict judge
                                        String verify_stime = cc.getString(cc.getColumnIndex(COLUMN_STIME));
                                        String verify_etime = cc.getString(cc.getColumnIndex(COLUMN_ETIME));
                                        int ver_st_h = Integer.parseInt(verify_stime.split(" : ")[0]);
                                        int ver_st_m = Integer.parseInt(verify_stime.split(" : ")[1]);
                                        int ver_et_h = Integer.parseInt(verify_etime.split(" : ")[0]);
                                        int ver_et_m = Integer.parseInt(verify_etime.split(" : ")[1]);
                                        if (timeConflict(st_h, st_m, et_h, et_m, ver_st_h, ver_st_m, ver_et_h, ver_et_m)) {
                                            titi = cc.getString(cc.getColumnIndex(COLUMN_TITLE));
                                            conflict = true;
                                            break;
                                        }
                                    }
                                } else if (repeatway.equals("Everyday")) {
                                    if (inBetween(startdate, enddate, verify_sdate)) {
                                        //time conflict judge
                                        String verify_stime = cc.getString(cc.getColumnIndex(COLUMN_STIME));
                                        String verify_etime = cc.getString(cc.getColumnIndex(COLUMN_ETIME));
                                        int ver_st_h = Integer.parseInt(verify_stime.split(" : ")[0]);
                                        int ver_st_m = Integer.parseInt(verify_stime.split(" : ")[1]);
                                        int ver_et_h = Integer.parseInt(verify_etime.split(" : ")[0]);
                                        int ver_et_m = Integer.parseInt(verify_etime.split(" : ")[1]);
                                        if (timeConflict(st_h, st_m, et_h, et_m, ver_st_h, ver_st_m, ver_et_h, ver_et_m)) {
                                            titi = cc.getString(cc.getColumnIndex(COLUMN_TITLE));
                                            conflict = true;
                                            break;
                                        }
                                    }
                                } else {
                                    if (inBetween(startdate, enddate, verify_sdate)) {
                                        int verify_sd_y = Integer.parseInt(verify_sdate.split("-")[0]);
                                        int verify_sd_m = Integer.parseInt(verify_sdate.split("-")[1]);
                                        int verify_sd_d = Integer.parseInt(verify_sdate.split("-")[2]);
                                        int ver_dayinweek = sc.getDayInWeek(verify_sd_y, verify_sd_m);
                                        int dayinweek = sc.getDayInWeek(sd_y, sd_m);
                                        if ((sd_d + dayinweek) % 7 == (verify_sd_d + ver_dayinweek) % 7) {
                                            //time conflict judge
                                            String verify_stime = cc.getString(cc.getColumnIndex(COLUMN_STIME));
                                            String verify_etime = cc.getString(cc.getColumnIndex(COLUMN_ETIME));
                                            int ver_st_h = Integer.parseInt(verify_stime.split(" : ")[0]);
                                            int ver_st_m = Integer.parseInt(verify_stime.split(" : ")[1]);
                                            int ver_et_h = Integer.parseInt(verify_etime.split(" : ")[0]);
                                            int ver_et_m = Integer.parseInt(verify_etime.split(" : ")[1]);
                                            if (timeConflict(st_h, st_m, et_h, et_m, ver_st_h, ver_st_m, ver_et_h, ver_et_m)) {
                                                titi = cc.getString(cc.getColumnIndex(COLUMN_TITLE));
                                                conflict = true;
                                                break;
                                            }
                                        }
                                    }
                                }
                            } else {
                                if (repeatway.equals("Never")) {
                                    if (inBetween(verify_sdate, verify_sdate, startdate)) {
                                        int verify_sd_y = Integer.parseInt(verify_sdate.split("-")[0]);
                                        int verify_sd_m = Integer.parseInt(verify_sdate.split("-")[1]);
                                        int verify_sd_d = Integer.parseInt(verify_sdate.split("-")[2]);
                                        int ver_dayinweek = sc.getDayInWeek(verify_sd_y, verify_sd_m);
                                        int dayinweek = sc.getDayInWeek(sd_y, sd_m);
                                        if ((sd_d + dayinweek) % 7 == (verify_sd_d + ver_dayinweek) % 7) {
                                            //time conflict judge
                                            String verify_stime = cc.getString(cc.getColumnIndex(COLUMN_STIME));
                                            String verify_etime = cc.getString(cc.getColumnIndex(COLUMN_ETIME));
                                            int ver_st_h = Integer.parseInt(verify_stime.split(" : ")[0]);
                                            int ver_st_m = Integer.parseInt(verify_stime.split(" : ")[1]);
                                            int ver_et_h = Integer.parseInt(verify_etime.split(" : ")[0]);
                                            int ver_et_m = Integer.parseInt(verify_etime.split(" : ")[1]);
                                            if (timeConflict(st_h, st_m, et_h, et_m, ver_st_h, ver_st_m, ver_et_h, ver_et_m)) {
                                                titi = cc.getString(cc.getColumnIndex(COLUMN_TITLE));
                                                conflict = true;
                                                break;
                                            }
                                        }
                                    }
                                } else if (repeatway.equals("Everyday")) {
                                    if (inBetween(verify_sdate, verify_edate, startdate) || inBetween(startdate, enddate, verify_sdate)) {
                                        //time conflict judge
                                        String verify_stime = cc.getString(cc.getColumnIndex(COLUMN_STIME));
                                        String verify_etime = cc.getString(cc.getColumnIndex(COLUMN_ETIME));
                                        int ver_st_h = Integer.parseInt(verify_stime.split(" : ")[0]);
                                        int ver_st_m = Integer.parseInt(verify_stime.split(" : ")[1]);
                                        int ver_et_h = Integer.parseInt(verify_etime.split(" : ")[0]);
                                        int ver_et_m = Integer.parseInt(verify_etime.split(" : ")[1]);
                                        if (timeConflict(st_h, st_m, et_h, et_m, ver_st_h, ver_st_m, ver_et_h, ver_et_m)) {
                                            titi = cc.getString(cc.getColumnIndex(COLUMN_TITLE));
                                            conflict = true;
                                            break;
                                        }
                                    }
                                } else {
                                    if (inBetween(verify_sdate, verify_edate, startdate) || inBetween(startdate, enddate, verify_sdate)) {
                                        int verify_sd_y = Integer.parseInt(verify_sdate.split("-")[0]);
                                        int verify_sd_m = Integer.parseInt(verify_sdate.split("-")[1]);
                                        int verify_sd_d = Integer.parseInt(verify_sdate.split("-")[2]);
                                        int ver_dayinweek = sc.getDayInWeek(verify_sd_y, verify_sd_m);
                                        int dayinweek = sc.getDayInWeek(sd_y, sd_m);
                                        if ((sd_d + dayinweek) % 7 == (verify_sd_d + ver_dayinweek) % 7) {
                                            //time conflict judge
                                            String verify_stime = cc.getString(cc.getColumnIndex(COLUMN_STIME));
                                            String verify_etime = cc.getString(cc.getColumnIndex(COLUMN_ETIME));
                                            int ver_st_h = Integer.parseInt(verify_stime.split(" : ")[0]);
                                            int ver_st_m = Integer.parseInt(verify_stime.split(" : ")[1]);
                                            int ver_et_h = Integer.parseInt(verify_etime.split(" : ")[0]);
                                            int ver_et_m = Integer.parseInt(verify_etime.split(" : ")[1]);
                                            if (timeConflict(st_h, st_m, et_h, et_m, ver_st_h, ver_st_m, ver_et_h, ver_et_m)) {
                                                titi = cc.getString(cc.getColumnIndex(COLUMN_TITLE));
                                                conflict = true;
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }//check if it is itself
                    }while (cc.moveToNext());
                }
                cc.close();
            }
            if(conflict){
                Toast.makeText(MainActivity_UpdateEvents.this, "Time Conflict Exist with " + titi, Toast.LENGTH_LONG).show();
                return true;
            }


            edb.updateEvent(id_in_this_view, title, description, starttime, endtime, startdate, enddate, notification, repeatway, eventcolor, countday);

            //clear notification with this id
            /*AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent myi = new Intent(getApplicationContext(), MyIntentService.class);
            PendingIntent pd = PendingIntent.getService(getApplicationContext(), Integer.parseInt(id), myi, PendingIntent.FLAG_ONE_SHOT);
            am.cancel(pd);

            //then add noti back
            if(!notification.equals("No Notifications")) {

                Intent myIntent = new Intent(MainActivity_UpdateEvents.this, MyIntentService.class);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                PendingIntent pendingIntent = PendingIntent.getService(MainActivity_UpdateEvents.this, 0, myIntent, PendingIntent.FLAG_ONE_SHOT);

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

                if(repeatway.equals("Once Per Week"))
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24 * 60 * 60 * 1000 * 7, pendingIntent);  //set repeating every 24 hours
                else if(repeatway.equals("Everyday"))
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 2 * 60 * 1000, pendingIntent);  //set repeating every 24 hours
            }*/

            finish();
            //Toast.makeText(MainActivity_UpdateEvents.this, starttime, Toast.LENGTH_SHORT).show();
            return true;
        }
        if(item.getItemId() == R.id.deleteevent){
            EventDataBase edb = EventDataBase.getInstance(MainActivity_UpdateEvents.this);
            edb.deleteEvent(id_in_this_view, MainActivity_UpdateEvents.this);

            //clear notification with this id
            /*AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent myi = new Intent(getApplicationContext(), MyIntentService.class);
            PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), Integer.parseInt(id), myi, PendingIntent.FLAG_ONE_SHOT);
            am.cancel(pendingIntent);*/

            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void loadView(String endTime, String day){
        EventDataBase edb = EventDataBase.getInstance(MainActivity_UpdateEvents.this);
        SQLiteDatabase dbThisWeek = edb.getMyWritableDataBase();
        Cursor cc = dbThisWeek.rawQuery("SELECT " + COLUMN_ID + ", " + COLUMN_TITLE + ", " + COLUMN_DESC + ", " + COLUMN_STIME + ", " + COLUMN_ETIME + ", " + COLUMN_SDATE + ", " + COLUMN_EDATE + ", " + COLUMN_NOTI + ", " + COLUMN_COUNT + ", " + COLUMN_COLOR + ", " + COLUMN_REPEAT + " FROM " + TABLE_EVENTS + ";", null);

        if(cc != null){
            if(cc.moveToFirst()){
                do {
                    endtime = cc.getString(cc.getColumnIndex(COLUMN_ETIME));
                    if(endtime.equals(endTime)) {
                        startdate = cc.getString(cc.getColumnIndex(COLUMN_SDATE));
                        enddate = cc.getString(cc.getColumnIndex(COLUMN_EDATE));
                        repeatway = cc.getString(cc.getColumnIndex(COLUMN_REPEAT));
                        if(inBetween(startdate, enddate, day) && repeatway.equals("Everyday")){
                            id = cc.getString(cc.getColumnIndex(COLUMN_ID));
                            title = cc.getString(cc.getColumnIndex(COLUMN_TITLE));
                            description = cc.getString(cc.getColumnIndex(COLUMN_DESC));
                            starttime = cc.getString(cc.getColumnIndex(COLUMN_STIME));
                            //notification = cc.getString(cc.getColumnIndex(COLUMN_NOTI));
                            eventcolor = cc.getString(cc.getColumnIndex(COLUMN_COLOR));
                            repeatway = cc.getString(cc.getColumnIndex(COLUMN_REPEAT));
                            //countday = cc.getString(cc.getColumnIndex(COLUMN_COUNT));

                            tv_title.setText(title);
                            tv_desc.setText(description);
                            tv_stime.setText(starttime);
                            tv_etime.setText(endtime);
                            tv_sdate.setText(startdate);
                            tv_edate.setText(enddate);
                            //tv_noti.setText(notification);
                            tv_repeat.setText(repeatway);
                            tv_color.setBackgroundColor(Integer.parseInt(eventcolor));
                            switch (Integer.parseInt(eventcolor)){
                                case Color.GREEN:
                                    tv_color.setText("Green");
                                    tv_color.setGravity(Gravity.CENTER);
                                    break;
                                case Color.BLUE:
                                    tv_color.setText("Blue");
                                    tv_color.setGravity(Gravity.CENTER);
                                    break;
                                case Color.RED:
                                    tv_color.setText("Red");
                                    tv_color.setGravity(Gravity.CENTER);
                                    break;
                                case Color.YELLOW:
                                    tv_color.setText("Yellow");
                                    tv_color.setGravity(Gravity.CENTER);
                                    break;
                                case Color.CYAN:
                                    tv_color.setText("Cyan");
                                    tv_color.setGravity(Gravity.CENTER);
                                    break;
                            }
                            //if(countday.equals("1"))
                            //    count_day.setChecked(true);

                            id_in_this_view = id;
                        }else if(inBetween(startdate, startdate, day) && repeatway.equals("Never")){
                            id = cc.getString(cc.getColumnIndex(COLUMN_ID));
                            title = cc.getString(cc.getColumnIndex(COLUMN_TITLE));
                            description = cc.getString(cc.getColumnIndex(COLUMN_DESC));
                            starttime = cc.getString(cc.getColumnIndex(COLUMN_STIME));
                            //notification = cc.getString(cc.getColumnIndex(COLUMN_NOTI));
                            eventcolor = cc.getString(cc.getColumnIndex(COLUMN_COLOR));
                            repeatway = cc.getString(cc.getColumnIndex(COLUMN_REPEAT));
                            //countday = cc.getString(cc.getColumnIndex(COLUMN_COUNT));

                            tv_title.setText(title);
                            tv_desc.setText(description);
                            tv_stime.setText(starttime);
                            tv_etime.setText(endtime);
                            tv_sdate.setText(startdate);
                            tv_edate.setText(enddate);
                            //tv_noti.setText(notification);
                            tv_repeat.setText(repeatway);
                            tv_color.setBackgroundColor(Integer.parseInt(eventcolor));
                            switch (Integer.parseInt(eventcolor)){
                                case Color.GREEN:
                                    tv_color.setText("Green");
                                    tv_color.setGravity(Gravity.CENTER);
                                    break;
                                case Color.BLUE:
                                    tv_color.setText("Blue");
                                    tv_color.setGravity(Gravity.CENTER);
                                    break;
                                case Color.RED:
                                    tv_color.setText("Red");
                                    tv_color.setGravity(Gravity.CENTER);
                                    break;
                                case Color.YELLOW:
                                    tv_color.setText("Yellow");
                                    tv_color.setGravity(Gravity.CENTER);
                                    break;
                                case Color.CYAN:
                                    tv_color.setText("Cyan");
                                    tv_color.setGravity(Gravity.CENTER);
                                    break;
                            }
                            //if(countday.equals("1"))
                            //    count_day.setChecked(true);

                            id_in_this_view = id;
                        }else if(inBetween(startdate, enddate, day) && repeatway.equals("Once Per Week")){
                            int yy_sd = Integer.parseInt(startdate.split("-")[0]);
                            int mm_sd = Integer.parseInt(startdate.split("-")[1]);
                            int dd_sd = Integer.parseInt(startdate.split("-")[2]);
                            int dinw_sd = sc.getDayInWeek(yy_sd, mm_sd);
                            int yy_cd = Integer.parseInt(day.split("-")[0]);
                            int mm_cd = Integer.parseInt(day.split("-")[1]);
                            int dd_cd = Integer.parseInt(day.split("-")[2]);
                            int dinw_cd = sc.getDayInWeek(yy_cd, mm_cd);
                            if(((dd_sd+dinw_sd)%7) == ((dd_cd+dinw_cd)%7)){
                                id = cc.getString(cc.getColumnIndex(COLUMN_ID));
                                title = cc.getString(cc.getColumnIndex(COLUMN_TITLE));
                                description = cc.getString(cc.getColumnIndex(COLUMN_DESC));
                                starttime = cc.getString(cc.getColumnIndex(COLUMN_STIME));
                                //notification = cc.getString(cc.getColumnIndex(COLUMN_NOTI));
                                eventcolor = cc.getString(cc.getColumnIndex(COLUMN_COLOR));
                                repeatway = cc.getString(cc.getColumnIndex(COLUMN_REPEAT));
                                //countday = cc.getString(cc.getColumnIndex(COLUMN_COUNT));

                                tv_title.setText(title);
                                tv_desc.setText(description);
                                tv_stime.setText(starttime);
                                tv_etime.setText(endtime);
                                tv_sdate.setText(startdate);
                                tv_edate.setText(enddate);
                                //tv_noti.setText(notification);
                                tv_repeat.setText(repeatway);
                                tv_color.setBackgroundColor(Integer.parseInt(eventcolor));
                                switch (Integer.parseInt(eventcolor)){
                                    case Color.GREEN:
                                        tv_color.setText("Green");
                                        tv_color.setGravity(Gravity.CENTER);
                                        break;
                                    case Color.BLUE:
                                        tv_color.setText("Blue");
                                        tv_color.setGravity(Gravity.CENTER);
                                        break;
                                    case Color.RED:
                                        tv_color.setText("Red");
                                        tv_color.setGravity(Gravity.CENTER);
                                        break;
                                    case Color.YELLOW:
                                        tv_color.setText("Yellow");
                                        tv_color.setGravity(Gravity.CENTER);
                                        break;
                                    case Color.CYAN:
                                        tv_color.setText("Cyan");
                                        tv_color.setGravity(Gravity.CENTER);
                                        break;
                                }
                                //if(countday.equals("1"))
                                //    count_day.setChecked(true);

                                id_in_this_view = id;
                            }
                        }
                    }
                }while (cc.moveToNext());
            }
            cc.close();
        }
    }

    public void loadViewForMainActivity(String id_info){
        EventDataBase edb = EventDataBase.getInstance(MainActivity_UpdateEvents.this);
        SQLiteDatabase dbThisWeek = edb.getMyWritableDataBase();
        Cursor cc = dbThisWeek.rawQuery("SELECT " + COLUMN_ID + ", " + COLUMN_TITLE + ", " + COLUMN_DESC + ", " + COLUMN_STIME + ", " + COLUMN_ETIME + ", " + COLUMN_SDATE + ", " + COLUMN_EDATE + ", " + COLUMN_NOTI + ", " + COLUMN_COUNT + ", " + COLUMN_COLOR + ", " + COLUMN_REPEAT + " FROM " + TABLE_EVENTS + ";", null);

        if(cc != null){
            if(cc.moveToFirst()){
                do {
                    id = cc.getString(cc.getColumnIndex(COLUMN_ID));
                    if(id.equals(id_info)) {
                        startdate = cc.getString(cc.getColumnIndex(COLUMN_SDATE));
                        enddate = cc.getString(cc.getColumnIndex(COLUMN_EDATE));
                        title = cc.getString(cc.getColumnIndex(COLUMN_TITLE));
                        description = cc.getString(cc.getColumnIndex(COLUMN_DESC));
                        starttime = cc.getString(cc.getColumnIndex(COLUMN_STIME));
                        endtime = cc.getString(cc.getColumnIndex(COLUMN_ETIME));
                        //notification = cc.getString(cc.getColumnIndex(COLUMN_NOTI));
                        eventcolor = cc.getString(cc.getColumnIndex(COLUMN_COLOR));
                        repeatway = cc.getString(cc.getColumnIndex(COLUMN_REPEAT));
                        //countday = cc.getString(cc.getColumnIndex(COLUMN_COUNT));

                        tv_title.setText(title);
                        tv_desc.setText(description);
                        tv_stime.setText(starttime);
                        tv_etime.setText(endtime);
                        tv_sdate.setText(startdate);
                        tv_edate.setText(enddate);
                        //tv_noti.setText(notification);
                        tv_repeat.setText(repeatway);
                        tv_color.setBackgroundColor(Integer.parseInt(eventcolor));
                        switch (Integer.parseInt(eventcolor)){
                            case Color.GREEN:
                                tv_color.setText("Green");
                                tv_color.setGravity(Gravity.CENTER);
                                break;
                            case Color.BLUE:
                                tv_color.setText("Blue");
                                tv_color.setGravity(Gravity.CENTER);
                                break;
                            case Color.RED:
                                tv_color.setText("Red");
                                tv_color.setGravity(Gravity.CENTER);
                                break;
                            case Color.YELLOW:
                                tv_color.setText("Yellow");
                                tv_color.setGravity(Gravity.CENTER);
                                break;
                            case Color.CYAN:
                                tv_color.setText("Cyan");
                                tv_color.setGravity(Gravity.CENTER);
                                break;
                        }
                        //if(countday.equals("1"))
                        //   count_day.setChecked(true);
                        //else
                        //    count_day.setChecked(false);

                        //Toast.makeText(MainActivity_UpdateEvents.this, id, Toast.LENGTH_SHORT).show();
                    }
                }while (cc.moveToNext());
            }
            cc.close();
        }
    }

    public boolean inBetween(String sdate, String edate, String cdate){
        int y_s = Integer.parseInt(sdate.split("-")[0]);
        int m_s = Integer.parseInt(sdate.split("-")[1]);
        int d_s = Integer.parseInt(sdate.split("-")[2]);
        int y_e = Integer.parseInt(edate.split("-")[0]);
        int m_e = Integer.parseInt(edate.split("-")[1]);
        int d_e = Integer.parseInt(edate.split("-")[2]);
        int y_c = Integer.parseInt(cdate.split("-")[0]);
        int m_c = Integer.parseInt(cdate.split("-")[1]);
        int d_c = Integer.parseInt(cdate.split("-")[2]);
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

    public boolean timeConflict(int st_h_1, int st_m_1, int et_h_1, int et_m_1,int st_h_2, int st_m_2, int et_h_2, int et_m_2){
        if(st_h_2 < et_h_1 || (st_h_2 == et_h_1 && st_m_2 < et_m_1)){
            if(st_h_2 > st_h_1 || (st_h_2 == st_h_1 && st_m_2 >= st_m_1)){
                return true;
            }
        }
        if(et_h_2 > st_h_1 || (et_h_2 == st_h_1 && et_m_2 > st_m_1)){
            if(et_h_2 < et_h_1 || (et_h_2 == et_h_1 && et_m_2 <= et_m_1)){
                return true;
            }
        }
        return false;
    }
}
