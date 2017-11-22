package com.hemaapp.jhctm.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.jhctm.JhActivity;
import com.hemaapp.jhctm.JhHttpInformation;
import com.hemaapp.jhctm.JhctmApplication;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.model.Account;

/**
 * Created by lenovo on 2016/12/26.
 * 支付
 */
public class CreditPayActivity extends JhActivity {
    private ImageButton back_button;
    private TextView title_text;
    private ImageButton next_button;
    private TextView money_text;//支付款项
    private TextView data;//余额信息
    private TextView up_text;//确定支付
    private String gold_score;
    private String keytype;
    private String keyid;
    private String order;
    private TextView show_close;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_credit_pay);
        super.onCreate(savedInstanceState);
        inIt();
    }
    private void inIt() {
        String token = JhctmApplication.getInstance().getUser().getToken();
        getNetWorker().accountGet(token);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ACCOUNT_GET:
                showProgressDialog("请稍后...");
                break;
            case FEEACCOUNT_REMOVE:
            case ALIPAY:
            case UNIONPAY:
            case WXPAYMENT:
                showProgressDialog("正在支付...");
                break;
            case GOLD_SCORE_REMOVE:
                showProgressDialog("正在支付");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ACCOUNT_GET:
            case FEEACCOUNT_REMOVE:
            case ALIPAY:
            case UNIONPAY:
            case WXPAYMENT:
            case GOLD_SCORE_REMOVE:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ACCOUNT_GET:
                HemaArrayResult<Account> uResult = (HemaArrayResult<Account>) hemaBaseResult;
                Account account = uResult.getObjects().get(0);
                data.setText("当前兑换券积分余额: "+account.getGold_score());
                break;
            case GOLD_SCORE_REMOVE:
                showTextDialog("支付成功");
                next_button.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                },1000);
                break;

        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information){
            case CLIENT_GET:
            case FEEACCOUNT_REMOVE:
            case ALIPAY:
            case UNIONPAY:
            case WXPAYMENT:
            case GOLD_SCORE_REMOVE:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information){
            case CLIENT_GET:
                showTextDialog("抱歉，获取数据失败");
                break;
            case FEEACCOUNT_REMOVE:
            case ALIPAY:
            case UNIONPAY:
            case WXPAYMENT:
            case GOLD_SCORE_REMOVE:
                showTextDialog("抱歉，支付失败");
                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (ImageButton) findViewById(R.id.next_button);
        money_text = (TextView) findViewById(R.id.money_text);
        data = (TextView) findViewById(R.id.data);
        up_text = (TextView) findViewById(R.id.up_text);
        show_close = (TextView) findViewById(R.id.show_close);
    }

    @Override
    protected void getExras() {
        gold_score = mIntent.getStringExtra("gold_score");
        if (isNull(gold_score))
            gold_score="0.00";
        keytype = mIntent.getStringExtra("keytype");
        keyid = mIntent.getStringExtra("keyid");
        order = mIntent.getStringExtra("order");
    }

    @Override
    protected void setListener() {
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_text.setText("支付");
        next_button.setVisibility(View.INVISIBLE);
        money_text.setText("需支付兑换券积分:"+gold_score);
        //支付
        up_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = JhctmApplication.getInstance().getUser().getToken();
                getNetWorker().gold_score_remove(token,keytype,keyid,gold_score);
            }
        });
        //判断是否显示返还
        if ("0".equals(order))
            show_close.setVisibility(View.GONE);
        else
            show_close.setVisibility(View.VISIBLE);
    }
}
