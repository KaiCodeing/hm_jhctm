package com.hemaapp.jhctm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.jhctm.R;

/**
 * Created by lenovo on 2016/12/23.
 * 品牌商详情中的adapter
 */
public class BrandbusinessInforAdapter extends HemaAdapter {
    public BrandbusinessInforAdapter(Context mContext) {
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
        if (convertView==null)
        {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_item_brandbusiness_infor,null);
            ViewHolder holder = new ViewHolder();
            convertView.setTag(R.id.TAG_VIEWHOLDER,holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
        setData(holder,position);
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

    }
}
