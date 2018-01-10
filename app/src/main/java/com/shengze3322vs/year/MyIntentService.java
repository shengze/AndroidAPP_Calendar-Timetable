package com.shengze3322vs.year;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

import static com.shengze3322vs.year.EventDataBase.*;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.shengze3322vs.year.action.FOO";
    private static final String ACTION_BAZ = "com.shengze3322vs.year.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.shengze3322vs.year.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.shengze3322vs.year.extra.PARAM2";

    public static final HashMap<String, NotificationCompat.Builder> hmap_noti = new HashMap<>();

    public MyIntentService() {
        super("MyIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        /*if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
        }*/

        /*Calendar rightNow = Calendar.getInstance();
        int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
        int currentMin = rightNow.get(Calendar.MINUTE);
        String curT = Integer.toString(currentHour) + " : " + Integer.toString(currentMin);
*/
        /*
        EventDataBase dataBaseHandler = EventDataBase.getInstance(this);
        SQLiteDatabase getId = dataBaseHandler.getMyWritableDataBase();
        Cursor cc = getId.rawQuery("SELECT " + COLUMN_ID + ", " + COLUMN_STIME + ", " + COLUMN_ETIME + ", " + COLUMN_SDATE + " FROM " + TABLE_EVENTS + ";", null);
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
        Random random = new Random();
        int uniqueID = random.nextInt(100000);

        //if(!SmartStackClass.smartstack.isEmpty()) {

        //    uniqueID = SmartStackClass.smartstack_noti_id.pop();
        //    String tt = SmartStackClass.smartstack.pop();
        //    String dd = SmartStackClass.smartstack2.pop();

            //start time
        //    String stime = dd.split(" - ")[0];

        //    if(!hmap_noti.containsKey(stime)) {
                NotificationCompat.Builder notification;

                notification = new NotificationCompat.Builder(this);
                notification.setAutoCancel(true);
                //build notification
                notification.setSmallIcon(R.mipmap.ic_launcher);
                notification.setTicker("Event Notification.");
                notification.setWhen(System.currentTimeMillis());
                notification.setContentTitle("title");
                notification.setContentText("description");

                //set notification sound
                Uri alarmSound = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_NOTIFICATION);
                notification.setSound(alarmSound);


                Intent ii = new Intent(this, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, random.nextInt(100000), ii, PendingIntent.FLAG_ONE_SHOT);
                notification.setContentIntent(pendingIntent);

        //        hmap_noti.put(stime, notification);
        //    }
        //}

        //if(hmap_noti.containsKey(curT)) {
            //issue notification
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nm.notify(uniqueID, notification.build());
        //    nm.notify(uniqueID, hmap_noti.get(curT).build());
        //}
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
