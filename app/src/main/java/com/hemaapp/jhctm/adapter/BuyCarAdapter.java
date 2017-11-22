package com.hemaapp.jhctm.adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.fragment.BuyCarFragment;
import com.hemaapp.jhctm.model.CartItems;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by lenovo on 2016/12/29.
 * 购物车的adapter
 */
public class BuyCarAdapter extends HemaAdapter {
    private ArrayList<CartItems> cartItemses;
    private BuyCarFragment fragment;

    public BuyCarAdapter(BuyCarFragment fragment, ArrayList<CartItems> cartItemses) {
        super(fragment);
        this.fragment = fragment;
        this.cartItemses = cartItemses;
    }

    @Override
    public boolean isEmpty() {
        return cartItemses == null || cartItemses.size() == 0;
    }

    public void setCartItemses(ArrayList<CartItems> cartItemses) {
        this.cartItemses = cartItemses;
    }

    @Override
    public int getCount() {
        return isEmpty() ? 1 : cartItemses.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_buy_car, null);
            ViewHolder holder = new ViewHolder();
            findView(holder, convertView);
            convertView.setTag(R.id.TAG_VIEWHOLDER, holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
        setData(holder, position);
        return convertView;
    }

    /**
     * adapter_buy_car
     */
    private class ViewHolder {
        LinearLayout view_top;//清空layout
        TextView delete_out;//清空
        ImageView out_car;//失效
        ImageView check_select_img;//选择
        ImageView chanp_img;//商品图像
        TextView commod_name;//商品名称
        TextView content_commid;//规格
        TextView commod_moeny;//积分
        ImageView subtarct;//减
        TextView add_or_subtract;//数量
        ImageView add;//加
        LinearLayout layout1_in;
    }

    private void findView(ViewHolder holder, View view) {
        holder.view_top = (LinearLayout) view.findViewById(R.id.view_top);
        holder.delete_out = (TextView) view.findViewById(R.id.delete_out);
        holder.out_car = (ImageView) view.findViewById(R.id.out_car);
        holder.check_select_img = (ImageView) view.findViewById(R.id.check_select_img);
        holder.commod_name = (TextView) view.findViewById(R.id.commod_name);
        holder.chanp_img = (ImageView) view.findViewById(R.id.chanp_img);
        holder.content_commid = (TextView) view.findViewById(R.id.content_commid);
        holder.commod_moeny = (TextView) view.findViewById(R.id.commod_moeny);
        holder.subtarct = (ImageView) view.findViewById(R.id.subtarct);
        holder.add_or_subtract = (TextView) view.findViewById(R.id.add_or_subtract);
        holder.add = (ImageView) view.findViewById(R.id.add);
        holder.layout1_in = (LinearLayout) view.findViewById(R.id.layout1_in);
    }

    private void setData(ViewHolder holder, int position) {
        final CartItems items = cartItemses.get(position);
        //商品图像
        String path = items.getImgurl();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.defult_gridview_img)
                .showImageForEmptyUri(R.mipmap.defult_gridview_img)
                .showImageOnFail(R.mipmap.defult_gridview_img).cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoader.getInstance().displayImage(path, holder.chanp_img, options);
        //是否选中
        if (items.isCheck())
        {
            holder.check_select_img.setImageResource(R.mipmap.fapiao_check_on);
        }
        else
            holder.check_select_img.setImageResource(R.mipmap.fapiao_check_off);
        holder.commod_name.setText(items.getName());
        //规格
        holder.content_commid.setText("规格:" + items.getSpec_name());
        //积分
        holder.commod_moeny.setText(items.getScore());
        //数量
        holder.add_or_subtract.setText(items.getBuycount());
        //减
        holder.subtarct.setTag(R.id.TAG, items);
        holder.subtarct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartItems itemses = (CartItems) v.getTag(R.id.TAG);
                if (itemses.getBuycount().equals("1")) {
                //    fragment.showTextDialog("数量最少为1哦");
                } else {
                    int num = Integer.valueOf(itemses.getBuycount()) - 1;
                    fragment.changeNum(itemses.getId(), String.valueOf(num), "3");
                }

            }
        });
        //加
        holder.add.setTag(R.id.TAG, items);
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartItems itemses = (CartItems) v.getTag(R.id.TAG);
                int num = 1 + Integer.valueOf(itemses.getBuycount());
                fragment.changeNum(itemses.getId(), String.valueOf(num), "3");
            }
        });
        //选择
        holder.check_select_img.setTag(R.id.TAG, items);
        holder.check_select_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartItems itemses = (CartItems) v.getTag(R.id.TAG);
                fragment.selectCart(Integer.valueOf(itemses.getId()));
            }
        });

    }
}
