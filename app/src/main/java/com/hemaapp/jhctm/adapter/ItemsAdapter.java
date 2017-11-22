package com.hemaapp.jhctm.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.activity.YiWuDuiActivity;
import com.hemaapp.jhctm.model.City;

import java.util.ArrayList;

/**
 * Created by lenovo on 2017/2/20.
 */
public class ItemsAdapter extends HemaAdapter {
    private Context mContext;
    private ArrayList<City> cities;
    private String goodId;
    public ItemsAdapter(Context mContext,ArrayList<City> cities,String brandId) {
        super(mContext);
        this.cities = cities;
        this.mContext = mContext;
        this.goodId = brandId;
    }

    @Override
    public boolean isEmpty() {
        return cities==null || cities.size()==0;
    }

    @Override
    public int getCount() {
        return isEmpty()?0:cities.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_item_brandbusiness_infor, null);
            ViewHolder holder = new ViewHolder();
            findView(holder, convertView);
            convertView.setTag(R.id.TAG_VIEWHOLDER, holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
        setData(holder, position);
        return convertView;
    }
    private class ViewHolder{
        TextView hotel_name;
    }
    private void findView(ViewHolder holder,View view)
    {
        holder.hotel_name = (TextView) view.findViewById(R.id.hotel_name);
    }
    private void setData(ViewHolder holder,int position)
    {
        City city = cities.get(position);
        holder.hotel_name.setText(city.getName());
        //跳转分类
        holder.hotel_name.setTag(R.id.TAG,city);
        holder.hotel_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                City city1 = (City) v.getTag(R.id.TAG);
                Intent intent = new Intent(mContext, YiWuDuiActivity.class);
                intent.putExtra("goodId",goodId);
                intent.putExtra("commodityId",city1.getId());
                intent.putExtra("goodName",city1.getName());
                mContext.startActivity(intent);
            }
        });
    }
}
