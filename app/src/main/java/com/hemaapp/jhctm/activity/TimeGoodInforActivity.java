package com.hemaapp.jhctm.activity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.view.HemaWebView;
import com.hemaapp.jhctm.JhActivity;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.view.JhViewPager;

/**
 * Created by lenovo on 2016/12/27.
 * 限时抢购商品详情
 */
public class TimeGoodInforActivity extends JhActivity {
    private ImageButton back_button;
    private TextView title_text;
    private ImageButton next_button;
    private JhViewPager adviewpager;
    private TextView jifen;//积分
    private TextView add_text;//加
    private TextView fenxiang_text;//分享
    private TextView good_name;//商品名称
    private TextView yunfei_text;//运费
    private HemaWebView webview_aboutwe;//商品详情
    private TextView share_text;//分享
    private TextView goto_apliy;//立即购买

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_time_good_infor);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {

    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {

    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {

    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (ImageButton) findViewById(R.id.next_button);
        adviewpager = (JhViewPager) findViewById(R.id.adviewpager);
        jifen = (TextView) findViewById(R.id.jifen);
        add_text = (TextView) findViewById(R.id.add_text);
        fenxiang_text = (TextView) findViewById(R.id.fenxiang_text);
        good_name = (TextView) findViewById(R.id.good_name);
        webview_aboutwe = (HemaWebView) findViewById(R.id.webview_aboutwe);
        yunfei_text = (TextView) findViewById(R.id.yunfei_text);
        share_text = (TextView) findViewById(R.id.share_text);
        goto_apliy = (TextView) findViewById(R.id.goto_apliy);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {

    }
}
