package com.hemaapp.jhctm.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.hemaapp.jhctm.adapter.MyCollectionAdapter;
import com.hemaapp.jhctm.model.Blog1List;
import com.hemaapp.jhctm.view.JhctmGridView;

import java.util.ArrayList;

import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * Created by lenovo on 2017/1/4.
 * 我的收藏
 * MyCollectionAdapter
 */
public class MyCollectionActivity extends JhActivity {
    private TextView title_text;
    private ImageButton back_button;
    private Button next_button;
    private JhctmGridView gridview;
    private RefreshLoadmoreLayout refreshLoadmoreLayout;
    private ProgressBar progressbar;
    private ImageView all_buy_img;//全选
    private TextView buy_text;//删除
    private MyCollectionAdapter adapter;
    private ArrayList<Blog1List> blog1Lists = new ArrayList<>();
    private String type = "0";
    private LinearLayout delete_layout;
    private String all_delte = "1";
    private Integer page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_collection);
        super.onCreate(savedInstanceState);
        inIt();
    }

    //初始化
    private void inIt() {
        String token = JhctmApplication.getInstance().getUser().getToken();
        String userId = JhctmApplication.getInstance().getUser().getId();
        getNetWorker().followCollectList(token, "1", userId, String.valueOf(page));
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case FOLLOW_COLLECT_LIST:
                showProgressDialog("获取收藏商品");
                break;
            case FOLLOW_COLLECT_OPERATOR:
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case FOLLOW_COLLECT_LIST:
                cancelProgressDialog();
                progressbar.setVisibility(View.GONE);
                refreshLoadmoreLayout.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case FOLLOW_COLLECT_LIST:
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
                        XtomToastUtil.showShortToast(this, "已经到最后啦");
                    }
                }
                freshData();
                break;
            case FOLLOW_COLLECT_OPERATOR:
                showTextDialog("取消收藏成功!");
                page = 0;
                type = "0";
                all_delte = "1";
                delete_layout.setVisibility(View.GONE);
                inIt();
                break;
        }
    }

    private void freshData() {
        if (adapter == null) {
            adapter = new MyCollectionAdapter(MyCollectionActivity.this, blog1Lists, type);
            adapter.setEmptyString("暂无收藏商品");
            gridview.setAdapter(adapter);
        } else {
            adapter.setEmptyString("暂无收藏商品");
            adapter.setBlog1Lists(blog1Lists);
            adapter.setType(type);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case FOLLOW_COLLECT_LIST:
            case FOLLOW_COLLECT_OPERATOR:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case FOLLOW_COLLECT_LIST:
                showTextDialog("获取收藏商品失败，请稍后重试");
                String page = hemaNetTask.getParams().get("page");
                if ("0".equals(page)) {
                    refreshLoadmoreLayout.refreshFailed();
                } else {
                    refreshLoadmoreLayout.loadmoreFailed();
                }
                break;
            case FOLLOW_COLLECT_OPERATOR:
                showTextDialog("取消收藏失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        title_text = (TextView) findViewById(R.id.title_text);
        back_button = (ImageButton) findViewById(R.id.back_button);
        next_button = (Button) findViewById(R.id.next_button);
        gridview = (JhctmGridView) findViewById(R.id.gridview);
        refreshLoadmoreLayout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        all_buy_img = (ImageView) findViewById(R.id.all_buy_img);
        buy_text = (TextView) findViewById(R.id.buy_text);
        delete_layout = (LinearLayout) findViewById(R.id.delete_layout);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        title_text.setText("我的收藏");
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        next_button.setText("编辑");
        next_button.setTextColor(getResources().getColor(R.color.color_text));
        delete_layout.setVisibility(View.GONE);
        //编辑
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("0".equals(type)) {
                    type = "1";
                    delete_layout.setVisibility(View.VISIBLE);
                } else {
                    type = "0";
                    delete_layout.setVisibility(View.GONE);
                }
                freshData();
            }
        });
        all_buy_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("1".equals(all_delte)) {
                    for (Blog1List list : blog1Lists
                            ) {
                        list.setCheck(true);
                    }
                    all_buy_img.setImageResource(R.mipmap.fapiao_check_on);
                    all_delte = "2";
                } else {
                    for (Blog1List list : blog1Lists
                            ) {
                        list.setCheck(false);
                    }
                    all_buy_img.setImageResource(R.mipmap.fapiao_check_off);
                    all_delte = "1";
                }
                freshData();
            }
        });
        refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page = 0;
                inIt();
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page++;
                inIt();
            }
        });
        //删除
        buy_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (blog1Lists == null || blog1Lists.size() == 0)
                    return;
                int a = 0;
                StringBuffer buffer = new StringBuffer();
                for (int i = 0; i < blog1Lists.size(); i++) {
                    if (blog1Lists.get(i).isCheck()) {
                        a++;
                        buffer.append(blog1Lists.get(i).getKeyid() + ",");
                    }
                }
                if (a == 0) {
                    showTextDialog("请选择商品");
                    return;
                }
                String token = JhctmApplication.getInstance().getUser().getToken();
                getNetWorker().goodsOperate(token, "1", "2", buffer.substring(0, buffer.length() - 1));
            }
        });
    }

    //检测是否全部选择了
    public void allDelte() {
        int m = 0;
        for (Blog1List list : blog1Lists
                ) {
            if (list.isCheck())
                m++;
        }
        if (m == blog1Lists.size()) {
            all_delte="2";
            all_buy_img.setImageResource(R.mipmap.fapiao_check_on);
        } else {
            all_delte="1";
            all_buy_img.setImageResource(R.mipmap.fapiao_check_off);
        }
        freshData();
    }
}
