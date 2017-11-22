package com.hemaapp.jhctm.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.activity.GoodsInformationActivity;
import com.hemaapp.jhctm.activity.LimitExchangeActivity;
import com.hemaapp.jhctm.model.Blog1List;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import xtom.frame.util.XtomTimeUtil;

/**
 * Created by lenovo on 2016/12/27.
 * 限时兑换的adapter
 */
public class LimitExchangeAdapter extends HemaAdapter {
    private LimitExchangeActivity activity;
    private ArrayList<Blog1List> blog1Lists;
    private String keytype;

    public LimitExchangeAdapter(LimitExchangeActivity activity, ArrayList<Blog1List> blog1Lists, String keytype) {
        super(activity);
        this.activity = activity;
        this.blog1Lists = blog1Lists;
        this.keytype = keytype;
    }

    public void setBlog1Lists(ArrayList<Blog1List> blog1Lists) {
        this.blog1Lists = blog1Lists;
    }

    public void setKeytype(String keytype) {
        this.keytype = keytype;
    }

    @Override
    public boolean isEmpty() {
        return blog1Lists == null || blog1Lists.size() == 0;
    }

    @Override
    public int getCount() {
        return isEmpty() ? 1 : blog1Lists.size();
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
            convertView = LayoutInflater.from(activity).inflate(R.layout.adapter_limit_exchange, null);
            ViewHolder holder = new ViewHolder();
            findView(holder, convertView);
            convertView.setTag(R.id.TAG, holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag(R.id.TAG);
        setData(holder, position);
        return convertView;
    }

    /**
     * layout adapter_limit_exchange
     */
    private class ViewHolder {
        LinearLayout time_layout;//时间的layout
        TextView time_h;
        TextView time_m;
        TextView time_s;
        LinearLayout item_hoetl;
        ImageView commod_img;
        TextView hotel_name;//名称商品
        TextView jifen_money;//积分
        TextView jffh_text;//积分不返还
        LinearLayout pay_layout;
        TextView pay_or_out;
        View bottom_line;
    }

    private void findView(ViewHolder holder, View view) {
        holder.time_layout = (LinearLayout) view.findViewById(R.id.time_layout);
        holder.time_h = (TextView) view.findViewById(R.id.time_h);
        holder.time_m = (TextView) view.findViewById(R.id.time_m);
        holder.time_s = (TextView) view.findViewById(R.id.time_s);
        holder.item_hoetl = (LinearLayout) view.findViewById(R.id.item_hoetl);
        holder.commod_img = (ImageView) view.findViewById(R.id.commod_img);
        holder.hotel_name = (TextView) view.findViewById(R.id.hotel_name);
        holder.jifen_money = (TextView) view.findViewById(R.id.jifen_money);
        holder.jffh_text = (TextView) view.findViewById(R.id.jffh_text);
        holder.pay_layout = (LinearLayout) view.findViewById(R.id.pay_layout);
        holder.pay_or_out = (TextView) view.findViewById(R.id.pay_or_out);
        holder.bottom_line = view.findViewById(R.id.bottom_line);
    }

    private void setData(ViewHolder holder, int position) {
        //判断是第几个 第一个没有时间
        if ("1".equals(keytype)) {
            holder.time_layout.setVisibility(View.GONE);
            holder.pay_layout.setVisibility(View.GONE);
            holder.bottom_line.setVisibility(View.GONE);
        } else {
            if (position == 0)
                holder.time_layout.setVisibility(View.VISIBLE);
            else
                holder.time_layout.setVisibility(View.GONE);
            if ("2".equals(keytype)) {
                holder.pay_layout.setVisibility(View.VISIBLE);
                holder.bottom_line.setVisibility(View.VISIBLE);
            } else {
                holder.pay_layout.setVisibility(View.GONE);
                holder.bottom_line.setVisibility(View.GONE);
            }
        }
        Blog1List blog1List = blog1Lists.get(position);
        //商品图片
        String path = blog1List.getImgurl();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.defult_gridview_img)
                .showImageForEmptyUri(R.mipmap.defult_gridview_img)
                .showImageOnFail(R.mipmap.defult_gridview_img).cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoader.getInstance().displayImage(path, holder.commod_img, options);
        holder.hotel_name.setText(blog1List.getName());
        holder.jifen_money.setText(blog1List.getGold_score());
        //不返现
        if (isNull(blog1List.getRebatflag()) || "0".equals(blog1List.getRebatflag()))
            holder.jffh_text.setVisibility(View.GONE);
        else
            holder.jffh_text.setVisibility(View.VISIBLE);
        //获取时间
        String h = XtomTimeUtil.TransTime(blog1List.getStarttime(),
                "HH");
        h = blog1List.getStarttime().substring(0, 2);
        String m = XtomTimeUtil.TransTime(blog1List.getStarttime(),
                "mm");
        m = blog1List.getStarttime().substring(3, 5);
        String s = XtomTimeUtil.TransTime(blog1List.getStarttime(),
                "ss");
        s = blog1List.getStarttime().substring(6, 8);
        holder.time_h.setText(h);
        holder.time_m.setText(m);
        holder.time_s.setText(s);
        //s是否售罄
        if (isNull(blog1List.getLeftcount()) || "0".equals(blog1List.getLeftcount())) {
            holder.pay_or_out.setEnabled(false);
            holder.pay_or_out.setText("已告罄");
            holder.pay_or_out.setBackgroundResource(R.color.tuikuan);
        } else {
            holder.pay_or_out.setEnabled(true);
            holder.pay_or_out.setText("立即兑换");
            holder.pay_or_out.setBackgroundResource(R.color.title_backgroid);
        }
        //立即购买
        holder.pay_or_out.setTag(R.id.TAG, blog1List);
        holder.pay_or_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Blog1List list = (Blog1List) v.getTag(R.id.TAG);
                Intent intent = new Intent(activity, GoodsInformationActivity.class);
                intent.putExtra("goodId", list.getId());
                activity.startActivity(intent);
            }
        });
        //点击查看详情
        holder.item_hoetl.setTag(R.id.TAG, blog1List);
        holder.item_hoetl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("3".equals(keytype))
                    return;
                Blog1List list = (Blog1List) v.getTag(R.id.TAG);
                Intent intent = new Intent(activity, GoodsInformationActivity.class);
                intent.putExtra("goodId", list.getId());
                activity.startActivity(intent);
            }
        });
    }
}
