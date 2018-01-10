package com.shengze3322vs.year.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by Administrator on 2017/9/19 0019.
 */

public class CustomView extends View {

    public Rect RectSquare0 = new Rect();
    public Rect RectSquare1 = new Rect();
    public Rect RectSquare2 = new Rect();
    public Rect RectSquare3 = new Rect();
    public Rect RectSquare4 = new Rect();
    public Rect RectSquare5 = new Rect();
    public Paint paint0 = new Paint();
    public Paint paint1 = new Paint();
    public Paint paint2 = new Paint();
    public Paint paint3 = new Paint();
    public Paint paint4 = new Paint();
    public Paint paint5 = new Paint();

    public CustomView(Context context) {
        super(context);

        init(null);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(attrs);
    }

    private void init(@Nullable AttributeSet set){

        paint0.setColor(Color.WHITE);
        paint1.setColor(Color.WHITE);
        paint2.setColor(Color.WHITE);
        paint3.setColor(Color.WHITE);
        paint4.setColor(Color.WHITE);
        paint5.setColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Resources r = getResources();
        float unit = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) 3, r.getDisplayMetrics());
        RectSquare0.left = (int)unit;
        RectSquare0.top = (int)unit*2;
        RectSquare0.right = RectSquare0.left + (int)unit*3;
        RectSquare0.bottom = RectSquare0.top + (int)unit*4;
        canvas.drawRect(RectSquare0, paint0);

        RectSquare1.left = (int)unit*7;
        RectSquare1.top = (int)unit*2;
        RectSquare1.right = RectSquare1.left + (int)unit*3;
        RectSquare1.bottom = RectSquare1.top + (int)unit*4;
        canvas.drawRect(RectSquare1, paint1);

        RectSquare2.left = (int)unit*13;
        RectSquare2.top = (int)unit*2;
        RectSquare2.right = RectSquare2.left + (int)unit*3;
        RectSquare2.bottom = RectSquare2.top + (int)unit*4;
        canvas.drawRect(RectSquare2, paint2);

        RectSquare3.left = (int)unit;
        RectSquare3.top = (int)unit*10;
        RectSquare3.right = RectSquare3.left + (int)unit*3;
        RectSquare3.bottom = RectSquare3.top + (int)unit*4;
        canvas.drawRect(RectSquare3, paint3);

        RectSquare4.left = (int)unit*7;
        RectSquare4.top = (int)unit*10;
        RectSquare4.right = RectSquare4.left + (int)unit*3;
        RectSquare4.bottom = RectSquare4.top + (int)unit*4;
        canvas.drawRect(RectSquare4, paint4);

        RectSquare5.left = (int)unit*13;
        RectSquare5.top = (int)unit*10;
        RectSquare5.right = RectSquare5.left + (int)unit*3;
        RectSquare5.bottom = RectSquare5.top + (int)unit*4;
        canvas.drawRect(RectSquare5, paint5);

        init(null);

    }

    public void setColor(int cc){
        switch (cc){
            case Color.GRAY:
                paint0.setColor(Color.GRAY);
                break;
            case Color.GREEN:
                paint1.setColor(Color.GREEN);
                //postInvalidate();
                break;
            case Color.BLUE:
                paint2.setColor(Color.BLUE);
                //postInvalidate();
                break;
            case Color.RED:
                paint3.setColor(Color.RED);
                //postInvalidate();
                break;
            case Color.YELLOW:
                paint4.setColor(Color.YELLOW);
                //postInvalidate();
                break;
            case Color.CYAN:
                paint5.setColor(Color.CYAN);
                //postInvalidate();
                break;
            case Color.WHITE:
                paint1.setColor(Color.WHITE);
                paint2.setColor(Color.WHITE);
                paint3.setColor(Color.WHITE);
                paint4.setColor(Color.WHITE);
                paint5.setColor(Color.WHITE);
                break;
        }
    }

}
