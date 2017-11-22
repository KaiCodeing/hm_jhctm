package com.hemaapp.jhctm.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
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
import com.hemaapp.jhctm.model.Address;
import com.hemaapp.jhctm.model.Bill;
import com.hemaapp.jhctm.model.Blog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by lenovo on 2016/12/26.
 * 确认订单
 */
public class ConfirmOrderActivity extends JhActivity {
    private ImageButton back_button;
    private TextView title_text;
    private ImageButton next_button;
    private TextView add_address;//添加地址
    private LinearLayout select_address_layout;//选择地址
    private TextView name_nick_text;//用户昵称
    private TextView user_iphone;//电话号码
    private TextView user_address;//收货地址
    private TextView from_where;//商品类型
    private ImageView commod_img;//商品图片
    private TextView hotel_name;//商品名称
    private TextView hotel_money;//商品规格
    private TextView price;//积分
    private TextView add_bz;//加号
    private TextView fenxiang_text;//分享币积分
    private TextView guige_all;//商品数量，合计
    private LinearLayout select_song;//配送方式
    private TextView price_number;//商品总计
    private TextView price_yunfei;//运费
    private TextView price_yes;//实付
    private LinearLayout select_fapiao;//选择发票
    private TextView fapiao_text;//发票方式
    private TextView heji_text;//合计
    private TextView yes_order;//下单
    private Blog blog;
    private String num;
    private ArrayList<Address> addresses = new ArrayList<>();
    private Address address;
    //规格，数量
    String score = "";
    String share_score = "";
    String gold_score = "";
    String brandId = "";
    String yfmoney = "";
    String mallId = "0";
    String deliveryType = "";
    private TextView fapiao_content;
    private TextView song_text;
    String fptype = "1";
    String p_or_c;
    String content;
    String username;
    private Bill bill;
    private TextView jf_orj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_confirm_order);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        String token = JhctmApplication.getInstance().getUser().getToken();
        getNetWorker().addressList(token);
        super.onResume();
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ADDRESS_LIST:
                showProgressDialog("获取收货地址");
                break;
            case BILL_SAVE:
                showProgressDialog("提交订单");
                break;
            case BILL_BILLS_SAVE:
                showProgressDialog("保存发票信息");
                break;

        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ADDRESS_LIST:
            case BILL_SAVE:
            case BILL_BILLS_SAVE:
                cancelProgressDialog();
                break;

        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ADDRESS_LIST:
                HemaArrayResult<Address> result = (HemaArrayResult<Address>) hemaBaseResult;
                addresses = result.getObjects();
                if (addresses.size() != 0) {
                    select_address_layout.setVisibility(View.VISIBLE);
                    add_address.setVisibility(View.GONE);
                    address = addresses.get(0);
                    name_nick_text.setText(address.getName());
                    user_iphone.setText(address.getTel());
                    user_address.setText(address.getAddress());
                    select_address_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(ConfirmOrderActivity.this, SelectAddressActivity.class);
                            startActivity(intent);
                        }
                    });
                    String feeId = "0";
                    for (int i = 0; i < blog.getSpecItems().size(); i++) {
                        if (blog.getSpecItems().get(i).isCheck()) {
                            feeId = blog.getSpecItems().get(i).getId();
                        }
                    }
                    String token = JhctmApplication.getInstance().getUser().getToken();
                    getNetWorker().billExpressfeeGet(token, blog.getId(), feeId, num, address.getId());
                } else {
                    add_address.setVisibility(View.VISIBLE);
                    select_address_layout.setVisibility(View.GONE);
                    add_address.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(ConfirmOrderActivity.this, SelectAddressActivity.class);
                            startActivity(intent);
                        }
                    });
                    setData();
                }

                break;
            case BILL_SAVE:
                HemaArrayResult<Bill> result1 = (HemaArrayResult<Bill>) hemaBaseResult;
                bill = result1.getObjects().get(0);
                //合计积分
                int all = Integer.valueOf(num) * Integer.valueOf(score);
                String total_fee = String.valueOf(all + Integer.valueOf(yfmoney));
                //金积分
                int allGold = Integer.valueOf(num) * Integer.valueOf(gold_score);
                String goldScore = String.valueOf(allGold + Integer.valueOf(yfmoney));
                //分享积分
                int allshare = Integer.valueOf(num) * Integer.valueOf(share_score);
                //需要发票
                if ("0".equals(fptype)) {
                    String token = JhctmApplication.getInstance().getUser().getToken();
                    if ("1".equals(blog.getKeytype()) || "3".equals(blog.getKeytype()))
                        getNetWorker().billBillsSave(token, bill.getBill_ids(), p_or_c, username, content, String.valueOf(all));
                    else
                        getNetWorker().billBillsSave(token, bill.getBill_ids(), p_or_c, username, content, String.valueOf(allGold));
                } else {
                    showTextDialog("提交订单成功");
                    next_button.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ConfirmOrderActivity.this.finish();
                        }
                    }, 1000);
                    if ("1".equals(blog.getKeytype())) {
                        Intent intent = new Intent(ConfirmOrderActivity.this, PayTwoTypeActivity.class);
                        intent.putExtra("billId", bill.getBill_ids());
                        intent.putExtra("total_fee", total_fee);
//                        intent.putExtra("share_fee",bill.getShare_fee());
                        intent.putExtra("billType", "1");
                        intent.putExtra("type", "2");
                        startActivity(intent);
                    } else if ("2".equals(blog.getKeytype())) {
                        Intent intent = new Intent(ConfirmOrderActivity.this, CreditPayActivity.class);
                        intent.putExtra("keytype", "1");
                        intent.putExtra("keyid", bill.getBill_ids());
                        intent.putExtra("gold_score", goldScore);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(ConfirmOrderActivity.this, PayTwoTypeActivity.class);
                        intent.putExtra("billType", "2");
                        intent.putExtra("total_share_score", String.valueOf(allshare));
                        intent.putExtra("type", "2");
                        intent.putExtra("total_fee", total_fee);
                        intent.putExtra("billId", bill.getBill_ids());
                        startActivity(intent);
                    }

                }
                break;
            case BILL_BILLS_SAVE:
                showTextDialog("提交订单成功");
                //合计积分
                int all1 = Integer.valueOf(num) * Integer.valueOf(score);
                String total_fee1 = String.valueOf(all1 + Integer.valueOf(yfmoney));
                //金积分
                int allGold1 = Integer.valueOf(num) * Integer.valueOf(gold_score);
                String goldScore1 = String.valueOf(allGold1 + Integer.valueOf(yfmoney));
                //分享积分
                int allshare1 = Integer.valueOf(num) * Integer.valueOf(share_score);
                next_button.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ConfirmOrderActivity.this.finish();
                    }
                }, 1000);
                //易物兑
                if ("1".equals(blog.getKeytype())) {
                    Intent intent = new Intent(ConfirmOrderActivity.this, PayTwoTypeActivity.class);
                    intent.putExtra("billId", bill.getBill_ids());
                    intent.putExtra("total_fee", total_fee1);
//                    intent.putExtra("share_fee",bill.getShare_fee());
                    intent.putExtra("billType", "1");
                    intent.putExtra("type", "2");
                    startActivity(intent);
                } else if ("2".equals(blog.getKeytype())) {
                    Intent intent = new Intent(ConfirmOrderActivity.this, CreditPayActivity.class);
                    intent.putExtra("keytype", "1");
                    intent.putExtra("keyid", bill.getBill_ids());
                    intent.putExtra("order", blog.getRebatflag());
                    intent.putExtra("gold_score", goldScore1);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(ConfirmOrderActivity.this, PayTwoTypeActivity.class);
                    intent.putExtra("billType", "2");
                    intent.putExtra("total_fee", total_fee1);
//                    intent.putExtra("total_score", total_fee1);
                    intent.putExtra("total_share_score", String.valueOf(allshare1));
                    intent.putExtra("type", "2");
                    intent.putExtra("billId", bill.getBill_ids());
                    startActivity(intent);
                }
                break;
            case BILL_EXPRESSFEE_GET:
                HemaArrayResult<String> result2 = (HemaArrayResult<String>) hemaBaseResult;
                String feemoney = result2.getObjects().get(0);
                if ("2".equals(deliveryType))
                    feemoney = "0";
                yfmoney = feemoney;
                setData();
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ADDRESS_LIST:
            case BILL_SAVE:
            case BILL_BILLS_SAVE:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ADDRESS_LIST:
                showTextDialog("获取地址失败，请稍后重试");
                break;
            case BILL_SAVE:
                showTextDialog("提交订单失败，请稍后重试");
                break;
            case BILL_BILLS_SAVE:
                showTextDialog("保存发票信息失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (ImageButton) findViewById(R.id.next_button);
        add_address = (TextView) findViewById(R.id.add_address);
        select_address_layout = (LinearLayout) findViewById(R.id.select_address_layout);
        name_nick_text = (TextView) findViewById(R.id.name_nick_text);
        user_iphone = (TextView) findViewById(R.id.user_iphone);
        user_address = (TextView) findViewById(R.id.user_address);
        from_where = (TextView) findViewById(R.id.from_where);
        commod_img = (ImageView) findViewById(R.id.commod_img);
        hotel_name = (TextView) findViewById(R.id.hotel_name);
        hotel_money = (TextView) findViewById(R.id.hotel_money);
        price = (TextView) findViewById(R.id.price);
        add_bz = (TextView) findViewById(R.id.add_bz);
        fenxiang_text = (TextView) findViewById(R.id.fenxiang_text);
        guige_all = (TextView) findViewById(R.id.guige_all);
        select_song = (LinearLayout) findViewById(R.id.select_song);
        price_number = (TextView) findViewById(R.id.price_number);
        price_yunfei = (TextView) findViewById(R.id.price_yunfei);
        price_yes = (TextView) findViewById(R.id.price_yes);
        select_fapiao = (LinearLayout) findViewById(R.id.select_fapiao);
        fapiao_text = (TextView) findViewById(R.id.fapiao_text);
        heji_text = (TextView) findViewById(R.id.heji_text);
        yes_order = (TextView) findViewById(R.id.yes_order);
        fapiao_content = (TextView) findViewById(R.id.fapiao_content);
        song_text = (TextView) findViewById(R.id.song_text);
        jf_orj = (TextView) findViewById(R.id.jf_orj);
    }

    @Override
    protected void getExras() {
        blog = (Blog) mIntent.getSerializableExtra("good");
        num = mIntent.getStringExtra("num");
        yfmoney = mIntent.getStringExtra("yfmoney");
        if (isNull(yfmoney))
            yfmoney = "0";
    }

    @Override
    protected void setListener() {
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_text.setText("确认订单");
        next_button.setVisibility(View.INVISIBLE);

    }

    private void setData() {
        //平台自营
        if ("1".equals(blog.getClient_id()))
            from_where.setText("平台自营");
        else
            from_where.setText(blog.getSellerItems().get(0).getNickname());
        String path = blog.getImgurl();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.defult_gridview_img)
                .showImageForEmptyUri(R.mipmap.defult_gridview_img)
                .showImageOnFail(R.mipmap.defult_gridview_img).cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoader.getInstance().displayImage(path, commod_img, options);
        hotel_name.setText(blog.getName());

        for (int i = 0; i < blog.getSpecItems().size(); i++) {
            if (blog.getSpecItems().get(i).isCheck()) {
                hotel_money.setText("规格:" + blog.getSpecItems().get(i).getSpec_name() + " 数量：" + num);
                score = blog.getSpecItems().get(i).getScore();
                share_score = blog.getSpecItems().get(i).getShare_score();
                gold_score = blog.getSpecItems().get(i).getGold_score();
                brandId = blog.getSpecItems().get(i).getId();
            }
        }
        //判断是那种方式，积分，金积分，分享币积分
        if ("1".equals(blog.getKeytype())) {
            add_bz.setVisibility(View.GONE);
            fenxiang_text.setVisibility(View.GONE);
            price.setText(score);
            //合计
            int all = Integer.valueOf(num) * Integer.valueOf(score);
            guige_all.setText("共" + num + "个商品，合计：" + String.valueOf(all) + "积分");
            //商品总计
            price_number.setText(String.valueOf(all) + "积分");
            price_yunfei.setText(yfmoney + "积分");
            price_yes.setText(String.valueOf(all + Integer.valueOf(yfmoney)) + "积分");
            heji_text.setText("合计：" + String.valueOf(all + Integer.valueOf(yfmoney)) + "积分");
        } else if ("2".equals(blog.getKeytype())) {
            add_bz.setVisibility(View.GONE);
            fenxiang_text.setVisibility(View.GONE);
            price.setText("金积分" + gold_score);
            int all = Integer.valueOf(num) * Integer.valueOf(gold_score);
            guige_all.setText("共" + num + "个商品，合计：金积分" + String.valueOf(all) + "");
            //商品总计
            price_number.setText("金积分" + String.valueOf(all) + "");
            price_yunfei.setText("金积分" + yfmoney + "");
            price_yes.setText("金积分" + String.valueOf(all + Integer.valueOf(yfmoney)) + "");
            heji_text.setText("合计：金积分" + String.valueOf(all + Integer.valueOf(yfmoney)) + "");
        } else {
            fenxiang_text.setText(share_score + "分享币");
            price.setText("¥" + score);
            jf_orj.setText("元");
            int all = Integer.valueOf(num) * Integer.valueOf(score);
            int allshare = Integer.valueOf(num) * Integer.valueOf(share_score);
            guige_all.setText("共" + num + "个商品，合计：¥" + String.valueOf(all) + "元+" + allshare + "分享币");
            //商品总计
            price_number.setText("¥" + String.valueOf(all) + "元+" + String.valueOf(allshare) + "分享币");
            price_yunfei.setText(yfmoney + "积分");
            price_yes.setText("¥" + String.valueOf(all + Integer.valueOf(yfmoney)) + "元+" + allshare + "分享币");
            heji_text.setText("合计：¥" + String.valueOf(all + Integer.valueOf(yfmoney)) + "元+" + allshare + "分享币");
        }
        //选择配送方式song_text
        select_song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfirmOrderActivity.this, SelectDispatchingActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        //选择发票 fapiao_content
        select_fapiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfirmOrderActivity.this, InvoiceActivity.class);
                startActivityForResult(intent, 2);
            }
        });
        //确认订单
        yes_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = JhctmApplication.getInstance().getUser().getToken();
                if (address == null) {
                    showTextDialog("请添加收货地址");
                    return;
                }
                if (isNull(deliveryType)) {
                    showTextDialog("请选择配送方式");
                    return;
                }

                getNetWorker().billSave(token, "2", blog.getId(), address.getId(), brandId, num, deliveryType, mallId);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK != resultCode) {
            return;
        }
        switch (requestCode) {
            case 2:
                fptype = data.getStringExtra("type");
                p_or_c = data.getStringExtra("p_or_c");
                content = data.getStringExtra("content");
                username = data.getStringExtra("username");
                //普通发票
                if ("0".equals(fptype)) {
                    fapiao_content.setVisibility(View.VISIBLE);
                    fapiao_content.setText(content);
                    if ("1".equals(p_or_c)) {
                        //个人
                        fapiao_text.setText("普通发票-个人");
                    } else {
                        fapiao_text.setText("普通发票-公司");
                    }
                } else {
                    fapiao_content.setVisibility(View.GONE);
                    fapiao_text.setText("不开具发票");
                }
                break;
            case 1:
                deliveryType = data.getStringExtra("type");
                mallId = data.getStringExtra("mallId");
                if ("1".equals(deliveryType))
                    song_text.setText("厂家配送");
                else
                    song_text.setText("店内购买");
                break;

            default:
                break;
        }
    }
}
