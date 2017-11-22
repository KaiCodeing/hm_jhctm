package com.hemaapp.jhctm.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * Created by lenovo on 2017/2/7.
 */
public class GridViewMy extends GridView {

    public GridViewMy(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置上下不滚动
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_MOVE){
            return true;//true:禁止滚动
        }

        return super.dispatchTouchEvent(ev);
    }
}
