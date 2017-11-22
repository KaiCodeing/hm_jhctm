package com.hemaapp.jhctm.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.view.RefreshLoadmoreLayout;
import com.hemaapp.jhctm.JhActivity;
import com.hemaapp.jhctm.JhHttpInformation;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.adapter.SearchResultSupAdapter;
import com.hemaapp.jhctm.model.Mall;

import java.util.ArrayList;

import xtom.frame.view.XtomListView;

/**
 * Created by lenovo on 2016/12/19.
 * 归属超市搜索结果
 * SearchResultSupAdapter
 */
public class SearchResultSupActivity extends JhActivity {
    private ImageButton back_button;
    private TextView title_text;
    private ImageButton next_button;
    private ProgressBar progressbar;
    private RefreshLoadmoreLayout refreshLoadmoreLayout;
    private XtomListView listview;
    private String search;
    private String cityName;
    private TextView no_asc;
    private ArrayList<Mall> malls = new ArrayList<>();
    private SearchResultSupAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_search_result_sup);
        super.onCreate(savedInstanceState);
        getNetWorker().mallList(cityName,search);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case MALL_LIST:
                progressbar.setVisibility(View.GONE);
                refreshLoadmoreLayout.setVisibility(View.VISIBLE);
                showProgressDialog("获取超市列表");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case MALL_LIST:
              cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case MALL_LIST:
                HemaArrayResult<Mall> result = (HemaArrayResult<Mall>) hemaBaseResult;
                malls = result.getObjects();
                freshData();
                break;
        }
    }
    private void freshData() {
        if (adapter == null) {
            adapter = new SearchResultSupAdapter(SearchResultSupActivity.this,malls);
            adapter.setEmptyString("暂无数据");
            listview.setAdapter(adapter);
        } else {
            adapter.setEmptyString("暂无数据");
            adapter.notifyDataSetChanged();

        }
    }
    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case MALL_LIST:
               showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case MALL_LIST:
                String page = hemaNetTask.getParams().get("page");
                if ("0".equals(page)) {
                    refreshLoadmoreLayout.refreshFailed();
                } else {
                    refreshLoadmoreLayout.loadmoreFailed();
                }
               showTextDialog("获取超市列表失败，请稍后");
                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (ImageButton) findViewById(R.id.next_button);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        refreshLoadmoreLayout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        listview = (XtomListView) findViewById(R.id.listview);
        no_asc = (TextView) findViewById(R.id.no_asc);
    }

    @Override
    protected void getExras() {
        cityName = mIntent.getStringExtra("cityName");
        if ("定位中".equals(cityName))
            cityName="";
        search = mIntent.getStringExtra("search");
    }

    @Override
    protected void setListener() {
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_text.setText("搜索结果");
        next_button.setVisibility(View.INVISIBLE);
        no_asc.setText("当前搜索:"+search);
        refreshLoadmoreLayout.setEnabled(false);
        refreshLoadmoreLayout.setRefreshable(false);
        refreshLoadmoreLayout.setLoadmoreable(false);
    }
}
