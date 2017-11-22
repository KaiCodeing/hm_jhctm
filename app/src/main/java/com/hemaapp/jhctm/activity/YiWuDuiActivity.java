package com.hemaapp.jhctm.activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.hemaapp.jhctm.adapter.HomeGridAdapter;
import com.hemaapp.jhctm.adapter.TextSelectAdapter;
import com.hemaapp.jhctm.adapter.Type2ClassAdapter;
import com.hemaapp.jhctm.model.Blog1List;
import com.hemaapp.jhctm.model.DistrictInfor;
import com.hemaapp.jhctm.view.JhctmGridView;

import java.util.ArrayList;

import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * Created by lenovo on 2016/12/28.
 * 易物兑商城
 * pop:pop_select_type PopSelectClassAdapter
 */
public class YiWuDuiActivity extends JhActivity {
    private ImageButton back_button;
    private TextView title_text;
    private ImageButton next_button;
    private ProgressBar progressbar;
    private RefreshLoadmoreLayout refreshLoadmoreLayout;
    private JhctmGridView gridview;
    private TextView text1;
    private TextView text2;
    private TextView text3;
    private String type = "0";//0：分类 1：价格升序 2：价格倒序 3 销量加 4：销量减
    private ViewPop viewPop;
    private ArrayList<DistrictInfor> districtInfors1 = new ArrayList<>();
    private ArrayList<DistrictInfor> districtInfors2 = new ArrayList<>();
    private TextSelectAdapter adapter;
    private Type2ClassAdapter selectadapter;
    private PopupWindow popupWindow;
    private Integer page = 0;
    private String commodityId = "";
    private String maxprice = "";
    private String lowprice = "";
    private ArrayList<Blog1List> blog1Lists = new ArrayList<>();
    private HomeGridAdapter gridAdapter;
    private String goodId;
    private String goodName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_yiwudui);
        super.onCreate(savedInstanceState);
        inIt();
    }

    private void inIt() {
        String keytype = "";
        String orderby = "";
        if ("0".equals(type))
            keytype = "1";
        else if ("1".equals(type)) {
            keytype = "3";
            orderby = "2";
        } else if ("2".equals(type)) {
            keytype = "3";
            orderby = "1";
        } else if ("3".equals(type)) {
            keytype = "2";
            orderby = "2";
        } else if ("4".equals(type)) {
            keytype = "2";
            orderby = "1";
        }

        getNetWorker().blog1List3("", commodityId, "", keytype, orderby, lowprice, maxprice, "", String.valueOf(page));
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case BLOGTYPE_LIST:
                showProgressDialog("获取分类信息");
                break;
            case BLOG_1_LIST:
                showProgressDialog("获取商品列表");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case BLOGTYPE_LIST:
                cancelProgressDialog();
                break;
            case BLOG_1_LIST:
                cancelProgressDialog();
                refreshLoadmoreLayout.setVisibility(View.VISIBLE);
                progressbar.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case BLOGTYPE_LIST:
                HemaArrayResult<DistrictInfor> result = (HemaArrayResult<DistrictInfor>) hemaBaseResult;
                String parentid = hemaNetTask.getParams().get("parentid");
                if ("0".equals(parentid)) {
                    this.districtInfors1.clear();
                    this.districtInfors1 = result.getObjects();
                    int m = 0;
                    districtInfors1.get(0).setCheck(true);
                    for (DistrictInfor infor : districtInfors1
                            ) {
                        if (infor.isCheck())
                            m = Integer.valueOf(infor.getParentid());
                    }
                    if (m == 0) {
                        getNetWorker().blogtypeList(districtInfors1.get(0).getId());
                    } else
                        getNetWorker().blogtypeList(String.valueOf(m));
                } else {
                    this.districtInfors2.clear();
                    this.districtInfors2 = result.getObjects();
                    showSelete();
                    freshData();
                }
                break;
            case BLOG_1_LIST:
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
                freshData1();
                break;
        }
    }

    private void freshData1() {
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

    /**
     *
     */
    private void freshData() {
        //商品列表
        if (adapter == null) {
            adapter = new TextSelectAdapter(YiWuDuiActivity.this, districtInfors1);
            adapter.setEmptyString("暂无数据");
            viewPop.list1.setAdapter(adapter);
        } else {
            adapter.setEmptyString("暂无数据");
            adapter.setInfors(districtInfors1);
            adapter.notifyDataSetChanged();
        }
        if (selectadapter == null) {
            selectadapter = new Type2ClassAdapter(YiWuDuiActivity.this, districtInfors2);
            selectadapter.setEmptyString("暂无数据");
            viewPop.list2.setAdapter(selectadapter);
        } else {
            selectadapter.setInfors(districtInfors2);
            selectadapter.setEmptyString("暂无数据");
            selectadapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case BLOGTYPE_LIST:
                showTextDialog(hemaBaseResult.getMsg());
                break;
            case BLOG_1_LIST:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case BLOGTYPE_LIST:
                showTextDialog("获取分类信息失败，请稍后重试");
                break;
            case BLOG_1_LIST:
                String page = hemaNetTask.getParams().get("page");
                if ("0".equals(page)) {
                    refreshLoadmoreLayout.refreshFailed();
                } else {
                    refreshLoadmoreLayout.loadmoreFailed();
                }
                showTextDialog("获取商品失败，请稍后重试");
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
        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);
        text3 = (TextView) findViewById(R.id.text3);
    }

    @Override
    protected void getExras() {
        goodId = mIntent.getStringExtra("goodId");
        if (isNull(goodId))
            goodId = "";
        commodityId = mIntent.getStringExtra("commodityId");
        if (isNull(commodityId))
            commodityId = "";
        goodName = mIntent.getStringExtra("goodName");
    }

    @Override
    protected void setListener() {
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_text.setText("易物商城");
        next_button.setVisibility(View.INVISIBLE);
        if (!isNull(goodId)) {
            title_text.setText(goodName);
            text1.setText(goodName);
        }
        //分类点击
        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "0";
                text2.setTextColor(getResources().getColor(R.color.shaixuan));
                text2.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.defult_jiao_img), null);
                text3.setTextColor(getResources().getColor(R.color.shaixuan));
                text3.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.defult_jiao_img), null);
                text1.setTextColor(getResources().getColor(R.color.title_backgroid));
                text1.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.only_xia_img), null);
                if (isNull(goodId)) {
                    if (popupWindow != null && popupWindow.isShowing()) {
                        text2.setTextColor(getResources().getColor(R.color.shaixuan));
                        text2.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.defult_jiao_img), null);
                        text3.setTextColor(getResources().getColor(R.color.shaixuan));
                        text3.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.defult_jiao_img), null);
                        text1.setTextColor(getResources().getColor(R.color.title_backgroid));
                        text1.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.only_xia_img), null);
                        popupWindow.dismiss();
                        return;
                    }
                    if (districtInfors1 == null || districtInfors1.size() == 0)
                        getNetWorker().blogtypeList("0");
                    else {
                        showSelete();
                        freshData();
                    }
                } else {
                    inIt();
                }
            }
        });
        //价格
        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("1".equals(type)) {
                    type = "2";
                    text1.setTextColor(getResources().getColor(R.color.shaixuan));
                    text2.setTextColor(getResources().getColor(R.color.title_backgroid));
                    text3.setTextColor(getResources().getColor(R.color.shaixuan));
                    text1.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.only_jiao_img), null);
                    text3.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.defult_jiao_img), null);
                    text2.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.max_check_img), null);
                } else if ("2".equals(type)) {
                    type = "1";
                    text1.setTextColor(getResources().getColor(R.color.shaixuan));
                    text2.setTextColor(getResources().getColor(R.color.title_backgroid));
                    text3.setTextColor(getResources().getColor(R.color.shaixuan));
                    text1.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.only_jiao_img), null);
                    text3.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.defult_jiao_img), null);
                    text2.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.min_check_img), null);
                } else {
                    type = "1";
                    text1.setTextColor(getResources().getColor(R.color.shaixuan));
                    text2.setTextColor(getResources().getColor(R.color.title_backgroid));
                    text3.setTextColor(getResources().getColor(R.color.shaixuan));
                    text1.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.only_jiao_img), null);
                    text3.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.defult_jiao_img), null);
                    text2.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.min_check_img), null);
                }
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                page = 0;
                inIt();
            }
        });
        //销量
        text3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("3".equals(type)) {
                    type = "4";
                    text1.setTextColor(getResources().getColor(R.color.shaixuan));
                    text3.setTextColor(getResources().getColor(R.color.title_backgroid));
                    text2.setTextColor(getResources().getColor(R.color.shaixuan));
                    text1.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.only_jiao_img), null);
                    text2.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.defult_jiao_img), null);
                    text3.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.max_check_img), null);
                } else if ("4".equals(type)) {
                    type = "3";
                    text1.setTextColor(getResources().getColor(R.color.shaixuan));
                    text3.setTextColor(getResources().getColor(R.color.title_backgroid));
                    text2.setTextColor(getResources().getColor(R.color.shaixuan));
                    text1.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.only_jiao_img), null);
                    text2.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.defult_jiao_img), null);
                    text3.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.min_check_img), null);
                } else {
                    type = "3";
                    text1.setTextColor(getResources().getColor(R.color.shaixuan));
                    text3.setTextColor(getResources().getColor(R.color.title_backgroid));
                    text2.setTextColor(getResources().getColor(R.color.shaixuan));
                    text1.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.only_jiao_img), null);
                    text2.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.defult_jiao_img), null);
                    text3.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.min_check_img), null);
                }
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                page = 0;
                inIt();
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

    /**
     * pop:pop_select_type
     */
    private class ViewPop {
        ListView list1;
        ListView list2;
    }

    private void showSelete() {
        View view = LayoutInflater.from(YiWuDuiActivity.this).inflate(
                R.layout.pop_select_type, null);
        if (viewPop == null) {
            viewPop = new ViewPop();
            viewPop.list1 = (ListView) view.findViewById(R.id.list1);
            viewPop.list2 = (ListView) view.findViewById(R.id.list2);
        }
        if (popupWindow == null) {
            popupWindow = new PopupWindow(view,
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        }
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(false);
        popupWindow.setWidth(RadioGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(RadioGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new
                BitmapDrawable()
        );
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.showAsDropDown(findViewById(R.id.layout_view));
        popupWindow.setAnimationStyle(R.style.PopupAnimation);
        // popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    /**
     * 改变数据
     *
     * @param infor
     */
    public void selectData(DistrictInfor infor) {
        for (DistrictInfor disinfor : districtInfors1
                ) {
            if (infor.getId().equals(disinfor.getId())) {
                disinfor.setCheck(true);
            } else
                disinfor.setCheck(false);
        }
        getNetWorker().blogtypeList(String.valueOf(infor.getId()));
    }

    /**
     * 选择分类
     */
    public void selectType(DistrictInfor infor) {
        type = "0";
        text2.setTextColor(getResources().getColor(R.color.shaixuan));
        text2.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.defult_jiao_img), null);
        text3.setTextColor(getResources().getColor(R.color.shaixuan));
        text3.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.defult_jiao_img), null);
        text1.setTextColor(getResources().getColor(R.color.title_backgroid));
        text1.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.only_xia_img), null);
        popupWindow.dismiss();
        commodityId = infor.getId();
        page = 0;
        inIt();
    }
}
