package com.hemaapp.jhctm.activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.hm_FrameWork.view.RefreshLoadmoreLayout;
import com.hemaapp.jhctm.JhActivity;
import com.hemaapp.jhctm.JhctmApplication;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.adapter.FeeezeListAdapter;
import com.hemaapp.jhctm.model.Rebat;

import java.util.ArrayList;

import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * Created by lenovo on 2017/4/10.
 * 冻结积分列表
 */
public class FreezeListActivity extends JhActivity {
    private ImageButton back_button;
    private TextView title_text;
    private ImageButton next_button;
    private RefreshLoadmoreLayout refreshLoadmoreLayout;
    private XtomListView listview;
    private ProgressBar progressbar;
    private ViewHolder holder;
    private Integer page = 0;
    private FeeezeListAdapter adapter;
    private String type = "0";
    private ArrayList<Rebat> rebats = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_freeze_list);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        inIt();
        super.onResume();
    }

    private void inIt() {
        String token = JhctmApplication.getInstance().getUser().getToken();
        getNetWorker().goldRebatList(token, type, String.valueOf(page));
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        showProgressDialog("获取信息...");
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        cancelProgressDialog();
        refreshLoadmoreLayout.setVisibility(View.VISIBLE);
        progressbar.setVisibility(View.GONE);
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        HemaPageArrayResult<Rebat> result3 = (HemaPageArrayResult<Rebat>) hemaBaseResult;
        ArrayList<Rebat> rebats = result3.getObjects();
        String page2 = hemaNetTask.getParams().get("page");
        if ("0".equals(page2)) {// 刷新
            refreshLoadmoreLayout.refreshSuccess();
            this.rebats.clear();
            this.rebats.addAll(rebats);

            JhctmApplication application = JhctmApplication.getInstance();
            int sysPagesize = application.getSysInitInfo()
                    .getSys_pagesize();
            if (rebats.size() < sysPagesize) {
                refreshLoadmoreLayout.setLoadmoreable(false);
                // leftRE = false;
            } else {
                refreshLoadmoreLayout.setLoadmoreable(true);
                // leftRE = true;
            }
        } else {// 更多
            refreshLoadmoreLayout.loadmoreSuccess();
            if (rebats.size() > 0)
                this.rebats.addAll(rebats);
            else {
                refreshLoadmoreLayout.setLoadmoreable(false);
                // leftRE = false;
                XtomToastUtil.showShortToast(mContext, "已经到最后啦");
            }
        }
        freshData();
    }

    private void freshData() {
        if (adapter == null) {
            adapter = new FeeezeListAdapter(mContext, rebats);
            adapter.setEmptyString("暂无数据");
            listview.setAdapter(adapter);
        } else {
            adapter.setEmptyString("暂无数据");
            adapter.setRebats(rebats);
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        showTextDialog(hemaBaseResult.getMsg());
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        String page = hemaNetTask.getParams().get("page");
        if ("0".equals(page)) {
            refreshLoadmoreLayout.refreshFailed();
        } else {
            refreshLoadmoreLayout.loadmoreFailed();
        }
        showProgressDialog("获取冻结积分失败，请稍后重试");
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (ImageButton) findViewById(R.id.next_button);
        refreshLoadmoreLayout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        listview = (XtomListView) findViewById(R.id.listview);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
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
        title_text.setText("冻结金积分");
        next_button.setVisibility(View.INVISIBLE);
        title_text.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.freeze_jian_img), null);
        title_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelect();
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
    }

    //选择类型
    private class ViewHolder {
        TextView all_type;
        TextView dai_free_type;
        TextView to_free_type;
        TextView over_type;
    }

    private void showSelect() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.pop_freeze_type, null);
        holder = new ViewHolder();
        holder.all_type = (TextView) view.findViewById(R.id.all_type);
        holder.dai_free_type = (TextView) view.findViewById(R.id.dai_free_type);
        holder.to_free_type = (TextView) view.findViewById(R.id.to_free_type);
        holder.over_type = (TextView) view.findViewById(R.id.over_type);
        final PopupWindow popupWindow = new PopupWindow(view,
                RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT);
        //选择全部
        holder.all_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                type = "0";
                page = 0;
                inIt();
            }
        });
        //选择待解冻
        holder.dai_free_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                type = "1";
                page = 0;
                inIt();
            }
        });
        //选择解冻中
        holder.to_free_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                type = "2";
                page = 0;
                inIt();
            }
        });
        //已完成
        holder.over_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                type = "3";
                page = 0;
                inIt();
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
        popupWindow.showAsDropDown(findViewById(R.id.title_text));
        popupWindow.setAnimationStyle(R.style.PopupAnimation);
        //   popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }
}
