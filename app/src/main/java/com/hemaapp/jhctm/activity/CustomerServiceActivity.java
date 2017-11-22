package com.hemaapp.jhctm.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.view.RefreshLoadmoreLayout;
import com.hemaapp.jhctm.JhActivity;
import com.hemaapp.jhctm.R;

/**
 * Created by lenovo on 2017/1/6.
 * 售后
 * CustomerServiceAdapter
 */
public class CustomerServiceActivity extends JhActivity {
    private TextView title_text;
    private ImageButton back_button;
    private Button next_button;
    private RefreshLoadmoreLayout refreshLoadmoreLayout;
    private ExpandableListView listview;
    private ProgressBar progressbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_customer_service);
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
        title_text = (TextView) findViewById(R.id.title_text);
        back_button = (ImageButton) findViewById(R.id.back_button);
        next_button = (Button) findViewById(R.id.next_button);
        refreshLoadmoreLayout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        listview = (ExpandableListView) findViewById(R.id.listview);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {

    }
}
