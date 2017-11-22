package com.hemaapp.jhctm.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.activity.AddBankActivity;
import com.hemaapp.jhctm.activity.UserInformationActivity;
import com.hemaapp.jhctm.model.Bank;

import java.util.ArrayList;

/**
 * Created by lenovo on 2017/1/4.
 * 修改个人信息的adapter
 */
public class UserInformationAdapter extends HemaAdapter {
    private UserInformationActivity mContext;
    private ArrayList<Bank> banks;

    public UserInformationAdapter(UserInformationActivity mContext, ArrayList<Bank> banks) {
        super(mContext);
        this.mContext = mContext;
        this.banks = banks;
    }

    public void setBanks(ArrayList<Bank> banks) {
        this.banks = banks;
    }

    @Override
    public boolean isEmpty() {
        return banks == null || banks.size() == 0;
    }

    @Override
    public int getCount() {
        return isEmpty() ? 0 : banks.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_user_information, null);
            ViewHolder holder = new ViewHolder();
            findView(holder, convertView);
            convertView.setTag(R.id.TAG_VIEWHOLDER, holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
        setData(holder, position);
        return convertView;
    }

    //adapter_user_information
    private class ViewHolder {
        TextView bank_name;//银行名称
        TextView bank_number;//银行卡号
        TextView user_name;//真实姓名
        ImageView select_img;//默认
        LinearLayout bank_layout;
    }

    private void findView(ViewHolder holder, View view) {
        holder.bank_name = (TextView) view.findViewById(R.id.bank_name);
        holder.bank_number = (TextView) view.findViewById(R.id.bank_number);
        holder.user_name = (TextView) view.findViewById(R.id.user_name);
        holder.select_img = (ImageView) view.findViewById(R.id.select_img);
        holder.bank_layout = (LinearLayout) view.findViewById(R.id.bank_layout);
    }

    private void setData(ViewHolder holder, int position) {
        Bank bank = banks.get(position);
        log_i("PPPPPPPPPPPPPPPPPPPPPPPPPPPP"+position);
        holder.bank_name.setText(bank.getBankname());
        holder.bank_number.setText(bank.getBankcard());
        holder.user_name.setText(bank.getBankuser());
        if (bank.isCheck())
            holder.select_img.setBackgroundResource(R.mipmap.fapiao_check_on);
        else
            holder.select_img.setBackgroundResource(R.mipmap.fapiao_check_off);
        //选择默认
        holder.select_img.setTag(R.id.TAG, position);
        holder.select_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int bank1 = (int) v.getTag(R.id.TAG);
                mContext.changeData(bank1);
            }
        });
        //编辑
        holder.bank_layout.setTag(R.id.TAG,bank);
        holder.bank_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bank bank1 = (Bank) v.getTag(R.id.TAG);
                Intent intent = new Intent(mContext, AddBankActivity.class);
                intent.putExtra("bank",bank1);
                mContext.startActivity(intent);
            }
        });
    }
}
