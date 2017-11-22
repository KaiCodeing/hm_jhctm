package com.hemaapp.jhctm.activity;

import android.os.Bundle;
import android.view.View;
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
import com.hemaapp.jhctm.adapter.HomeGridAdapter;
import com.hemaapp.jhctm.model.Blog1List;
import com.hemaapp.jhctm.view.JhctmGridView;

import java.util.ArrayList;

import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * Created by lenovo on 2016/12/26.
 * 分享专区
 * adaper:HomeGridAdapter
 */
public class ShareGoodsActivity extends JhActivity {
    private ImageButton back_button;
    private TextView title_text;
    private ImageButton next_button;
    private ProgressBar progressbar;
    private RefreshLoadmoreLayout refreshLoadmoreLayout;
    private JhctmGridView gridview;
    private Integer page = 0;
    private HomeGridAdapter gridAdapter;
    private ArrayList<Blog1List> blog1Lists= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_share_goods);
        super.onCreate(savedInstanceState);
        inIt();
    }
    private void inIt()
    {
        getNetWorker().blog3List(String.valueOf(page));
    }
    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case BLOG_3_LIST:
                showProgressDialog("获取分享商品列表");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case BLOG_3_LIST:
                progressbar.setVisibility(View.GONE);
                refreshLoadmoreLayout.setVisibility(View.VISIBLE);
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case BLOG_3_LIST:
                HemaPageArrayResult<Blog1List> result3 = (HemaPageArrayResult<Blog1List>) hemaBaseResult;
                ArrayList<Blog1List> blog1Lists = result3.getObjects();
                String page2 = hemaNetTask.getParams().get("page");
                if ("0".equals(page2)) {// 刷新
                    refreshLoadmoreLayout.refreshSuccess();
                    this.blog1Lists.clear();
                    this.blog1Lists.addAll(blog1Lists);

                    JhctmApplication application = JhctmApplication.getInstance();
                    int sysPagesize = application.getSysInitInfo()
                            .getSys_pagesize();
                    if (blog1Lists.size() < sysPagesize) {
                        refreshLoadmoreLayout.setLoadmoreable(false);
                        // leftRE = false;
                    } else {
                        refreshLoadmoreLayout.setLoadmoreable(true);
                        // leftRE = true;
                    }
                } else {// 更多
                    refreshLoadmoreLayout.loadmoreSuccess();
                    if (blog1Lists.size() > 0)
                        this.blog1Lists.addAll(blog1Lists);
                    else {
                        refreshLoadmoreLayout.setLoadmoreable(false);
                        // leftRE = false;
                        XtomToastUtil.showShortToast(mContext, "已经到最后啦");
                    }
                }
                freshData();
                break;
        }
    }
    private void freshData() {
        //商品列表
        if (gridAdapter == null) {
            gridAdapter = new HomeGridAdapter(mContext, blog1Lists);
            gridAdapter.setEmptyString("暂无数据");
            gridview.setAdapter(gridAdapter);
        } else {
            gridAdapter.setEmptyString("暂无数据");
            gridAdapter.notifyDataSetChanged();
        }

    }
    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case BLOG_3_LIST:
              showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case BLOG_3_LIST:
                String page = hemaNetTask.getParams().get("page");
                if ("0".equals(page)) {
                    refreshLoadmoreLayout.refreshFailed();
                } else {
                    refreshLoadmoreLayout.loadmoreFailed();
                }
                showTextDialog("获取商品列表失败，请稍后重试");
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
        gridview = (JhctmGridView) findViewById(R.id.gridview);
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
        title_text.setText("名品折扣");
        next_button.setVisibility(View.INVISIBLE);
        refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page=0;
                inIt();
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page++;
                inIt();
            }
        });
    }
}
