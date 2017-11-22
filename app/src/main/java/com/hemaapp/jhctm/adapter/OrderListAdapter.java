package com.hemaapp.jhctm.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.jhctm.JhNetWorker;
import com.hemaapp.jhctm.JhctmApplication;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.activity.CreditPayActivity;
import com.hemaapp.jhctm.activity.OrderActivity;
import com.hemaapp.jhctm.activity.OrderInformationActivity;
import com.hemaapp.jhctm.activity.PayTwoTypeActivity;
import com.hemaapp.jhctm.model.OrderGoodInfor;
import com.hemaapp.jhctm.model.OrderListInfor;
import com.hemaapp.jhctm.model.User;
import com.hemaapp.jhctm.view.ButtonDialog;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import xtom.frame.image.load.XtomImageTask;
import xtom.frame.view.XtomListView;

/**
 * Created by WangYuxia on 2017/2/10.
 */

public class OrderListAdapter extends HemaAdapter {

    private ArrayList<OrderListInfor> goods;
    private XtomListView listView;
    private JhNetWorker netWorker;
    public OrderListInfor operInfor;
    private OrderActivity mActivity;
    private User user;
    private String keytype;

    public OrderListAdapter(OrderActivity mContext, ArrayList<OrderListInfor> goods,
                            XtomListView listView, JhNetWorker netWorker, String keytype) {
        super(mContext);
        this.mActivity = mContext;
        this.goods = goods;
        this.listView = listView;
        this.netWorker = netWorker;
        this.keytype = keytype;
        user = JhctmApplication.getInstance().getUser();
    }

    @Override
    public boolean isEmpty() {
        if (goods == null || goods.size() == 0)
            return true;
        return false;
    }

