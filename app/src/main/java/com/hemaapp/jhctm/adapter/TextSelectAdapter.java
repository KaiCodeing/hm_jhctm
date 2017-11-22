package com.hemaapp.jhctm.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.activity.YiWuDuiActivity;
import com.hemaapp.jhctm.model.DistrictInfor;

import java.util.ArrayList;

/**
 * Created by lenovo on 2017/1/23.
 * 选择分类
 */
public class TextSelectAdapter extends HemaAdapter {
    private ArrayList<DistrictInfor> infors;
    private YiWuDuiActivity activity;

    public TextSelectAdapter(YiWuDuiActivity activity, ArrayList<DistrictInfor> infors) {
        super(activity);
        this.activity = activity;
        this.infors = infors;
    }
    public void setInfors(ArrayList<DistrictInfor> infors) {
        this.infors = infors;
    }
    @Override
    public boolean isEmpty() {
        return infors == null || infors.size() == 0;
    }
    @Override
    public int getCount() {
        return isEmpty() ? 0 : infors.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.text_left_type, null);
            ViewHolder holder1 = new ViewHolder();
            findView(holder1, convertView);
            convertView.setTag(R.id.TAG, holder1);

        }
        ViewHolder holder1 = (ViewHolder) convertView.getTag(R.id.TAG);
        setData(holder1, position);
        return convertView;
    }

    private class ViewHolder {
        TextView text_type;
    }

    private void findView(ViewHolder holder, View view) {
        holder.text_type = (TextView) view.findViewById(R.id.text_type);
    }

    private void setData(ViewHolder holder, int position) {
        DistrictInfor infor = infors.get(position);
        if (infor.isCheck()) {
            holder.text_type.setBackgroundResource(R.color.white);
            holder.text_type.setTextColor(activity.getResources().getColor(R.color.title_backgroid));
        } else {
            holder.text_type.setBackgroundResource(R.color.text_select_bank);
            holder.text_type.setTextColor(activity.getResources().getColor(R.color.color_text));
        }
        holder.text_type.setText(infor.getName());
        holder.text_type.setTag(R.id.TAG, infor);
        holder.text_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DistrictInfor infor1 = (DistrictInfor) v.getTag(R.id.TAG);
                activity.selectData(infor1);
            }
        });
    }
}
