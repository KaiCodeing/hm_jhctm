package com.hemaapp.jhctm.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.activity.TypeSelectPopActivity;
import com.hemaapp.jhctm.model.DistrictInfor;

import java.util.ArrayList;

/**
 * Created by lenovo on 2017/2/7.
 */
public class TypeGrid2Adapter extends HemaAdapter {
    private ArrayList<DistrictInfor> districtInfors;
    private String type;
    private TypeSelectPopActivity mContext;

    public TypeGrid2Adapter(TypeSelectPopActivity mContext, ArrayList<DistrictInfor> districtInfors, String type) {
        super(mContext);
        this.districtInfors = districtInfors;
        this.type = type;
        this.mContext = mContext;
    }

    public void setDistrictInfors(ArrayList<DistrictInfor> districtInfors) {
        this.districtInfors = districtInfors;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean isEmpty() {
        return districtInfors == null || districtInfors.size() == 0;
    }

    @Override
    public int getCount() {
        return isEmpty() ? 0 : ("0".equals(type) ? (districtInfors.size() <= 9 ? districtInfors.size() : 9) : (districtInfors.size()));
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_type_view, null);
            ViewHolder holder = new ViewHolder();
            findView(holder, convertView);
            convertView.setTag(R.id.TAG_VIEWHOLDER, holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
        setData(holder, position);
        return convertView;
    }

    private class ViewHolder {
        TextView text_select;
    }

    private void findView(ViewHolder holder, View view) {
        holder.text_select = (TextView) view.findViewById(R.id.text_select);
    }

    private void setData(ViewHolder holder, int position) {
        final DistrictInfor districtInfor = districtInfors.get(position);
        holder.text_select.setText(districtInfor.getName());
        holder.text_select.setTag(R.id.TAG,districtInfor);
        if (districtInfor.isCheck()) {
            holder.text_select.setTextColor(mContext.getResources().getColor(R.color.white));
            holder.text_select.setBackgroundResource(R.color.title_backgroid);
        } else {
            holder.text_select.setTextColor(mContext.getResources().getColor(R.color.tuikuan));
            holder.text_select.setBackgroundResource(R.color.guige_b);
        }
        holder.text_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DistrictInfor infor = (DistrictInfor) v.getTag(R.id.TAG);
                for (DistrictInfor districtInfor1 : districtInfors) {
                    if (infor == districtInfor1) {
                        districtInfor1.setCheck(true);
                    } else {
                        districtInfor1.setCheck(false);
                    }
                }
                notifyDataSetChanged();
            }
        });
    }
}
