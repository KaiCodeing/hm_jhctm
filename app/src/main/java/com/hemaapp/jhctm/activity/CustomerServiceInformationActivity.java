package com.hemaapp.jhctm.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.jhctm.JhActivity;
import com.hemaapp.jhctm.R;

/**
 * Created by lenovo on 2017/1/6.
 * 售后详情
 */
public class CustomerServiceInformationActivity extends JhActivity {
    private TextView title_text;
    private ImageButton back_button;
    private Button next_button;
    private TextView customer_content;//售后原因
    private TextView customer_price;//退款积分
    private TextView customer_time;//退款提交时间
    private TextView feedbank_content;//商家反馈
    private ImageView commod_img;//商品图片
    private TextView hotel_name;//商品名称
    private TextView hotel_money;//商品规格
    private TextView price;//积分
    private TextView jifen_text;//积分字
    private TextView add_bz;//加的符号
    private TextView fenxiang_text;//分享
    private TextView guige_all;//商品数量，合计
    private TextView price_all;//商品总计
    private TextView price_freight;//运费
    private TextView price_real;//实付

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_sustomer_information);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {

    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {

    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {

    }

    @Override
    protected void findView() {
        title_text = (TextView) findViewById(R.id.title_text);
        back_button = (ImageButton) findViewById(R.id.back_button);
        next_button = (Button) findViewById(R.id.next_button);
        customer_content = (TextView) findViewById(R.id.customer_content);
        customer_price = (TextView) findViewById(R.id.customer_price);
        customer_time = (TextView) findViewById(R.id.customer_time);
        feedbank_content = (TextView) findViewById(R.id.feedbank_content);
        commod_img = (ImageView) findViewById(R.id.commod_img);
        hotel_name = (TextView) findViewById(R.id.hotel_name);
        hotel_money = (TextView) findViewById(R.id.hotel_money);
        price = (TextView) findViewById(R.id.price);
        jifen_text = (TextView) findViewById(R.id.jifen_text);
        add_bz = (TextView) findViewById(R.id.add_bz);
        fenxiang_text = (TextView) findViewById(R.id.fenxiang_text);
        guige_all = (TextView) findViewById(R.id.guige_all);
        price_all = (TextView) findViewById(R.id.price_all);
        price_freight = (TextView) findViewById(R.id.price_freight);
        price_real = (TextView) findViewById(R.id.price_real);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        next_button.setVisibility(View.INVISIBLE);
        title_text.setText("售后详情");
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
