package com.hemaapp.jhctm.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.activity.AdviteGeneralizeActivity;
import com.hemaapp.jhctm.model.Rebat;

import java.util.ArrayList;

/**
 * Created by lenovo on 2017/4/10.
 * 冻结积分的adapter
 */
public class FeeezeListAdapter extends HemaAdapter {
    private ArrayList<Rebat> rebats;

    public FeeezeListAdapter(Context mContext, ArrayList<Rebat> rebats) {
        super(mContext);
        this.rebats = rebats;
    }

    @Override
    public boolean isEmpty() {
        return rebats == null || rebats.size() == 0;
    }

    public void setRebats(ArrayList<Rebat> rebats) {
        this.rebats = rebats;
    }

    @Override
    public int getCount() {
        return isEmpty() ? 1 : rebats.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_list_freeze, null);
            ViewHolder holder = new ViewHolder();
            findView(holder, convertView);
            convertView.setTag(R.id.TAG_VIEWHOLDER, holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
        setData(holder, position);
        return convertView;
    }

    private class ViewHolder {
        LinearLayout go_to_share;
        TextView freeze_time;
        TextView no_freeze_time;
        TextView number;
        TextView type;
        TextView all_number;
        TextView type_freeze;
        TextView sum;
        ImageView go_to_image;
        LinearLayout number_layout;
        View view1;
    }

    private void findView(ViewHolder holder, View view) {
        holder.go_to_share = (LinearLayout) view.findViewById(R.id.go_to_share);
        holder.freeze_time = (TextView) view.findViewById(R.id.freeze_time);
        holder.no_freeze_time = (TextView) view.findViewById(R.id.no_freeze_time);
        holder.number = (TextView) view.findViewById(R.id.number);
        holder.type = (TextView) view.findViewById(R.id.type);
        holder.all_number = (TextView) view.findViewById(R.id.all_number);
        holder.type_freeze = (TextView) view.findViewById(R.id.type_freeze);
        holder.sum = (TextView) view.findViewById(R.id.sum);
        holder.go_to_image = (ImageView) view.findViewById(R.id.go_to_image);
        holder.number_layout = (LinearLayout) view.findViewById(R.id.number_layout);
        holder.view1 = view.findViewById(R.id.view1);
    }

    /**
     * 填充数据
     *
     * @param holder
     * @param positiion
     */
    private void setData(ViewHolder holder, int positiion) {
        Rebat rebat = rebats.get(positiion);
        //冻结时间
        holder.freeze_time.setText("冻结时间:" + rebat.getRegdate());
        holder.no_freeze_time.setText("解冻时间:" + rebat.getRegdate1());
        holder.number.setText(rebat.getTotalscore());
        //次数
        holder.all_number.setText(String.valueOf(Integer.valueOf(rebat.getTotalcount()) - Integer.valueOf(rebat.getLeftcount())) + "/");
        holder.sum.setText(rebat.getTotalcount());
        //状态判断
        ///带解冻
        if ("0".equals(rebat.getStatus())) {
            holder.type.setText(" 待解冻");
            holder.type_freeze.setText("待解冻");
        } else if ("1".equals(rebat.getStatus())) {
            holder.type.setText(" 解冻中");
            holder.type_freeze.setText("解冻中");
        } else if ("2".equals(rebat.getStatus())) {
            holder.type.setText(" 解冻完成");
            holder.type_freeze.setText("解冻完成");
            holder.view1.setVisibility(View.GONE);
            holder.go_to_image.setVisibility(View.GONE);
            holder.type_freeze.setVisibility(View.GONE);
            holder.number_layout.setVisibility(View.INVISIBLE);
        }
        //跳转分享
        holder.go_to_share.setTag(R.id.TAG_VIEWHOLDER, rebat);
        holder.go_to_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Rebat rebat1 = (Rebat) v.getTag(R.id.TAG_VIEWHOLDER);
                Intent intent = new Intent(mContext, AdviteGeneralizeActivity.class);
                intent.putExtra("rebatid", rebat1.getId());
                if ("0".equals(rebat1.getStatus())) {
                    mContext.startActivity(intent);
                } else if ("1".equals(rebat1.getStatus())) {
                    mContext.startActivity(intent);
                } else if (0 == Integer.valueOf(rebat1.getLeftcount())) {
                }
            }
        });
    }
}
