package com.shengze3322vs.year;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2017/9/14 0014.
 */

public class MyHorizontalScrollView extends HorizontalScrollView {

    private View mView;

    public MyHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyHorizontalScrollView(Context context) {
        super(context);

        setScrollView(mView);

    }
/*
    protected void onScrollChanged(int l, int t, int oldl, int oldt){
        super.onScrollChanged(l, t, oldl, oldt);
        if(null != mView){
            mView.scrollTo(l, t);
        }
    }
*/
    public void setScrollView(View mView){
        this.mView = mView;
    }

}
