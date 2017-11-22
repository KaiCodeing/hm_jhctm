package com.hemaapp.jhctm.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
//可设置最大高度
public class Mylist extends ListView {

    /** listview高度 */
    private int listViewHeight;

    public int getListViewHeight() {
            return listViewHeight;
    }

    public void setListViewHeight(int listViewHeight) {
            this.listViewHeight = listViewHeight;
    }

    public Mylist(Context context) {
            super(context);
            // TODO Auto-generated constructor stub
    }

    public Mylist(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            // TODO Auto-generated constructor stub
    }

    public Mylist(Context context, AttributeSet attrs) {
            super(context, attrs);
            // TODO Auto-generated constructor stub
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            // TODO Auto-generated method stub
            if (listViewHeight > -1) {
                    heightMeasureSpec = MeasureSpec.makeMeasureSpec(listViewHeight,
                                    MeasureSpec.AT_MOST);
            }
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}