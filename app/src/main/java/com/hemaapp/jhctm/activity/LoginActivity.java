package com.hemaapp.jhctm.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.HemaUtil;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.jhctm.JhActivity;
import com.hemaapp.jhctm.JhHttpInformation;
import com.hemaapp.jhctm.JhNetWorker;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.model.User;

import xtom.frame.XtomActivityManager;
import xtom.frame.XtomConfig;
import xtom.frame.util.Md5Util;
import xtom.frame.util.XtomSharedPreferencesUtil;

/**
 * Created by lenovo on 2016/12/16.
 * 登录界面
 */
public class LoginActivity extends JhActivity {
    private ImageButton back_button;//关闭
    private TextView add_user;//注册
    private TextView login_user;//登录
    private LinearLayout login_layout;//登录的layout
    private FrameLayout back_layout;//背景
    private EditText username_text;//登录的输入手机号
    private EditText password_text;//登录的输入密码
    private TextView find_password;//忘记密码
    private TextView login_text;//登录
    private LinearLayout add_user_layout;//注册
    private EditText username_text_add;//注册输入手机号码
    private EditText yanzheng_text;//验证码
    private Button send_button;//获取验证码
    private LinearLayout agin_layout;//重新获取
    private TextView second;//秒
    private EditText pwd_text_add;//输入密码注册
    private EditText pwd_text_add_yes;//注册确认密码
    private EditText share_tel;//分享人电话
    private TextView blong_supermarket;//归属超市
    private CheckBox check_zu;//是否同意
    private TextView xieyi_text;//注册协议
    private TextView up_text;//提交
    private LinearLayout select_supermarket;//选择超市
    private TextView phone_show;
    private ImageView xian;

    private String type = "0";//0登录，1 注册
    private ImageView jiao2;
    private ImageView jiao1;
    private ViewHolder holder;
    private TextView supter_id;
    private TimeThread timeThread;
    private String username;

