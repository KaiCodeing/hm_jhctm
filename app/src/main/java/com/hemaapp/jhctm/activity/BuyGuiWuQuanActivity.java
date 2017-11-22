package com.hemaapp.jhctm.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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

/**
 * Created by lenovo on 2017/1/3.
 * 购买购物券积分
 */
public class BuyGuiWuQuanActivity extends JhActivity {
    private ImageButton back_button;
    private TextView title_text;
    private ImageButton next_button;
    private TextView price_number;//兑换积分比例
    private EditText input_money;//输入兑换钱数
    private TextView money_bl;//比例钱数
    private TextView yue_number;//账户余额
    private TextView user_number;//账户积分
    private TextView duihuan_number;//兑换券积分
    private ImageView yue_img;//余额支付选择
    private ImageView user_pay_img;//账户积分选择
    private ImageView duihuan_pay_img;//兑换券积分选择
    private ImageView weixin_img;//微信支付选择
    private ImageView zhifubao_img;//支付宝选择
    private ImageView zhaohang_img;//招商银行选择
    private TextView up_text;//确认支付
    private Account account;
    private String payType = "0";
    IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
    private ReceiveBroadCast receiveBroadCast; // 广播实例
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_buy_gouwuquan);
        super.onCreate(savedInstanceState);
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
    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case FEEACCOUNT_REMOVE:
                showProgressDialog("支付购买中");
                break;
            case ALIPAY:
            case UNIONPAY:
            case WXPAYMENT:
                showProgressDialog("正在支付...");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case FEEACCOUNT_REMOVE:
            case ALIPAY:
            case UNIONPAY:
            case WXPAYMENT:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case FEEACCOUNT_REMOVE:
                showTextDialog("支付成功");
                next_button.postDelayed(new Runnable() {
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

    private void goWeixin(WeixinTrade trade) {
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

    private class AlipayThread extends Thread {
        String orderInfo;
        AlipayHandler alipayHandler;

        public AlipayThread(String orderInfo) {
            this.orderInfo = orderInfo;
            alipayHandler = new AlipayHandler(BuyGuiWuQuanActivity.this);
        }

        @Override
        public void run() {
            PayTask alipay = new PayTask(BuyGuiWuQuanActivity.this);
            // 调用支付接口，获取支付结果
            String result = alipay.pay(orderInfo);

            Message msg = new Message();
            msg.obj = result;
            alipayHandler.sendMessage(msg);
        }
    }

    private static class AlipayHandler extends Handler {
        BuyGuiWuQuanActivity activity;
        String order_id;

        public AlipayHandler(BuyGuiWuQuanActivity activity) {
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
        price_number = (TextView) findViewById(R.id.price_number);
        yue_number = (TextView) findViewById(R.id.yue_number);
        user_number = (TextView) findViewById(R.id.user_number);
        duihuan_number = (TextView) findViewById(R.id.duihuan_number);
        yue_img = (ImageView) findViewById(R.id.yue_img);
        duihuan_pay_img = (ImageView) findViewById(R.id.duihuan_pay_img);
        weixin_img = (ImageView) findViewById(R.id.weixin_img);
        zhifubao_img = (ImageView) findViewById(R.id.zhifubao_img);
        zhaohang_img = (ImageView) findViewById(R.id.zhaohang_img);
        up_text = (TextView) findViewById(R.id.up_text);
        input_money = (EditText) findViewById(R.id.input_money);
        money_bl = (TextView) findViewById(R.id.money_bl);
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
                                       }
        );
        title_text.setText("购买金积分");
        next_button.setVisibility(View.INVISIBLE);
        price_number.setVisibility(View.GONE);
        price_number.setText("每金积分" + account.getGold_price() + "元人民币");
        //余额
        yue_number.setText("¥" + account.getFeeaccount());
        //改变数据
        input_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isNull(input_money.getText().toString().trim())) {
                    money_bl.setText("0");
                } else {
                    int num = Integer.valueOf(input_money.getText().toString().trim());
                    money_bl.setText(String.valueOf(num * Integer.valueOf(account.getGold_price())));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //余额支付
        yue_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payType = "0";
                yue_img.setBackgroundResource(R.mipmap.fapiao_check_on);
                weixin_img.setBackgroundResource(R.mipmap.fapiao_check_off);
                zhifubao_img.setBackgroundResource(R.mipmap.fapiao_check_off);
                zhaohang_img.setBackgroundResource(R.mipmap.fapiao_check_off);
            }
        });
        //微信支付
        weixin_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payType = "1";
                yue_img.setBackgroundResource(R.mipmap.fapiao_check_off);
                weixin_img.setBackgroundResource(R.mipmap.fapiao_check_on);
                zhifubao_img.setBackgroundResource(R.mipmap.fapiao_check_off);
                zhaohang_img.setBackgroundResource(R.mipmap.fapiao_check_off);
            }
        });
        //支付宝支付
        zhifubao_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payType = "2";
                yue_img.setBackgroundResource(R.mipmap.fapiao_check_off);
                weixin_img.setBackgroundResource(R.mipmap.fapiao_check_off);
                zhifubao_img.setBackgroundResource(R.mipmap.fapiao_check_on);
                zhaohang_img.setBackgroundResource(R.mipmap.fapiao_check_off);
            }
        });
        //招商银行
        zhaohang_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payType = "3";
                yue_img.setBackgroundResource(R.mipmap.fapiao_check_off);
                weixin_img.setBackgroundResource(R.mipmap.fapiao_check_off);
                zhifubao_img.setBackgroundResource(R.mipmap.fapiao_check_off);
                zhaohang_img.setBackgroundResource(R.mipmap.fapiao_check_on);
            }
        });
        //确定支付
        up_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = JhctmApplication.getInstance().getUser().getToken();
                String num = input_money.getText().toString().trim();
                String money = money_bl.getText().toString().trim();
                if (isNull(num)) {
                    showTextDialog("请填写购买兑换券积分数量");
                    return;
                }
                if (Integer.valueOf(num) <= 0) {
                    showTextDialog("数量必须大于零!");
                    return;
                }
                if ("0".equals(payType)) {
                    getNetWorker().feeaccountRemove(token, "5", num, "", money, "0", "0");
                }
                //微信
                else if ("1".equals(payType)) {
                    getNetWorker().weixinpay(token, "5", num, money, "0", "0");
                }
                //支付宝
                else if ("2".equals(payType)) {
                    getNetWorker().alipay(token, "5", num, money, "0", "0");
                }
                //银联
                else if ("3".equals(payType)) {
                    getNetWorker().unionpay(token, "5", num, money, "0", "0");
                }
            }
        });
    }
}
