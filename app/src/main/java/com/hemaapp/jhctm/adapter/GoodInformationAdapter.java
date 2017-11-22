package com.hemaapp.jhctm.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.hm_FrameWork.view.RoundedImageView;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.model.Reply;
import com.hemaapp.jhctm.view.JhctmGridView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by lenovo on 2016/12/26.
 * 商品详情的adapter
 */
public class GoodInformationAdapter extends HemaAdapter {
    private Context mContext;
    private ArrayList<Reply> replies;
    public GoodInformationAdapter(Context mContext,ArrayList<Reply> replies) {
        super(mContext);
        this.mContext = mContext;
        this.replies = replies;
    }

    public void setReplies(ArrayList<Reply> replies) {
        this.replies = replies;
    }

    @Override
    public boolean isEmpty() {
        return replies==null || replies.size()==0;
    }

    @Override
    public int getCount() {
        return isEmpty()?0:replies.size();
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
        if (convertView==null)
        {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_good_repley,null);
            ViewHolder1 holder1 = new ViewHolder1();
            findView1(holder1,convertView);
            convertView.setTag(R.id.TAG,holder1);

        }
        ViewHolder1 holder1 = (ViewHolder1) convertView.getTag(R.id.TAG);
        setData1(holder1,position);
        return convertView;
    }
    /**
     * 评论
     */
    private class ViewHolder1{
        RoundedImageView user_img;
        TextView username_text;
        TextView content_time_text;
        TextView content_word_text;
        JhctmGridView pl_gridview;
        LinearLayout add_star_view;
    }
    private void findView1(ViewHolder1 holder1,View view)
    {
        holder1.user_img = (RoundedImageView) view.findViewById(R.id.user_img);
        holder1.username_text = (TextView) view.findViewById(R.id.username_text);
        holder1.content_time_text = (TextView) view.findViewById(R.id.content_time_text);
        holder1.content_word_text = (TextView) view.findViewById(R.id.content_word_text);
        holder1.pl_gridview = (JhctmGridView) view.findViewById(R.id.pl_gridview);
        holder1.add_star_view = (LinearLayout) view.findViewById(R.id.add_star_view);
    }
    private void setData1(ViewHolder1 holder1,int position)
    {
        Reply reply = replies.get(position);
        String path = reply.getAvatar();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.defult_gridview_img)
                .showImageForEmptyUri(R.mipmap.defult_gridview_img)
                .showImageOnFail(R.mipmap.defult_gridview_img).cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoader.getInstance().displayImage(path, holder1.user_img, options);
        holder1.username_text.setText(reply.getNickname());
        holder1.content_time_text.setText(reply.getRegdate());
        holder1.content_word_text.setText(reply.getContent());
        int starNumber = Integer.valueOf(reply.getStars());
        holder1.add_star_view.removeAllViews();
        for (int i = 0; i < 5; i++) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.star_add_view, null);
            ImageView star = (ImageView) view.findViewById(R.id.star_img_off);
            ImageView star_on = (ImageView) view.findViewById(R.id.star_img_on);
            if (i < starNumber)
                star_on.setVisibility(View.GONE);
            else
                star.setVisibility(View.GONE);
            holder1.add_star_view.addView(view);
        }
        //照片
        if (reply.getImgItems() == null || reply.getImgItems().size() == 0)
            holder1.pl_gridview.setVisibility(View.GONE);
        else {
            holder1.pl_gridview.setVisibility(View.VISIBLE);
            int a = reply.getImgItems().size();
            BlogReplyImgAdapter imgAdapter = new BlogReplyImgAdapter(mContext,
                    reply.getImgItems());
            holder1.pl_gridview.setAdapter(imgAdapter);
        }
    }


}
