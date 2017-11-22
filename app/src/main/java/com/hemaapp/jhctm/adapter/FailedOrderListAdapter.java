package com.hemaapp.jhctm.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.jhctm.JhActivity;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.activity.FailedOrderInforActivity;
import com.hemaapp.jhctm.model.FailedOrderListInfor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import xtom.frame.image.load.XtomImageTask;

/**
 * Created by WangYuxia on 2017/2/10.
 * 售后列表的数据适配器
 */
public class FailedOrderListAdapter extends HemaAdapter {

    private ArrayList<FailedOrderListInfor> infors;
    private String type;

    public FailedOrderListAdapter(Context mContext, ArrayList<FailedOrderListInfor> infors,
                                  String keytype) {
        super(mContext);
        this.infors = infors;
        this.type = keytype;
    }

    @Override
    public boolean isEmpty() {
        if(infors == null || infors.size() == 0)
            return true;
        return false;
    }

    @Override
    public int getCount() {
        return infors == null || infors.size() == 0 ? 1 : infors.size();
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
        if(isEmpty())
            return getEmptyView(parent);
        ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_failedorder, null);
            holder = new ViewHolder();
            findview(holder, convertView);
            convertView.setTag(R.id.button, holder);
        }else{
            holder = (ViewHolder) convertView.getTag(R.id.button);
        }

        FailedOrderListInfor infor = infors.get(position);
        setdata(holder, convertView, infor);
        return convertView;
    }

    private void setdata(ViewHolder holder, View view, FailedOrderListInfor infor){
        holder.text_no.setText(infor.getBill_sn());
        String keytype = infor.getItemtype();
        if("1".equals(keytype))
            holder.text_status.setText("审核中");
        else if("2".equals(keytype))
            holder.text_status.setText("审核成功");
        else if("3".equals(keytype))
            holder.text_status.setText("审核失败");

        try {
            URL url = new URL(infor.getImgurl());
            ((JhActivity)mContext).imageWorker.loadImage(new XtomImageTask(holder.image_avatar, url, mContext));
        } catch (MalformedURLException e) {
            holder.image_avatar.setImageResource(R.mipmap.hotel_defult_img);
        }
        holder.text_goodname.setText(infor.getName());
        holder.text_attri.setText("规格:"+infor.getSpec_name()+";数量"+infor.getBuycount());
        String orderType = infor.getKeytype();
        if("1".equals(orderType)){
            holder.text_left.setVisibility(View.GONE);
            holder.text_middle.setText(infor.getScore());
            holder.text_right.setVisibility(View.VISIBLE);
            holder.text_right.setText("消费积分");
        }else if("2".equals(orderType)){
            holder.text_left.setVisibility(View.VISIBLE);
            holder.text_left.setText("金积分");
            holder.text_middle.setText(infor.getGold_score());
            holder.text_right.setVisibility(View.GONE);
        }else if("3".equals(orderType)){
            holder.text_left.setVisibility(View.GONE);
            holder.text_middle.setText(infor.getScore());
            holder.text_right.setVisibility(View.VISIBLE);
            holder.text_right.setText("积分+分享币"+infor.getShare_score());
        }

        view.setTag(infor);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FailedOrderListInfor infor = (FailedOrderListInfor) v.getTag();
                Intent it = new Intent(mContext, FailedOrderInforActivity.class);
                it.putExtra("id", infor.getBill_id());
                it.putExtra("keytype", type);
                mContext.startActivity(it);
            }
        });
    }

    private void findview(ViewHolder holder, View view){
        holder.text_no = (TextView) view.findViewById(R.id.textview_0);
        holder.text_status = (TextView) view.findViewById(R.id.textview_1);
        holder.image_avatar = (ImageView) view.findViewById(R.id.imageview);
        holder.text_goodname = (TextView) view.findViewById(R.id.textview_2);
        holder.text_attri = (TextView) view.findViewById(R.id.textview_3);
        holder.text_left = (TextView) view.findViewById(R.id.textview_4);
        holder.text_middle = (TextView) view.findViewById(R.id.textview_5);
        holder.text_right = (TextView) view.findViewById(R.id.textview_6);
    }

    private class ViewHolder{
        TextView text_no;
        TextView text_status;
        ImageView image_avatar;
        TextView text_goodname;
        TextView text_attri;
        TextView text_left; //小字
        TextView text_middle; //大字
        TextView text_right; //小字
    }
}
