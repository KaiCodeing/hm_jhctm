package com.hemaapp.jhctm.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.activity.AdvertisingPromotionActivity;
import com.hemaapp.jhctm.activity.MyCardPackageActivity;
import com.hemaapp.jhctm.model.Card;

import java.util.ArrayList;

import xtom.frame.util.XtomTimeUtil;

/**
 * Created by lenovo on 2017/1/4.
 * 我的卡包
 */
public class MyCardPackageAdapter extends HemaAdapter {
    private MyCardPackageActivity activity;
    private ArrayList<Card> cards;
    public MyCardPackageAdapter(MyCardPackageActivity activity,ArrayList<Card> cards) {
        super(activity);
        this.activity = activity;
        this.cards = cards;
    }

    @Override
    public boolean isEmpty() {
        return cards==null || cards.size()==0;
    }

    @Override
    public int getCount() {
        return isEmpty()?1:cards.size();
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
        if (isEmpty())
        return getEmptyView(parent);
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.adapter_my_card_package, null);
            ViewHolder holder = new ViewHolder();
            findView(holder, convertView);
            convertView.setTag(R.id.TAG_VIEWHOLDER, holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
        setData(holder, position);
        return convertView;
    }

    //adapter_my_card_package
    private class ViewHolder {
        TextView number;//编号
        TextView time;//合成时间
        TextView sell_middle;//出售中
        TextView sell;//出售
        TextView split;//拆分
    }
    private void findView(ViewHolder holder,View view)
    {
        holder.number = (TextView) view.findViewById(R.id.number);
        holder.time = (TextView) view.findViewById(R.id.time);
        holder.sell_middle = (TextView) view.findViewById(R.id.sell_middle);
        holder.sell = (TextView) view.findViewById(R.id.sell);
        holder.split = (TextView) view.findViewById(R.id.split);
    }
    private void setData(ViewHolder holder,int position)
    {
        Card card = cards.get(position);
        holder.number.setText("编号 "+card.getSn());
        String time = card.getRegdate();
        String regdate = XtomTimeUtil.TransTime(time, "yyyy/MM/dd hh:mm");
        holder.time.setText("合成时间:"+regdate);
        //判断状态  未出售
        if ("0".equals(card.getSaleflag()))
        {
            holder.sell_middle.setVisibility(View.GONE);
            holder.sell.setVisibility(View.VISIBLE);
            holder.split.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.sell_middle.setVisibility(View.VISIBLE);
            holder.sell.setVisibility(View.GONE);
            holder.split.setVisibility(View.GONE);
        }
        //出售
        holder.sell.setTag(R.id.TAG,card);
        holder.sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Card card1 = (Card) v.getTag(R.id.TAG);
                Intent intent = new Intent(activity, AdvertisingPromotionActivity.class);
                intent.putExtra("type","2");
                intent.putExtra("cardid",card1.getId());
                activity.startActivity(intent);
            }
        });
        //拆分
        holder.split.setTag(R.id.TAG,card.getId());
        holder.split.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String cardid = (String) v.getTag(R.id.TAG);
                activity.removeCard(cardid);
            }
        });
    }
}
