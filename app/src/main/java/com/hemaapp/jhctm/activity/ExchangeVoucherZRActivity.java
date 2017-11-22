package com.hemaapp.jhctm.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.jhctm.JhActivity;
import com.hemaapp.jhctm.JhHttpInformation;
import com.hemaapp.jhctm.JhctmApplication;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.model.Account;

/**
 * Created by lenovo on 2017/1/3.
 * 兑换券转让
 */
public class ExchangeVoucherZRActivity extends JhActivity {
    private ImageButton back_button;
    private TextView title_text;
    private ImageButton next_button;
    private EditText integral_number;//积分数量
    private EditText integral_price;//积分价格
    private EditText buy_tel;//买家手机号
    private TextView up_text;//确定转让
    private Account account;
    private TextView score_only;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_exchange_vou_zr);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GOLDSCORE_SELL:
                showProgressDialog("发送转让请求");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GOLDSCORE_SELL:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GOLDSCORE_SELL:
                showTextDialog("发送转让请求成功");
                next_button.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1000);
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GOLDSCORE_SELL:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GOLDSCORE_SELL:
                showTextDialog("转让请求发送失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (ImageButton) findViewById(R.id.next_button);
        integral_number = (EditText) findViewById(R.id.integral_number);
        integral_price = (EditText) findViewById(R.id.integral_price);
        buy_tel = (EditText) findViewById(R.id.buy_tel);
        up_text = (TextView) findViewById(R.id.up_text);
        score_only = (TextView) findViewById(R.id.score_only);
    }

    @Override
    protected void getExras() {
        account = (Account) mIntent.getSerializableExtra("account");
    }

    @Override
    protected void setListener() {
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_text.setText("金积分转让");
        next_button.setVisibility(View.INVISIBLE);
        score_only.setText("最多可转让" + String.valueOf((Integer.valueOf(account.getGold_score()) - Integer.valueOf(account.getFreeze_gold_score())) < 0 ? "0" : String.valueOf((Integer.valueOf(account.getGold_score()) - Integer.valueOf(account.getFreeze_gold_score())))));
        //提交转让
        up_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String scoreNumber = integral_number.getText().toString().trim();
                String scorePrice = integral_price.getText().toString().trim();
                String tel = buy_tel.getText().toString().trim();
                if (isNull(scoreNumber)) {
                    showTextDialog("请填写积分数量");
                    return;
                }
                if (Integer.valueOf(scoreNumber) == 0) {
                    showTextDialog("转让积分数量必须大于零!");
                    return;
                }
                if (isNull(scorePrice)) {
                    showTextDialog("请填写积分价格");
                    return;
                }
                if (isNull(tel)) {
                    showTextDialog("请填写被转让人账号");
                    return;
                }
                String mobile = "\\d{11}";// 只判断11位
                if (!tel.matches(mobile)) {
                    showTextDialog("请填写正确的手机号");
                    return;
                }
                String usertel = JhctmApplication.getInstance().getUser().getUsername();
                if (usertel.equals(tel)) {
                    showTextDialog("金积分不能转让到自己的账户!");
                    return;
                }
                //
                String token = JhctmApplication.getInstance().getUser().getToken();
                getNetWorker().goldScoreSell(token, scoreNumber, tel, scorePrice);
            }
        });
    }
}
