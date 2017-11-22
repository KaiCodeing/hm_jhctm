package com.hemaapp.jhctm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.jhctm.JhActivity;
import com.hemaapp.jhctm.JhHttpInformation;
import com.hemaapp.jhctm.JhctmApplication;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.model.Card;
import com.hemaapp.jhctm.model.ScoreBill;

/**
 * Created by lenovo on 2017/1/5.
 * 订单activity
 */
public class NewsToPay extends JhActivity {
    private TextView title_text;//
    private ImageButton back_button;
    private Button next_button;
    private RadioGroup radio_selete;
    private TextView num, content, status, up_text;
    private String keyid;
    private Card card;
    private String keytype;
    private ScoreBill scoreBill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_news_to_pay);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        inIt();
        super.onResume();
    }

    /**
     * 初始化
     */
    private void inIt() {
        String token = JhctmApplication.getInstance().getUser().getToken();
        //积分卡转让
        if ("7".equals(keytype))
            getNetWorker().cardGet(token, keyid);
        else
            getNetWorker().scoreBillGet(token, keyid);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {

        JhHttpInformation infomation = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (infomation) {
            case CARD_GET:
                showProgressDialog("获取卡片信息");
                break;
            case SCORE_BILL_GET:
                showProgressDialog("获取积分信息");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {

        JhHttpInformation infomation = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (infomation) {
            case CARD_GET:
                cancelProgressDialog();
                break;
            case SCORE_BILL_GET:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CARD_GET:
                HemaArrayResult<Card> result = (HemaArrayResult<Card>) hemaBaseResult;
                card = result.getObjects().get(0);
                //编号
                num.setText("编号:" + card.getSn());
                //买家手机号
                content.setText("卖家:" + card.getNickname() + " " + card.getUsername());
                //判断状态
                if ("1".equals(card.getSaleflag()))
                    status.setText("待付款");
                else {
                    status.setText("已付款");
                    up_text.setVisibility(View.GONE);
                }
                break;
            case SCORE_BILL_GET:
                HemaArrayResult<ScoreBill> result1 = (HemaArrayResult<ScoreBill>) hemaBaseResult;
                scoreBill = result1.getObjects().get(0);
                //编号
                num.setText("卖家:" + scoreBill.getUsername());
                content.setText("金积分总价: ¥" + scoreBill.getFee() + " 积分" + scoreBill.getKeyid());
                if ("1".equals(scoreBill.getStatetype())) {
                    status.setText("待付款");
                } else if ("2".equals(scoreBill.getStatetype())) {
                    status.setText("已失效");
                    up_text.setVisibility(View.GONE);
                } else {
                    status.setText("交易完成");
                    up_text.setVisibility(View.GONE);
                }
                break;
        }

    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        showTextDialog(hemaBaseResult.getMsg());
        if (hemaBaseResult.getMsg().equals("该卡包已不存在"))
            next_button.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 1000);
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {

        JhHttpInformation infomation = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (infomation) {
            case CARD_GET:
                showTextDialog("获取卡包信息失败，请稍后重试");
                break;
            case SCORE_BILL_GET:
                showTextDialog("获取积分信息失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        title_text = (TextView) findViewById(R.id.title_text);
        back_button = (ImageButton) findViewById(R.id.back_button);
        next_button = (Button) findViewById(R.id.next_button);
        num = (TextView) findViewById(R.id.num);
        content = (TextView) findViewById(R.id.content);
        status = (TextView) findViewById(R.id.status);
        up_text = (TextView) findViewById(R.id.up_text);
    }

    @Override
    protected void getExras() {
        keyid = mIntent.getStringExtra("keyid");
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
        if ("7".equals(keytype))
            title_text.setText("卡片详情");
        else
            title_text.setText("金积分交易详情");
        next_button.setVisibility(View.INVISIBLE);
        //支付
        up_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsToPay.this, PayTwoTypeActivity.class);
                //支付
                if ("7".equals(keytype)) {
                    intent.putExtra("type", "4");
                    intent.putExtra("total_fee", "1000");
                    intent.putExtra("billId", keyid);
                } else {
                    intent.putExtra("type", "7");
                    intent.putExtra("total_fee", scoreBill.getFee());
                    intent.putExtra("billId", scoreBill.getId());
                }
                startActivity(intent);
            }
        });
    }
}
