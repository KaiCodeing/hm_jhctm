package com.hemaapp.jhctm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.jhctm.JhActivity;
import com.hemaapp.jhctm.JhHttpInformation;
import com.hemaapp.jhctm.JhctmApplication;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.adapter.ScoreTimeAdapter;
import com.hemaapp.jhctm.model.Account;
import com.hemaapp.jhctm.model.ScoreTime;
import com.hemaapp.jhctm.view.TwoBtnDialog;

import java.util.ArrayList;

import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * Created by lenovo on 2017/1/4.
 * 账户积分
 * pop:pop_split_view
 */
public class AccountIntegrationActivity extends JhActivity {
    private ImageButton back_button;
    private TextView title_text;
    private Button next_button;
    private TextView jifen_text;//剩余积分
    private TextView buy_jf;//购买积分
    private Integer currentPage=0;
    private LinearLayout goto_all_layout;//兑换成兑换券积分
    private TextView proportion;//兑换比例
    private LinearLayout proportion_layout;//兑换比例的layout
    private LinearLayout add_view;//添加的用户积分进程
    String token;
    private ScoreTimeAdapter adapter;
    private XtomRefreshLoadmoreLayout layout;
    private ListView list;
    private View show_nolist;
    private ArrayList<ScoreTime> infos1=new ArrayList<ScoreTime>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_account_integration);
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
        token = JhctmApplication.getInstance().getUser().getToken();
        getNetWorker().accountGet(token);
    }
    private void getlist(Integer page)
    {
        getNetWorker().score_reabt_list(token,page.toString());
    }
    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ACCOUNT_GET:
                showProgressDialog("获取账户详情");
                break;
            default:
                showProgressDialog("");
                break;

        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ACCOUNT_GET:
                cancelProgressDialog();
                break;
            default:
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
                jifen_text.setText(result.getObjects().get(0).getScore());
                proportion.setText("兑换比例"+result.getObjects().get(0).getScore2gold()+":1，后台可设置");
                getlist(currentPage);
                break;
            case SCORE_TO_GOLDSCORE:
                inIt();
                break;
            case SCORE_REABT_LIST:
                HemaPageArrayResult<ScoreTime> iResult = (HemaPageArrayResult<ScoreTime>) hemaBaseResult;
                //adapter.notifyDataSetChanged();
                if("0".equals(currentPage.toString()))
                {layout.refreshSuccess();
                    infos1.clear();
                    infos1.addAll(iResult.getObjects());
                    int sysPagesize = getApplicationContext().getSysInitInfo()
                            .getSys_pagesize();
                    if (infos1.size() < sysPagesize)
                        layout.setLoadmoreable(false);
                    else
                        layout.setLoadmoreable(true);
                }
                else {// 更多
                    layout.loadmoreSuccess();
                    if (iResult.getObjects().size() > 0)
                        this.infos1.addAll(iResult.getObjects());
                    else {
                        layout.setLoadmoreable(false);
                        XtomToastUtil.showShortToast(mContext,"已经到最后啦");
                    }
                }
                //adapter.notifyDataSetChanged();
                if(infos1.size()<1)
                {
                    show_nolist.setVisibility(View.VISIBLE);
                    layout.setVisibility(View.GONE);
                }else {
                    show_nolist.setVisibility(View.GONE);
                    layout.setVisibility(View.VISIBLE);
                    freshData();
                }
                break;

        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ACCOUNT_GET:
                showTextDialog(hemaBaseResult.getMsg());
                break;
            default:
                showTextDialog(hemaBaseResult.getMsg());
                break;

        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ACCOUNT_GET:
                showTextDialog("数据出错");
                break;
            default:
                showTextDialog("数据出错");
                break;

        }
    }
    private void freshData() {
        if (adapter == null) {
            adapter=new ScoreTimeAdapter(this,infos1,"1");
            adapter.setEmptyString("暂无数据");
            list.setAdapter(adapter);
        } else {
            adapter.setEmptyString("暂无数据");
            adapter.notifyDataSetChanged();
        }
    }
    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (Button) findViewById(R.id.next_button);
        jifen_text = (TextView) findViewById(R.id.jifen_text);
        buy_jf = (TextView) findViewById(R.id.buy_jf);
        goto_all_layout = (LinearLayout) findViewById(R.id.goto_all_layout);
        proportion = (TextView) findViewById(R.id.proportion);
        proportion_layout = (LinearLayout) findViewById(R.id.proportion_layout);
        add_view = (LinearLayout) findViewById(R.id.add_view);
        list=(ListView)findViewById(R.id.listview);
        show_nolist=findViewById(R.id.show_nolist);
        layout=(XtomRefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
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
        title_text.setText("账户消费积分");
        next_button.setText("消费积分明细");
        buy_jf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountIntegrationActivity.this,BuyCarPayActivity.class);
                intent.putExtra("fromtype",1);
                startActivity(intent);
            }
        });
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AccountIntegrationActivity.this,JifenRecord.class);
                startActivity(intent);
            }
        });
        layout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {

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
        goto_all_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwoBtnDialog dialog=new TwoBtnDialog(AccountIntegrationActivity.this);
                dialog.setSecondTextVisible(false);
                dialog.setButtonListener(new TwoBtnDialog.OnButtonListener() {
                    @Override
                    public void onLeftButtonClick(TwoBtnDialog dialog) {
                        dialog.cancel();
                    }
                    @Override
                    public void onRightButtonClick(TwoBtnDialog dialog) {
                        if(isNull(dialog.getmTextView().getText().toString()))
                        {
                            showTextDialog("请输入数量");
                            return;
                        }
                     getNetWorker().score_to_goldscore(token,dialog.getmTextView().getText().toString());
                        dialog.cancel();
                    }
                });
            }
        });
    }
}
