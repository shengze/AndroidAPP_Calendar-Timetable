package com.shengze3322vs.year;

/**
 * Created by Administrator on 2017/9/19 0019.
 */

public class DataBaseClass {
    private int _id;
    private String _title;
    private String _description;
    private String _starttime;
    private String _endtime;
    private String _startdate;
    private String _enddate;
    private String _notification;
    private String _eventcolor;
    private String _countday;

    public DataBaseClass(){
    }

    public DataBaseClass(String title, String description, String starttime, String endtime, String startdate, String enddate, String notification, String eventcolor, String countday){
        this._title = title;
        this._description = description;
        this._starttime = starttime;
        this._endtime = endtime;
        this._startdate = startdate;
        this._enddate = enddate;
        this._notification = notification;
        this._eventcolor = eventcolor;
        this._countday = countday;
    }

    public void set_id(int _id){
        this._id = _id;
    }
    public void set_title(String title){
        this._title = title;
    }
    public void set_description(String description){
        this._description = description;
    }
    public void set_starttime(String starttime){
        this._starttime = starttime;
    }
    public void set_endtime(String endtime){
        this._endtime = endtime;
    }
    public void set_startdate(String startdate){
        this._startdate = startdate;
    }
    public void set_enddate(String enddate){
        this._enddate = enddate;
    }
    public void set_notification(String notification){
        this._notification = notification;
    }
    public void set_eventcolor(String eventcolor){
        this._eventcolor = eventcolor;
    }
    public void set_countday(String countday){
        this._countday = countday;
    }
    public int get_id(){
        return _id;
    }
    public String get_title(){
        return _title;
    }
    public String get_description(){
        return _description;
    }
    public String get_starttime(){
        return _starttime;
    }
    public String get_endtime(){
        return _endtime;
    }
    public String get_startdate(){
        return _startdate;
    }
    public String get_enddate(){
        return _enddate;
    }
    public String get_notification(){
        return _notification;
    }
    public String get_eventcolor(){
        return _eventcolor;
    }
    public String get_countday(){
        return _countday;
    }
}
