package com.hemaapp.jhctm.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.jhctm.JhActivity;
import com.hemaapp.jhctm.R;

/**
 * Created by lenovo on 2016/12/19.
 * 搜索归属超市
 */
public class SearchSupermarketActivity extends JhActivity {
    private EditText search_log;//搜索输入
    private TextView message_view;//搜索
    private String cityName;
    private ImageButton back_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_supermarket_search);
        super.onCreate(savedInstanceState);
        //注册广播
        registerBoradcastReceiver();
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
        search_log = (EditText) findViewById(R.id.search_log);
        message_view = (TextView) findViewById(R.id.message_view);
        back_button = (ImageButton) findViewById(R.id.back_button);
    }

    @Override
    protected void getExras() {
        cityName = mIntent.getStringExtra("cityName");
    }

    @Override
    protected void setListener() {
    back_button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    });
        //搜索
        message_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String seach = search_log.getText().toString().trim();
                if (isNull(seach))
                {
                    showTextDialog("请填写搜索关键字");
                    return;
                }
                Intent intent = new Intent(SearchSupermarketActivity.this,SearchResultSupActivity.class);
                intent.putExtra("cityName",cityName);
                intent.putExtra("search",seach);
                startActivity(intent);
            }
        });
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
