package com.hemaapp.jhctm.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.jhctm.R;

/**
 * Created by lenovo on 2016/12/28.
 * 选择分类的adapter
 */
public class PopSelectClassAdapter extends HemaAdapter {
    public PopSelectClassAdapter(Context mContext) {
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
    /**
     * adapter_select_type
     */
    private class ViewHolder{
        TextView type_name;
    }
    private void findView(ViewHolder holder,View view)
    {
        holder.type_name = (TextView) view.findViewById(R.id.type_name);
    }
    private void setData(ViewHolder holder,int position)
    {

    }
}
