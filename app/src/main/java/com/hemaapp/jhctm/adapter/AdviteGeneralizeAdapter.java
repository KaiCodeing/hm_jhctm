package com.hemaapp.jhctm.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.activity.AdviteGeneralizeActivity;
import com.hemaapp.jhctm.model.AdList;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by lenovo on 2017/4/10.
 * 广告推广的adapter  都是广告页
 */
public class AdviteGeneralizeAdapter extends HemaAdapter {
    private ArrayList<AdList> adLists;

    public AdviteGeneralizeAdapter(Context mContext, ArrayList<AdList> adLists) {
        super(mContext);
        this.adLists = adLists;
    }

    public void setAdLists(ArrayList<AdList> adLists) {
        this.adLists = adLists;
    }

    @Override
    public boolean isEmpty() {
        return adLists == null || adLists.size() == 0;
    }

    @Override
    public int getCount() {
        return isEmpty() ? 1 : adLists.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_advite_generalize, null);
            ViewHolder holder = new ViewHolder();
            findView(holder, convertView);
            convertView.setTag(R.id.TAG_VIEWHOLDER, holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
        setData(holder, position);
        return convertView;
    }

    private class ViewHolder {
        ImageView image_show;
        FrameLayout layout_show;
    }

    private void findView(ViewHolder holder, View view) {
        holder.image_show = (ImageView) view.findViewById(R.id.image_show);
        holder.layout_show = (FrameLayout) view.findViewById(R.id.layout_show);
    }

    private void setData(ViewHolder holder, int postion) {
        final AdList adList = adLists.get(postion);
        //图片
        String path = adList.getImgurlbig();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.defult_gridview_img)
                .showImageForEmptyUri(R.mipmap.defult_gridview_img)
                .showImageOnFail(R.mipmap.defult_gridview_img).cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoader.getInstance().displayImage(path, holder.image_show, options);
        //判断是否显示过
        if ("1".equals(adList.getShareflag())) {
            holder.layout_show.setVisibility(View.VISIBLE);
        } else
            holder.layout_show.setVisibility(View.GONE);
        holder.image_show.setTag(R.id.TAG, adList);
        holder.image_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdList adList1 = (AdList) v.getTag(R.id.TAG);
                if ("1".equals(adList1.getShareflag())) {
                } else {
                    AdviteGeneralizeActivity adviteGeneralizeActivity = (AdviteGeneralizeActivity) mContext;
                    ((AdviteGeneralizeActivity) mContext).showShareView(adList1.getId(),adList.getKeyid());
                }
            }
        });

    }
}
