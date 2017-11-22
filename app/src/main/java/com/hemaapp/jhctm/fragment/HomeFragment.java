package com.hemaapp.jhctm.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.hm_FrameWork.view.RefreshLoadmoreLayout;
import com.hemaapp.jhctm.JhFragment;
import com.hemaapp.jhctm.JhHttpInformation;
import com.hemaapp.jhctm.JhLocation;
import com.hemaapp.jhctm.JhctmApplication;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.activity.BrandbusinessActivity;
import com.hemaapp.jhctm.activity.City0Activity;
import com.hemaapp.jhctm.activity.LimitExchangeActivity;
import com.hemaapp.jhctm.activity.MyXiaoXi;
import com.hemaapp.jhctm.activity.SearchActivity;
import com.hemaapp.jhctm.activity.ShareGoodsActivity;
import com.hemaapp.jhctm.activity.YiWuDuiActivity;
import com.hemaapp.jhctm.adapter.HomeGridAdapter;
import com.hemaapp.jhctm.adapter.TopAdAdapter;
import com.hemaapp.jhctm.model.AdList;
import com.hemaapp.jhctm.model.Blog1List;
import com.hemaapp.jhctm.model.User;
import com.hemaapp.jhctm.view.JhViewPager;
import com.hemaapp.jhctm.view.JhctmGridView;

import java.util.ArrayList;

import xtom.frame.util.XtomSharedPreferencesUtil;
import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * Created by lenovo on 2016/12/20.
 * 首页
 * AddView表头 addview_home_top
 */