    @Override
    public int getCount() {
        return goods == null || goods.size() == 0 ? 1 : goods.size();
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
        if (isEmpty()) {
            return getEmptyView(parent);
        }
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_order, null);
            holder = new ViewHolder();
            findview(holder, convertView);
            convertView.setTag(R.id.TAG, holder);
        } else {
            holder = (ViewHolder) convertView.getTag(R.id.TAG);
        }
        OrderListInfor infor = goods.get(position);
        holder.text_order_no.setText(infor.getBill_sn());
        holder.text_good_count.setText("共" + infor.getTotal_buycount() + "个商品，");
        if ("1".equals(keytype)) { //易物兑订单
            holder.text_order_money.setText(infor.getTotal_fee() + "积分");
        } else if ("2".equals(keytype)) { //限时兑换订单
            holder.text_order_money.setText("金积分" + String.valueOf(Integer.valueOf(infor.getGoods_gold_score())+ Integer.valueOf(infor.getShipping_fee())));
        } else if ("3".equals(keytype)) {
            holder.text_order_money.setText("¥"+infor.getTotal_fee() + "元+" + infor.getGoods_share_score()+"分享币");
        }

        ArrayList<OrderGoodInfor> infors = infor.getChildItems();
        if (infor == null || infors.size() == 0) {
            holder.layout_ordercontent.setVisibility(View.GONE);
        } else {
            holder.layout_ordercontent.setVisibility(View.VISIBLE);
            holder.layout_ordercontent.removeAllViews();
            for (int i = 0; i < infors.size(); i++) {
                View goodview = LayoutInflater.from(mContext).inflate(R.layout.listitem_order_good, null);
                ImageView imageView = (ImageView) goodview.findViewById(R.id.imageview);
                TextView text_name = (TextView) goodview.findViewById(R.id.textview);
                TextView text_desc = (TextView) goodview.findViewById(R.id.textview_0);
                TextView text_price = (TextView) goodview.findViewById(R.id.textview_1); //兑换金积分
                TextView text_oldprice = (TextView) goodview.findViewById(R.id.textview_3); //积分数量
                TextView text_count = (TextView) goodview.findViewById(R.id.textview_2); //+兑换币积分

                OrderGoodInfor goodInfor = infors.get(i);
                try {
                    URL url = new URL(goodInfor.getImgurl());
                    XtomImageTask task = new XtomImageTask(imageView, url, mContext);
                    listView.addTask(position, i, task);
                } catch (MalformedURLException e) {
                    imageView.setImageResource(R.mipmap.hotel_defult_img);
                }
                text_name.setText(goodInfor.getName());
                text_desc.setText("规格: " + goodInfor.getSpec_name() + ";数量：" + goodInfor.getBuycount());
                if ("1".equals(keytype)) {
                    text_price.setVisibility(View.GONE);
                    text_oldprice.setText(goodInfor.getScore());
                    text_count.setVisibility(View.VISIBLE);
                    text_count.setText("积分");
                } else if ("2".equals(keytype)) {
                    text_price.setVisibility(View.VISIBLE);
                    text_price.setText("金积分");
                    text_oldprice.setText(goodInfor.getGold_score());
                    text_count.setVisibility(View.GONE);
                } else if ("3".equals(keytype)) {
                    text_price.setVisibility(View.GONE);
                    text_oldprice.setText("¥"+goodInfor.getScore());
                    text_count.setVisibility(View.VISIBLE);
                    text_count.setText("元+" + goodInfor.getShare_score()+"分享币");
                }
                holder.layout_ordercontent.addView(goodview);
            }
        }
        setStatus(holder, infor);
        setListener(holder, infor);

        convertView.setTag(R.id.tag_4, infor);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderListInfor infor = (OrderListInfor) v.getTag(R.id.tag_4);
                Intent it = new Intent(mContext, OrderInformationActivity.class);
                it.putExtra("id", infor.getId());
                it.putExtra("keytype", keytype);
                mActivity.startActivityForResult(it, R.id.layout);
            }
        });
        return convertView;
    }

    private void setListener(ViewHolder holder, final OrderListInfor infor) {
        holder.text_hui.setTag(R.id.tag_0, infor);
        holder.text_hui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operInfor = (OrderListInfor) v.getTag(R.id.tag_0);
                String value = ((TextView) v).getText().toString();
                if ("取消订单".equals(value)) {
                    showSubmitDialog(0);
                }
            }
        });

        holder.text_hong.setTag(R.id.tag_1, infor);
        holder.text_hong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operInfor = (OrderListInfor) v.getTag(R.id.tag_1);
                String value = ((TextView) v).getText().toString();
                if ("去支付".equals(value)) {
                    if ("1".equals(keytype)) {
                        Intent intent = new Intent(mActivity, PayTwoTypeActivity.class);
                        intent.putExtra("type", "2");
                        intent.putExtra("billType", "1");
                        intent.putExtra("billId", operInfor.getId());
                        intent.putExtra("total_fee", operInfor.getTotal_fee());
                        mActivity.startActivity(intent);
                    } else if ("2".equals(keytype)) {
                        Intent intent = new Intent(mActivity, CreditPayActivity.class);
                        intent.putExtra("keytype", "1");
                        intent.putExtra("keyid", operInfor.getId());
                        intent.putExtra("gold_score", operInfor.getGoods_gold_score());
                        mActivity.startActivity(intent);
                    } else {
                        Intent intent = new Intent(mActivity, PayTwoTypeActivity.class);
                        intent.putExtra("type", "2");
                        intent.putExtra("billType", "2");
                        intent.putExtra("billId", operInfor.getId());
                        intent.putExtra("total_fee", operInfor.getTotal_fee());
                      //  intent.putExtra("total_fee", operInfor.getTotal_fee());
                        intent.putExtra("total_share_score", operInfor.getGoods_share_score());
                        mActivity.startActivity(intent);
                    }
                } else if ("确认收货".equals(value)) {
                    showSubmitDialog(1);
                } else if ("去评价".equals(value)) {
                    Intent it = new Intent(mContext, OrderInformationActivity.class);
                    it.putExtra("id", operInfor.getId());
                    it.putExtra("keytype", keytype);
                    mActivity.startActivityForResult(it, R.id.layout);

                }
            }
        });
    }

    private void setStatus(ViewHolder holder, OrderListInfor infor) {
        String statetype = infor.getStatetype(); // 1：有效;2：已取消
        String tradetype = infor.getTrade_type(); //0:待付款；1：待发货；2：待收货；3：待评价；4：已完成
        if ("2".equals(statetype)) {
            holder.text_order_status.setText("已取消");
            holder.text_order_status.setTextColor(0x969696);
            holder.image_driver.setVisibility(View.GONE);
            holder.layout_bottom.setVisibility(View.GONE);
        } else {
            switch (Integer.parseInt(tradetype)) {
                case 0:
                    holder.text_order_status.setText("待付款");
                    holder.text_order_status.setTextColor(mContext.getResources().getColor(R.color.blue));
                    holder.image_driver.setVisibility(View.VISIBLE);
                    holder.layout_bottom.setVisibility(View.VISIBLE);
                    holder.text_hui.setVisibility(View.VISIBLE);
                    holder.text_hui.setText("取消订单");
                    holder.text_hong.setVisibility(View.VISIBLE);
                    holder.text_hong.setText("去支付");
                    break;
                case 1:
                case 2:
                    holder.text_order_status.setText("待收货");
                    holder.image_driver.setVisibility(View.VISIBLE);
                    holder.layout_bottom.setVisibility(View.VISIBLE);
                    holder.text_hui.setVisibility(View.GONE);
                    holder.text_hong.setVisibility(View.VISIBLE);
                    holder.text_hong.setText("确认收货");
                    break;
                case 3:
                    holder.text_order_status.setText("待评价");
                    holder.image_driver.setVisibility(View.VISIBLE);
                    holder.layout_bottom.setVisibility(View.VISIBLE);
                    holder.text_hui.setVisibility(View.GONE);
                    holder.text_hong.setVisibility(View.VISIBLE);
                    holder.text_hong.setText("去评价");
                    break;
                case 4:
                    holder.text_order_status.setText("已完成");
                    holder.text_order_status.setTextColor(0x969696);
                    holder.image_driver.setVisibility(View.GONE);
                    holder.layout_bottom.setVisibility(View.GONE);
                    break;
            }
        }
    }

    private ButtonDialog mDialog;

    private void showSubmitDialog(int type) {
        if (mDialog == null) {
            mDialog = new ButtonDialog(mContext);
            mDialog.setLeftButtonText("取消");
            mDialog.setRightButtonText("确定");
        }
        if (type == 0) {
            mDialog.setText("确定要取消当前订单?");
        } else if (type == 1) {
            mDialog.setText("您确定已收到货物了吗?");
        }
        mDialog.setButtonListener(new ButtonListener(type));
        mDialog.show();
    }

    private class ButtonListener implements ButtonDialog.OnButtonListener {
        private int type;

        public ButtonListener(int type) {
            this.type = type;
        }

        @Override
        public void onLeftButtonClick(ButtonDialog dialog) {
            dialog.cancel();
        }

        @Override
        public void onRightButtonClick(ButtonDialog dialog) {
            dialog.cancel();
            if (type == 0)
                netWorker.billSaveOperate(user.getToken(), operInfor.getId(), "1", "", "");
            else if (type == 1)
                netWorker.billSaveOperate(user.getToken(), operInfor.getId(), "2", "", "");
        }
    }

    private void findview(ViewHolder holder, View view) {
        holder.text_order_no = (TextView) view.findViewById(R.id.textview_0);
        holder.text_order_status = (TextView) view.findViewById(R.id.textview_1);
        holder.layout_ordercontent = (LinearLayout) view.findViewById(R.id.linearlayout);
        holder.text_good_count = (TextView) view.findViewById(R.id.textview_4);
        holder.text_order_money = (TextView) view.findViewById(R.id.textview_2);
        holder.image_driver = (ImageView) view.findViewById(R.id.imageview);
        holder.layout_bottom = (LinearLayout) view.findViewById(R.id.layout);
        holder.text_hui = (TextView) view.findViewById(R.id.textview_5);
        holder.text_hong = (TextView) view.findViewById(R.id.textview_6);

    }

    private static class ViewHolder {
        TextView text_order_no; //订单号
        TextView text_order_status; //订单状态
        LinearLayout layout_ordercontent; //订单中的商品列表
        TextView text_good_count;  //商品的数量
        TextView text_order_money; //兑换需要积分，包括种类
        ImageView image_driver; //间隔线
        LinearLayout layout_bottom; //订单的操作按钮
        TextView text_hui; //取消订单
        TextView text_hong; //去支付，确认收货，去评价
    }
}
