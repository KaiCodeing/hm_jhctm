package com.hemaapp.jhctm.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hemaapp.jhctm.R;

import xtom.frame.XtomObject;
public class TwoBtnDialog extends XtomObject {
	private Dialog mDialog;
	private ViewGroup mContent;
	private TextView mTextView1;
			private EditText mTextView;
	private Button leftButton;
	private Button rightButton;
	private OnButtonListener buttonListener;

	public TextView getmTextView1() {
		return mTextView1;
	}

	public void setmTextView1(TextView mTextView1) {
		this.mTextView1 = mTextView1;
	}

	public EditText getmTextView() {
		return mTextView;
	}

	public void setmTextView(EditText mTextView) {
		this.mTextView = mTextView;
	}

	public TwoBtnDialog(Context context) {
		mDialog = new Dialog(context, R.style.toast);
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.two_btn_dialog, null);
		mContent = (ViewGroup) view.findViewById(R.id.content);
		mTextView = (EditText) view.findViewById(R.id.textview);
		mTextView1 = (TextView) view.findViewById(R.id.textview1);

		leftButton = (Button) view.findViewById(R.id.left);
		leftButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (buttonListener != null)
					buttonListener.onLeftButtonClick(TwoBtnDialog.this);
			}
		});
		rightButton = (Button) view.findViewById(R.id.right);
		rightButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (buttonListener != null)
					buttonListener.onRightButtonClick(TwoBtnDialog.this);
			}
		});
		mDialog.setCancelable(false);
		mDialog.setContentView(view);
		mDialog.show();
	}
	public void setSecondTextVisible(Boolean visible)
	{
		if(visible)
		{
			mTextView1.setVisibility(View.VISIBLE);
		}else
		{
			mTextView1.setVisibility(View.GONE);
		}
	}
	public void setView(View v) {
		mContent.removeAllViews();
		mContent.addView(v);
	}

	public void setText(String text) {
		mTextView.setText(text);
	}
    public void setText1(String text)
    {
	   mTextView1.setText(text);
	}
	public void setText(int textID) {
		mTextView.setText(textID);
	}

	public void setLeftButtonText(String text) {
		leftButton.setText(text);
	}

	public void setLeftButtonText(int textID) {
		leftButton.setText(textID);
	}

	public void setRightButtonText(String text) {
		rightButton.setText(text);
	}

	public void setRightButtonText(int textID) {
		rightButton.setText(textID);
	}

	public void setRightButtonTextColor(int color) {
		rightButton.setTextColor(color);
	}

	public void show() {
		mDialog.show();
	}

	public void cancel() {
		mDialog.cancel();
	}

	public OnButtonListener getButtonListener() {
		return buttonListener;
	}

	public void setButtonListener(OnButtonListener buttonListener) {
		this.buttonListener = buttonListener;
	}

	public interface OnButtonListener {
		public void onLeftButtonClick(TwoBtnDialog dialog);

		public void onRightButtonClick(TwoBtnDialog dialog);
	}

}
