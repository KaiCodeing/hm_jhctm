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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.hemaapp.jhctm.model.User;
import com.hemaapp.jhctm.model.WeixinTrade;
import com.hemaapp.jhctm.util.BaseUtil;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;

import xtom.frame.util.XtomSharedPreferencesUtil;

/**
 * Created by lenovo on 2016/12/29.
 * 支付买卡
 */
public class BuyCarPayActivity extends JhActivity {
    private ImageButton back_button;
    private TextView title_text;
    private ImageButton next_button;
    private TextView price_number;//需要支付的积分
    private TextView yue_number;//账户余额
    private TextView user_number;//账户积分
    private TextView duihuan_number;//兑换券积分
    private CheckBox yue_img,yue_img1;//余额支付选择
    private CheckBox user_pay_img;//账户积分选择
    private CheckBox duihuan_pay_img;//兑换券积分选择
    private CheckBox weixin_img;//微信支付选择
    private CheckBox zhifubao_img;//支付宝选择
    private CheckBox zhaohang_img;//招商银行选择
    private TextView up_text;//确认支付
    private CheckBox lastTap;
    private View inputLn,commonLn;
    private int Tap=1;
    private User user;
    private TextView showMoney;
    private int fromtype=0;//默认商品支付,1购买积分卡
    private EditText input_num;
    private String billId,money,total_fee,share_fee;

    IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
    private ReceiveBroadCast receiveBroadCast; // 广播实例
    private TextView yue_number1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_buy_car_pay);
        super.onCreate(savedInstanceState);
        user = JhctmApplication.getInstance().getUser();
        getNetWorker().accountGet(user.getToken());
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
        switch (information){
            case ACCOUNT_GET:
                showProgressDialog("请稍后...");
                break;
            case FEEACCOUNT_REMOVE:
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
        switch (information){
            case ACCOUNT_GET:
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
        switch (information){
            case ACCOUNT_GET:
                HemaArrayResult<Account> uResult = (HemaArrayResult<Account>) hemaBaseResult;
                Account account= uResult.getObjects().get(0);
                yue_number.setText("(余额￥"+ BaseUtil.get2double(Double.parseDouble(account.getFeeaccount()))+")");
                user_number.setText(account.getScore());
                duihuan_number.setText(account.getGold_score());
                yue_number1.setText("¥"+account.getFeeaccount());
                break;
            case FEEACCOUNT_REMOVE:
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
            },1000);
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
            alipayHandler = new AlipayHandler(BuyCarPayActivity.this,billId);
        }

        @Override
        public void run() {
            PayTask alipay = new PayTask(BuyCarPayActivity.this);
            // 调用支付接口，获取支付结果
            String result = alipay.pay(orderInfo);

            Message msg = new Message();
            msg.obj = result;
            alipayHandler.sendMessage(msg);
        }
    }

    private static class AlipayHandler extends Handler {
        BuyCarPayActivity activity;
        String order_id;

        public AlipayHandler(BuyCarPayActivity activity,
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
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information){
            case CLIENT_GET:
                showTextDialog("抱歉，获取数据失败");
                break;
            case FEEACCOUNT_REMOVE:
            case ALIPAY:
            case UNIONPAY:
            case WXPAYMENT:
                showTextDialog("抱歉，支付失败");
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask netTask, HemaBaseResult baseResult) {
        JhHttpInformation information = (JhHttpInformation) netTask.getHttpInformation();
        switch (information){
            case CLIENT_GET:
            case FEEACCOUNT_REMOVE:
            case ALIPAY:
            case UNIONPAY:
            case WXPAYMENT:
                showTextDialog(baseResult.getMsg());
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
        yue_img = (CheckBox) findViewById(R.id.yue_img);
        yue_img1= (CheckBox) findViewById(R.id.yue_img1);
        user_pay_img= (CheckBox) findViewById(R.id.user_pay_img);
        duihuan_pay_img = (CheckBox) findViewById(R.id.duihuan_pay_img);
        weixin_img = (CheckBox) findViewById(R.id.weixin_img);
        zhifubao_img = (CheckBox) findViewById(R.id.zhifubao_img);
        zhaohang_img = (CheckBox) findViewById(R.id.zhaohang_img);
        up_text = (TextView) findViewById(R.id.up_text);
        input_num= (EditText) findViewById(R.id.input_num);
        showMoney= (TextView) findViewById(R.id.showMoney);
        inputLn=findViewById(R.id.show_pay_input_ln);
        commonLn=findViewById(R.id.show_pay_ln);
        yue_number1 = (TextView) findViewById(R.id.yue_number1);
        if(fromtype==0)
        {
            inputLn.setVisibility(View.GONE);
            commonLn.setVisibility(View.VISIBLE);
            price_number.setText("需支付积分："+total_fee);
        }else
        {
            inputLn.setVisibility(View.VISIBLE);
            commonLn.setVisibility(View.GONE);
        }
        lastTap=yue_img;
        lastTap.setChecked(true);
    }

    @Override
    protected void getExras() {
        billId=getIntent().getStringExtra("billId");
        total_fee=getIntent().getStringExtra("total_fee");
        share_fee=getIntent().getStringExtra("share_fee");
        fromtype=getIntent().getIntExtra("fromtype",0);
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
        input_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!isNull(input_num.getText().toString())) {
                    total_fee=String.valueOf(Integer.parseInt(input_num.getText().toString()) * 100)+"";
                    showMoney.setText("￥" + total_fee);
                }
                else
                    showMoney.setText("￥0");
            }
        });
        title_text.setText("支付");
        next_button.setVisibility(View.INVISIBLE);
        lastTap=yue_img1;
        lastTap.setChecked(true);
        yue_img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(yue_img1.isChecked())
                {
                    lastTap.setChecked(false);
                    lastTap=yue_img1;
                    Tap=1;
                    lastTap.setChecked(true);
                }
            }
        });
        yue_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(yue_img.isChecked())
                {
                    lastTap.setChecked(false);
                    lastTap=yue_img;
                    Tap=1;
                    lastTap.setChecked(true);
                }
            }
        });
        user_pay_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user_pay_img.isChecked())
                {
                    lastTap.setChecked(false);
                    lastTap=user_pay_img;
                    Tap=2;
                    lastTap.setChecked(true);
                }
            }
        });
        duihuan_pay_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(duihuan_pay_img.isChecked())
                {
                    lastTap.setChecked(false);
                    lastTap=duihuan_pay_img;
                    Tap=3;
                    lastTap.setChecked(true);
                }
            }
        });
        weixin_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(weixin_img.isChecked())
                {
                    lastTap.setChecked(false);
                    lastTap=weixin_img;
                    Tap=4;
                    lastTap.setChecked(true);
                }
            }
        });
        zhifubao_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(zhifubao_img.isChecked())
                {
                    lastTap.setChecked(false);
                    lastTap=zhifubao_img;
                    Tap=5;
                    lastTap.setChecked(true);
                }
            }
        });
        zhaohang_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(zhaohang_img.isChecked())
                {
                    lastTap.setChecked(false);
                    lastTap=zhaohang_img;
                    Tap=6;
                    lastTap.setChecked(true);
                }
            }
        });
        up_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = JhctmApplication.getInstance().getUser().getToken();
                if(isNull(input_num.getText().toString()))
                {
                    showTextDialog("请输入购买数量");
                    return;
                }
                switch (Tap)
                {
                    case 1://余额
                        //XtomSharedPreferencesUtil.get(BuyCarPayActivity.this,"password");
                        if(fromtype==0)
                        getNetWorker().feeaccount_remove(user.getToken(),"2",billId,total_fee,"0","0");
                        else if(fromtype==1) {
                            if(isNull(input_num.getText().toString()))
                            {
                                showTextDialog("请输入购买数量");
                                return;
                            }
                            getNetWorker().feeaccount_remove(user.getToken(), "3", input_num.getText().toString(),total_fee, "0", "0");
                        }
                        break;
                    case 2://账户积分支付
                        break;
                    case 3://兑换券积分
                        break;
                    case 4://微信
                        //getNetWorker().weixinpay(token,"1","");
                        getNetWorker().weixinpay(token,"3", input_num.getText().toString(),total_fee,"0","0");
                        break;
                    case 5://支付宝
                        getNetWorker().alipay(token,"3", input_num.getText().toString(),total_fee,"0","0");
                        break;
                    case 6://招商银行
                        getNetWorker().unionpay(token,"3", input_num.getText().toString(),total_fee,"0","0");
                        break;
                }
            }
        });
    }
}
