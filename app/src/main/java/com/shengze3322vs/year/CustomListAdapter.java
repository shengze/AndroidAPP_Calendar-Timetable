package com.shengze3322vs.year;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/9/28 0028.
 */

public class CustomListAdapter extends ArrayAdapter<String> {
    public String[] eventtime;
    public Integer[] eventcolor;
    CustomListAdapter(@NonNull Context context, String[] titles, String[] times, Integer[] colors) {
        super(context, R.layout.custom_list, titles);
        eventtime = times;
        eventcolor = colors;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater shengzeInflater = LayoutInflater.from(getContext());
        View customView = convertView;
        if(customView == null)
            customView = shengzeInflater.inflate(R.layout.custom_list, parent, false);

        String eventtitle = getItem(position);
        TextView shengzeTitle = (TextView) customView.findViewById(R.id.textView1);
        TextView shengzeTime = (TextView) customView.findViewById(R.id.textView2);
        TextView shengzeColor = (TextView) customView.findViewById(R.id.textView3);

        shengzeTitle.setText(eventtitle);
        shengzeTime.setText(eventtime[position]);
        shengzeColor.setBackgroundColor(eventcolor[position]);
        shengzeColor.setText("");

        return customView;
    }
}
