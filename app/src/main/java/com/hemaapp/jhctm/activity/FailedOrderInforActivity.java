package com.hemaapp.jhctm.activity;

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
import com.hemaapp.jhctm.model.OrderDetailInfor;
import com.hemaapp.jhctm.model.OrderGoodInfor;
import com.hemaapp.jhctm.model.User;

import java.net.MalformedURLException;
import java.net.URL;

import xtom.frame.image.load.XtomImageTask;

/**
 * Created by WangYuxia on 2017/2/13.
 * 售后订单详情。
 * id :订单的id
 * keytype:售后订单的类型
 */
public class FailedOrderInforActivity extends JhActivity {

    private TextView title_text;
    private ImageButton back_button;
    private ImageButton next_button;

    private TextView text_cancelreason;
    private TextView text_return_score;
    private TextView text_time;
    private LinearLayout layout_response;
    private TextView text_response;

    private TextView text_shopname;
    private ImageView image_avatar;
    private TextView text_goodname;
    private TextView text_attri;
    private TextView text_left;
    private TextView text_middle;
    private TextView text_right;
    private TextView text_count;
    private TextView text_score;

    private TextView text_score1;
    private TextView text_freight;
    private TextView text_totalscore;

    private String id, keytype;
    private User user;
    private OrderDetailInfor infor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_failedorder);
        super.onCreate(savedInstanceState);
        user = JhctmApplication.getInstance().getUser();
        getNetWorker().billGet(user.getToken(), id);
    }

    private void initData() {
        OrderGoodInfor good = infor.getChildItems().get(0);
        text_cancelreason.setText(good.getReason());
        if ("3".equals(good.getItemtype()))
            text_time.setText("通过时间：" + good.getHandledate().substring(0, good.getHandledate().length() - 3));
        else
            text_time.setText("提交时间：" + good.getApplydate().substring(0, good.getApplydate().length() - 3));
        if (isNull(good.getMemo()))
            layout_response.setVisibility(View.GONE);
        else {
            layout_response.setVisibility(View.VISIBLE);
            text_response.setText(good.getMemo());
        }

        try {
            URL url = new URL(good.getImgurl());
            imageWorker.loadImage(new XtomImageTask(image_avatar, url, mContext));
        } catch (MalformedURLException e) {
            image_avatar.setImageResource(R.mipmap.hotel_defult_img);
        }

        text_goodname.setText(good.getName());
        text_attri.setText("规格：" + good.getSpec_name() + ";数量：" + good.getBuycount());
        text_count.setText("共" + infor.getTotal_buycount() + "个商品，");

        if ("1".equals(keytype)) {
            if (isNull(infor.getSeller_name()))
                text_shopname.setVisibility(View.GONE);
            else {
                text_shopname.setText(infor.getSeller_name());
                text_shopname.setVisibility(View.VISIBLE);
            }
            text_return_score.setText("退款积分:" + infor.getGoods_score() + "积分");
            text_left.setVisibility(View.GONE);
            text_middle.setText(good.getScore());
            text_right.setVisibility(View.VISIBLE);
            text_right.setText("积分");
            text_score.setText(String.valueOf(Integer.valueOf(good.getScore())*Integer.valueOf(good.getBuycount())) + "积分");
            text_score1.setText(infor.getGoods_score() + "积分");
            text_freight.setText(infor.getShipping_fee() + "积分");
            text_totalscore.setText(infor.getTotal_fee() + "积分");
        } else if ("2".equals(keytype)) {
            text_shopname.setVisibility(View.GONE);
            text_return_score.setText("退款积分:" + "金积分" + infor.getGoods_gold_score());
            text_left.setVisibility(View.VISIBLE);
            text_left.setText("金积分");
            text_middle.setText(good.getGold_score());
            text_right.setVisibility(View.GONE);
            text_score.setText(String.valueOf(Integer.valueOf(good.getGold_score())*Integer.valueOf(good.getBuycount())) + "积分");
            text_score1.setText("金积分" + infor.getGoods_gold_score());
            text_freight.setText("金积分" + infor.getShipping_fee());
            text_totalscore.setText("金积分" + infor.getTotal_fee());
        } else {
            text_shopname.setVisibility(View.GONE);
            text_return_score.setText("退款积分:" + infor.getGoods_score() + "积分+分享币" + infor.getGoods_share_score());
            text_left.setVisibility(View.GONE);
            text_middle.setText(good.getShare_score());
            text_right.setVisibility(View.VISIBLE);
            text_right.setText("积分+分享币" + infor.getGoods_share_score());
            text_score.setText(String.valueOf(Integer.valueOf(good.getScore())*Integer.valueOf(good.getBuycount())) + "积分+"+String.valueOf(Integer.valueOf(good.getShare_score())*Integer.valueOf(good.getBuycount())));
            text_score1.setText(infor.getGoods_score() + "积分+分享币" + infor.getGoods_share_score());
            text_freight.setText(infor.getShipping_fee() + "分享币");
            text_totalscore.setText(infor.getTotal_fee() + "积分+分享币" + infor.getGoods_share_score());
        }
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case BILL_GET:
                showProgressDialog("正在获取...");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case BILL_GET:
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
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case BILL_GET:
                showTextDialog(hemaBaseResult.getMsg());
                if ("404".equals(hemaBaseResult.getError_code()))
                    finish();
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case BILL_GET:
                showTextDialog("获取数据失败，请稍后重试...");
                break;
        }
    }

    @Override
    protected void findView() {
        title_text = (TextView) findViewById(R.id.title_text);
        back_button = (ImageButton) findViewById(R.id.back_button);
        next_button = (ImageButton) findViewById(R.id.next_button);
        text_cancelreason = (TextView) findViewById(R.id.textview);
        text_return_score = (TextView) findViewById(R.id.textview_0);
        text_time = (TextView) findViewById(R.id.textview_1);

        layout_response = (LinearLayout) findViewById(R.id.layout);
        text_response = (TextView) findViewById(R.id.textview_2);
        text_shopname = (TextView) findViewById(R.id.shop_name);
        image_avatar = (ImageView) findViewById(R.id.imageview);
        text_goodname = (TextView) findViewById(R.id.textview_3);
        text_attri = (TextView) findViewById(R.id.textview_4);
        text_left = (TextView) findViewById(R.id.textview_5); //金积分
        text_middle = (TextView) findViewById(R.id.textview_6); //积分数量
        text_right = (TextView) findViewById(R.id.textview_7); //+分享积分
        text_count = (TextView) findViewById(R.id.textview_8);
        text_score = (TextView) findViewById(R.id.textview_10);

        text_score1 = (TextView) findViewById(R.id.price_all);
        text_freight = (TextView) findViewById(R.id.price_freight);
        text_totalscore = (TextView) findViewById(R.id.price_real);
    }

    @Override
    protected void getExras() {
        id = mIntent.getStringExtra("id");
        keytype = mIntent.getStringExtra("keytype");
    }

    @Override
    protected void setListener() {
        title_text.setText("售后详情");
        next_button.setVisibility(View.INVISIBLE);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
