package com.hemaapp.jhctm.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import com.hemaapp.jhctm.model.CartItems;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by lenovo on 2017/2/8.
 * 购物车的确认订单
 */
public class CarOrderConfirmActivity extends JhActivity {
    private ImageButton back_button;
    private TextView title_text;
    private ImageButton next_button;
    private TextView add_address;//添加地址
    private LinearLayout select_address_layout;//选择地址
    private TextView name_nick_text;//用户昵称
    private TextView user_iphone;//电话号码
    private TextView user_address;//收货地址
    private TextView guige_all;//商品数量，合计
    private LinearLayout select_song;//配送方式
    private TextView price_number;//商品总计
    private TextView price_yunfei;//运费
    private TextView price_yes;//实付
    private LinearLayout select_fapiao;//选择发票
    private TextView fapiao_text;//发票方式
    private TextView heji_text;//合计
    private TextView yes_order;//下单
    private ArrayList<Address> addresses = new ArrayList<>();
    private Address address;
    String mallId = "";
    String deliveryType = "";
    private TextView fapiao_content;
    private TextView song_text;
    String fptype = "1";
    String p_or_c;
    String content;
    String username;
    private Bill bill;
    private ArrayList<CartItems> cartItemses = new ArrayList<>();
    private String yffee = "0";
    private LinearLayout add_view;
    int price = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_car_order);
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
            case BILL_EXPRESSFEE_GET:

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
                            Intent intent = new Intent(CarOrderConfirmActivity.this, SelectAddressActivity.class);
                            startActivity(intent);
                        }
                    });
                    //获取运费
                    String token = JhctmApplication.getInstance().getUser().getToken();
                    //获取商品id
                    //获取商品规格id
                    //获取购买数量
                    StringBuffer bufferid = new StringBuffer();
                    StringBuffer bufferSid = new StringBuffer();
                    StringBuffer buffernum = new StringBuffer();
                    for (CartItems items : cartItemses) {
                        if (items.isCheck()) {
                            bufferid.append(items.getKeyid() + ",");
                            buffernum.append(items.getBuycount() + ",");
                            bufferSid.append(items.getSpecid() + ",");
                        }
                    }
                    getNetWorker().billExpressfeeGet(token, bufferid.substring(0, bufferid.length() - 1),
                            bufferSid.substring(0, bufferSid.length() - 1),
                            buffernum.substring(0, buffernum.length() - 1),
                            address.getId());
                } else {
                    add_address.setVisibility(View.VISIBLE);
                    select_address_layout.setVisibility(View.GONE);
                    add_address.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(CarOrderConfirmActivity.this, SelectAddressActivity.class);
                            startActivity(intent);
                        }
                    });
                    //获取运费
                    String token = JhctmApplication.getInstance().getUser().getToken();
                    //获取商品id
                    //获取商品规格id
                    //获取购买数量
                    StringBuffer bufferid = new StringBuffer();
                    StringBuffer bufferSid = new StringBuffer();
                    StringBuffer buffernum = new StringBuffer();
                    for (CartItems items : cartItemses) {
                        if (items.isCheck()) {
                            bufferid.append(items.getKeyid() + ",");
                            buffernum.append(items.getBuycount() + ",");
                            bufferSid.append(items.getSpecid() + ",");
                        }
                    }
                    getNetWorker().billExpressfeeGet(token, bufferid.substring(0, bufferid.length() - 1),
                            bufferSid.substring(0, bufferSid.length() - 1),
                            buffernum.substring(0, buffernum.length() - 1),
                            "0");
                }
                break;
            case BILL_SAVE:
                HemaArrayResult<Bill> result1 = (HemaArrayResult<Bill>) hemaBaseResult;
                bill = result1.getObjects().get(0);
                //需要发票
                if ("0".equals(fptype)) {
                    String token = JhctmApplication.getInstance().getUser().getToken();
                    getNetWorker().billBillsSave(token, bill.getBill_ids(), p_or_c, username, content,String.valueOf(price));
                } else {
                    showTextDialog("提交订单成功");
                    next_button.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            CarOrderConfirmActivity.this.finish();
                        }
                    }, 1000);
                    Intent intent = new Intent(CarOrderConfirmActivity.this, PayTwoTypeActivity.class);
                    intent.putExtra("billType", "1");
                    intent.putExtra("total_fee", String.valueOf(price + Integer.valueOf(yffee)));
                    intent.putExtra("type", "2");
                    intent.putExtra("billId", bill.getBill_ids());
                    startActivity(intent);
                }
                break;
            case BILL_BILLS_SAVE:
                showTextDialog("提交订单成功");
                next_button.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        CarOrderConfirmActivity.this.finish();
                    }
                }, 1000);
                Intent intent = new Intent(CarOrderConfirmActivity.this, PayTwoTypeActivity.class);
                intent.putExtra("billType", "1");
                intent.putExtra("total_fee", String.valueOf(price + Integer.valueOf(yffee)));
                intent.putExtra("type", "2");
                intent.putExtra("billId", bill.getBill_ids());
                startActivity(intent);
                break;
            case BILL_EXPRESSFEE_GET:
                HemaArrayResult<String> result2 = (HemaArrayResult<String>) hemaBaseResult;
                String temp_token = result2.getObjects().get(0);
                if ("2".equals(deliveryType))
                    temp_token="0";
                yffee = temp_token;
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
        add_view = (LinearLayout) findViewById(R.id.add_view);
    }

    @Override
    protected void getExras() {
        cartItemses = (ArrayList<CartItems>) mIntent.getSerializableExtra("cartItemses");
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
        //判断是那种方式，积分，兑换券，分享积分
        //计算总价值
        int m = 0;
        price=0;
        for (int i = 0; i < cartItemses.size(); i++) {
            if (cartItemses.get(i).isCheck()) {
                price = (Integer.valueOf(cartItemses.get(i).getScore()) * Integer.valueOf(cartItemses.get(i).getBuycount())) + price;
                m = Integer.valueOf(cartItemses.get(i).getBuycount()) + m;
            }
        }
        guige_all.setText("共" + String.valueOf(m) + "个商品，合计：" + String.valueOf(price) + "积分");
        //商品总计
        price_number.setText(String.valueOf(price) + "积分");
        price_yunfei.setText(String.valueOf(yffee)+"积分");
        price_yes.setText(String.valueOf(price + Integer.valueOf(yffee)) + "积分");
        heji_text.setText("合计：" + String.valueOf(price + Integer.valueOf(yffee)) + "积分");
        //选择配送方式song_text
        select_song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CarOrderConfirmActivity.this, SelectDispatchingActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        //选择发票 fapiao_content
        select_fapiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CarOrderConfirmActivity.this, InvoiceActivity.class);
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
                StringBuffer carId = new StringBuffer();
                StringBuffer carNum = new StringBuffer();
                for (CartItems items : cartItemses) {
                    if (items.isCheck())
                        carId.append(items.getId() + ",");
                    carNum.append(items.getBuycount() + ",");
                }
                getNetWorker().billSave(token, "1", carId.substring(0, carId.length() - 1), address.getId(), "0", carNum.substring(0, carNum.length() - 1), deliveryType, mallId);
            }
        });
        //添加商品列表
        add_view.removeAllViews();
        for (CartItems items : cartItemses) {
            if (items.isCheck()) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.addview_good_item_car, null);
                ImageView commod_img = (ImageView) view.findViewById(R.id.commod_img);
                TextView hotel_name = (TextView) view.findViewById(R.id.hotel_name);
                TextView hotel_money = (TextView) view.findViewById(R.id.hotel_money);
                TextView price_text = (TextView) view.findViewById(R.id.price);
                String path = items.getImgurl();
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .showImageOnLoading(R.mipmap.defult_gridview_img)
                        .showImageForEmptyUri(R.mipmap.defult_gridview_img)
                        .showImageOnFail(R.mipmap.defult_gridview_img).cacheInMemory(true)
                        .bitmapConfig(Bitmap.Config.RGB_565).build();
                ImageLoader.getInstance().displayImage(path, commod_img, options);
                hotel_name.setText(items.getName());
                //规格
                hotel_money.setText("规格:" + items.getSpec_name() + ";数量" + items.getBuycount());
                //积分
                price_text.setText(items.getScore());
                add_view.addView(view);
            }
        }
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
