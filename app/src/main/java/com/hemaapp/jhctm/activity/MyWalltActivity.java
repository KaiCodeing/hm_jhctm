package com.hemaapp.jhctm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.hm_FrameWork.view.RefreshLoadmoreLayout;
import com.hemaapp.jhctm.JhActivity;
import com.hemaapp.jhctm.JhHttpInformation;
import com.hemaapp.jhctm.JhctmApplication;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.adapter.IntegralDetailedAdapter;
import com.hemaapp.jhctm.model.Reply;
import com.hemaapp.jhctm.model.User;
import com.hemaapp.jhctm.view.MyListView;

import java.util.ArrayList;

import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * Created by lenovo on 2017/2/4.
 * 我的钱包
 */
public class MyWalltActivity extends JhActivity {
    private ImageButton back_button;
    private TextView title_text;
    private Button next_button;
    private TextView title_score;//账户积分或钱包余额
    private TextView buy_jf;//购买积分
    private TextView jifen_text;//积分数量
    private ImageView image_next;//三角号
    private TextView jf_or_mx;//积分或明细
    private LinearLayout proportion_layout;
   // private LinearLayout add_view;//
    private MyListView listview;
    private RefreshLoadmoreLayout refreshLoadmoreLayout;
    private Integer page = 0;
    private User user;
    private ArrayList<Reply> replies = new ArrayList<>();
    private IntegralDetailedAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_wallt);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        inIt();
        super.onResume();
    }

    /**
     * 初始化
     */
    private void inIt() {
        String token = JhctmApplication.getInstance().getUser().getToken();
        String userid = JhctmApplication.getInstance().getUser().getId();
        getNetWorker().clientGet(token, userid);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_GET:
                showProgressDialog("获取用户余额");
                break;
            case TRADE_LIST:
                showProgressDialog("获取交易记录");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_GET:
            case TRADE_LIST:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_GET:
                HemaArrayResult<User> result = (HemaArrayResult<User>) hemaBaseResult;
                user = result.getObjects().get(0);
                //账户余额
                if (isNull(user.getFeeaccount()))
                    jifen_text.setText("0");
                else
                    jifen_text.setText(user.getFeeaccount());
                String token = JhctmApplication.getInstance().getUser().getToken();
                getNetWorker().tradelist(token, "0");
                break;
            case TRADE_LIST:
                HemaPageArrayResult<Reply> result1 = (HemaPageArrayResult<Reply>) hemaBaseResult;
                ArrayList<Reply> replies = result1.getObjects();
                String page2 = hemaNetTask.getParams().get("page");
                if ("0".equals(page2)) {// 刷新
                    refreshLoadmoreLayout.refreshSuccess();
                    this.replies.clear();
                    this.replies.addAll(replies);

                    JhctmApplication application = JhctmApplication.getInstance();
                    int sysPagesize = application.getSysInitInfo()
                            .getSys_pagesize();
                    if (replies.size() < sysPagesize) {
                        refreshLoadmoreLayout.setLoadmoreable(false);
                        // leftRE = false;
                    } else {
                        refreshLoadmoreLayout.setLoadmoreable(true);
                        // leftRE = true;
                    }
                } else {// 更多
                    refreshLoadmoreLayout.loadmoreSuccess();
                    if (replies.size() > 0)
                        this.replies.addAll(replies);
                    else {
                        refreshLoadmoreLayout.setLoadmoreable(false);
                        // leftRE = false;
                        XtomToastUtil.showShortToast(this, "已经到最后啦");
                    }
                }
                freshData();
                break;
        }
    }

    private void freshData() {
        //商品列表
        if (adapter == null) {
            adapter = new IntegralDetailedAdapter(mContext, replies);
            adapter.setEmptyString("暂无数据");
            adapter.setReplies(replies);
            listview.setAdapter(adapter);
        } else {
            adapter.setEmptyString("暂无数据");
            adapter.setReplies(replies);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_GET:
                showTextDialog(hemaBaseResult.getMsg());
                break;
            case TRADE_LIST:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_GET:
                showTextDialog("获取用户余额失败，请稍后重试");
                break;
            case TRADE_LIST:
                String page = hemaNetTask.getParams().get("page");
                if ("0".equals(page)) {
                    refreshLoadmoreLayout.refreshFailed();
                } else {
                    refreshLoadmoreLayout.loadmoreFailed();
                }
                showTextDialog("获取交易记录失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (Button) findViewById(R.id.next_button);
        title_score = (TextView) findViewById(R.id.title_score);
        buy_jf = (TextView) findViewById(R.id.buy_jf);
        jifen_text = (TextView) findViewById(R.id.jifen_text);
        image_next = (ImageView) findViewById(R.id.image_next);
        jf_or_mx = (TextView) findViewById(R.id.jf_or_mx);
        proportion_layout = (LinearLayout) findViewById(R.id.proportion_layout);
       // add_view = (LinearLayout) findViewById(R.id.add_view);
        listview = (MyListView) findViewById(R.id.listview);
        refreshLoadmoreLayout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
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
        title_text.setText("现金钱包");
        next_button.setVisibility(View.INVISIBLE);
        title_score.setText("账户余额 (元)");
        buy_jf.setText("我要提现");
        image_next.setVisibility(View.INVISIBLE);
        proportion_layout.setVisibility(View.GONE);
      //  add_view.setVisibility(View.GONE);
        jf_or_mx.setText("交易明细");
        refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page=0;
                inIt();
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page++;
                String token = JhctmApplication.getInstance().getUser().getToken();
                getNetWorker().tradelist(token, String.valueOf(page));
            }
        });
        //我要提现
        buy_jf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyWalltActivity.this,WithdrawBankActivity.class);
                intent.putExtra("feecount",user.getFeeaccount());
                startActivity(intent);
            }
        });
    }
}
