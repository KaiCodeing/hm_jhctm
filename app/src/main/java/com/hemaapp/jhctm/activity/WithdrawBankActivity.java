package com.hemaapp.jhctm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.jhctm.JhActivity;
import com.hemaapp.jhctm.JhHttpInformation;
import com.hemaapp.jhctm.JhctmApplication;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.model.Bank;

import java.util.ArrayList;

/**
 * Created by lenovo on 2017/2/4.
 * 提现银行
 */
public class WithdrawBankActivity extends JhActivity {
    private ImageButton back_button;
    private TextView title_text;
    private ImageButton next_button;
    private LinearLayout bank_select_layout;
    private TextView bank_name;
    private TextView bank_number;
    private TextView bank_username;
    private EditText input_money;
    private TextView money_residue;
    private TextView up_text;//提交申请
    private String feecount;//余额
    private Bank bank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_withdraw_bank);
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
        getNetWorker().clientBankList(token);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_BANK_LIST:
                showProgressDialog("获取用户银行信息");
                break;
            case CASH_ADD:
                showProgressDialog("发送提现请求");
                break;
            case BANK_SAVE:
                showProgressDialog("保存提现银行卡信息");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_BANK_LIST:
                cancelProgressDialog();
                break;
            case CASH_ADD:
            case BANK_SAVE:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_BANK_LIST:
                HemaArrayResult<Bank> result1 = (HemaArrayResult<Bank>) hemaBaseResult;
                ArrayList<Bank> banks = result1.getObjects();
                if (banks == null || banks.size() == 0) {
                } else {
                    bank = banks.get(0);
                    bank_number.setVisibility(View.VISIBLE);
                    bank_username.setVisibility(View.VISIBLE);
                    bank_name.setText(bank.getBankname());
                    bank_number.setText(bank.getBankcard());
                    bank_username.setText(bank.getBankuser());
                }
                break;
            case CASH_ADD:
                showTextDialog("发送请求成功");
                next_button.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1000);
                break;
            case BANK_SAVE:
                String token = JhctmApplication.getInstance().getUser().getToken();
                String money = input_money.getText().toString().trim();
                getNetWorker().cashAdd(token, "1", money, "");
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_BANK_LIST:
            case CASH_ADD:
            case BANK_SAVE:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_BANK_LIST:
                showTextDialog("获取银行卡信息失败，请稍后重试");
                break;
            case CASH_ADD:
                showTextDialog("提现请求发送失败，请稍后重试");
                break;
            case BANK_SAVE:
                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (ImageButton) findViewById(R.id.next_button);
        bank_select_layout = (LinearLayout) findViewById(R.id.bank_select_layout);
        bank_name = (TextView) findViewById(R.id.bank_name);
        bank_number = (TextView) findViewById(R.id.bank_number);
        bank_username = (TextView) findViewById(R.id.bank_username);
        input_money = (EditText) findViewById(R.id.input_money);
        money_residue = (TextView) findViewById(R.id.money_residue);
        up_text = (TextView) findViewById(R.id.up_text);
    }

    @Override
    protected void getExras() {
        feecount = mIntent.getStringExtra("feecount");
    }

    @Override
    protected void setListener() {
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_text.setText("银行卡提现");
        next_button.setVisibility(View.INVISIBLE);
        money_residue.setText("可用" + feecount + "元");
        bank_number.setVisibility(View.GONE);
        bank_username.setVisibility(View.GONE);
        bank_name.setText("选择银行卡");
        //选择银行卡
        bank_select_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WithdrawBankActivity.this, SelectBankActivity.class);
                startActivity(intent);
            }
        });
        up_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = JhctmApplication.getInstance().getUser().getToken();
                String money = input_money.getText().toString().trim();
                if (bank == null) {
                    showTextDialog("请选择提现银行");
                    return;
                }
                if (isNull(money)) {
                    showTextDialog("请填写提现金额");
                    return;
                }
                if (Integer.valueOf(money) % 100 != 0) {
                    showTextDialog("请输入100的整数倍");
                    return;
                }
                getNetWorker().bankSave(token, bank.getBankuser(), bank.getBankname(), bank.getBankcard(), bank.getBankaddress());

            }
        });
    }
}
