package com.hemaapp.jhctm.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.view.HemaWebView;
import com.hemaapp.jhctm.JhActivity;
import com.hemaapp.jhctm.R;

/**
 * Created by lenovo on 2016/12/23.
 */
public class JhWebView extends JhActivity {
    private ImageButton back_button;
    private TextView title_text;
    private ImageButton next_button;
    private HemaWebView webview_aboutwe;
    private String type;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_webview);
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
        webview_aboutwe = (HemaWebView) findViewById(R.id.webview_aboutwe);
    }

    @Override
    protected void getExras() {
        type = mIntent.getStringExtra("type");
        id = mIntent.getStringExtra("id");
    }

    @Override
    protected void setListener() {

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        next_button.setVisibility(View.INVISIBLE);
        String sys_web_service = getApplicationContext().getSysInitInfo()
                .getSys_web_service();
        if ("1".equals(type)) {
            String path = sys_web_service + "webview/parm/protocal";
            webview_aboutwe.loadUrl(path);
            title_text.setText("注册协议");
            return;
        }
        //合成卡问号
        else if("2".equals(type))
        {
            String path = sys_web_service + "webview/parm/protocal";
            webview_aboutwe.loadUrl(path);
            title_text.setText("注册协议");
            return;
        }

        else if("3".equals(type)){ //跳转到网页
            String path = sys_web_service + "webview/parm/indexAdDetail_"+id;
            webview_aboutwe.loadUrl(path);
            title_text.setText("详情");
            return;
        }
    }
}