    //pop pop_show_toast
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        super.onCreate(savedInstanceState);
        //注册广播
        registerBoradcastReceiver();
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {

        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_LOGIN:

                showProgressDialog("正在验证登录信息");
                break;
            case CLIENT_VERIFY:
                showProgressDialog("正在验证手机号");
                break;
            case CODE_GET:
                showProgressDialog("正在发送验证码");
                break;
            case CODE_VERIFY:
                showProgressDialog("正在验证验证码");
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK != resultCode) {
            return;
        }
        switch (requestCode) {
            case 1:
                String mallName = data.getStringExtra("mallName");
                String mallId = data.getStringExtra("mallId");
                supter_id.setText(mallId);
                blong_supermarket.setText(mallName);
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_LOGIN:
            case CLIENT_VERIFY:
            case CODE_GET:
            case CODE_VERIFY:
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
            case CLIENT_LOGIN:
                HemaArrayResult<User> userArrayResult = (HemaArrayResult<User>) hemaBaseResult;
                User user = userArrayResult.getObjects().get(0);

                getApplicationContext().setUser(user);
                String username = hemaNetTask.getParams().get("username");
                String password = hemaNetTask.getParams().get("password");
                XtomSharedPreferencesUtil.save(mContext, "username", username);
                XtomSharedPreferencesUtil.save(mContext, "password", password);
                XtomActivityManager.finishAll();
                String token = user.getToken();
                GotoMain();
                break;
            case CLIENT_VERIFY:
                showTextDialog("手机号已被注册");
                break;
            case CODE_GET:
                this.username = hemaNetTask.getParams().get("username");
                phone_show.setText("验证码已发送到 "
                        + HemaUtil.hide(hemaNetTask.getParams().get("username"), "1"));
                phone_show.setVisibility(View.VISIBLE);
                xian.setVisibility(View.VISIBLE);
                timeThread = new TimeThread(new TimeHandler(this));
                timeThread.start();
                break;
            case CODE_VERIFY:
                HemaArrayResult<String> result = (HemaArrayResult<String>) hemaBaseResult;
                String temp_token = result.getObjects().get(0);
                log_i("接口返回的token++" + temp_token);

                Intent intent = new Intent(LoginActivity.this,
                        RegisterActivity.class);
                String userName = username_text_add.getText().toString();

                if (isNull(this.username)) {
                    showTextDialog("请填写手机号");
                    return;
                }
                if (this.username.equals(userName)) {
                    intent.putExtra("type", type);
                    intent.putExtra("username", userName);
                    intent.putExtra("password", pwd_text_add.getText().toString());
                    intent.putExtra("tempToken", temp_token);
                    intent.putExtra("supid", supter_id.getText().toString());
                    intent.putExtra("share_tel", share_tel.getText().toString().trim());
                    startActivity(intent);
                } else {
                    showTextDialog("两次输入手机号码不一致，\n请确认");
                    return;
                }

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
        LoginActivity activity;

        public TimeHandler(LoginActivity activity) {
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

    /**
     * @方法名称: GotoMain
     * @功能描述: TODO跳转到主界面
     * @返回值: void
     */
    private void GotoMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_LOGIN:
                HemaArrayResult<User> result = (HemaArrayResult<User>) hemaBaseResult;
                log_i(result.getError_code() + "Error_code");
                if ("102".equals(result.getError_code() + "")) {
                    showDialog("账号或密码错误，请重新填写");
                } else if ("106".equals(hemaBaseResult.getError_code() + "")) {
                    showDialog("该手机号尚未注册，请先注册");
                } else {
                    showDialog(result.getMsg());
                }
                break;
            case CLIENT_VERIFY:
                String username = hemaNetTask.getParams().get("username");
                getNetWorker().codeGet(username);
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
            default:
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_LOGIN:
                showTextDialog("登录失败，请稍后登录");
                break;
            case CLIENT_VERIFY:
                showTextDialog("验证手机号失败");
                break;
            case CODE_GET:
                showTextDialog("发送验证码失败");
                break;
            case CODE_VERIFY:
                showTextDialog("校验验证码失败");
                break;
            default:
                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        add_user = (TextView) findViewById(R.id.add_user);
        login_user = (TextView) findViewById(R.id.login_user);
        login_layout = (LinearLayout) findViewById(R.id.login_layout);
        username_text = (EditText) findViewById(R.id.username_text);
        password_text = (EditText) findViewById(R.id.password_text);
        find_password = (TextView) findViewById(R.id.find_password);
        add_user_layout = (LinearLayout) findViewById(R.id.add_user_layout);
        username_text_add = (EditText) findViewById(R.id.username_text_add);
        yanzheng_text = (EditText) findViewById(R.id.yanzheng_text);
        agin_layout = (LinearLayout) findViewById(R.id.agin_layout);
        second = (TextView) findViewById(R.id.second);
        pwd_text_add = (EditText) findViewById(R.id.pwd_text_add);
        pwd_text_add_yes = (EditText) findViewById(R.id.pwd_text_add_yes);
        share_tel = (EditText) findViewById(R.id.share_tel);
        blong_supermarket = (TextView) findViewById(R.id.blong_supermarket);
        login_text = (TextView) findViewById(R.id.login_text);
        check_zu = (CheckBox) findViewById(R.id.check_zu);
        xieyi_text = (TextView) findViewById(R.id.xieyi_text);
        up_text = (TextView) findViewById(R.id.up_text);
        select_supermarket = (LinearLayout) findViewById(R.id.select_supermarket);
        phone_show = (TextView) findViewById(R.id.phone_show);
        xian = (ImageView) findViewById(R.id.xian);
        jiao1 = (ImageView) findViewById(R.id.jiao1);
        jiao2 = (ImageView) findViewById(R.id.jiao2);
        supter_id = (TextView) findViewById(R.id.supter_id);
        send_button = (Button) findViewById(R.id.send_button);
    }

    @Override
    protected void getExras() {
    }

    @Override
    protected void setListener() {
        //关闭
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XtomActivityManager.finishAll();
            }
        });
        //登录
        login_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "0";
                jiao1.setVisibility(View.GONE);
                jiao2.setVisibility(View.VISIBLE);
                login_layout.setVisibility(View.VISIBLE);
                add_user_layout.setVisibility(View.GONE);
            }
        });
        //注册
        add_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "1";
                jiao1.setVisibility(View.VISIBLE);
                jiao2.setVisibility(View.GONE);
                login_layout.setVisibility(View.GONE);
                add_user_layout.setVisibility(View.VISIBLE);
            }
        });
        /**
         * 电话号码变化
         */
        username_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isNull(username_text.getText().toString())) {
                    login_text.setEnabled(false);
                    login_text.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_bg_off));
                } else {
                    login_text.setEnabled(true);
                    login_text.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_bg_on));

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        /**
         * 注册的手机号登录
         */
        username_text_add.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isNull(username_text_add.getText().toString())) {
                    up_text.setEnabled(false);
                    up_text.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_bg_off));
                } else {
                    up_text.setEnabled(true);
                    up_text.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_bg_on));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //登录
        login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = username_text.getText().toString();
                String password = password_text.getText().toString();
                if (isNull(username) || username.equals("")) {
                    showTextDialog("请填写手机号");
                    return;
                }
                String mobile = "\\d{11}";// 只判断11位
                if (!username.matches(mobile)) {
                    showTextDialog("手机格式不正确，请重新输入");
                    return;
                }
                if (isNull(password) || password.equals("")) {
                    showTextDialog("请填写登录密码");
                    return;
                }
                if (password.trim().length() >= 6 && password.trim().length() <= 12) {

                } else {
                    showTextDialog("密码输入不正确\n请输入6-12位密码");
                    return;
                }
                JhNetWorker netWorker = getNetWorker();
                netWorker.clientLogin(username, Md5Util.getMd5(XtomConfig.DATAKEY
                        + Md5Util.getMd5(password)));
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
                String shareNum = share_tel.getText().toString().trim();//分享手机号
                String supId = supter_id.getText().toString();
                boolean check = check_zu.isChecked();
                if (isNull(username)) {
                    showTextDialog("请填写手机号！");
                    return;
                }
                if (isNull(verfication)) {
                    showTextDialog("请填写验证码！");
                    return;
                }
                if (isNull(pwd)) {
                    showTextDialog("请填写密码！");
                    return;
                }
                if (pwd.trim().length() >= 6 && pwd.trim().length() <= 12) {

                } else {
                    showTextDialog("密码输入不正确\n请输入6-12位密码");
                    return;
                }
                if (!pwd.equals(pwdCon)) {
                    showTextDialog("密码不一致，请重新输入");
                    return;
                }
                if (isNull(supId)) {
                    showTextDialog("请选择归属超市");
                    return;
                }
                if (!check) {
                    showTextDialog("请同意注册协议");
                    return;
                }
                // String mobile = "^[1][3-8]+\\d{9}";
                String mobile = "\\d{11}";// 只判断11位
                if (!isNull(shareNum) && !shareNum.matches(mobile)) {
                    showTextDialog("您输入的手机号不正确");
                    return;
                }
                getNetWorker().codeVerify(username,
                        verfication);
            }
        });
        //选择归属超市
        blong_supermarket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SelectSupermarketActivity.class);
                startActivityForResult(intent, 1);
            }
        });
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
        //注册协议
        xieyi_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, JhWebView.class);
                intent.putExtra("type", "1");
                startActivity(intent);
            }
        });
        /**
         * 忘记密码
         */
        find_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, FindPwdActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 提示
     */
    private class ViewHolder {
        TextView content_text;
        TextView content_yes;
    }

    private void showDialog(String text) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.pop_show_toast, null);
        holder = new ViewHolder();
        holder.content_text = (TextView) view.findViewById(R.id.content_text);
        holder.content_yes = (TextView) view.findViewById(R.id.content_yes);
        holder.content_text.setText(text);
        final PopupWindow popupWindow = new PopupWindow(view,
                RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT);

        holder.content_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setWidth(RadioGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(RadioGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new
                BitmapDrawable()
        );
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // popupWindow.showAsDropDown(findViewById(R.id.ll_item));
        popupWindow.setAnimationStyle(R.style.PopupAnimation);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("com.hemaapp.jh.sup")){
                String mallName = intent.getStringExtra("mallName");
                String mallId = intent.getStringExtra("mallId");
                supter_id.setText(mallId);
                blong_supermarket.setText(mallName);
            }
        }

    };
    public void registerBoradcastReceiver(){
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("com.hemaapp.jh.sup");
        //注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }
}
