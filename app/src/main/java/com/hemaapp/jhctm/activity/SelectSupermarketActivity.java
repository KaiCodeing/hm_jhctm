package com.hemaapp.jhctm.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.jhctm.JhActivity;
import com.hemaapp.jhctm.JhHttpInformation;
import com.hemaapp.jhctm.JhLocation;
import com.hemaapp.jhctm.JhctmApplication;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.adapter.SuperAdapter;
import com.hemaapp.jhctm.model.Mall;
import com.hemaapp.jhctm.util.CharacterParser;
import com.hemaapp.jhctm.view.LetterListView;
import com.hemaapp.jhctm.view.SuperComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by lenovo on 2016/12/19.
 * 选择归属超市
 */
public class SelectSupermarketActivity extends JhActivity {
    private ImageButton back_button;
    private TextView search_log;//搜索
    private TextView message_view;//地址
    private ListView city_list;//超市列表
    private LetterListView letterListView;

    private TextView overlay;
    private HashMap<String, Integer> alphaIndexer;// 存放存在的汉语拼音首字母和与之对应的列表位置
    private Handler handler;
    private OverlayThread overlayThread;
    private WindowManager windowManager;
    private TextView no_asc;
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private SuperComparator pinyinComparator;
    private LoacationReceiver loacationReceiver;

    private String locationCity;
    private ArrayList<Mall> malls;

    private SuperAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_supermarket_select);
        super.onCreate(savedInstanceState);
        handler = new Handler();
        overlayThread = new OverlayThread();
        initOverlay();
        initViews();
        getLocationCity();
        //注册广播
        registerBoradcastReceiver();
    }

    @Override
    protected void onDestroy() {
        windowManager.removeView(overlay);
        if (loacationReceiver != null)
            unregisterReceiver(loacationReceiver);
        super.onDestroy();
    }

    // 获取定位城市
    private void getLocationCity() {
        AMapLocation location = JhLocation.getInstance().getaMapLocation();
        if (location != null)
            locationCity = location.getCity();
        if (isNull(locationCity)) {
            JhctmApplication application = JhctmApplication.getInstance();
            String action = application.getPackageName() + ".location";
            IntentFilter mFilter = new IntentFilter(action);
            loacationReceiver = new LoacationReceiver();
            registerReceiver(loacationReceiver, mFilter);
        } else {
            message_view.setText(locationCity);
            getNetWorker().mallList(locationCity,"");
        }
    }

    // 设置overlay不可见
    private class OverlayThread implements Runnable {

        @Override
        public void run() {
            overlay.setVisibility(View.GONE);
        }
    }

    private void initViews() {
        // 实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new SuperComparator();
        // 根据输入框输入值的改变来过滤搜索
        //  etSearch.addTextChangedListener(new TextChageListener());
    }

    private void initOverlay() {
        LayoutInflater inflater = LayoutInflater.from(this);
        overlay = (TextView) inflater.inflate(R.layout.overlay, null);
        overlay.setVisibility(View.INVISIBLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
        windowManager = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(overlay, lp);
    }

    private class LoacationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            JhctmApplication application = JhctmApplication.getInstance();
            String realAction = application.getPackageName() + ".location";
            String action = intent.getAction();
            if (realAction.equals(action)) {
                locationCity = JhLocation.getInstance().getaMapLocation()
                        .getCity();
                if (isNull(locationCity))
                    message_view.setText("定位中");
                else
                    message_view.setText(locationCity);
                getNetWorker().mallList(locationCity,"");
            }
        }
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case MALL_LIST:
                showProgressDialog("获取超市信息");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case MALL_LIST:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case MALL_LIST:
                HemaArrayResult<Mall> result = (HemaArrayResult<Mall>) hemaBaseResult;
                malls = result.getObjects();
                Collections.sort(malls, pinyinComparator);
                adapter = new SuperAdapter(this, false);
                adapter.setList(malls);
                setAdapter(malls);
                adapter.notifyDataSetChanged();
                break;
        }
    }

    private void setAdapter(ArrayList<Mall> list) {
        if (list != null) {
            city_list.setAdapter(adapter);
            alphaIndexer = adapter.getAlphaIndexer();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK != resultCode) {
            return;
        }
        switch (requestCode)
        {
            case 1:
                String cityName = data.getStringExtra("name");
                String cityId = data.getStringExtra("cityId");
                message_view.setText(cityName);
                getNetWorker().mallList(cityName,"");
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case MALL_LIST:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case MALL_LIST:
                showTextDialog("获取失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        search_log = (TextView) findViewById(R.id.search_log);
        message_view = (TextView) findViewById(R.id.message_view);
        city_list = (ListView) findViewById(R.id.city_list);
        letterListView = (LetterListView) findViewById(R.id.letterListView);
        no_asc = (TextView) findViewById(R.id.no_asc);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        letterListView
                .setOnTouchingLetterChangedListener(new LetterListViewListener());
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        no_asc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent.putExtra("mallName","无归属");
                mIntent.putExtra("mallId","0");
                setResult(RESULT_OK,mIntent);
                finish();
            }
        });
        /**
         * 选择城市
         */
        message_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectSupermarketActivity.this,City0Activity.class);
                startActivityForResult(intent,1);
            }
        });
        /**
         * 搜索
         */
        search_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectSupermarketActivity.this,SearchSupermarketActivity.class);
                intent.putExtra("cityName",message_view.getText().toString());
                startActivity(intent);
            }
        });
    }

    private class LetterListViewListener implements
            LetterListView.OnTouchingLetterChangedListener {

        @SuppressLint("NewApi")
        @Override
        public void onTouchingLetterChanged(final String s) {
            if (alphaIndexer != null && alphaIndexer.get(s) != null) {
                final int position = alphaIndexer.get(s);
                log_w("=s=" + s);
                city_list.setSelection(position);
            } else if ("↑".equals(s)) {
                city_list.setSelection(0);
            }
            overlay.setText(s);
            overlay.setVisibility(View.VISIBLE);
            handler.removeCallbacks(overlayThread);
            // 延迟一秒后执行，让overlay为不可见
            handler.postDelayed(overlayThread, 1000);
        }
    }

    /**
     * 选择归属超市
     * @param mall
     */
    public void getSuper(Mall mall)
    {
        mIntent.putExtra("mallName",mall.getNickname());
        mIntent.putExtra("mallId",mall.getId());
        setResult(RESULT_OK,mIntent);
        finish();
    }
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("com.hemaapp.jh.sup")){
                finish();
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
