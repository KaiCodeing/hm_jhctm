package com.hemaapp.jhctm.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.activity.SearchResultSupActivity;
import com.hemaapp.jhctm.model.Mall;

import java.util.ArrayList;

/**
 * Created by lenovo on 2017/1/10.
 * 搜索超市
 */
public class SearchResultSupAdapter extends HemaAdapter {
    private SearchResultSupActivity mContext;
    private ArrayList<Mall> malls;
    public SearchResultSupAdapter(SearchResultSupActivity mContext,ArrayList<Mall> malls) {
        super(mContext);
        this.mContext = mContext;
        this.malls = malls;
    }

    @Override
    public boolean isEmpty() {
        return malls==null || malls.size()==0;
    }

    @Override
    public int getCount() {
        return isEmpty()?0:malls.size();
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
        if (convertView==null)
        {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.search_result_sup,null);
            ViewHolder holder = new ViewHolder();
            findView(holder,convertView);
            convertView.setTag(R.id.TAG_VIEWHOLDER,holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
        setData(holder,position);
        return convertView;
    }
    private class ViewHolder{
        TextView sup_name;
    }
    private void findView(ViewHolder holder,View view)
    {
        holder.sup_name = (TextView) view.findViewById(R.id.sup_name);
    }
    private void setData(ViewHolder holder,int position)
    {
        Mall mall = malls.get(position);
        holder.sup_name.setText(mall.getNickname());
        //点击事件 发送广播
        holder.sup_name.setTag(R.id.TAG_VIEWHOLDER,mall);
        holder.sup_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mall mall1 = (Mall) v.getTag(R.id.TAG_VIEWHOLDER);
                Intent intent = new Intent();
                intent.setAction("com.hemaapp.jh.sup");
                intent.putExtra("mallName",mall1.getNickname());
                intent.putExtra("mallId",mall1.getId());
                mContext.sendBroadcast(intent);
                mContext.finish();
            }
        });
    }
}
