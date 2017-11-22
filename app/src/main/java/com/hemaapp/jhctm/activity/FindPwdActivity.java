package com.hemaapp.jhctm.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.HemaUtil;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.jhctm.JhActivity;
import com.hemaapp.jhctm.JhHttpInformation;
import com.hemaapp.jhctm.R;

import xtom.frame.XtomConfig;
import xtom.frame.util.Md5Util;

/**
 * Created by lenovo on 2016/12/20.
 * 找回密码
 */
public class FindPwdActivity extends JhActivity {
    private ImageButton back_button;
    private TextView title_text;
    private ImageButton next_button;
    private EditText username_text_add;//输入手机号
    private EditText yanzheng_text;//输入验证码
    private Button send_button;//获取验证码
    private TextView second;//秒
    private TextView phone_show;//显示电话号码
    private EditText pwd_text_add;//输入密码
    private EditText pwd_text_add_yes;//确认密码
    private TextView up_text;//提交
    private String username;
    private LinearLayout agin_layout;
    private TimeThread timeThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_pwd_find);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_VERIFY:
                showProgressDialog("正在验证手机号");
                break;
            case CODE_GET:
                showProgressDialog("正在发送验证码");
                break;
            case CODE_VERIFY:
                showProgressDialog("正在验证验证码");
                break;
            case PASSWORD_RESET:
                showProgressDialog("正在修改密码");
                break;
            default:
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_VERIFY:
            case CODE_GET:
            case CODE_VERIFY:
            case PASSWORD_RESET:
                cancelProgressDialog();
                break;

            default:
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_VERIFY:
                String username = hemaNetTask.getParams().get("username");
                getNetWorker().codeGet(username);
                break;
            case CODE_GET:
                this.username = hemaNetTask.getParams().get("username");
                phone_show.setText("验证码已发送到 "
                        + HemaUtil.hide(hemaNetTask.getParams().get("username"), "1"));
                phone_show.setVisibility(View.VISIBLE);
                timeThread = new TimeThread(new TimeHandler(this));
                timeThread.start();
                break;
            case CODE_VERIFY:
                HemaArrayResult<String> result = (HemaArrayResult<String>) hemaBaseResult;
                String temp_token = result.getObjects().get(0);
                log_i("接口返回的token++" + temp_token);
                String userName = username_text_add.getText().toString();
                if (isNull(this.username)) {
                    showTextDialog("请填写手机号");
                    return;
                }
                if (this.username.equals(userName)) {
                } else {
                    showTextDialog("两次输入手机号码不一致，\n请确认");
                    return;
                }
                getNetWorker().passwordReset(temp_token,"1", Md5Util.getMd5(XtomConfig.DATAKEY
                        + Md5Util.getMd5(pwd_text_add.getText().toString())));
                break;
            case PASSWORD_RESET:
                showTextDialog("密码修改成功");
                next_button.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        finish();
                    }
                }, 1000);
                break;
            default:

                break;
        }
    }
    private class TimeThread extends Thread {
        private int curr;

        private TimeHandler timeHandler;

        public TimeThread(TimeHandler timeHandler) {
            this.timeHandler = timeHandler;
        }

        void cancel() {
            curr = 0;
        }

        @Override
        public void run() {
            curr = 60;
            while (curr > 0) {
                timeHandler.sendEmptyMessage(curr);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // ignore
                }
                curr--;
            }
            timeHandler.sendEmptyMessage(-1);
        }
    }

    private static class TimeHandler extends Handler {
        FindPwdActivity activity;

        public TimeHandler(FindPwdActivity activity) {
            this.activity = activity;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -1:
                    activity.send_button.setText("重新发送");
                    activity.send_button.setVisibility(View.VISIBLE);
                    activity.agin_layout.setVisibility(View.GONE);
                    break;
                default:
                    activity.send_button.setVisibility(View.GONE);
                    activity.agin_layout.setVisibility(View.VISIBLE);
                    activity.second.setText("" + msg.what);
                    break;
            }
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_VERIFY:
                    showTextDialog("手机号未注册！");
                break;
            case CODE_GET:
                showTextDialog(hemaBaseResult.getMsg());
                break;
            case CODE_VERIFY:
                if (hemaBaseResult.getError_code() == 103) {
                    showTextDialog("输入的验证码不正确！");
                } else {
                    showTextDialog(hemaBaseResult.getMsg());
                }

                break;
            case PASSWORD_RESET:
                showTextDialog(hemaBaseResult.getMsg());
                break;
            default:
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_VERIFY:
                showTextDialog("验证手机号失败");
                break;
            case CODE_GET:
                showTextDialog("发送验证码失败");
                break;
            case CODE_VERIFY:
                showTextDialog("校验验证码失败");
                break;
            case PASSWORD_RESET:
                showTextDialog("修改密码失败，请稍后重试");
                break;
            default:
                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (ImageButton) findViewById(R.id.next_button);
        username_text_add = (EditText) findViewById(R.id.username_text_add);
        yanzheng_text = (EditText) findViewById(R.id.yanzheng_text);
        second = (TextView) findViewById(R.id.second);
        phone_show = (TextView) findViewById(R.id.phone_show);
        pwd_text_add = (EditText) findViewById(R.id.pwd_text_add);
        pwd_text_add_yes = (EditText) findViewById(R.id.pwd_text_add_yes);
        up_text = (TextView) findViewById(R.id.up_text);
        agin_layout = (LinearLayout) findViewById(R.id.agin_layout);
        send_button = (Button) findViewById(R.id.send_button);
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
        title_text.setText("找回密码");
        next_button.setVisibility(View.INVISIBLE);
        /**
         * 发送验证码
         */
        send_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String username = username_text_add.getText().toString();
                if (isNull(username)) {
                    showTextDialog("请输入手机号");
                    return;
                }

                // String mobile = "^[1][3-8]+\\d{9}";
                String mobile = "\\d{11}";// 只判断11位
                if (!username.matches(mobile)) {
                    showTextDialog("您输入的手机号不正确");
                    return;
                }
                getNetWorker().clientVerify(username);
            }
        });
        //注册
        up_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = username_text_add.getText().toString().trim();
                String verfication = yanzheng_text.getText().toString().trim();
                String pwd = pwd_text_add.getText().toString().trim();
                String pwdCon = pwd_text_add_yes.getText().toString().trim();

                if (isNull(username))
                {
                    showTextDialog("请填写手机号！");
                    return;
                }
                if (isNull(verfication))
                {
                    showTextDialog("请填写验证码！");
                    return;
                }
                if(isNull(pwd))
                {
                    showTextDialog("请填写密码！");
                    return;
                }
                if (pwd.trim().length() >= 6 && pwd.trim().length() <= 12) {

                } else {
                    showTextDialog("密码输入不正确\n请输入6-12位密码");
                    return;
                }
                if (!pwd.equals(pwdCon))
                {
                    showTextDialog("密码不一致，请重新输入");
                    return;
                }
                getNetWorker().codeVerify(username,
                        verfication);
            }
        });
    }
}
