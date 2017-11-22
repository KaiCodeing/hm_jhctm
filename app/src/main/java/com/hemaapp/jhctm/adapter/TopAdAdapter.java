package com.hemaapp.jhctm.adapter;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.activity.BrandbusinessInforActivity;
import com.hemaapp.jhctm.activity.GoodsInformationActivity;
import com.hemaapp.jhctm.activity.JhWebView;
import com.hemaapp.jhctm.fragment.HomeFragment;
import com.hemaapp.jhctm.model.AdList;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import xtom.frame.image.load.XtomImageTask;
import xtom.frame.image.load.XtomImageWorker;


public class TopAdAdapter extends PagerAdapter {
	private HomeFragment fragment;
	private RadioGroup mIndicator;
	private View view;
	private int size;
	private XtomImageWorker imgWorker;
	private ArrayList<AdList> indexads;

	public TopAdAdapter(HomeFragment fragment, RadioGroup mIndicator,
						View view, ArrayList<AdList> indexads) {
		this.fragment = fragment;
		this.mIndicator = mIndicator;
		this.view = view;
		this.indexads = indexads;
		imgWorker = new XtomImageWorker(fragment.getActivity());
		init();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return indexads.size();
	}

	private void init() {
		float density = fragment.getResources().getDisplayMetrics().density;
		size = (int) (density * 8);
		mIndicator = (RadioGroup) view.findViewById(R.id.radiogroup);
		mIndicator.removeAllViews();
		if (getCount() > 0)
			for (int i = 0; i < getCount(); i++) {
				RadioButton button = new RadioButton(fragment.getActivity());
				button.setId(i);
				button.setClickable(false);
				RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
						size, size);
				params.leftMargin = (int) (3 * density);
				params.rightMargin = (int) (3 * density);
				if (i == 0)
					button.setChecked(true);
				button.setButtonDrawable(android.R.color.transparent);
				button.setBackgroundResource(R.drawable.indicator_show);
				mIndicator.addView(button, params);
			}
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	@Override
	public void notifyDataSetChanged() {
		init();
		super.notifyDataSetChanged();
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		// super.destroyItem(container, position, object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		ImageView mView;
		if (container.getChildAt(position) == null) {
			mView = new ImageView(fragment.getActivity());
			mView.setScaleType(ScaleType.CENTER_CROP);
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			mView.setLayoutParams(params);
			container.addView(mView, position);
			AdList ad = indexads.get(position);
			mView.setImageResource(R.mipmap.defult_gridview_img);
			try {
				XtomImageTask task = new XtomImageTask(mView, new URL(
						ad.getImgurlbig()), fragment.getActivity());
				imgWorker.loadImage(task);
			} catch (MalformedURLException e) {
			//	mView.setImageBitmap(null);
				mView.setImageResource(R.mipmap.defult_gridview_img);
			}
			mView.setTag(R.id.TAG, ad);
			mView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					AdList aa = (AdList) v.getTag(R.id.TAG);
					/**
					 * 商品
					 */
					if (aa.getKeytype().equals("1")) {
						Intent it = new Intent(fragment.getActivity(), GoodsInformationActivity.class);
						it.putExtra("goodId", aa.getKeyid());
						fragment.startActivity(it);
					}
					//商家
					else if (aa.getKeytype().equals("2")) {
						Intent it = new Intent(fragment.getActivity(), BrandbusinessInforActivity.class);
						it.putExtra("brandId", aa.getKeyid());
						fragment.startActivity(it);
					}
					//webview
					if (aa.getKeytype().equals("3")) {
						Intent it = new Intent(fragment.getActivity(), JhWebView.class);
						it.putExtra("type", "3");
						it.putExtra("id", aa.getId());
						fragment.startActivity(it);
					}
				}
			});
		} else
			mView = (ImageView) container.getChildAt(position);
		return mView;
	}

	public ViewGroup getIndicator() {
		return mIndicator;
	}

}
