package com.shengze3322vs.year;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Administrator on 2017/9/19 0019.
 */

public class Singleton {
    public Singleton eventObj;

    public ArrayList<ArrayList<String>> eventSet;
    public HashSet<String> checkRe;

    private Singleton(){
        eventSet = new ArrayList<ArrayList<String>>();
        checkRe = new HashSet<>();
    }

    public Singleton getInstance(){
        if(eventObj == null)
            eventObj = new Singleton();
        return eventObj;
    }
}
