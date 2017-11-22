package com.hemaapp.jhctm.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by lenovo on 2016/11/28.
 */
public class MyWebViewClient extends WebViewClient {
    private static final String TAG = "MyWebViewClient";
  //  private ProgressBar layout_loding;
    private Context mContext;
    private WebView mwebView;
    public MyWebViewClient( Context context,WebView webView) {
    //    this.layout_loding = layout_loding;
        this.mContext = context;
        this.mwebView =webView;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        // TODO Auto-generated method stub
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        addImageClickListner();
  //      layout_loding.setVisibility(View.GONE);
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

        super.onReceivedError(view, errorCode, description, failingUrl);
  //      layout_loding.setVisibility(View.GONE);
    }

    private void addImageClickListner() {

        mwebView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByClassName(\"upload-img\"); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "    objs[i].onclick=function()  " +
                "    {  "
                + "        window.imagelistner.openImage(this.src);  " +
                "    }  " +
                "}" +
                "})()");
    }
}
