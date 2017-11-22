package com.hemaapp.jhctm.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.hm_FrameWork.model.Image;
import com.hemaapp.hm_FrameWork.showlargepic.ShowLargePicActivity;
import com.hemaapp.jhctm.R;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import xtom.frame.image.load.XtomImageTask;
import xtom.frame.image.load.XtomImageWorker;

/**
 * 图片
 * @author lenovo
 *
 */
public class BlogReplyImgAdapter extends HemaAdapter {
	private ArrayList<Image> imgs;
	private int size;
	public BlogReplyImgAdapter(Context mContext, ArrayList<Image> imgs) {
		super(mContext);
		this.imgs = imgs;
	}

	@Override
	public int getCount() {
		return imgs.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.reply_gridview_img, null);
			holder = new ViewHolder();
			findView(holder, convertView);
			convertView.setTag(R.id.TAG_VIEWHOLDER, holder);
		} else
			holder = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
		setData(holder, position);
		return convertView;
	}
	
	private void setData(ViewHolder h, int position) {
		Image image = imgs.get(position);
		XtomImageWorker imgWorker = new XtomImageWorker(mContext);
		try {
			XtomImageTask task = new XtomImageTask(h.iv, new URL(image.getImgurl()), mContext);
			imgWorker.loadImage(task);
		} catch (MalformedURLException e) {
			h.iv.setImageDrawable(null);
		}
		h.iv.setTag(R.id.TAG, position);
		h.iv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int p = (Integer) v.getTag(R.id.TAG);
				Intent it = new Intent(mContext, ShowLargePicActivity.class);
				it.putExtra("position", p);
				it.putExtra("images", imgs);
				it.putExtra("titleAndContentVisible", false);
				mContext.startActivity(it);
			}
		});
	}
	
	private void findView(ViewHolder h, View v) {
		h.iv = (ImageView) v.findViewById(R.id.imageview);
	}

	private static class ViewHolder {
		ImageView iv;
	}
}
