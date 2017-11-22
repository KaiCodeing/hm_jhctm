package com.hemaapp.jhctm.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.activity.SelectDispatchingActivity;
import com.hemaapp.jhctm.model.Mall;

import java.util.ArrayList;

/**
 * Created by lenovo on 2016/12/28.
 * 选择配送方式，超市
 */
public class SelectDispatchingAdapter extends HemaAdapter {
    private SelectDispatchingActivity activity;
    private ArrayList<Mall> malls;

    public SelectDispatchingAdapter(SelectDispatchingActivity activity, ArrayList<Mall> malls) {
        super(activity);
        this.activity = activity;
        this.malls = malls;
    }

    @Override
    public boolean isEmpty() {
        return malls == null || malls.size() == 0;
    }

    @Override
    public int getCount() {
        return isEmpty() ? 0 : malls.size();
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
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.adapter_dispatching_view, null);
            ViewHolder holder = new ViewHolder();
            findView(holder, convertView);
            convertView.setTag(R.id.TAG, holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag(R.id.TAG);
        setData(holder, position);
        return convertView;
    }

    /**
     * adapter_dispatching_view
     */
    private class ViewHolder {
        LinearLayout item_layout;
        TextView supermarket_name;//超市名称
        TextView supermarket_address;//超市地址

    }

    private void findView(ViewHolder holder, View view) {
        holder.item_layout = (LinearLayout) view.findViewById(R.id.item_layout);
        holder.supermarket_name = (TextView) view.findViewById(R.id.supermarket_name);
        holder.supermarket_address = (TextView) view.findViewById(R.id.supermarket_address);

    }

    private void setData(ViewHolder holder, int position) {
        Mall mall = malls.get(position);
        holder.supermarket_name.setText(mall.getNickname());
        holder.supermarket_address.setText(mall.getAddress());
    }
}