public class HomeFragment extends JhFragment {
    private String locationCity;
    private LoacationReceiver loacationReceiver;
    private TextView city_name;//城市名
    private TextView search_log;//搜索
    private ImageView message_view;
    private JhViewPager adviewpager;
    private  RadioGroup radiogroup;
    private ImageView pinpai_image;//品牌商
    private ImageView chanping_image;//限时兑
    private ImageView ershou_image;//分享专区
    private ImageView pintai_image;//易物兑商城
    private RefreshLoadmoreLayout refreshLoadmoreLayout;
    private ProgressBar progressbar;
    private JhctmGridView gridview;
    private ArrayList<AdList> adLists =new ArrayList<>();//广告
    private TopAdAdapter adAdapter;
    private HomeGridAdapter gridAdapter;
    private Integer page = 0;
    private FrameLayout vp_top;
    private ArrayList<Blog1List> blog1Lists = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_home);
        super.onCreate(savedInstanceState);
        getLocationCity();
    }

    @Override
    public void onResume() {
        //获取用户未读消息条数
        getNetWorker().clientGet(JhctmApplication.getInstance().getUser().getToken(), JhctmApplication.getInstance().getUser().getId());
        super.onResume();
    }

    // 获取定位城市
    private void getLocationCity() {
        AMapLocation location = JhLocation.getInstance().getaMapLocation();
        if (location != null)
            locationCity = location.getCity();
        if (isNull(locationCity)) {
            JhctmApplication application = JhctmApplication.getInstance();
            String action = application.getPackageName() + ".location";
            IntentFilter mFilter = new IntentFilter(action);
            loacationReceiver = new LoacationReceiver();
           getActivity().registerReceiver(loacationReceiver, mFilter);
        } else {
            city_name.setText(locationCity);
            XtomSharedPreferencesUtil.save(getContext(),"locationCity",locationCity);
        }
    }
    private class LoacationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            JhctmApplication application = JhctmApplication.getInstance();
            String realAction = application.getPackageName() + ".location";
            String action = intent.getAction();
            if (realAction.equals(action)) {
                locationCity = JhLocation.getInstance().getaMapLocation()
                        .getCity();
//                mLocationAdapter.setLocCity(locationCity);
//                if (mCityList.getAdapter() != null)
//                    mLocationAdapter.notifyDataSetChanged();
                if (isNull(locationCity))
                {  city_name.setText("定位中");
                    XtomSharedPreferencesUtil.save(getContext(),"locationCity","");
                }
                else {
                    city_name.setText(locationCity);
                    XtomSharedPreferencesUtil.save(getContext(),"locationCity",locationCity);
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (getActivity().RESULT_OK != resultCode) {
            return;
        }
        switch (requestCode) {
            case 1:
                String cityName = data.getStringExtra("name");
                city_name.setText(cityName);
                XtomSharedPreferencesUtil.save(getContext(),"cityName",cityName);
                break;
        }
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case CLIENT_GET:
           //     showProgressDialog("获取用户信息");
                break;
            case AD_LIST:
              //  showProgressDialog("获取广告信息");
                break;
            case BLOG_1_LIST:
               // showProgressDialog("获取商品列表");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case CLIENT_GET:
            case AD_LIST:
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
        switch (information)
        {
            case CLIENT_GET:
                HemaArrayResult<User> userArrayResult = (HemaArrayResult<User>) hemaBaseResult;
                User user = userArrayResult.getObjects().get(0);
                if (user!=null)
                {
                    if (user.getNoticecount().equals("0"))
                    {
                        message_view.setImageResource(R.mipmap.emil_img_off);
                    }
                    else {
                        message_view.setImageResource(R.mipmap.emil_img_on);
                    }
                }
                else
                {
                    message_view.setImageResource(R.mipmap.emil_img_off);
                }
                getNetWorker().adList();
                break;
           case AD_LIST:
                HemaArrayResult<AdList> result = (HemaArrayResult<AdList>) hemaBaseResult;
                adLists = result.getObjects();
                 page = 0;
                getNetWorker().blog1List("1","","","",String.valueOf(page));
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
                        XtomToastUtil.showShortToast(getActivity(), "已经到最后啦");
                    }
                }
                freshData();
                break;
        }
    }
    private void freshData() {
        //商品列表
        if (gridAdapter == null) {
            gridAdapter = new HomeGridAdapter(getContext(), blog1Lists);
            gridAdapter.setEmptyString("暂无数据");
            gridview.setAdapter(gridAdapter);
        } else {
            gridAdapter.setEmptyString("暂无数据");
            gridAdapter.notifyDataSetChanged();
        }
        //广告列表
        adAdapter = new TopAdAdapter(HomeFragment.this, radiogroup, vp_top, adLists);
        adviewpager.setAdapter(adAdapter);
        adviewpager.setOnPageChangeListener(new PageChangeListener());
    }
    private class PageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int arg0) {
            if (adAdapter != null) {
                ViewGroup indicator = adAdapter.getIndicator();
                if (indicator != null) {
                    RadioButton rbt = (RadioButton) indicator.getChildAt(arg0);
                    if (rbt != null)
                        rbt.setChecked(true);
                }
            }
        }

    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case CLIENT_GET:
            case AD_LIST:
            case BLOG_1_LIST:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case CLIENT_GET:
                showTextDialog("获取用户信息失败，请稍后重试");
                break;
            case AD_LIST:
                showTextDialog("获取广告信息失败，请稍后重试");
                break;
            case BLOG_1_LIST:
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
        city_name = (TextView) findViewById(R.id.city_name);
        search_log = (TextView) findViewById(R.id.search_log);
        message_view = (ImageView) findViewById(R.id.message_view);
        adviewpager = (JhViewPager) findViewById(R.id.adviewpager);
        radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
        pinpai_image = (ImageView) findViewById(R.id.pinpai_image);
        chanping_image = (ImageView) findViewById(R.id.chanping_image);
        ershou_image = (ImageView) findViewById(R.id.ershou_image);
        pintai_image = (ImageView) findViewById(R.id.pintai_image);
        refreshLoadmoreLayout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        gridview = (JhctmGridView) findViewById(R.id.gridview);
        vp_top = (FrameLayout) findViewById(R.id.vp_top);
    }

    @Override
    protected void setListener() {
        //刷新
        refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
              page = 0;
                getNetWorker().clientGet(JhctmApplication.getInstance().getUser().getToken(), JhctmApplication.getInstance().getUser().getId());
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page++;
                getNetWorker().blog1List("1","","","",String.valueOf(page));
            }
        });
        //选择城市
        city_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), City0Activity.class);
                HomeFragment.this.startActivityForResult(intent,1);
            }
        });
        //分享商品
        ershou_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShareGoodsActivity.class);
                startActivity(intent);
            }
        });
        //限时兑换
        chanping_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LimitExchangeActivity.class);
                startActivity(intent);
            }
        });
        //品牌商
        pinpai_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BrandbusinessActivity.class);
                startActivity(intent);
            }
        });
        //搜索
        search_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                getActivity().startActivity(intent);
            }
        });
        //易物兑商城
        pintai_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), YiWuDuiActivity.class);
                getActivity().startActivity(intent);
            }
        });
        message_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyXiaoXi.class);
                getActivity().startActivity(intent);
            }
        });
    }
}
