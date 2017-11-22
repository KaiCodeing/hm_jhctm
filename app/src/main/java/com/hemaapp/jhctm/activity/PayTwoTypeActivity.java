package com.hemaapp.jhctm.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.jhctm.BaseConfig;
import com.hemaapp.jhctm.JhActivity;
import com.hemaapp.jhctm.JhHttpInformation;
import com.hemaapp.jhctm.JhctmApplication;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.alipay.PayResult;
import com.hemaapp.jhctm.config.JhConfig;
import com.hemaapp.jhctm.model.Account;
import com.hemaapp.jhctm.model.AlipayTrade;
import com.hemaapp.jhctm.model.UnionTrade;
import com.hemaapp.jhctm.model.WeixinTrade;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;

import xtom.frame.util.XtomSharedPreferencesUtil;

/**
 * Created by lenovo on 2017/2/17.
 * 两种支付方式
 */
public class PayTwoTypeActivity extends JhActivity {
    private ImageButton back_button;
    private TextView title_text;
    private ImageButton next_button;
    private TextView input_num;//需要支付的钱
    private TextView yue_number1;//账户余额
    private CheckBox yue_img1;//选择余额
    private TextView duihuan_number1;//兑换币数量
    private CheckBox duihuan_img1;//选择兑换币
    private TextView user_number1;//账户积分
    private CheckBox userimg1;
    private TextView quan_number1;//兑换券积分
    private CheckBox quan_img1;//选择兑换券积分
    private CheckBox weixin_img;//微信支付选择
    private CheckBox zhifubao_img;//支付宝选择
    private CheckBox zhaohang_img;//招商银行选择
    private TextView up_text;//
    private String type = "2";//2：购买商品;3:购买平台积分卡;4:购买用户的积分卡;5:购买平台兑换券金积分;6:合成卡;7:购买用户兑换券金积分
    private String total_fee = "0";//现金总额
    private String total_score = "0";//积分总额
    private String total_share_score = "0";//兑换币总额
    private String billId = "0";
    private LinearLayout layout_duihuan;
    private LinearLayout layout_user;
    private LinearLayout layout_quan;
    private String billType = "1";//1 易物兑，2 分享
    private CheckBox slectPay;
    private String payType = "1";
    IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
    private ReceiveBroadCast receiveBroadCast; // 广播实例
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_pay_car_or_share);
        super.onCreate(savedInstanceState);
        inIt();
        msgApi.registerApp(JhConfig.APPID_WEIXIN);
        registerMyBroadcast();
    }
    @Override
    protected void onDestroy() {
        unregisterMyBroadcast();
        super.onDestroy();
    }

    private void registerMyBroadcast() {
        // 注册广播接收
        receiveBroadCast = new ReceiveBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.addAction("hemaapp.dtyw.buy.congzhi.infor"); // 只有持有相同的action的接受者才能接收此广播

        registerReceiver(receiveBroadCast, filter);
    }

    public class ReceiveBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if ("hemaapp.dtyw.buy.congzhi.infor".equals(intent.getAction())) {
                int err = intent.getIntExtra("res",-1);
                //成功
                if (0==err)
                {
                    showTextDialog("支付成功");
                    next_button.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    },1000);
                }
                else if(-1==err)
                {
                    showTextDialog("支付错误");

                }
                else
                {
                    showTextDialog("取消支付");

                }

            }
        }
    }


    private void unregisterMyBroadcast() {
        unregisterReceiver(receiveBroadCast);
    }
    private void inIt() {
        String token = JhctmApplication.getInstance().getUser().getToken();
        getNetWorker().accountGet(token);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ACCOUNT_GET:
                showProgressDialog("请稍后...");
                break;
            case FEEACCOUNT_REMOVE:
            case ALIPAY:
            case UNIONPAY:
            case WXPAYMENT:
            case GOLD_SCORE_REMOVE:
                showProgressDialog("正在支付...");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ACCOUNT_GET:
            case FEEACCOUNT_REMOVE:
            case ALIPAY:
            case UNIONPAY:
            case WXPAYMENT:
            case GOLD_SCORE_REMOVE:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ACCOUNT_GET:
                HemaArrayResult<Account> uResult = (HemaArrayResult<Account>) hemaBaseResult;
                Account account = uResult.getObjects().get(0);
//                yue_number.setText("(余额￥"+ BaseUtil.get2double(Double.parseDouble(account.getFeeaccount()))+")");
//                user_number.setText(account.getScore());
//                duihuan_number.setText(account.getGold_score());
                yue_number1.setText("¥" + account.getFeeaccount());
                duihuan_number1.setText(account.getShare_score());
                user_number1.setText(account.getScore());
                quan_number1.setText(String.valueOf((Integer.valueOf(account.getGold_score())-Integer.valueOf(account.getFreeze_gold_score()))<0?"0":String.valueOf(Integer.valueOf(account.getGold_score())-Integer.valueOf(account.getFreeze_gold_score()))));
                break;
            case FEEACCOUNT_REMOVE:
            case GOLD_SCORE_REMOVE:
                showTextDialog("支付成功");
                title_text.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1000);
                break;
            case ALIPAY:
                HemaArrayResult<AlipayTrade> aResult = (HemaArrayResult<AlipayTrade>) hemaBaseResult;
                AlipayTrade trade = aResult.getObjects().get(0);
                String orderInfo = trade.getAlipaysign();
                new AlipayThread(orderInfo).start();
                break;
            case UNIONPAY:
                HemaArrayResult<UnionTrade> uuResult = (HemaArrayResult<UnionTrade>) hemaBaseResult;
                UnionTrade uTrade = uuResult.getObjects().get(0);
                String uInfo = uTrade.getTn();
                UPPayAssistEx.startPayByJAR(mContext, PayActivity.class, null,
                        null, uInfo, BaseConfig.UNIONPAY_TESTMODE);
                break;
            case WXPAYMENT:
                HemaArrayResult<WeixinTrade> wResult = (HemaArrayResult<WeixinTrade>) hemaBaseResult;
                WeixinTrade wTrade = wResult.getObjects().get(0);
                goWeixin(wTrade);
                break;
        }
    }

    private void goWeixin(WeixinTrade trade) {
        XtomSharedPreferencesUtil.save(mContext, "order_id", billId);
        msgApi.registerApp(BaseConfig.APPID_WEIXIN);
        PayReq request = new PayReq();
        request.appId = trade.getAppid();
        request.partnerId = trade.getPartnerid();
        request.prepayId = trade.getPrepayid();
        request.packageValue = trade.getPackageValue();
        request.nonceStr = trade.getNoncestr();
        request.timeStamp = trade.getTimestamp();
        request.sign = trade.getSign();
        msgApi.sendReq(request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*************************************************
         * 处理银联手机支付控件返回的支付结果
         ************************************************/
        if (data == null) {
            return;
        }

        String msg = "";
        /*
		 * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
		 */
        String str = data.getExtras().getString("pay_result");
        if (str.equalsIgnoreCase("success")) {
            msg = "支付成功！";
//            if("1".equals(flag)){
//                setResult(RESULT_OK, mIntent);
//                finish();
//            }else{
//                Intent it = new Intent(mContext, OrderDetailInforActivity.class);
//                it.putExtra("id", id);
//                startActivity(it);
//                finish();
//            }
            showTextDialog(msg);
            next_button.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 1000);
        } else if (str.equalsIgnoreCase("fail")) {
            msg = "支付失败！";
            showTextDialog(msg);

        } else if (str.equalsIgnoreCase("cancel")) {
            msg = "您取消了支付";
            showTextDialog(msg);

        }

    }

    private class AlipayThread extends Thread {
        String orderInfo;
        AlipayHandler alipayHandler;

        public AlipayThread(String orderInfo) {
            this.orderInfo = orderInfo;
            alipayHandler = new AlipayHandler(PayTwoTypeActivity.this, billId);
        }

        @Override
        public void run() {
            PayTask alipay = new PayTask(PayTwoTypeActivity.this);
            // 调用支付接口，获取支付结果
            String result = alipay.pay(orderInfo);

            Message msg = new Message();
            msg.obj = result;
            alipayHandler.sendMessage(msg);
        }
    }

    private static class AlipayHandler extends Handler {
        PayTwoTypeActivity activity;
        String order_id;

        public AlipayHandler(PayTwoTypeActivity activity,
                             String order_id) {
            this.activity = activity;
            this.order_id = order_id;
        }

        public void handleMessage(Message msg) {
            if (msg == null) {
                activity.showTextDialog("支付失败");
                return;
            }
            PayResult result = new PayResult((String) msg.obj);
            String staus = result.getResultStatus();
            switch (staus) {
                case "9000":
                    activity.showTextDialog("恭喜\n支付成功");

                    postAtTime(new Runnable() {

                        @Override
                        public void run() {
                            activity.finish();
                        }
                    }, 1500);
                    break;
                case "8000":
                    activity.showTextDialog("支付结果确认中");
                    break;
                default:
                    activity.showTextDialog("您取消了支付");
                    break;
            }
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_GET:
            case FEEACCOUNT_REMOVE:
            case ALIPAY:
            case UNIONPAY:
            case WXPAYMENT:
            case GOLD_SCORE_REMOVE:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {

        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_GET:
                showTextDialog("抱歉，获取数据失败");
                break;
            case FEEACCOUNT_REMOVE:
            case ALIPAY:
            case UNIONPAY:
            case WXPAYMENT:
            case GOLD_SCORE_REMOVE:
                showTextDialog("抱歉，支付失败");
                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (ImageButton) findViewById(R.id.next_button);
        input_num = (TextView) findViewById(R.id.input_num);
        yue_number1 = (TextView) findViewById(R.id.yue_number1);
        yue_img1 = (CheckBox) findViewById(R.id.yue_img1);
        duihuan_number1 = (TextView) findViewById(R.id.duihuan_number1);
        duihuan_img1 = (CheckBox) findViewById(R.id.duihuan_img1);
        user_number1 = (TextView) findViewById(R.id.user_number1);
        userimg1 = (CheckBox) findViewById(R.id.userimg1);
        quan_number1 = (TextView) findViewById(R.id.quan_number1);
        quan_img1 = (CheckBox) findViewById(R.id.quan_img1);
        weixin_img = (CheckBox) findViewById(R.id.weixin_img);
        zhifubao_img = (CheckBox) findViewById(R.id.zhifubao_img);
        zhaohang_img = (CheckBox) findViewById(R.id.zhaohang_img);
        up_text = (TextView) findViewById(R.id.up_text);
        layout_duihuan = (LinearLayout) findViewById(R.id.layout_duihuan);
        layout_user = (LinearLayout) findViewById(R.id.layout_user);
        layout_quan = (LinearLayout) findViewById(R.id.layout_quan);
        duihuan_img1.setEnabled(false);
    }

    @Override
    protected void getExras() {
        type = mIntent.getStringExtra("type");
        total_fee = mIntent.getStringExtra("total_fee");
        if (isNull(total_fee))
            total_fee = "0";
        total_score = mIntent.getStringExtra("total_score");
        if (isNull(total_score))
            total_score = "0";
        total_share_score = mIntent.getStringExtra("total_share_score");
        if (isNull(total_share_score))
            total_share_score = "0";
        billId = mIntent.getStringExtra("billId");
        if (isNull(billId))
            billId = "0";
        billType = mIntent.getStringExtra("billType");
        log_i("total_fee:" + total_fee + "  total_score:" + total_score + "  total_share_score:" + total_share_score);
    }

    @Override
    protected void setListener() {
        title_text.setText("支付");
        next_button.setVisibility(View.INVISIBLE);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        slectPay = yue_img1;
        slectPay.setChecked(true);
        //判断显示
        if ("2".equals(type)) {
            if ("1".equals(billType)) {
                layout_duihuan.setVisibility(View.GONE);
                input_num.setText("需支付积分:" + total_fee);
            } else {
                layout_user.setVisibility(View.GONE);
                layout_quan.setVisibility(View.GONE);
                duihuan_img1.setChecked(true);
                input_num.setText("需支付: ¥" + total_fee + "兑换币" + total_share_score);
            }
        } else if ("4".equals(type) || "7".equals(type) || "6".equals(type)) {
            layout_user.setVisibility(View.GONE);
            layout_quan.setVisibility(View.GONE);
            layout_duihuan.setVisibility(View.GONE);
            input_num.setText("支付金额: ¥" + total_fee);
        } else {
            layout_user.setVisibility(View.GONE);
            layout_quan.setVisibility(View.GONE);
            layout_duihuan.setVisibility(View.GONE);
            input_num.setText("支付金额: ¥" + total_score);
        }
        ///余额
        yue_img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yue_img1.isChecked()) {
                    slectPay.setChecked(false);
                    slectPay = yue_img1;
                    slectPay.setChecked(true);

                } else {
                    slectPay.setChecked(true);
                    slectPay = yue_img1;
                    slectPay.setChecked(true);
                }
                payType = "1";
            }
        });
        //userimg1账户积分
        userimg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userimg1.isChecked()) {
                    slectPay.setChecked(false);
                    slectPay = userimg1;
                    slectPay.setChecked(true);

                } else {
                    slectPay.setChecked(true);
                    slectPay = userimg1;
                    slectPay.setChecked(true);
                }
                payType = "2";
            }
        });
        //quan_img1兑换券积分
        quan_img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quan_img1.isChecked()) {
                    slectPay.setChecked(false);
                    slectPay = quan_img1;
                    slectPay.setChecked(true);

                } else {
                    slectPay.setChecked(true);
                    slectPay = quan_img1;
                    slectPay.setChecked(true);
                }
                payType = "3";
            }
        });
        //weixin_img微信支付
        weixin_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (weixin_img.isChecked()) {
                    slectPay.setChecked(false);
                    slectPay = weixin_img;
                    slectPay.setChecked(true);

                } else {
                    slectPay.setChecked(true);
                    slectPay = weixin_img;
                    slectPay.setChecked(true);
                }
                payType = "4";
            }
        });
        //支付宝
        zhifubao_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (zhifubao_img.isChecked()) {
                    slectPay.setChecked(false);
                    slectPay = zhifubao_img;
                    slectPay.setChecked(true);

                } else {
                    slectPay.setChecked(true);
                    slectPay = zhifubao_img;
                    slectPay.setChecked(true);
                }
                payType = "5";
            }
        });
        //招行
        zhaohang_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (zhaohang_img.isChecked()) {
                    slectPay.setChecked(false);
                    slectPay = zhaohang_img;
                    slectPay.setChecked(true);
                } else {
                    slectPay.setChecked(true);
                    slectPay = zhaohang_img;
                    slectPay.setChecked(true);
                }
                payType = "6";
            }
        });
        //提交
        up_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = JhctmApplication.getInstance().getUser().getToken();
                switch (payType) {
                    case "1":
                        getNetWorker().feeaccount_remove(token, type, billId, total_fee, total_score, total_share_score);
                        break;
                    case "2":
                        getNetWorker().feeaccount_remove(token, type, billId, "0", total_fee, "0");
                        break;
                    case "3":
                        getNetWorker().gold_score_remove(token,"1",billId,total_fee);
                        break;
                    case "4":
                        getNetWorker().weixinpay(token, type, billId, total_fee, total_score, total_share_score);
                        break;
                    case "5":
                        getNetWorker().alipay(token, type, billId, total_fee, total_score, total_share_score);
                        break;
                    case "6":
                        getNetWorker().unionpay(token, type, billId, total_fee, total_score, total_share_score);
                        break;
                }
            }
        });
    }
}
