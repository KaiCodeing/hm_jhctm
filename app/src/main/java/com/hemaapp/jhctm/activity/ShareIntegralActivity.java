package com.hemaapp.jhctm.activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
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
import com.hemaapp.jhctm.adapter.ShareIntegralAdapter;
import com.hemaapp.jhctm.model.Account;
import com.hemaapp.jhctm.model.Reply;
import com.hemaapp.jhctm.view.MyListView;

import java.util.ArrayList;

import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * Created by lenovo on 2017/1/3.
 * 兑换币积分
 * IntegralDetailedAdapter
 * pop: pop_input_jf_number
 */
public class ShareIntegralActivity extends JhActivity {
    private ImageButton back_button;
    private TextView title_text;
    private ImageButton next_button;
    private TextView integral_number;//积分数量
    private TextView change_pwd;//兑换成兑换券金积分
    private TextView proportion;//比例
    private MyListView listview;//
    private RefreshLoadmoreLayout refreshLoadmoreLayout;
    private ProgressBar progressbar;
    private Account account;
    private Integer page = 0;
    private ArrayList<Reply> replies = new ArrayList<>();
    private ShareIntegralAdapter adapter;
    private ViewHolder holder;
    private LinearLayout layout_show;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_share_integral);
        super.onCreate(savedInstanceState);
        inIt();
    }
    /**
     * 初始化
     */
    private void inIt() {
        String token = JhctmApplication.getInstance().getUser().getToken();
        getNetWorker().accountGet(token);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ACCOUNT_GET:
              //  showProgressDialog("获取账户详情");
                break;
            case SHARE_SCORE_LIST:
             //   showProgressDialog("获取兑换币明细");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ACCOUNT_GET:
                cancelProgressDialog();
                refreshLoadmoreLayout.setVisibility(View.VISIBLE);
                progressbar.setVisibility(View.GONE);
                break;
            case SHARE_SCORE_LIST:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ACCOUNT_GET:
                HemaArrayResult<Account> result = (HemaArrayResult<Account>) hemaBaseResult;
                account = result.getObjects().get(0);
                integral_number.setText(account.getShare_score());
                layout_show.setVisibility(View.GONE);
                proportion.setText("兑换比例"+account.getShare2gold()+":1,后台可设置");
                proportion.setVisibility(View.GONE);
                page = 0;
                String token = JhctmApplication.getInstance().getUser().getToken();
                getNetWorker().shareScoreList(token,String.valueOf(page));
                break;
            case SHARE_SCORE_LIST:
                HemaPageArrayResult<Reply> result3 = (HemaPageArrayResult<Reply>) hemaBaseResult;
                ArrayList<Reply> replies = result3.getObjects();
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
        if (adapter == null) {
            adapter = new ShareIntegralAdapter(mContext, replies);
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
        switch (information) {
            case ACCOUNT_GET:
            case SHARE_SCORE_LIST:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ACCOUNT_GET:
                showTextDialog("获取账户详情失败，请稍后重试");
                break;
            case SHARE_SCORE_LIST:

                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (ImageButton) findViewById(R.id.next_button);
        listview = (MyListView) findViewById(R.id.listview);
        refreshLoadmoreLayout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        integral_number = (TextView) findViewById(R.id.integral_number);
        proportion = (TextView) findViewById(R.id.proportion);
        layout_show = (LinearLayout) findViewById(R.id.layout_show);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                finish();
            }
        });
        title_text.setText("分享币");
        next_button.setVisibility(View.INVISIBLE);
        refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page=0;
                String token = JhctmApplication.getInstance().getUser().getToken();
                getNetWorker().shareScoreList(token,String.valueOf(page));
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page++;
                String token = JhctmApplication.getInstance().getUser().getToken();
                getNetWorker().shareScoreList(token,String.valueOf(page));
            }
        });
    }

    private class ViewHolder {
        EditText input_text;
        TextView content_no;//取消
        TextView content_yes;//确定

    }
    private void showView()
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.pop_input_jf_number, null);
        holder = new ViewHolder();
        holder.content_no = (TextView) view.findViewById(R.id.content_no);
        holder.content_yes = (TextView) view.findViewById(R.id.content_yes);
        holder.input_text = (EditText) view.findViewById(R.id.input_text);

        final PopupWindow popupWindow = new PopupWindow(view,
                RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT);
        holder.content_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        holder.content_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = JhctmApplication.getInstance().getUser().getToken();
                String content = holder.input_text.getText().toString().trim();
                if (isNull(content))
                {
                    showTextDialog("请填写兑换积分数量");
                    return;
                }

            }
        });
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setWidth(RadioGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(RadioGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new
                BitmapDrawable()
        );
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // popupWindow.showAsDropDown(findViewById(R.id.ll_item));
        popupWindow.setAnimationStyle(R.style.PopupAnimation);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }
}
