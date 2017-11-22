package com.hemaapp.jhctm.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.view.RoundedImageView;
import com.hemaapp.jhctm.JhFragment;
import com.hemaapp.jhctm.JhHttpInformation;
import com.hemaapp.jhctm.JhctmApplication;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.activity.AccountIntegrationActivity;
import com.hemaapp.jhctm.activity.AdvertisingPromotionActivity;
import com.hemaapp.jhctm.activity.ExchangeVoucherActivity;
import com.hemaapp.jhctm.activity.MyCardPackageActivity;
import com.hemaapp.jhctm.activity.MyCollectionActivity;
import com.hemaapp.jhctm.activity.MyWalltActivity;
import com.hemaapp.jhctm.activity.OrderActivity;
import com.hemaapp.jhctm.activity.SelectAddressActivity;
import com.hemaapp.jhctm.activity.SetActivity;
import com.hemaapp.jhctm.activity.ShareIntegralActivity;
import com.hemaapp.jhctm.activity.UserInformationActivity;
import com.hemaapp.jhctm.model.User;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;


/**
 * Created by lenovo on 2016/12/29.
 * 我的
 */
public class MyFragment extends JhFragment {
    private ImageView setting_img;//设置
    private RoundedImageView user_img;//用户头像
    private TextView user_name_text;//用户名
    private TextView card_jifen;//卡内积分
    private TextView duihuan_jifen;//兑换券积分
    private TextView share_jifen;//分享积分
    private TextView card_my;//我的卡片
    private TextView hecheng_card;//合成卡片
    private TextView card_buy;//卡片出售
    private TextView card_resolve = (TextView) findViewById(R.id.card_resolve);//卡片拆分
    private TextView yiwudui_text;//易物兑订单
    private TextView time_huan;//限时兑换订单
    private TextView share_d;//分享订单
    private TextView wall_my;//我的钱包
    private TextView shou_my;//我的收藏
    private TextView addrss_manage;//地址管理
    private LinearLayout my_card_layout;
    private TextView guang_tui;//广告推广
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_my);
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onResume() {
        super.onResume();
        String token = JhctmApplication.getInstance().getUser().getToken();
        String id = JhctmApplication.getInstance().getUser().getId();
        getNetWorker().clientGet(token,id);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case CLIENT_GET:
                HemaArrayResult<User> result = (HemaArrayResult<User>) hemaBaseResult;
                User user = result.getObjects().get(0);
                //头像
                String path = user.getAvatar();
                user_img.setCornerRadius(100);
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .showImageOnLoading(R.mipmap.defult_gridview_img)
                        .showImageForEmptyUri(R.mipmap.defult_gridview_img)
                        .showImageOnFail(R.mipmap.defult_gridview_img).cacheInMemory(true)
                        .bitmapConfig(Bitmap.Config.RGB_565).build();
                ImageLoader.getInstance().displayImage(path, user_img, options);
                user_name_text.setText(user.getNickname());
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case CLIENT_GET:
                showTextDialog(hemaBaseResult.getMsg());
                break;

        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case CLIENT_GET:
                showTextDialog("获取个人信息失败，请稍后重试");
                break;

        }
    }

    @Override
    protected void findView() {
        setting_img = (ImageView) findViewById(R.id.setting_img);
        user_img = (RoundedImageView) findViewById(R.id.user_img);
        user_name_text = (TextView) findViewById(R.id.user_name_text);
        card_jifen = (TextView) findViewById(R.id.card_jifen);
        duihuan_jifen = (TextView) findViewById(R.id.duihuan_jifen);
        card_my = (TextView) findViewById(R.id.card_my);
        hecheng_card = (TextView) findViewById(R.id.hecheng_card);
        card_buy = (TextView) findViewById(R.id.card_buy);
        yiwudui_text = (TextView) findViewById(R.id.yiwudui_text);
        time_huan = (TextView) findViewById(R.id.time_huan);
        share_d = (TextView) findViewById(R.id.share_d);
        wall_my = (TextView) findViewById(R.id.wall_my);
        shou_my = (TextView) findViewById(R.id.shou_my);
        addrss_manage = (TextView) findViewById(R.id.addrss_manage);
        my_card_layout = (LinearLayout) findViewById(R.id.my_card_layout);
        share_jifen = (TextView) findViewById(R.id.share_jifen);
        guang_tui = (TextView) findViewById(R.id.guang_tui);
    }

    @Override
    protected void setListener() {
        //广告推广
        guang_tui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),AdvertisingPromotionActivity.class);
                intent.putExtra("type","1");
                startActivity(intent);
            }
        });
        //设置
        setting_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SetActivity.class);
                getActivity().startActivity(intent);
            }
        });
        //地址管理
        addrss_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SelectAddressActivity.class);
                getActivity().startActivity(intent);
            }
        });
        //我的收藏
        shou_my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyCollectionActivity.class);
                getActivity().startActivity(intent);
            }
        });
        //我的信息
        user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserInformationActivity.class);
                getActivity().startActivity(intent);
            }
        });
        //兑换券积分
        duihuan_jifen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ExchangeVoucherActivity.class);
                getActivity().startActivity(intent);
            }
        });
        //我的钱包
        wall_my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyWalltActivity.class);
                getActivity().startActivity(intent);
            }
        });
        //我的卡包
        my_card_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyCardPackageActivity.class);
                getActivity().startActivity(intent);
            }
        });
        //分享积分
        share_jifen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShareIntegralActivity.class);
                getActivity().startActivity(intent);
            }
        });
        //卡片积分
        card_jifen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AccountIntegrationActivity.class);
                getActivity().startActivity(intent);
            }
        });
        setListener(yiwudui_text); //易物兑订单
        setListener(time_huan); //限时兑换订单
        setListener(share_d); //分享订单
    }
    public void setListener(View view){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getActivity(), OrderActivity.class);
                switch (v.getId()){
                    case R.id.yiwudui_text:
                        it.putExtra("name", "易物商城订单");
                        it.putExtra("keytype", "1");
                        break;
                    case R.id.time_huan:
                        it.putExtra("name", "限时秒杀订单");
                        it.putExtra("keytype", "2");
                        break;
                    case R.id.share_d:
                        it.putExtra("name", "名品折扣订单");
                        it.putExtra("keytype", "3");
                        break;
                }
                startActivity(it);
            }
        });
    }
    }

