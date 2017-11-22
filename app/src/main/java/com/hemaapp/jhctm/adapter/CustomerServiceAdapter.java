package com.hemaapp.jhctm.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.jhctm.R;

/**
 * Created by lenovo on 2017/1/6.、
 * 售后
 */
public class CustomerServiceAdapter extends BaseExpandableListAdapter {
    @Override
    public int getGroupCount() {
        return 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
    //父view  adapter_parent_order_view
    private class ViewGroupHolder{
        TextView number_text;//订单编号
        TextView shouhuo_type;//收货状态
    }
    private void findGroup(ViewGroupHolder group,View view)
    {
        group.number_text = (TextView) view.findViewById(R.id.number_text);
        group.shouhuo_type = (TextView) view.findViewById(R.id.shouhuo_type);
    }
    private void setGroup(ViewGroupHolder group,int position)
    {

    }
    //子view adapter_child_order_view
    private class ChildViewHolder{
        LinearLayout item_hoetl;//条目
        ImageView commod_img;//商品图片
        TextView hotel_name;//商品名称
        TextView hotel_money;//规格
        TextView price;//积分
        TextView jifen_text;//积分字t
        TextView add_bz;//加号
        TextView fenxiang_text;//分享积分
        TextView guige_all;//商品数量 钱数
        TextView order_cancel;//取消订单
        TextView order_over;//去支付 评价，确认收货
        LinearLayout button_layout;
    }
    private void findChild(ChildViewHolder holder, View view)
    {
        holder.item_hoetl = (LinearLayout) view.findViewById(R.id.item_hoetl);
        holder.commod_img = (ImageView) view.findViewById(R.id.commod_img);
        holder.hotel_name = (TextView) view.findViewById(R.id.hotel_name);
        holder.hotel_money = (TextView) view.findViewById(R.id.hotel_money);
        holder.price = (TextView) view.findViewById(R.id.price);
        holder.jifen_text = (TextView) view.findViewById(R.id.jifen_text);
        holder.add_bz = (TextView) view.findViewById(R.id.add_bz);
        holder.guige_all = (TextView) view.findViewById(R.id.guige_all);
        holder.fenxiang_text = (TextView) view.findViewById(R.id.fenxiang_text);
        holder.order_cancel = (TextView) view.findViewById(R.id.order_cancel);
        holder.order_over = (TextView) view.findViewById(R.id.order_over);
        holder.button_layout = (LinearLayout) view.findViewById(R.id.button_layout);

    }
    private void setChild(ChildViewHolder holder,int position)
    {

    }
}
