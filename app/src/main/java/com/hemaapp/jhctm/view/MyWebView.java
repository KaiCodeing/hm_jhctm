package com.hemaapp.jhctm.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hemaapp.hm_FrameWork.view.HemaWebView;

/**
 * Created by HuHu on 2016-05-23.
 */
public class MyWebView extends HemaWebView {
    public MyWebView(Context context) {
        this(context, (AttributeSet) null);
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        addListener(context);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        addListener(context);
    }

    protected void addListener(Context context) {
        // 添加js交互接口类，并起别名 imagelistner
        addJavascriptInterface(new JavascriptInterface(context), "imagelistner");
        setWebViewClient(new MyWebViewClient());
    }

    /*捕捉webview点击事件*/
    // 注入js函数监听
    private void addImageClickListner() {
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，在还是执行的时候调用本地接口传递url过去
        String js = "javascript:(function(){" +
                "    var objs = document.getElementsByTagName(\"img\"); " +
                "    var images = \"\";" +
                "    for(var i=0;i<objs.length;i++)  " +
                "    {"
                + "        if(\"\"==images){images=objs[i].src;}"
                + "        else{images+=\";\"+objs[i].src;}"
                + "        objs[i].title=i;"
                + "        objs[i].onclick=function()  " +
                "          {  "
                + "            window.imagelistner.openImage(this.src);  " +
                "              window.imagelistner.openMultiImage(this.title, images);" +
                "          }  " +
                "    }" +
                "})()";
        loadUrl(js);
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            view.getSettings().setJavaScriptEnabled(true);
            super.onPageFinished(view, url);
            // html加载完成之后，添加监听图片的点击js函数
            addImageClickListner();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            view.getSettings().setJavaScriptEnabled(true);

            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);

        }
    }

    public class JavascriptInterface {
        private Context context;

        public JavascriptInterface(Context context) {
            this.context = context;
        }

        @android.webkit.JavascriptInterface
        public void openImage(String img) {
            if (onImageClickListener != null) {
                onImageClickListener.clickImage(img);
            }

        }

        @android.webkit.JavascriptInterface
        public void openMultiImage(String position, String images) {
            if (onMultiImageClickListener != null) {
                onMultiImageClickListener.clickMultiImage(position, images);

            }
        }
    }

    private OnImageClickListener onImageClickListener;
    private OnMultiImageClickListener onMultiImageClickListener;

    /**
     * 设置点击图片事件
     *
     * @param onImageClickListener
     */
    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }

    /**
     * 设置返回全部图片链接的点击事件
     *
     * @param onMultiImageClickListener
     */
    public void setOnMultiImageClickListener(OnMultiImageClickListener onMultiImageClickListener) {
        this.onMultiImageClickListener = onMultiImageClickListener;
    }

    public interface OnImageClickListener {
        void clickImage(String imageUrl);
    }


    public interface OnMultiImageClickListener {
        /**
         * 点击图片的回掉事件
         *
         * @param position 点击图片的序号，从0开始排序
         * @param images   所有图片的链接，用英文分号隔开
         */
        void clickMultiImage(String position, String images);
    }

    /*捕捉webview点击事件结束*/
}
