package com.hemaapp.jhctm.adapter;

import android.content.Context;
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
import com.hemaapp.jhctm.model.Blog1List;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by lenovo on 2016/12/21.
 * 首页的gridview的adapter
 */
public class HomeGridAdapter extends HemaAdapter {
    private ArrayList<Blog1List> blog1Lists ;
    private Context mContext;
    public HomeGridAdapter(Context mContext,ArrayList<Blog1List> blog1Lists) {
        super(mContext);
        this.blog1Lists = blog1Lists;
        this.mContext = mContext;
    }

    @Override
    public boolean isEmpty() {
        return blog1Lists==null || blog1Lists.size()==0;
    }

    @Override
    public int getCount() {
        return isEmpty()?1:blog1Lists.size();
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
        if (convertView==null)
        {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_home_gridview,null);
            ViewHolder holder = new ViewHolder();
            findView(holder,convertView);
            convertView.setTag(R.id.TAG_VIEWHOLDER,holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
        setData(holder,position);
        return convertView;
    }
    private class ViewHolder{
        ImageView commind_img;
        TextView commod_name;//商品名称
        TextView num_credit;//积分
        TextView commod_content;//商品简介
        TextView add_bz;
        TextView fenxiang_text;
        TextView pinpai_name;
        LinearLayout layout_item;
        TextView jf_or_money;
    }
    private void findView(ViewHolder holder,View view)
    {
        holder.commind_img = (ImageView) view.findViewById(R.id.commind_img);
        holder.commod_name = (TextView) view.findViewById(R.id.commod_name);
        holder.num_credit = (TextView) view.findViewById(R.id.num_credit);
        holder.commod_content = (TextView) view.findViewById(R.id.commod_content);
        holder.add_bz = (TextView) view.findViewById(R.id.add_bz);
        holder.fenxiang_text = (TextView) view.findViewById(R.id.fenxiang_text);
        holder.pinpai_name = (TextView) view.findViewById(R.id.pinpai_name);
        holder.layout_item = (LinearLayout) view.findViewById(R.id.layout_item);
        holder.jf_or_money = (TextView) view.findViewById(R.id.jf_or_money);
    }
    private void setData(ViewHolder holder,int position)
    {
        Blog1List blog1List = blog1Lists.get(position);
        //商品图片
        String path = blog1List.getImgurl();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.defult_gridview_img)
                .showImageForEmptyUri(R.mipmap.defult_gridview_img)
                .showImageOnFail(R.mipmap.defult_gridview_img).cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoader.getInstance().displayImage(path, holder.commind_img, options);
        //积分
        if (!isNull(blog1List.getScore()))
            holder.num_credit.setText(blog1List.getScore());
        //名称
        if (!isNull(blog1List.getName()))
            holder.commod_name.setText(blog1List.getName());
        //判断是否显示分享积分
        //分享
        holder.jf_or_money.setText("积分");
        if ("3".equals(blog1List.getKeytype()))
        {
            holder.add_bz.setVisibility(View.VISIBLE);
            holder.fenxiang_text.setVisibility(View.VISIBLE);
            holder.fenxiang_text.setText(blog1List.getShare_score()+"分享币");
            holder.num_credit.setText("¥"+blog1List.getScore());
            holder.jf_or_money.setText("元");
        }
        else
        {
            holder.add_bz.setVisibility(View.GONE);
            holder.fenxiang_text.setVisibility(View.GONE);
        }

        //分享

        //判断是否是品牌商
        //平台自营
        if ("1".equals(blog1List.getClient_id()))
            holder.pinpai_name.setVisibility(View.GONE);
        else
            holder.pinpai_name.setVisibility(View.VISIBLE);
        //销量 好评率
        holder.commod_content.setText("已售"+blog1List.getSalecount()+"  好评率"+blog1List.getGood_score()+"%");
        //点击查看详情
        holder.layout_item.setTag(R.id.TAG,blog1List);
        holder.layout_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Blog1List blog1List1 = (Blog1List) v.getTag(R.id.TAG);
                Intent intent = new Intent(mContext, GoodsInformationActivity.class);
                intent.putExtra("goodId",blog1List1.getId());
                mContext.startActivity(intent);
            }
        });
    }
}
