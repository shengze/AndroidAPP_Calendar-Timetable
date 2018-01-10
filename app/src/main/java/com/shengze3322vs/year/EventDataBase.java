package com.shengze3322vs.year;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/9/19 0019.
 */

public class EventDataBase extends SQLiteOpenHelper {

    private final Context myContext_dbH;
    private static EventDataBase mInstance;
    private static SQLiteDatabase myWritableDb;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "eventDatabase.db";
    public static final String TABLE_EVENTS = "events";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESC = "description";
    public static final String COLUMN_STIME = "start_time";
    public static final String COLUMN_ETIME = "end_time";
    public static final String COLUMN_SDATE = "start_date";
    public static final String COLUMN_EDATE = "end_date";
    public static final String COLUMN_NOTI = "notification";
    public static final String COLUMN_REPEAT = "repeat_way";
    public static final String COLUMN_COLOR = "event_color";
    public static final String COLUMN_COUNT = "count_day";

    public EventDataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        this.myContext_dbH = context;
    }

    public static EventDataBase getInstance(Context context){
        if(mInstance == null)
            mInstance = new EventDataBase(context, null, null, 1);
        return mInstance;
    }

    public SQLiteDatabase getMyWritableDataBase(){
        if((myWritableDb == null) || (!myWritableDb.isOpen()))
            myWritableDb = this.getWritableDatabase();
        return myWritableDb;
    }

    @Override
    public synchronized void close() {
        super.close();
        if(myWritableDb != null){
            myWritableDb.close();
            myWritableDb = null;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_EVENTS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_TITLE + " TEXT, " + COLUMN_DESC + " TEXT, " + COLUMN_STIME + " TEXT, " + COLUMN_ETIME + " TEXT, " + COLUMN_SDATE + " TEXT, " + COLUMN_EDATE + " TEXT, " + COLUMN_NOTI + " TEXT, " + COLUMN_REPEAT + " TEXT, " + COLUMN_COLOR + " TEXT, " + COLUMN_COUNT + " TEXT " + ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        onCreate(db);
    }

    public void addEvent(String title, String description, String starttime, String endtime, String startdate, String enddate, String notification, String repeatway, String eventcolor, String countday){
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_DESC, description);
        values.put(COLUMN_STIME, starttime);
        values.put(COLUMN_ETIME, endtime);
        values.put(COLUMN_SDATE, startdate);
        values.put(COLUMN_EDATE, enddate);
        values.put(COLUMN_NOTI, notification);
        values.put(COLUMN_REPEAT, repeatway);
        values.put(COLUMN_COLOR, eventcolor);
        values.put(COLUMN_COUNT, countday);
        SQLiteDatabase db = getMyWritableDataBase();
        db.insert(TABLE_EVENTS, null, values);
        db.close();
    }

    public void updateEvent(String id, String title, String description, String starttime, String endtime, String startdate, String enddate, String notification, String repeatway, String eventcolor, String countday){
        SQLiteDatabase db = getMyWritableDataBase();
        db.execSQL("UPDATE " + TABLE_EVENTS + " SET " + COLUMN_TITLE + " = \"" + title + "\", " + COLUMN_DESC + " = \"" + description + "\", " + COLUMN_STIME + " = \"" + starttime + "\", " + COLUMN_ETIME + " = \"" + endtime + "\", " + COLUMN_SDATE + " = \"" + startdate + "\", " + COLUMN_EDATE + " = \"" + enddate + "\", " + COLUMN_NOTI + " = \"" + notification + "\", " + COLUMN_REPEAT + " = \"" + repeatway + "\", " + COLUMN_COLOR + " = \"" + eventcolor + "\", " + COLUMN_COUNT + " = \"" + countday + "\" WHERE " + COLUMN_ID + " = \"" + id + "\";");

    }

    public void deleteEvent(String id, Context context){
        SQLiteDatabase db = getMyWritableDataBase();
        db.execSQL("DELETE FROM " + TABLE_EVENTS + " WHERE " + COLUMN_ID + "=\"" + id + "\";");
        //Toast.makeText(context, "some items cleared", Toast.LENGTH_SHORT).show();
    }

    public void emptyDatabase(Context context){
        SQLiteDatabase db = getMyWritableDataBase();
        db.execSQL("DELETE FROM " + TABLE_EVENTS);
        Toast.makeText(context, "event table cleared", Toast.LENGTH_LONG).show();
    }

}
