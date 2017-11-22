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
 * Created by lenovo on 2017/2/6.
 */
public class ShareIntegralAdapter extends HemaAdapter {
    private ArrayList<Reply> replies;
    private Context mContext;

    public ShareIntegralAdapter(Context mContext, ArrayList<Reply> replies) {
        super(mContext);
        this.replies = replies;
        this.mContext = mContext;
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
        //消费
        if ("1".equals(keytype)) {
            holder.type_img.setImageResource(R.mipmap.card_shop);
            holder.type_name.setText("消费");
            holder.type_number.setTextColor(mContext.getResources().getColor(R.color.subtract_spect));
            holder.type_number.setText( reply.getScore()+"积分");
        } else if ("2".equals(keytype)) {
            //线下购买平台积分卡返还
            holder.type_img.setImageResource(R.mipmap.jf_tg_img);
            holder.type_name.setText("分享人购卡返积分");
            holder.type_number.setText( reply.getScore()+"积分");
            holder.type_number.setTextColor(mContext.getResources().getColor(R.color.title_backgroid));

        } else if ("3".equals(keytype)) {
            //合成卡返还
            holder.type_img.setImageResource(R.mipmap.jf_tg_img);
            holder.type_name.setText("合成卡返还积分");
            holder.type_number.setText( reply.getScore()+"积分");
            holder.type_number.setTextColor(mContext.getResources().getColor(R.color.title_backgroid));
        } else if ("4".equals(keytype)) {
            //4兑换成兑换金积分
            holder.type_img.setImageResource(R.mipmap.duiwu_img);
            holder.type_name.setText("兑换成兑换券金积分");
            holder.type_number.setText(reply.getScore()+"积分");
            holder.type_number.setTextColor(mContext.getResources().getColor(R.color.subtract_spect));
        } else if ("5".equals(keytype)) {
            //卖商品赚取
            holder.type_img.setImageResource(R.mipmap.jf_tg_img);
            holder.type_name.setText("出售卡片返积分");
            holder.type_number.setText(reply.getScore()+"积分");
            holder.type_number.setTextColor(mContext.getResources().getColor(R.color.title_backgroid));
        } else if ("6".equals(keytype)) {
            //6注册即送
            holder.type_img.setImageResource(R.mipmap.jf_tg_img);
            holder.type_name.setText("注册即送");
            holder.type_number.setTextColor(mContext.getResources().getColor(R.color.title_backgroid));
            holder.type_number.setText(reply.getScore()+"积分");
        }
        else if ("-1".equals(keytype))
        {
            holder.type_img.setImageResource(R.mipmap.jf_tg_img);
            holder.type_name.setText("平台操作");
            holder.type_number.setText(reply.getScore()+"积分");
            holder.type_number.setTextColor(mContext.getResources().getColor(R.color.subtract_spect));
        }
        else if("7".equals(keytype))
        {
            holder.type_img.setImageResource(R.mipmap.jf_tg_img);
            holder.type_name.setText("直推奖励");
            holder.type_number.setTextColor(mContext.getResources().getColor(R.color.title_backgroid));
            holder.type_number.setText(reply.getScore()+"积分");
        }
        holder.type_time.setText(reply.getRegdate());
    }
}
