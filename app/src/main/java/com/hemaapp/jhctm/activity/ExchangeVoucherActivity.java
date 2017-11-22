package com.hemaapp.jhctm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
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
import com.hemaapp.jhctm.model.Account;


/**
 * Created by lenovo on 2016/12/30.
 * 兑换券积分
 */
public class ExchangeVoucherActivity  extends JhActivity {
    private ImageButton back_button;
    private TextView title_text;
    private Button next_button;
    private TextView jifen_text;//积分余额
    private TextView tuiguang;//广告推广
    private TextView zhuanrang;//兑换券积分转让
    private TextView no_jifen;//冻结积分
    private TextView yes_jifen;//可用积分
    private LinearLayout goto_all_layout;//积分返还时间轴
    private LinearLayout record_layout;//交易记录
    private FrameLayout buy_quan_layout;//购买兑换券
    private ImageView go_to_web;//网页
    private Account account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_exchange_voucher);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        inIt();
        super.onResume();
    }

    private void inIt()
    {
        String token = JhctmApplication.getInstance().getUser().getToken();
        getNetWorker().accountGet(token);
    }
    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case ACCOUNT_GET:
                showProgressDialog("获取账户详情");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case ACCOUNT_GET:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case ACCOUNT_GET:
                HemaArrayResult<Account> result = (HemaArrayResult<Account>) hemaBaseResult;
                account = result.getObjects().get(0);
                jifen_text.setText(account.getGold_score());

                yes_jifen.setText("可用积分:"+String.valueOf((Integer.valueOf(account.getGold_score())-Integer.valueOf(account.getFreeze_gold_score()))<0?"0":String.valueOf(Integer.valueOf(account.getGold_score())-Integer.valueOf(account.getFreeze_gold_score()))));
                no_jifen.setText("冻结积分:"+account.getFreeze_gold_score());
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case ACCOUNT_GET:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case ACCOUNT_GET:
                showTextDialog("获取账户信息失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (Button) findViewById(R.id.next_button);
        jifen_text = (TextView) findViewById(R.id.jifen_text);
        tuiguang = (TextView) findViewById(R.id.tuiguang);
        zhuanrang = (TextView) findViewById(R.id.zhuanrang);
        no_jifen = (TextView) findViewById(R.id.no_jifen);
        yes_jifen = (TextView) findViewById(R.id.yes_jifen);
        goto_all_layout = (LinearLayout) findViewById(R.id.goto_all_layout);
        record_layout = (LinearLayout) findViewById(R.id.record_layout);
        buy_quan_layout = (FrameLayout) findViewById(R.id.buy_quan_layout);
        go_to_web = (ImageView) findViewById(R.id.go_to_web);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_text.setText("金积分");
        next_button.setText("金积分明细");
        //冻结积分跳转
        no_jifen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExchangeVoucherActivity.this,FreezeListActivity.class);
                startActivity(intent);
            }
        });
        //广告推广
        tuiguang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExchangeVoucherActivity.this,AdvertisingPromotionActivity.class);
                intent.putExtra("type","1");
                intent.putExtra("account",account);
                startActivity(intent);
            }
        });
        //购买记录
        record_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExchangeVoucherActivity.this,BuyRecordActivity.class);
                startActivity(intent);
            }
        });
        //积分明细
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExchangeVoucherActivity.this,TransactionRecordActivity.class);
                startActivity(intent);
            }
        });
        //积分返还时间轴
        goto_all_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ExchangeVoucherActivity.this,TimeScore.class);
                startActivity(intent);
            }
        });
        //转让积分
        zhuanrang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExchangeVoucherActivity.this,ExchangeVoucherZRActivity.class);
                intent.putExtra("account",account);
                startActivity(intent);
            }
        });
        //购买兑换券积分
        buy_quan_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExchangeVoucherActivity.this,BuyGuiWuQuanActivity.class);
                intent.putExtra("account",account);
                startActivity(intent);
            }
        });
    }
}
