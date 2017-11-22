package com.hemaapp.jhctm.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.hemaapp.jhctm.adapter.MyCardPackageAdapter;
import com.hemaapp.jhctm.model.Account;
import com.hemaapp.jhctm.model.Card;

import java.util.ArrayList;

import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * Created by lenovo on 2017/1/4.
 * 我的卡包
 * MyCardPackageAdapter
 */
public class MyCardPackageActivity extends JhActivity {
    private ImageButton back_button;
    private TextView title_text;
    private ImageButton next_button;
    private ProgressBar progressbar;
    private RefreshLoadmoreLayout refreshLoadmoreLayout;
    private XtomListView listview;
    private FrameLayout buy_quan_layout;//合成消费商卡
    private ImageView go_to_web;
    private Integer page = 0;
    private ArrayList<Card> cards = new ArrayList<>();
    private MyCardPackageAdapter adapter;
    private ViewHolder holder;
    private String money;
    private comHolder comholder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_card_package);
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
        getNetWorker().accountGet(token);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CARD_LIST:
                showProgressDialog("获取卡片信息");
                break;
            case CARD_REMOVE:
                showProgressDialog("拆分卡中");
                break;
            case ACCOUNT_GET:
                showProgressDialog("获取账户详情");
                break;
            case FEEACCOUNT_REMOVE:
                showProgressDialog("合成卡片中");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CARD_LIST:
                cancelProgressDialog();
                progressbar.setVisibility(View.GONE);
                refreshLoadmoreLayout.setVisibility(View.VISIBLE);
                break;
            case CARD_REMOVE:
            case ACCOUNT_GET:
            case FEEACCOUNT_REMOVE:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CARD_LIST:
                HemaPageArrayResult<Card> result3 = (HemaPageArrayResult<Card>) hemaBaseResult;
                ArrayList<Card> cards = result3.getObjects();
                String page2 = hemaNetTask.getParams().get("page");
                if ("0".equals(page2)) {// 刷新
                    refreshLoadmoreLayout.refreshSuccess();
                    this.cards.clear();
                    this.cards.addAll(cards);

                    JhctmApplication application = JhctmApplication.getInstance();
                    int sysPagesize = application.getSysInitInfo()
                            .getSys_pagesize();
                    if (cards.size() < sysPagesize) {
                        refreshLoadmoreLayout.setLoadmoreable(false);
                        // leftRE = false;
                    } else {
                        refreshLoadmoreLayout.setLoadmoreable(true);
                        // leftRE = true;
                    }
                } else {// 更多
                    refreshLoadmoreLayout.loadmoreSuccess();
                    if (cards.size() > 0)
                        this.cards.addAll(cards);
                    else {
                        refreshLoadmoreLayout.setLoadmoreable(false);
                        // leftRE = false;
                        XtomToastUtil.showShortToast(this, "已经到最后啦");
                    }
                }
                freshData();
                break;
            case CARD_REMOVE:
                showTextDialog("恭喜您，卡片拆分成功");
                page = 0;
                String token2 = JhctmApplication.getInstance().getUser().getToken();
                getNetWorker().cardList(token2, "0");
                break;
            case ACCOUNT_GET:
                HemaArrayResult<Account> result = (HemaArrayResult<Account>) hemaBaseResult;
                Account account = result.getObjects().get(0);
                money = account.getMixcard_price();
                String token = JhctmApplication.getInstance().getUser().getToken();
                getNetWorker().cardList(token, "0");
                break;
            case FEEACCOUNT_REMOVE:
                page = 0;
                showTextDialog("恭喜您，合成卡片成功");
                String token1 = JhctmApplication.getInstance().getUser().getToken();
                getNetWorker().cardList(token1, "0");
                break;
        }
    }

    private void freshData() {
        if (adapter == null) {
            adapter = new MyCardPackageAdapter(MyCardPackageActivity.this, cards);
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
            case CARD_LIST:
            case CARD_REMOVE:
            case FEEACCOUNT_REMOVE:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CARD_LIST:
                showTextDialog("获取卡片信息失败，请稍后重试");
                String page = hemaNetTask.getParams().get("page");
                if ("0".equals(page)) {
                    refreshLoadmoreLayout.refreshFailed();
                } else {
                    refreshLoadmoreLayout.loadmoreFailed();
                }
                break;
            case CARD_REMOVE:
                showTextDialog("拆分卡片失败，请稍后重试");
                break;
            case FEEACCOUNT_REMOVE:
                showTextDialog("合成卡片失败，请稍后重试");
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
        buy_quan_layout = (FrameLayout) findViewById(R.id.buy_quan_layout);
        go_to_web = (ImageView) findViewById(R.id.go_to_web);

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
        title_text.setText("我的卡包");
        next_button.setVisibility(View.INVISIBLE);
        refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                String token = JhctmApplication.getInstance().getUser().getToken();
                page = 0;
                getNetWorker().cardList(token, "0");
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page++;
                String token = JhctmApplication.getInstance().getUser().getToken();
                getNetWorker().cardList(token, String.valueOf(page));
            }
        });
        //合成卡
        buy_quan_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hechengShow();
            }
        });
        //问号
        go_to_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyCardPackageActivity.this, JhWebView.class);
                intent.putExtra("type", "2");
                startActivity(intent);
            }
        });
    }

    //拆分
    public void removeCard(String cardid) {
        showView(cardid);
    }

    private class ViewHolder {
        TextView content_no;//取消
        TextView content_yes;//确定
    }

    //展示是否拆分
    private void showView(final String cardid) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.pop_split_view, null);
        holder = new ViewHolder();
        holder.content_no = (TextView) view.findViewById(R.id.content_no);
        holder.content_yes = (TextView) view.findViewById(R.id.content_yes);
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
                popupWindow.dismiss();
                String token = JhctmApplication.getInstance().getUser().getToken();
                getNetWorker().cardRemove(token, cardid);

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

    //合成卡片
    private class comHolder {
        TextView content_text;
        TextView content_no;
        TextView content_yes;
    }

    private void hechengShow() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.pop_left_right_view, null);
        comholder = new comHolder();
        comholder.content_no = (TextView) view.findViewById(R.id.content_no);
        comholder.content_yes = (TextView) view.findViewById(R.id.content_yes);
        comholder.content_text = (TextView) view.findViewById(R.id.content_text);
        comholder.content_text.setText("确定要花费\n" + "1000积分+" + money + "元合成卡片吗?");
        final PopupWindow popupWindow = new PopupWindow(view,
                RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT);
        comholder.content_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        comholder.content_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String token = JhctmApplication.getInstance().getUser().getToken();
//                getNetWorker().feeaccountRemove(token, "6", "0", "", money, "0", "0");
                popupWindow.dismiss();
                Intent intent = new Intent(MyCardPackageActivity.this,PayTwoTypeActivity.class);
                intent.putExtra("type","6");
                intent.putExtra("total_score","1000");
                intent.putExtra("total_fee",money);
                intent.putExtra("billId","1");
                startActivity(intent);

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
