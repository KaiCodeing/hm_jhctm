package com.hemaapp.jhctm.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
 * Created by lenovo on 2017/1/3.
 * 广告推广/出售
 * type 1:广告推广 2：出售
 */
public class AdvertisingPromotionActivity extends JhActivity {
    private ImageButton back_button;
    private TextView title_text;
    private ImageButton next_button;
    private EditText buy_tel;//需要积分数量
    private TextView up_text;//确定转让
    private String type;
    private TextView show_text;
    private String cardid;
    private Account account;
    private TextView star_show;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_advertising);
        super.onCreate(savedInstanceState);
        if ("1".equals(type))
        {
            String token = JhctmApplication.getInstance().getUser().getToken();
            getNetWorker().accountGet(token);
        }
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CARD_SELL:
                showProgressDialog("出售卡片中");
                break;
            case GOLDSCORE_DOUBLY:
                showProgressDialog("积分推广中");
                break;
            case ACCOUNT_GET:
                showProgressDialog("获取账户详情");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CARD_SELL:
            case GOLDSCORE_DOUBLY:
            case ACCOUNT_GET:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CARD_SELL:
                showTextDialog("出售卡片请求发布成功");
                next_button.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1000);
                break;
            case GOLDSCORE_DOUBLY:
                showTextDialog("积分推广成功!");
                next_button.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1000);
                break;
            case ACCOUNT_GET:
                HemaArrayResult<Account> result = (HemaArrayResult<Account>) hemaBaseResult;
                account = result.getObjects().get(0);
                title_text.setText("广告推广");
                int num = 100 / Integer.valueOf(account.getGold_multi_adcount());
                show_text.setVisibility(View.GONE);
                star_show.setVisibility(View.GONE);
                show_text.setText("推广系数" + account.getGold_multi_param() + "，推广后积分数量冻结，冻结后的" + account.getGold_multi_freeze_days() + "天内(天数后台设置)，" +
                        "通过将平台信息分享至第三方的形式分" + account.getGold_multi_adcount() + "次返还，每次返回" + String.valueOf(num) + "%");
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CARD_SELL:
            case GOLDSCORE_DOUBLY:
            case ACCOUNT_GET:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CARD_SELL:
                showTextDialog("申请发送失败，请稍后重试");
                break;
            case GOLDSCORE_DOUBLY:
                showTextDialog("推广积分失败，请稍后重试");
                break;
            case ACCOUNT_GET:
                showTextDialog("获取账户信息失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (ImageButton) findViewById(R.id.next_button);
        buy_tel = (EditText) findViewById(R.id.buy_tel);
        up_text = (TextView) findViewById(R.id.up_text);
        show_text = (TextView) findViewById(R.id.show_text);
        star_show = (TextView) findViewById(R.id.star_show);
    }

    @Override
    protected void getExras() {
        type = mIntent.getStringExtra("type");
        cardid = mIntent.getStringExtra("cardid");
       // account = (Account) mIntent.getSerializableExtra("account");
    }

    @Override
    protected void setListener() {
        if ("2".equals(type)) {
            title_text.setText("出售卡包");
            buy_tel.setHint("请输入买家手机号");
            show_text.setText("输入买家手机号，系统将消息推送给买家，买家须在15分钟内完成支付，反之推送自动失效");
            up_text.setText("确定");
        } else {

        }
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        next_button.setVisibility(View.INVISIBLE);

        up_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String iphone = buy_tel.getText().toString().trim();
                String token = JhctmApplication.getInstance().getUser().getToken();
                //出售
                if ("2".equals(type)) {
                    String mobile = "\\d{11}";// 只判断11位
                    if (isNull(iphone)) {
                        showTextDialog("请填写手机号码");
                        return;
                    }
                    if (!iphone.matches(mobile)) {
                        showTextDialog("手机格式不正确，请重新输入");
                        return;
                    }
                    getNetWorker().cashSell(token, cardid, iphone);
                }
                else
                {
                    String num = buy_tel.getText().toString().trim();
                    if (isNull(num))
                    {
                        showTextDialog("请填写积分数量");
                        return;
                    }
                    if (Integer.valueOf(num)==0)
                    {
                        showTextDialog("积分数量必须大于零!");
                        return;
                    }
                    getNetWorker().goldScoreDoubly(token,num);
                }
            }
        });
    }
}
