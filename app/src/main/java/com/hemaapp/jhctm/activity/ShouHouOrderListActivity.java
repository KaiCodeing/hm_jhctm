package com.hemaapp.jhctm.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.hm_FrameWork.view.RefreshLoadmoreLayout;
import com.hemaapp.jhctm.JhActivity;
import com.hemaapp.jhctm.JhHttpInformation;
import com.hemaapp.jhctm.JhctmApplication;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.adapter.FailedOrderListAdapter;
import com.hemaapp.jhctm.model.FailedOrderListInfor;
import com.hemaapp.jhctm.model.User;

import java.util.ArrayList;

import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * Created by WangYuxia on 2017/2/10.
 * 售后订单列表
 */
public class ShouHouOrderListActivity extends JhActivity {

    private ImageButton left;
    private TextView title;
    private Button right;

    private RefreshLoadmoreLayout layout;
    private ProgressBar progressBar;
    private XtomListView mListView;

    private String keytype;
    private int page = 0;
    private ArrayList<FailedOrderListInfor> infors = new ArrayList<>();
    private FailedOrderListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_list);
        super.onCreate(savedInstanceState);
        getShopList();
    }

    private void getShopList(){
        User user = JhctmApplication.getInstance().getUser();
        getNetWorker().afterServiceBillList(user.getToken(), keytype, page);
    }

    private void freshData(){
        if(adapter == null){
            adapter = new FailedOrderListAdapter(mContext, infors, keytype);
            adapter.setEmptyString("抱歉，暂时没有数据");
            mListView.setAdapter(adapter);
        }else{
            adapter.setEmptyString("抱歉，暂时没有数据");
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case AFTERSERVICE_BILL_LIST:
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case AFTERSERVICE_BILL_LIST:
                layout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case AFTERSERVICE_BILL_LIST:
                String page = hemaNetTask.getParams().get("page");
                HemaPageArrayResult<FailedOrderListInfor> gResult = (HemaPageArrayResult<FailedOrderListInfor>) hemaBaseResult;
                ArrayList<FailedOrderListInfor> notices = gResult.getObjects();
                if ("0".equals(page)) {// 刷新
                    layout.refreshSuccess();
                    infors.clear();
                    infors.addAll(notices);

                    JhctmApplication application = JhctmApplication
                            .getInstance();
                    int sysPagesize = application.getSysInitInfo()
                            .getSys_pagesize();
                    if (notices.size() < sysPagesize)
                        layout.setLoadmoreable(false);
                    else
                        layout.setLoadmoreable(true);
                } else {// 更多
                    layout.loadmoreSuccess();
                    if (notices.size() > 0)
                        infors.addAll(notices);
                    else {
                        layout.setLoadmoreable(false);
                        XtomToastUtil.showShortToast(mContext, "已经到最后啦");
                    }
                }
                freshData();
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case AFTERSERVICE_BILL_LIST:
                showTextDialog("抱歉，获取数据失败");
                String page = hemaNetTask.getParams().get("page");
                if ("0".equals(page)) {// 刷新
                    layout.refreshFailed();
                    freshData();
                } else {// 更多
                    layout.loadmoreFailed();
                }
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case AFTERSERVICE_BILL_LIST:
                showTextDialog("抱歉，获取数据失败");
                String page = hemaNetTask.getParams().get("page");
                if ("0".equals(page)) {// 刷新
                    layout.refreshFailed();
                    freshData();
                } else {// 更多
                    layout.loadmoreFailed();
                }
                break;
        }
    }

    @Override
    protected void findView() {
        left = (ImageButton) findViewById(R.id.back_button);
        right = (Button) findViewById(R.id.next_button);
        title = (TextView) findViewById(R.id.title_text);

        layout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        mListView = (XtomListView) findViewById(R.id.listview);
    }

    @Override
    protected void getExras() {
        keytype = mIntent.getStringExtra("keytype");
    }

    @Override
    protected void setListener() {
        title.setText("售后");
        right.setVisibility(View.INVISIBLE);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        layout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page = 0;
                getShopList();
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page ++;
                getShopList();
            }
        });
    }
}
