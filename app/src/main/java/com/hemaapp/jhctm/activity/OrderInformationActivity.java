package com.hemaapp.jhctm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.jhctm.JhActivity;
import com.hemaapp.jhctm.JhHttpInformation;
import com.hemaapp.jhctm.JhctmApplication;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.model.FaPiaoInfor;
import com.hemaapp.jhctm.model.OrderDetailInfor;
import com.hemaapp.jhctm.model.OrderGoodInfor;
import com.hemaapp.jhctm.model.User;
import com.hemaapp.jhctm.view.ButtonDialog;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import xtom.frame.image.load.XtomImageTask;

/**
 * Created by lenovo on 2017/1/5.
 * 订单详情
 * id:订单的id, keytype 订单的类型
 */
public class OrderInformationActivity extends JhActivity {

    private ImageButton back_button;
    private TextView title_text;
    private Button next_button;
    private TextView order_number;//订单号
    private TextView order_time;//下单时间
    private TextView order_state;//订单状态
    private TextView user_tel;//收件人姓名电话
    private TextView user_address;//收货地址
    private TextView text_shopname;
    private LinearLayout linearLayout;
    private TextView guige_all;//商品数量，合计
    private LinearLayout look_chaoshi;//
    private TextView hexiao_number; //核销码
    private TextView text_peisongtype; //配送方式
    private TextView price_all;//商品总计
    private TextView price_freight;//运费
    private TextView price_real;//实付款
    private TextView fapiao_text;//是否开发票
    private TextView fapiao_content;//发票内容
    private TextView all_price_money;
    private LinearLayout button_layout;//
    private TextView order_cancel;//取消订单
    private TextView order_over;//去支付
    private LinearLayout show_where_detail;//自提
    private OrderDetailInfor infor;
    private String id, keytype;
    private Boolean isclicked = false;//显示点击的自提详情地址
    private TextView name, where;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_order_information);
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onResume() {
        user = JhctmApplication.getInstance().getUser();
        getNetWorker().billGet(user.getToken(), id);
        super.onResume();
    }

    private void initData() {
        order_number.setText("订单号：" + infor.getBill_sn());
        order_time.setText("下单时间：" + infor.getRegdate().substring(0, infor.getRegdate().length() - 3));
        setStatus();
        user_tel.setText(infor.getConsignee() + "  " + infor.getPhone());
        user_address.setText(infor.getAddress());
        text_shopname.setText(infor.getChildItems().get(0).getSeller_name());
        setGoodsInfor();
        if ("1".equals(keytype)) {
            guige_all.setText("共" + infor.getTotal_buycount() + "个商品，合计：" + infor.getGoods_score() + "积分");
            price_all.setText(infor.getGoods_score() + "积分");
            price_freight.setText(infor.getShipping_fee() + "积分");
            price_real.setText(infor.getTotal_fee() + "积分");
            all_price_money.setText("合计（包含运费" + infor.getShipping_fee() + "积分）:" + infor.getTotal_fee() + "积分");
        } else if ("2".equals(keytype)) {
            guige_all.setText("共" + infor.getTotal_buycount() + "个商品，合计：金积分" + infor.getGoods_gold_score());
            price_all.setText("金积分" + infor.getGoods_gold_score());
            price_freight.setText("金积分" + infor.getShipping_fee());
            price_real.setText("金积分" + String.valueOf(Integer.valueOf( infor.getGoods_gold_score())+Integer.valueOf(infor.getShipping_fee())));
            all_price_money.setText("合计（包含运费金积分" + infor.getShipping_fee() + "）: 金积分" +  String.valueOf(Integer.valueOf( infor.getGoods_gold_score())+Integer.valueOf(infor.getShipping_fee())));
        } else if ("3".equals(keytype)) {
            guige_all.setText("共" + infor.getTotal_buycount()
                    + "个商品，合计：¥" + infor.getGoods_score() + "元+" + infor.getGoods_share_score() + "分享币");
            price_all.setText("¥"+infor.getGoods_score() + "元+" + infor.getGoods_share_score() + "分享币");
            price_freight.setText(infor.getShipping_fee() + "积分");
            price_real.setText("¥"+infor.getTotal_fee() + "元+" + infor.getGoods_share_score() + "分享币");
            all_price_money.setText("合计（包含运费" + infor.getShipping_fee() + "积分）:¥"
                    + infor.getTotal_fee() + "元+" + infor.getGoods_share_score() + "分享币");
        }

        if ("1".equals(infor.getRecvtype())) {
            look_chaoshi.setVisibility(View.GONE);
            text_peisongtype.setText("平台配送");
        } else {
            look_chaoshi.setVisibility(View.VISIBLE);
//            if ("0".equals(infor.getTradetype())) {
//                hexiao_number.setVisibility(View.GONE);
//                text_peisongtype.setText("到店自提");
//            } else {
            hexiao_number.setVisibility(View.VISIBLE);
                hexiao_number.setText("核销码：" + infor.getCostcode());
                text_peisongtype.setText("到店自提");
//            }

        }

        ArrayList<FaPiaoInfor> infors = infor.getBillsItems();
        if (infors == null || infors.size() == 0) {
            fapiao_text.setText("不开具发票");
            fapiao_content.setVisibility(View.GONE);
        } else {
            FaPiaoInfor in = infors.get(0);
            if ("1".equals(in.getBills_type()))
                fapiao_text.setText("普通发票 - 个人");
            else
                fapiao_text.setText("普通发票 - 公司");
            fapiao_content.setVisibility(View.VISIBLE);
            fapiao_content.setText(in.getBills_content());
        }
    }

    private void setGoodsInfor() {
        ArrayList<OrderGoodInfor> goods = infor.getChildItems();
        linearLayout.removeAllViews();
        if (goods != null && goods.size() > 0) {
            for (int i = 0; i < goods.size(); i++) {
                View goodview = LayoutInflater.from(mContext).inflate(R.layout.listitem_detail_good, null);
                ImageView imageView = (ImageView) goodview.findViewById(R.id.imageview);
                TextView text_name = (TextView) goodview.findViewById(R.id.textview);
                TextView text_desc = (TextView) goodview.findViewById(R.id.textview_0);
                TextView text_price = (TextView) goodview.findViewById(R.id.textview_1); //金积分
                TextView text_oldprice = (TextView) goodview.findViewById(R.id.textview_3); //积分数量
                TextView text_count = (TextView) goodview.findViewById(R.id.textview_2); //+分享币积分
                LinearLayout layout = (LinearLayout) goodview.findViewById(R.id.layout);
                TextView text_apply = (TextView) goodview.findViewById(R.id.apply);
                TextView text_reply = (TextView) goodview.findViewById(R.id.reply);

                OrderGoodInfor goodInfor = goods.get(i);
                try {
                    URL url = new URL(goodInfor.getImgurl());
                    imageWorker.loadImage(new XtomImageTask(imageView, url, mContext));
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

                if ("2".equals(infor.getStatetype())) {
                    layout.setVisibility(View.GONE);
                } else {
                    if ("3".equals(infor.getTradetype())) {
                        OrderGoodInfor good = goods.get(i);
                        if ("0".equals(good.getServicetype())
                                && "0".equals(good.getReplytype())) {
                            layout.setVisibility(View.VISIBLE);
                            text_apply.setVisibility(View.VISIBLE);
                            text_reply.setVisibility(View.VISIBLE);
                        } else {
                            layout.setVisibility(View.GONE);
                        }
                        text_apply.setTag(R.id.tag_0, good);
                        text_apply.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                OrderGoodInfor good = (OrderGoodInfor) v.getTag(R.id.tag_0);
                                showCommitDialog(good);
                            }
                        });
                        text_reply.setTag(R.id.tag_1, good);
                        text_reply.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                OrderGoodInfor good = (OrderGoodInfor) v.getTag(R.id.tag_1);
                                Intent it = new Intent(mContext, EvaluateActivity.class);
                                it.putExtra("good", good);
                                it.putExtra("keytype", keytype);
                                startActivityForResult(it, R.id.layout);
                            }
                        });
                    } else
                        layout.setVisibility(View.GONE);
                }
                linearLayout.addView(goodview);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK != resultCode) {
            return;
        }
        finish();
    }

    private void setStatus() {
        String statetype = infor.getStatetype(); // 1：有效;2：已取消
        String tradetype = infor.getTradetype(); //0:待付款；1：待发货；2：待收货；3：待评价；4：已完成
        if ("2".equals(statetype)) {
            order_state.setText("已取消");
            order_state.setTextColor(0x969696);
            button_layout.setVisibility(View.GONE);
        } else {
            switch (Integer.parseInt(tradetype)) {
                case 0:
                    order_state.setText("待付款");
                    order_state.setTextColor(mContext.getResources().getColor(R.color.blue));
                    button_layout.setVisibility(View.VISIBLE);
                    order_cancel.setVisibility(View.VISIBLE);
                    order_cancel.setText("取消订单");
                    order_over.setVisibility(View.VISIBLE);
                    order_over.setText("去支付");
                    break;
                case 1:
                case 2:
                    order_state.setText("待收货");
                    button_layout.setVisibility(View.VISIBLE);
                    order_cancel.setVisibility(View.GONE);
                    order_over.setVisibility(View.VISIBLE);
                    order_over.setText("确认收货");
                    break;
                case 3:
                    order_state.setText("待评价");
                    button_layout.setVisibility(View.GONE);
                    break;
                case 4:
                    order_state.setText("已完成");
                    order_state.setTextColor(0x969696);
                    button_layout.setVisibility(View.GONE);
                    break;
            }
        }
    }

    private EditText editText;
    private ButtonDialog cancelDialog;
    private String keyid;

    private void showCommitDialog(OrderGoodInfor good) {
        keyid = good.getId();
        if (cancelDialog == null) {
            cancelDialog = new ButtonDialog(mContext);
            View view = LayoutInflater.from(mContext).inflate(
                    R.layout.pop_cancelorder, null);
            editText = (EditText) view.findViewById(R.id.edittext);
            cancelDialog.setView(view);
            cancelDialog.setLeftButtonText("取消");
            cancelDialog.setRightButtonText("确定");
        }
        cancelDialog.setButtonListener(new ButtonListener(2));
        cancelDialog.show();
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case BILL_GET:
            case BILL_SAVEOPERATE:
                showProgressDialog("请稍后...");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case BILL_GET:
            case BILL_SAVEOPERATE:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case BILL_GET:
                HemaArrayResult<OrderDetailInfor> oResult = (HemaArrayResult<OrderDetailInfor>) hemaBaseResult;
                infor = oResult.getObjects().get(0);
                initData();
                break;
            case BILL_SAVEOPERATE:
                String keytype = hemaNetTask.getParams().get("keytype");
                if ("1".equals(keytype)) {
                    showTextDialog("取消订单成功！");
                    next_button.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 1000);
                    return;
                } else if ("2".equals(keytype)) {
                    showTextDialog("确认收货成功！");
                }else
                {
                    showTextDialog("退货申请提交成功!");
                }
                getNetWorker().billGet(user.getToken(), id);
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case BILL_GET:
            case BILL_SAVEOPERATE:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case BILL_GET:
            case BILL_SAVEOPERATE:
                showTextDialog("操作失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (Button) findViewById(R.id.next_button);
        order_number = (TextView) findViewById(R.id.order_number);
        order_time = (TextView) findViewById(R.id.order_time);
        order_state = (TextView) findViewById(R.id.order_state);
        user_tel = (TextView) findViewById(R.id.user_tel);
        user_address = (TextView) findViewById(R.id.user_address);
        text_shopname = (TextView) findViewById(R.id.shop_name);
        linearLayout = (LinearLayout) findViewById(R.id.add_view);

        guige_all = (TextView) findViewById(R.id.guige_all);
        look_chaoshi = (LinearLayout) findViewById(R.id.look_chaoshi);
        show_where_detail = (LinearLayout) findViewById(R.id.show_where_detail);
        name = (TextView) findViewById(R.id.name);
        where = (TextView) findViewById(R.id.where);
        hexiao_number = (TextView) findViewById(R.id.hexiao_number);
        text_peisongtype = (TextView) findViewById(R.id.textview);
        price_all = (TextView) findViewById(R.id.price_all);
        price_freight = (TextView) findViewById(R.id.price_freight);
        price_real = (TextView) findViewById(R.id.price_real);
        fapiao_text = (TextView) findViewById(R.id.fapiao_text);
        fapiao_content = (TextView) findViewById(R.id.fapiao_content);
        all_price_money = (TextView) findViewById(R.id.all_price_money);
        button_layout = (LinearLayout) findViewById(R.id.button_layout);
        order_cancel = (TextView) findViewById(R.id.order_cancel);
        order_over = (TextView) findViewById(R.id.order_over);
    }

    @Override
    protected void getExras() {
        id = mIntent.getStringExtra("id");
        keytype = mIntent.getStringExtra("keytype");
    }

    @Override
    protected void setListener() {
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        next_button.setVisibility(View.INVISIBLE);
        title_text.setText("详情");

        order_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = ((TextView) v).getText().toString();
                if ("取消订单".equals(value)) {
                    showSubmitDialog(0);
                }
            }
        });

        order_over.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = ((TextView) v).getText().toString();
                if ("去支付".equals(value)) {
                    //  showTextDialog("抱歉，需要额外添加跳转页面");f
                    if ("1".equals(keytype)) {
                        Intent intent = new Intent(OrderInformationActivity.this, PayTwoTypeActivity.class);
                        intent.putExtra("type", "2");
                        intent.putExtra("billType", "1");
                        intent.putExtra("billId", infor.getId());
                        intent.putExtra("total_fee", infor.getTotal_fee());
                        startActivity(intent);
                    } else if ("2".equals(keytype)) {
                        Intent intent = new Intent(OrderInformationActivity.this, CreditPayActivity.class);
                        intent.putExtra("keytype", "1");
                        intent.putExtra("keyid", infor.getId());
                        intent.putExtra("gold_score", infor.getTotal_fee());
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(OrderInformationActivity.this, PayTwoTypeActivity.class);
                        intent.putExtra("type", "2");
                        intent.putExtra("billType", "2");
                        intent.putExtra("billId", infor.getId());
                     //   intent.putExtra("total_score", infor.getGoods_score());
                         intent.putExtra("total_fee", infor.getTotal_fee());
                        intent.putExtra("total_share_score", infor.getGoods_share_score());
                        startActivity(intent);
                    }
                } else if ("确认收货".equals(value)) {
                    showSubmitDialog(1);
                }
            }
        });

        look_chaoshi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isclicked == false) {
                    show_where_detail.setVisibility(View.VISIBLE);
                    name.setText(infor.getMall_name());
                    where.setText(infor.getMall_address());
                    isclicked=true;
                } else {
                    show_where_detail.setVisibility(View.GONE);
                    isclicked=false;
                }

            }
        });

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
                getNetWorker().billSaveOperate(user.getToken(), infor.getId(), "1", "", "");
            else if (type == 1)
                getNetWorker().billSaveOperate(user.getToken(), infor.getId(), "2", "", "");
            else if (type == 2) {
                String content = editText.getText().toString();
                if (isNull(content)) {
                    showTextDialog("抱歉，请输入退款原因");
                    return;
                }
                getNetWorker().billSaveOperate(user.getToken(), keyid, "3", content, content);
            }
        }
    }
}
