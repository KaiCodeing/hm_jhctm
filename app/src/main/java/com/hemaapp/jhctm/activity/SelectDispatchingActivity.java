package com.hemaapp.jhctm.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.jhctm.JhActivity;
import com.hemaapp.jhctm.JhHttpInformation;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.adapter.SelectDispatchingAdapter;
import com.hemaapp.jhctm.model.Mall;

import java.util.ArrayList;

import xtom.frame.util.XtomSharedPreferencesUtil;
import xtom.frame.view.XtomListView;

/**
 * Created by lenovo on 2016/12/28.
 * 选择配送方式
 */
public class SelectDispatchingActivity extends JhActivity {
    private ImageButton back_button;
    private TextView title_text;
    private ImageButton next_button;
    private ProgressBar progressbar;
    // private RefreshLoadmoreLayout refreshLoadmoreLayout;
    private XtomListView listview;
    private RadioGroup select_no_yes;
    private LinearLayout layout_1;
    private ArrayList<Mall> malls = new ArrayList<>();
    private SelectDispatchingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_select_dispatching);
        super.onCreate(savedInstanceState);
        inIt();
    }

    private void inIt() {
        String cityName = XtomSharedPreferencesUtil.get(mContext, "cityName");
        if (isNull(cityName)) {
            String loactionCity = XtomSharedPreferencesUtil.get(mContext, "locationCity");
            getNetWorker().mallList(loactionCity, "");
        } else {
            getNetWorker().mallList(cityName, "");
        }
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case MALL_LIST:

                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case MALL_LIST:
                progressbar.setVisibility(View.GONE);
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
                freshData();
                break;
        }
    }

    private void freshData() {
        if (adapter == null) {
            adapter = new SelectDispatchingAdapter(SelectDispatchingActivity.this, malls);
            listview.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();

        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case MALL_LIST:
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case MALL_LIST:
                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (ImageButton) findViewById(R.id.next_button);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        //  refreshLoadmoreLayout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        listview = (XtomListView) findViewById(R.id.listview);
        layout_1 = (LinearLayout) findViewById(R.id.layout_1);
        select_no_yes = (RadioGroup) findViewById(R.id.select_no_yes);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_text.setText("选择配送方式");
        next_button.setVisibility(View.INVISIBLE);
        select_no_yes.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    //到店自提
                    case R.id.putong:
                        layout_1.setVisibility(View.VISIBLE);
                        inIt();
                        break;
                    //平台配送
                    case R.id.no_putong: {
                        layout_1.setVisibility(View.GONE);
                        String type = "1";//0自提 1 平台配送“
                        mIntent.putExtra("type", type);
                        mIntent.putExtra("mallId", "0");
                        setResult(RESULT_OK, mIntent);
                        finish();
                    }
                    break;
                }
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Mall mall = malls.get(position);
                String type = "2";//2自提 1 平台配送“
                if (select_no_yes.getCheckedRadioButtonId() == R.id.putong) {
                    type = "2";
                } else
                    type = "1";
                String mallId = mall.getId();
                mIntent.putExtra("type", type);
                mIntent.putExtra("mallId", mallId);
                setResult(RESULT_OK, mIntent);
                finish();
            }
        });

    }
}
