package com.hemaapp.jhctm.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.activity.SelectSupermarketActivity;
import com.hemaapp.jhctm.model.Mall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by lenovo on 2017/1/9.
 */
public class SuperAdapter extends HemaAdapter {
    private SelectSupermarketActivity activity;
    private ArrayList<Mall> list = new ArrayList<>();
    private String city;
    private HashMap<String, Integer> alphaIndexer;
    private boolean isShow;

    public SuperAdapter(SelectSupermarketActivity activity, boolean isShow) {
        super(activity);
        this.activity = activity;
        this.isShow = isShow;
    }

    private Mall foundcity(String str) {
        for (Mall info : list) {
            if (info.getNickname().equals(str)) {
                return info;
            }
        }
        return null;
    }

    public void setList(ArrayList<Mall> list) {
        if (list == null)
            this.list = new ArrayList<Mall>();
        else {
            this.list = list;
        }

        alphaIndexer = new HashMap<String, Integer>();

        for (int i = 0; i < list.size(); i++) {
            // 当前汉语拼音首字母
            // getAlpha(list.get(i));
            String currentStr = list.get(i).getCharindex().toUpperCase(Locale.getDefault());
            // 上一个汉语拼音首字母，如果不存在为“ ”
            String previewStr = (i - 1) >= 0 ? list.get(i - 1).getCharindex().toUpperCase(Locale.getDefault())
                    : " ";
            if (!previewStr.equals(currentStr)) {
                String name = list.get(i).getCharindex().toUpperCase(Locale.getDefault());
                alphaIndexer.put(name, i);
            }
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(ArrayList<Mall> list) {
        this.list = list;
        notifyDataSetChanged();
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
            convertView = LayoutInflater.from(activity).inflate(R.layout.super_listview_view, null);
            HolderCity city = new HolderCity();
            findView(city, convertView);
            convertView.setTag(R.id.TAG_VIEWHOLDER, city);
        }
        HolderCity city = (HolderCity) convertView.getTag(R.id.TAG_VIEWHOLDER);
        setData(city, position);
        return convertView;
    }

    class HolderCity {
        TextView alpha;
        TextView name;
    }

    private void findView(HolderCity city, View view) {
        city.alpha = (TextView) view.findViewById(R.id.alpha);
        city.name = (TextView) view.findViewById(R.id.name);
    }

    private void setData(HolderCity city, int position) {
        final Mall mall = list.get(position);
        int size = 0;
        city.name.setText(list.get(position - size).getNickname());
        // city.name.setTag(list.get(position-size));
        String currentStr = list.get(position - size).getCharindex().toUpperCase(Locale.getDefault());
        String previewStr = (position - 1 - size) >= 0 ? list.get(
                position - 1 - size).getCharindex().toUpperCase(Locale.getDefault()) : " ";
        if (!previewStr.equals(currentStr)) {
            city.alpha.setVisibility(View.VISIBLE);
            city.alpha.setText(currentStr);
        } else {
            city.alpha.setVisibility(View.GONE);
        }
        /**
         * 选择归属超市
         */
        city.name.setTag(R.id.TAG_VIEWHOLDER,mall);
        city.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mall mall1 = (Mall) v.getTag(R.id.TAG_VIEWHOLDER);
                activity.getSuper(mall);
            }
        });
    }
    public HashMap<String, Integer> getAlphaIndexer() {
        return alphaIndexer;
    }
}
