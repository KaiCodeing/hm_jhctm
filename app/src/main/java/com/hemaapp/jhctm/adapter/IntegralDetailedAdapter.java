package com.hemaapp.jhctm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.model.Reply;

import java.util.ArrayList;

/**
 * Created by lenovo on 2017/1/3.
 * 积分明细的adapter
 */
public class IntegralDetailedAdapter extends HemaAdapter {
    private ArrayList<Reply> replies;
    private Context mContext;

    public IntegralDetailedAdapter(Context mContext, ArrayList<Reply> replies) {
        super(mContext);
        this.replies = replies;
        this.mContext = mContext;
    }

    public void setReplies(ArrayList<Reply> replies) {
        this.replies = replies;
    }

    @Override
    public boolean isEmpty() {
        return replies == null || replies.size() == 0;
    }

    @Override
    public int getCount() {
        return isEmpty() ? 0 : replies.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_integral_detailed, null);
            ViewHolder holder = new ViewHolder();
            findView(holder, convertView);
            convertView.setTag(R.id.TAG_VIEWHOLDER, holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
        setData(holder, position);
        return convertView;
    }

    private class ViewHolder {
        ImageView type_img;
        TextView type_name;//交易类型
        TextView type_time;//交易时间
        TextView type_number;//交易积分
    }

    private void findView(ViewHolder holder, View view) {
        holder.type_img = (ImageView) view.findViewById(R.id.type_img);
        holder.type_name = (TextView) view.findViewById(R.id.type_name);
        holder.type_time = (TextView) view.findViewById(R.id.type_time);
        holder.type_number = (TextView) view.findViewById(R.id.type_number);
    }

    private void setData(ViewHolder holder, int position) {
        Reply reply = replies.get(position);
        String keytype = reply.getKeytype();
        //购物
        if ("2".equals(keytype)) {
            holder.type_img.setImageResource(R.mipmap.card_shop);
            holder.type_name.setText("购物");
            holder.type_number.setTextColor(mContext.getResources().getColor(R.color.subtract_spect));
            holder.type_number.setText(reply.getFee());
        } else if ("3".equals(keytype)) {
            //购买平台积分
            holder.type_img.setImageResource(R.mipmap.card_shop);
            holder.type_name.setText("购买积分卡");
                holder.type_number.setText(reply.getFee());
                holder.type_number.setTextColor(mContext.getResources().getColor(R.color.subtract_spect));

        } else if ("5".equals(keytype)) {
            //兑换成兑换金积分
            holder.type_img.setImageResource(R.mipmap.conversion_img);
            holder.type_name.setText("购买平台金积分");
                holder.type_number.setText(reply.getFee());
                holder.type_number.setTextColor(mContext.getResources().getColor(R.color.subtract_spect));
        } else if ("6".equals(keytype)) {
            //合成卡
            holder.type_img.setImageResource(R.mipmap.card_compound);
            holder.type_name.setText("合成卡片");
            holder.type_number.setText(reply.getFee());
            holder.type_number.setTextColor(mContext.getResources().getColor(R.color.subtract_spect));
        } else if ("4".equals(keytype)) {
            //购买用户积分
            holder.type_img.setImageResource(R.mipmap.card_shop);
            holder.type_name.setText("购买用户的积分卡");
                holder.type_number.setText( reply.getFee());
                holder.type_number.setTextColor(mContext.getResources().getColor(R.color.subtract_spect));
        } else if ("7".equals(keytype)) {
            //推荐直奖;
            holder.type_img.setImageResource(R.mipmap.card_shop);
            holder.type_name.setText("购买用户金积分");
            holder.type_number.setTextColor(mContext.getResources().getColor(R.color.subtract_spect));
            holder.type_number.setText( reply.getFee());
        } else if ("22".equals(keytype)) {
            //22拆卡返还
            holder.type_img.setImageResource(R.mipmap.card_split);
            holder.type_name.setText("卡片拆分");
            holder.type_number.setText( reply.getFee());
            holder.type_number.setTextColor(mContext.getResources().getColor(R.color.title_backgroid));
        } else if("21".equals(keytype)){
            //出售合成卡
            holder.type_img.setImageResource(R.mipmap.card_sell);
            holder.type_name.setText("卡片出售");
                holder.type_number.setText(reply.getFee());
                holder.type_number.setTextColor(mContext.getResources().getColor(R.color.title_backgroid));
        } else if("23".equals(keytype)){
            //出售合成卡
            holder.type_img.setImageResource(R.mipmap.card_sell);
            holder.type_name.setText("出售金积分");
            holder.type_number.setText( reply.getFee());
            holder.type_number.setTextColor(mContext.getResources().getColor(R.color.title_backgroid));
        }

        holder.type_time.setText(reply.getRegdate());
    }
}
