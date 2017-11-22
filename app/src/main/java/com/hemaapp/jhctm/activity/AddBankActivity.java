package com.hemaapp.jhctm.activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.jhctm.JhActivity;
import com.hemaapp.jhctm.JhHttpInformation;
import com.hemaapp.jhctm.JhctmApplication;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.adapter.Time2ListAdapter;
import com.hemaapp.jhctm.model.Bank;
import com.hemaapp.jhctm.wheel.widget.OnWheelScrollListener;
import com.hemaapp.jhctm.wheel.widget.WheelView;

import java.util.ArrayList;

/**
 * Created by lenovo on 2017/1/4.
 * 添加银行卡
 */
public class AddBankActivity  extends JhActivity{
    private ImageButton back_button;
    private TextView title_text;
    private Button next_button;
    private LinearLayout crame_layout;//选择银行卡
    private TextView user_img;//银行名称
    private EditText user_name;//用户名
    private EditText bank_number;//银行卡号
    private EditText bank_kai_name;//开户行名称
    private PopupWindow timePop;
    private ViewGroup timeViewGroup;
    private WheelView ListView1;
    private FrameLayout allview;
    private ArrayList<Bank> banks = new ArrayList<>();
    private ArrayList<String> strs = new ArrayList<String>();
    private TextView clear;
    private TextView ok;
    private String str="";
    private int flag=0;
    private TextView bank_id;
    private Bank bank;
    private String defaultflag="0";
    private String bankid="0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_bank);
        super.onCreate(savedInstanceState);
        inIt();
    }
    //初始化
    private void inIt()
    {
        if (bank!=null)
        {
            user_img.setText(bank.getBankname());
            user_name.setText(bank.getBankuser());
            bank_number.setText(bank.getBankcard());
            bank_kai_name.setText(bank.getBankaddress());
            defaultflag = bank.getDefaultflag();
            bankid = bank.getId();
        }
    }
    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case BANK_LIST:
                showProgressDialog("获取银行列表");
                break;
            case CLIENT_BANK_SAVE:
                showProgressDialog("保存银行卡信息");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case BANK_LIST:
            case CLIENT_BANK_SAVE:
              cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case BANK_LIST:
                HemaArrayResult<Bank> result = (HemaArrayResult<Bank>) hemaBaseResult;
                banks.clear();
                banks = result.getObjects();
                if(flag==1)
                    showTypepop();
                break;
            case CLIENT_BANK_SAVE:
                showTextDialog("保存银行卡信息成功");
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
        switch (information)
        {
            case BANK_LIST:
            case CLIENT_BANK_SAVE:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case BANK_LIST:
                showTextDialog("获取银行卡列表失败，请稍后重试");
                break;
            case CLIENT_BANK_SAVE:
                showTextDialog("保存银行卡信息失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (Button) findViewById(R.id.next_button);
        crame_layout = (LinearLayout) findViewById(R.id.crame_layout);
        user_img = (TextView) findViewById(R.id.user_img);
        user_name = (EditText) findViewById(R.id.user_name);
        bank_number = (EditText) findViewById(R.id.bank_number);
        bank_kai_name = (EditText) findViewById(R.id.bank_kai_name);
        bank_id = (TextView) findViewById(R.id.bank_id);
    }

    @Override
    protected void getExras() {
        bank = (Bank) mIntent.getSerializableExtra("bank");
    }

    @Override
    protected void setListener() {
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_text.setText("添加银行卡");
        next_button.setText("保存");
        next_button.setTextColor(getResources().getColor(R.color.color_text));
        crame_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(banks.size()>0){
                    flag=0;
                    showTypepop();
                }else{
                    flag=1;
                    String token = JhctmApplication.getInstance().getUser().getToken();
                    getNetWorker().bankList(token);
                }
            }
        });
        //提交保存
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bankName = user_img.getText().toString().trim();
                String userName = user_name.getText().toString().trim();
                String bankNum = bank_number.getText().toString().trim();
                String kaiBankName = bank_kai_name.getText().toString().trim();
                if (isNull(bankName))
                {
                    showTextDialog("请选择银行卡");
                    return;
                }
                if (isNull(userName))
                {
                    showTextDialog("请填写户主姓名");
                    return;
                }
                if (isNull(bankNum))
                {
                    showTextDialog("请填写银行卡号");
                    return;
                }
                if (isNull(kaiBankName))
                {
                    showTextDialog("请填写开户行名称");
                    return;
                }
                String token = JhctmApplication.getInstance().getUser().getToken();
            getNetWorker().clientBankSave(token,bankid,bankName,userName,bankNum,kaiBankName,defaultflag);
            }
        });
    }
    private void showTypepop() {
        if (timePop != null) {
            timePop.dismiss();
        }
        for(Bank b:banks){
            strs.add(b.getName());
        }
        timePop = new PopupWindow(mContext);
        timePop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        timePop.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        timePop.setBackgroundDrawable(new BitmapDrawable());
        timePop.setFocusable(true);
        timePop.setAnimationStyle(R.style.PopupAnimation);
        timeViewGroup = (ViewGroup) LayoutInflater.from(mContext).inflate(
                R.layout.pop_selecttype, null);
        allview=(FrameLayout) timeViewGroup
                .findViewById(R.id.father);
        ListView1 = (WheelView) timeViewGroup
                .findViewById(R.id.id_province);
        clear = (TextView) timeViewGroup
                .findViewById(R.id.clear);
        ok = (TextView) timeViewGroup.findViewById(R.id.ok);
        timePop.setContentView(timeViewGroup);
        timePop.showAtLocation(timeViewGroup, Gravity.CENTER, 0, 0);
        ListView1.setShadowColor(0xefFFFFFF,
                0x0fFFFFFF, 0x1fFFFFFF );
        str=strs.get(0);
        ListView1.setVisibleItems(4);
        ListView1.setViewAdapter(new Time2ListAdapter(mContext, strs));
        ListView1.setCurrentItem(0);

        ListView1.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                // TODO Auto-generated method stub
                int h = wheel.getCurrentItem();
                str=strs.get(h);
                bank_id.setText(banks.get(h).toString());
            }
        })	;


        ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                timePop.dismiss();
                user_img.setText(str);
            }
        });
        allview.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                timePop.dismiss();
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                timePop.dismiss();
            }
        });
    }
}
