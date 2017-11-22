package com.hemaapp.jhctm.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.view.JhViewPager;
import com.hemaapp.jhctm.view.JhctmGridView;

/**
 * Created by lenovo on 2016/12/20.
 * 首页的adapter
 * +HomeGridAdapter
 */
public class HomeAdapter extends HemaAdapter {

    public HomeAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
    private class ViewHolder{
        JhViewPager adviewpager;
        RadioGroup radiogroup;
        ImageView pinpai_image;
        ImageView chanping_image;
        ImageView ershou_image;
        ImageView pintai_image;
        JhctmGridView gridview;
    }
    private void findView(ViewHolder holder,View view)
    {
        holder.adviewpager = (JhViewPager) view.findViewById(R.id.adviewpager);
        holder.radiogroup = (RadioGroup) view.findViewById(R.id.radiogroup);
        holder.pinpai_image = (ImageView) view.findViewById(R.id.pinpai_image);
        holder.chanping_image = (ImageView) view.findViewById(R.id.chanping_image);
        holder.ershou_image = (ImageView) view.findViewById(R.id.ershou_image);
        holder.pintai_image = (ImageView) view.findViewById(R.id.pintai_image);
        holder.gridview = (JhctmGridView) view.findViewById(R.id.gridview);

    }
    private void setData(ViewHolder holder,int position)
    {

    }
}
