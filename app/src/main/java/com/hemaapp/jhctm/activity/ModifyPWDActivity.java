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

import xtom.frame.XtomConfig;
import xtom.frame.util.Md5Util;
import xtom.frame.util.XtomSharedPreferencesUtil;

/**
 * Created by lenovo on 2017/1/3.
 * 修改登录密码
 */
public class ModifyPWDActivity extends JhActivity {
    private ImageButton back_button;
    private TextView title_text;
    private ImageButton next_button;
    private EditText pwd_old;//原密码
    private EditText pwd_new;//新密码
    private EditText pwd_new_yes;//确认密码
    private TextView up_text;//提交
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_modify_pwd);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case PASSWORD_SAVE:
                showProgressDialog("修改密码");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case PASSWORD_SAVE:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case PASSWORD_SAVE:
                showTextDialog("修改密码成功");
                XtomSharedPreferencesUtil.save(mContext, "password", hemaNetTask.getParams().get("new_password"));
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
            case PASSWORD_SAVE:
                showTextDialog(hemaBaseResult.getMsg());

                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case PASSWORD_SAVE:
                showTextDialog("修改密码失败，请稍后重试");

                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (ImageButton) findViewById(R.id.next_button);
        pwd_old = (EditText) findViewById(R.id.pwd_old);
        pwd_new = (EditText) findViewById(R.id.pwd_new);
        pwd_new_yes = (EditText) findViewById(R.id.pwd_new_yes);
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
        title_text.setText("修改登录密码");
        next_button.setVisibility(View.INVISIBLE);
        //提交
        up_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPwd = pwd_old.getText().toString().trim();
                String newPwd = pwd_new.getText().toString().trim();
                String newPwdYes = pwd_new_yes.getText().toString().trim();
                if (isNull(oldPwd))
                {
                    showTextDialog("请填写原密码");
                    return;
                }
                if (isNull(newPwd))
                {
                    showTextDialog("请填写新密码");
                    return;
                }
                if (isNull(newPwdYes))
                {
                    showTextDialog("请填写确认密码");
                    return;
                }
                if (!newPwd.equals(newPwdYes))
                {
                    showTextDialog("两次输入密码不一致，请重新输入");
                    return;
                }
                if (newPwd.trim().length() >= 6 && newPwd.trim().length() <= 12) {

                } else {
                    showTextDialog("密码输入不正确\n请输入6-12位密码");
                    return;
                }
                String token = JhctmApplication.getInstance().getUser().getToken();
                getNetWorker().passwordSave(token,"1", Md5Util.getMd5(XtomConfig.DATAKEY
                        + Md5Util.getMd5(oldPwd)),Md5Util.getMd5(XtomConfig.DATAKEY
                        + Md5Util.getMd5(newPwd)));
            }
        });
    }

}
