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
import com.hemaapp.jhctm.activity.MyCollectionActivity;
import com.hemaapp.jhctm.model.Blog1List;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by lenovo on 2017/1/4.
 * 我的收藏
 */
public class MyCollectionAdapter extends HemaAdapter {
    private MyCollectionActivity activity;
    private ArrayList<Blog1List> blog1Lists;
    private String type;

    public MyCollectionAdapter(MyCollectionActivity activity, ArrayList<Blog1List> blog1Lists, String type) {
        super(activity);
        this.activity = activity;
        this.blog1Lists = blog1Lists;
        this.type = type;
    }

    public void setBlog1Lists(ArrayList<Blog1List> blog1Lists) {
        this.blog1Lists = blog1Lists;
    }

    public void setType(String type) {
        this.type = type;
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
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_home_gridview, null);
            ViewHolder holder = new ViewHolder();
            findView(holder, convertView);
            convertView.setTag(R.id.TAG_VIEWHOLDER, holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
        setData(holder, position);
        return convertView;
    }

    //item_home_gridview
    private class ViewHolder {
        ImageView check_in;//是否选中
        ImageView commind_img;//商品图片
        TextView commod_name;//商品名称
        TextView num_credit;//积分数
        TextView add_bz;//加号
        TextView fenxiang_text;//分享积分
        TextView pinpai_name;//品牌商标志
        TextView commod_content;//已售和好评率
        LinearLayout layout_item;//整个item
    }

    private void findView(ViewHolder holder, View view) {
        holder.check_in = (ImageView) view.findViewById(R.id.check_in);
        holder.commind_img = (ImageView) view.findViewById(R.id.commind_img);
        holder.commod_name = (TextView) view.findViewById(R.id.commod_name);
        holder.num_credit = (TextView) view.findViewById(R.id.num_credit);
        holder.add_bz = (TextView) view.findViewById(R.id.add_bz);
        holder.fenxiang_text = (TextView) view.findViewById(R.id.fenxiang_text);
        holder.pinpai_name = (TextView) view.findViewById(R.id.pinpai_name);
        holder.commod_content = (TextView) view.findViewById(R.id.commod_content);
        holder.layout_item = (LinearLayout) view.findViewById(R.id.layout_item);

    }

    private void setData(ViewHolder holder, int position) {
        final Blog1List blog1List = blog1Lists.get(position);
        //商品图片
        String path = blog1List.getImgurl();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.defult_gridview_img)
                .showImageForEmptyUri(R.mipmap.defult_gridview_img)
                .showImageOnFail(R.mipmap.defult_gridview_img).cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoader.getInstance().displayImage(path, holder.commind_img, options);
        //名称
        if (!isNull(blog1List.getName()))
            holder.commod_name.setText(blog1List.getName());
        //判断是否显示分享积分
        //分享
        if ("3".equals(blog1List.getKeytype())) {
            holder.add_bz.setVisibility(View.VISIBLE);
            holder.fenxiang_text.setVisibility(View.VISIBLE);
        } else {
            holder.add_bz.setVisibility(View.GONE);
            holder.fenxiang_text.setVisibility(View.GONE);
        }
        //积分
        if (!isNull(blog1List.getScore()))
            holder.num_credit.setText(blog1List.getScore());
        //分享

        //判断是否是品牌商
        //平台自营
        if ("1".equals(blog1List.getClient_id()))
            holder.pinpai_name.setVisibility(View.GONE);
        else
            holder.pinpai_name.setVisibility(View.VISIBLE);
        //销量 好评率
        holder.commod_content.setText("已售" + blog1List.getSalecount() + "  好评率" + blog1List.getGood_score());
        //点击查看详情
        holder.layout_item.setTag(R.id.TAG, blog1List);
        holder.layout_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Blog1List blog1List1 = (Blog1List) v.getTag(R.id.TAG);
                if ("0".equals(type)) {
                    Intent intent = new Intent(mContext, GoodsInformationActivity.class);
                    intent.putExtra("goodId", blog1List1.getId());
                    mContext.startActivity(intent);
                }
                else
                {
                    if (blog1List1.isCheck())
                        blog1List1.setCheck(false);
                    else
                        blog1List1.setCheck(true);
                    activity.allDelte();
                }
            }
        });
        if ("0".equals(type)) {
            holder.check_in.setVisibility(View.GONE);
            holder.check_in.setBackgroundResource(R.mipmap.fapiao_check_off);
        } else {
            holder.check_in.setVisibility(View.VISIBLE);
            if (blog1List.isCheck())
                holder.check_in.setBackgroundResource(R.mipmap.fapiao_check_on);
            else
                holder.check_in.setBackgroundResource(R.mipmap.fapiao_check_off);
        }
        //删除操作
        holder.check_in.setTag(R.id.TAG, blog1List);
        holder.check_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Blog1List blog1List1 = (Blog1List) v.getTag(R.id.TAG);
                if (blog1List1.isCheck())
                    blog1List1.setCheck(false);
                else
                    blog1List1.setCheck(true);
                activity.allDelte();
            }
        });
    }
}
