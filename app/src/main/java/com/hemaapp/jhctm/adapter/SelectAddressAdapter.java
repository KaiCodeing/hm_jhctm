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
import com.hemaapp.jhctm.activity.AddAddressActivity;
import com.hemaapp.jhctm.activity.SelectAddressActivity;
import com.hemaapp.jhctm.model.Address;

import java.util.ArrayList;

/**
 * Created by lenovo on 2016/12/28.
 * 选择地址
 */
public class SelectAddressAdapter extends HemaAdapter {
    private SelectAddressActivity addressActivity;
    private ArrayList<Address> addresses;

    public SelectAddressAdapter(SelectAddressActivity addressActivity, ArrayList<Address> addresses) {
        super(addressActivity);
        this.addressActivity = addressActivity;
        this.addresses = addresses;
    }

    public void setAddresses(ArrayList<Address> addresses) {
        this.addresses = addresses;
    }

    @Override
    public boolean isEmpty() {
        return addresses == null || addresses.size() == 0;
    }

    @Override
    public int getCount() {
        return isEmpty() ? 1 : addresses.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_select_address, null);
            ViewHolder holder = new ViewHolder();
            findView(holder, convertView);
            convertView.setTag(R.id.TAG, holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag(R.id.TAG);
        setData(holder, position);
        return convertView;
    }

    private class ViewHolder {
        TextView username_text;//名称。电话
        TextView address_text;//地址
        ImageView check_address_img;//选择按钮
        TextView edit_address;//编辑
        TextView detele_address;//删除
        LinearLayout text_show;
        LinearLayout add_layout;
        TextView add_address_text;
    }

    private void findView(ViewHolder holder, View view) {
        holder.username_text = (TextView) view.findViewById(R.id.username_text);
        holder.address_text = (TextView) view.findViewById(R.id.address_text);
        holder.check_address_img = (ImageView) view.findViewById(R.id.check_address_img);
        holder.edit_address = (TextView) view.findViewById(R.id.edit_address);
        holder.detele_address = (TextView) view.findViewById(R.id.detele_address);
        holder.text_show = (LinearLayout) view.findViewById(R.id.text_show);
        holder.add_layout = (LinearLayout) view.findViewById(R.id.add_layout);
        holder.add_address_text = (TextView) view.findViewById(R.id.add_address_text);

    }

    private void setData(ViewHolder holder, int position) {
        if (addresses == null || addresses.size() == 0) {
            holder.text_show.setVisibility(View.GONE);
            holder.add_layout.setVisibility(View.VISIBLE);
        } else {
            //判断是否是最后一个
            final Address address = addresses.get(position);
            //最后一个
            if (position == addresses.size() - 1) {
                holder.text_show.setVisibility(View.VISIBLE);
                holder.add_layout.setVisibility(View.VISIBLE);

            } else {
                holder.text_show.setVisibility(View.VISIBLE);
                holder.add_layout.setVisibility(View.GONE);
            }
            //收件人
            holder.username_text.setText("收件人:" + address.getName() + "  电话:" + address.getTel());
            //收货地址
            holder.address_text.setText(address.getPosition2()+address.getAddress());
            //判断是否选择默认
            //没有
            if ("0".equals(address.getDefaultflag()))
                holder.check_address_img.setImageResource(R.mipmap.fapiao_check_off);
            else
                holder.check_address_img.setImageResource(R.mipmap.fapiao_check_on);
            //选择默认
            ///
            holder.check_address_img.setTag(R.id.TAG, address);
            holder.check_address_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Address address1 = (Address) v.getTag(R.id.TAG);
                    if ("0".equals(address.getDefaultflag())) {
                        addressActivity.changeAddress(address1);
                    } else {

                    }
                }
            });
            //编辑
            holder.edit_address.setTag(R.id.TAG, address);
            holder.edit_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Address address1 = (Address) v.getTag(R.id.TAG);
                    Intent intent = new Intent(addressActivity, AddAddressActivity.class);
                    intent.putExtra("address", address1);
                    addressActivity.startActivity(intent);
                }
            });
            //删除、
            holder.detele_address.setTag(R.id.TAG, address);
            holder.detele_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Address address1 = (Address) v.getTag(R.id.TAG);
                    addressActivity.deleteAddress(address1.getId());
                }
            });

        }
        holder.add_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(addressActivity, AddAddressActivity.class);
                addressActivity.startActivity(intent);
            }
        });
    }
}
