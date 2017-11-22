package com.hemaapp.jhctm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.model.ScoreTime;

import java.util.ArrayList;

/**
 * Created by lenovo on 2016/12/22.
 * 消息的adapter
 */
public class ScoreTimeAdapter extends HemaAdapter {
    private ArrayList<ScoreTime> infos;
    private ScoreTime time;
    private String kty;

    public ScoreTimeAdapter(Context mContext, ArrayList<ScoreTime> infos, String kty) {
        super(mContext);
        this.infos = infos;
        this.kty = kty;
    }

    @Override
    public boolean isEmpty() {
        return infos == null || infos.size() == 0;
    }

    @Override
    public int getCount() {
        return isEmpty() ? 1 : infos.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.score_time_item, null);
            ViewHolder holder = new ViewHolder();
            findView(holder, convertView);
            convertView.setTag(R.id.TAG_VIEWHOLDER, holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
        setData(holder, position);
        return convertView;
    }

    private class ViewHolder {
        TextView user_name;
        TextView time_text;
        TextView type_show;
    }

    private void findView(ViewHolder holder, View view) {
        holder.user_name = (TextView) view.findViewById(R.id.user_name);
        holder.time_text = (TextView) view.findViewById(R.id.time_text);
        holder.type_show = (TextView) view.findViewById(R.id.type_show);
    }

    private void setData(ViewHolder holder, int position) {
        time = infos.get(position);
        String keytype = time.getKeytype();
        if (isNull(kty)) {
            holder.time_text.setText("返赠时间: " + time.getRegdate1());
            holder.user_name.setText(time.getLeftscore());
            holder.type_show.setVisibility(View.VISIBLE);

            //消费
            if ("2".equals(keytype)) {
                holder.type_show.setText("消费");
                holder.type_show.setTextColor(mContext.getResources().getColor(R.color.lan_xiaofei));
                holder.type_show.setBackgroundResource(R.drawable.xiaofei_yuan_);
            } else //广告
            {
                holder.type_show.setText("广告");
                holder.type_show.setTextColor(mContext.getResources().getColor(R.color.title_backgroid));
                holder.type_show.setBackgroundResource(R.drawable.guanggao_yuan_);
            }
        } else {
            holder.time_text.setText("返赠时间: " + time.getRegdate());
            //判断
            if ("1".equals(keytype)) {
                holder.user_name.setText("购物" + time.getScore());
            } else if ("2".equals(keytype)) {
                holder.user_name.setText("购买平台积分" + time.getScore());
            } else if ("3".equals(keytype)) {
                holder.user_name.setText("兑换成兑换金积分" + time.getScore());
            } else if ("4".equals(keytype)) {
                holder.user_name.setText("合成卡" + time.getScore());
            } else if ("5".equals(keytype)) {
                holder.user_name.setText("购物返还" + time.getScore() + "金积分");
            } else if ("6".equals(keytype)) {
                holder.user_name.setText("购买用户积分" + time.getScore());
            } else if ("7".equals(keytype)) {
                holder.user_name.setText("积分返还" + time.getScore());
            } else if ("8".equals(keytype)) {
                holder.user_name.setText("转让" + time.getScore());
            } else if ("9".equals(keytype)) {
                holder.user_name.setText("转让退回" + time.getScore());
            } else if ("10".equals(keytype)) {
                holder.user_name.setText("购买用户的金积分" + time.getScore());
            } else if ("11".equals(keytype)) {
                holder.user_name.setText("卖商品赚取" + time.getScore());
            } else if ("12".equals(keytype)) {
                holder.user_name.setText("消耗分享币返还" + time.getScore());
            } else if ("21".equals(keytype)) {
                holder.user_name.setText("推荐直奖" + time.getScore());
            } else if ("22".equals(keytype)) {
                holder.user_name.setText("拆卡返还" + time.getScore());
            } else if ("23".equals(keytype)) {
                holder.user_name.setText("卖商品赚取" + time.getScore());
            }
        }

    }
}
