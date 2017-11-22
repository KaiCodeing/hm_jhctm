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
import com.hemaapp.jhctm.activity.BrandbusinessInforActivity;
import com.hemaapp.jhctm.model.City;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by lenovo on 2016/12/21.
 * 首页搜搜结果
 */
public class SearchResultAdapter extends HemaAdapter {
    private Context mContext;
    private ArrayList<City> cities;
    public SearchResultAdapter(Context mContext,ArrayList<City> cities) {
        super(mContext);
        this.mContext = mContext;
        this.cities = cities;
    }

    @Override
    public boolean isEmpty() {
        return cities==null || cities.size()==0;
    }

    @Override
    public int getCount() {
        return isEmpty()?0:cities.size();
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
            return  getEmptyView(parent);
        if (convertView == null)
        {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_search_hotel,null);
            ViewHolder holder = new ViewHolder();
            findView(holder,convertView);
            convertView.setTag(R.id.TAG,holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag(R.id.TAG);
        setData(holder,position);
        return convertView;
    }
    //酒店的view  item_search_hotel
    private class ViewHolder{
        ImageView commod_img;
        TextView hotel_name;
        TextView hotel_money;
        LinearLayout item_hoetl;
    }
    private void findView(ViewHolder holder,View view)
    {
        holder.commod_img  = (ImageView) view.findViewById(R.id.commod_img);
        holder.hotel_name  = (TextView) view.findViewById(R.id.hotel_name);
        holder.hotel_money  = (TextView) view.findViewById(R.id.hotel_money);
        holder.item_hoetl  = (LinearLayout) view.findViewById(R.id.item_hoetl);
    }
    private void setData(ViewHolder holder,int position)
    {
        City city = cities.get(position);
        //商品图片
        String path = city.getAvatar();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.defult_gridview_img)
                .showImageForEmptyUri(R.mipmap.defult_gridview_img)
                .showImageOnFail(R.mipmap.defult_gridview_img).cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoader.getInstance().displayImage(path, holder.commod_img, options);
        holder.hotel_name.setText(city.getNickname());
        holder.hotel_money.setText("保证金额 ¥"+city.getBail());
        holder.item_hoetl.setTag(R.id.TAG,city);
        holder.item_hoetl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                City city1 = (City) v.getTag(R.id.TAG);
                Intent intent = new Intent(mContext, BrandbusinessInforActivity.class);
                intent.putExtra("brandId",city1.getId());
                mContext.startActivity(intent);
            }
        });
    }

}
