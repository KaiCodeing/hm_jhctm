package com.hemaapp.jhctm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.jhctm.JhActivity;
import com.hemaapp.jhctm.JhHttpInformation;
import com.hemaapp.jhctm.JhctmApplication;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.adapter.ScoreTimeAdapter;
import com.hemaapp.jhctm.model.ScoreTime;
import com.hemaapp.jhctm.model.User;

import java.util.ArrayList;

import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomRefreshLoadmoreLayout;
import xtom.frame.view.XtomRefreshLoadmoreLayout.OnStartListener;


public class TimeScore extends JhActivity implements OnClickListener {
    private ListView list;
    private ImageButton left;
    private TextView title;
    private Button btn_right;
    private Integer currentPage = 0;
    private User user;
    private TextView photo, tuku, shipin, cancel;
    private Intent intent;
    private ScoreTimeAdapter adapter;
    private XtomRefreshLoadmoreLayout layout;
    private ArrayList<ScoreTime> infos = new ArrayList<ScoreTime>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        setContentView(R.layout.activity_timeline);
        super.onCreate(savedInstanceState);
        user = JhctmApplication.getInstance().getUser();
        getlist(currentPage);

    }

    private void getlist(Integer page) {
        getNetWorker().goldScoreRebatList(user.getToken(), page.toString());
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask netTask) {
        // TODO Auto-generated method stub
        showProgressDialog("加载中");
    }

    @Override
    protected void callAfterDataBack(HemaNetTask netTask) {
        // TODO Auto-generated method stub
        cancelProgressDialog();
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask netTask,
                                            HemaBaseResult baseResult) {
        // TODO Auto-generated method stub
        JhHttpInformation information = (JhHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case GOLD_SCORE_REBAT_LIST:
                HemaPageArrayResult<ScoreTime> iResult = (HemaPageArrayResult<ScoreTime>) baseResult;
                //adapter.notifyDataSetChanged();
                if ("0".equals(currentPage.toString())) {
                    layout.refreshSuccess();
                    infos.clear();
                    infos.addAll(iResult.getObjects());
                    int sysPagesize = getApplicationContext().getSysInitInfo()
                            .getSys_pagesize();
                    if (infos.size() < sysPagesize)
                        layout.setLoadmoreable(false);
                    else
                        layout.setLoadmoreable(true);
                } else {// 更多
                    layout.loadmoreSuccess();
                    if (iResult.getObjects().size() > 0)
                        this.infos.addAll(iResult.getObjects());
                    else {
                        layout.setLoadmoreable(false);
                        XtomToastUtil.showShortToast(mContext, "已经到最后啦");
                    }
                }
                //adapter.notifyDataSetChanged();
                freshData();
                break;
        }

    }

    @Override
    protected void callBackForServerFailed(HemaNetTask netTask,
                                           HemaBaseResult baseResult) {
        // TODO Auto-generated method stub
        JhHttpInformation information = (JhHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case SCORE_REABT_LIST:
                showTextDialog(baseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask netTask, int failedType) {
        // TODO Auto-generated method stub
        String page = netTask.getParams().get("page");
        if ("0".equals(page)) {
            layout.refreshFailed();
        } else {
            layout.loadmoreFailed();
        }
        showTextDialog("获取数据失败");
    }

    @Override
    protected void findView() {
        // TODO Auto-generated method stub
        left = (ImageButton) findViewById(R.id.back_button);
        title = (TextView) findViewById(R.id.title_text);
        title.setText("金积分返回时间轴");
        btn_right = (Button) findViewById(R.id.next_button);
        btn_right.setVisibility(View.GONE);
        list = (ListView) findViewById(R.id.listview);
        layout = (XtomRefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
    }

    @Override
    protected void getExras() {
        // TODO Auto-generated method stub
    }

    @Override
    protected void setListener() {
        // TODO Auto-generated method stub
        left.setOnClickListener(this);
        layout.setOnStartListener(new OnStartListener() {

            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout v) {
                currentPage = 0;
                getlist(currentPage);
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout v) {
                currentPage++;
                getlist(currentPage);
            }
        });
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.back_button:
                finish();
                break;
            default:
                break;
        }
    }

    private void freshData() {
        if (adapter == null) {
            adapter = new ScoreTimeAdapter(this, infos,null);
            adapter.setEmptyString("暂无数据");
            list.setAdapter(adapter);
        } else {
            adapter.setEmptyString("暂无数据");
            adapter.notifyDataSetChanged();
        }
    }

}
