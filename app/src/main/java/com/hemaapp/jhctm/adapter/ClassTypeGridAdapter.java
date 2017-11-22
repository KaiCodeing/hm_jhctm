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
import com.hemaapp.jhctm.activity.TypeGoodActivity;
import com.hemaapp.jhctm.model.DistrictInfor;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by lenovo on 2016/12/28.
 * 分类中的商品展示
 */
public class ClassTypeGridAdapter extends HemaAdapter {
    private ArrayList<DistrictInfor> infors;
    public ClassTypeGridAdapter(Context mContext,ArrayList<DistrictInfor> infors) {
        super(mContext);
        this.infors = infors;
    }

    public void setInfors(ArrayList<DistrictInfor> infors) {
        this.infors = infors;
    }

    @Override
    public boolean isEmpty() {
        return infors==null || infors.size()==0;
    }

    @Override
    public int getCount() {
        return isEmpty()?1:infors.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.gridview_class_sp, null);
            ViewHolder holder1 = new ViewHolder();
            findView(holder1, convertView);
            convertView.setTag(R.id.TAG, holder1);

        }
        ViewHolder holder1 = (ViewHolder) convertView.getTag(R.id.TAG);
        setData(holder1, position);
        return convertView;
    }

    /**
     * gridview_class_sp
     */
    private class ViewHolder{
        ImageView good_img;
        TextView good_name;
        LinearLayout item_layout;
    }
    private void findView(ViewHolder holder,View view)
    {
        holder.good_img = (ImageView) view.findViewById(R.id.good_img);
        holder.good_name = (TextView) view.findViewById(R.id.good_name);
        holder.item_layout = (LinearLayout) view.findViewById(R.id.item_layout);
    }
    private void setData(ViewHolder holder,int position)
    {
        DistrictInfor infor = infors.get(position);
        //商品图片
        String path = infor.getImgurl();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.defult_gridview_img)
                .showImageForEmptyUri(R.mipmap.defult_gridview_img)
                .showImageOnFail(R.mipmap.defult_gridview_img).cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoader.getInstance().displayImage(path, holder.good_img, options);
        holder.good_name.setText(infor.getName());
        /**
         * 跳转分类详细
         */
        holder.item_layout.setTag(R.id.TAG_VIEWHOLDER,infor);
        holder.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DistrictInfor districtInfor = (DistrictInfor) v.getTag(R.id.TAG_VIEWHOLDER);
                Intent intent = new Intent(mContext, TypeGoodActivity.class);
                intent.putExtra("infor",districtInfor);
                mContext.startActivity(intent);
            }
        });
    }
}
