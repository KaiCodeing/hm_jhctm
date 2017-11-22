package com.hemaapp.jhctm.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.wheel.adapter.AbstractWheelTextAdapter;

import java.util.ArrayList;


public class Time2ListAdapter extends AbstractWheelTextAdapter {

	private ArrayList<String> times;
	
	public Time2ListAdapter(Context context, ArrayList<String> times) {
		 super(context, R.layout.tempitem, NO_RESOURCE);
         this.times = times;
         setItemTextResource(R.id.tempValue);
	}

	@Override
	public View getItem(int index, View convertView, ViewGroup parent) {
		return super.getItem(index, convertView, parent);
	}
	
    @Override
    public int getItemsCount() {
        return times.size();
    }
    
    @Override
    protected CharSequence getItemText(int index) {
        return times.get(index);
    }

}
