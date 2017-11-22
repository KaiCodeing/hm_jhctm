package com.hemaapp.jhctm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.jhctm.JhActivity;
import com.hemaapp.jhctm.JhHttpInformation;
import com.hemaapp.jhctm.JhctmApplication;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.adapter.SelectBankAdapter;
import com.hemaapp.jhctm.model.Bank;
import com.hemaapp.jhctm.view.MyListView;

import java.util.ArrayList;

/**
 * Created by lenovo on 2017/2/4.
 * 选择银行卡
 */
public class SelectBankActivity extends JhActivity {
    private ImageButton back_button;
    private TextView title_text;
    private ImageButton next_button;
    private MyListView listview;
    private ProgressBar progressbar;
    private ArrayList<Bank> banks = new ArrayList<>();
    private SelectBankAdapter adapter;
    private TextView up_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_select_bank);
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
            case CLIENT_BANK_SAVE:
                showProgressDialog("保存银行卡操作");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_BANK_LIST:
                cancelProgressDialog();
                progressbar.setVisibility(View.GONE);
                break;
            case CLIENT_BANK_SAVE:
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
                banks.clear();
                banks = result1.getObjects();
                freshData();
                break;
            case CLIENT_BANK_SAVE:
                inIt();
                break;
        }
    }

    private void freshData() {
        if (adapter == null) {
            adapter = new SelectBankAdapter(SelectBankActivity.this, banks);
            adapter.setEmptyString("暂无数据");
            listview.setAdapter(adapter);
        } else {
            adapter.setEmptyString("暂无数据");
            adapter.setBanks(banks);
            adapter.notifyDataSetChanged();
        }
    }

    public void setBankData(Bank bankData) {
        String token1 = JhctmApplication.getInstance().getUser().getToken();
        String bankName = bankData.getBankname();
        String bankNum = bankData.getBankcard();
        String bankUser = bankData.getBankuser();
        String kaiBank = bankData.getBankaddress();
        String bankId = bankData.getId();
        getNetWorker().clientBankSave(token1, bankId, bankName, bankUser, bankNum, kaiBank, "1");
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_BANK_LIST:
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
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (ImageButton) findViewById(R.id.next_button);
        listview = (MyListView) findViewById(R.id.listview);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        up_text = (TextView) findViewById(R.id.up_text);
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
        title_text.setText("选择银行卡");
        next_button.setVisibility(View.INVISIBLE);
        up_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectBankActivity.this, AddBankActivity.class);
                startActivity(intent);
            }
        });
    }
}
