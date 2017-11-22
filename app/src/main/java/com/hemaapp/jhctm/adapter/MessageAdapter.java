package com.hemaapp.jhctm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.hm_FrameWork.view.RoundedImageView;
import com.hemaapp.jhctm.R;

/**
 * Created by lenovo on 2016/12/22.
 * 消息的adapter
 */
public class MessageAdapter extends HemaAdapter {
    public MessageAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public int getCount() {
        return 0;
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
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_message_view, null);
            ViewHolder holder = new ViewHolder();
            findView(holder,convertView);
            convertView.setTag(R.id.TAG_VIEWHOLDER,holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
        setData(holder,position);
        return convertView;
    }
    private class ViewHolder {
        LinearLayout layout_in;
        RoundedImageView user_img;
        TextView user_name;
        TextView time_text;
        TextView message_content;
        View show_message;
    }

    private void findView(ViewHolder holder, View view) {
        holder.layout_in = (LinearLayout) view.findViewById(R.id.layout_in);
        holder.user_img = (RoundedImageView) view.findViewById(R.id.user_img);
        holder.user_name = (TextView) view.findViewById(R.id.user_name);
        holder.time_text = (TextView) view.findViewById(R.id.time_text);
        holder.message_content = (TextView) view.findViewById(R.id.message_content);
        holder.show_message = view.findViewById(R.id.show_message);
    }
 private void setData(ViewHolder holder,int position)
 {

 }
}
