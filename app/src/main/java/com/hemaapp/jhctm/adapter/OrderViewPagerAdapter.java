package com.hemaapp.jhctm.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.HemaNetWorker;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.hm_FrameWork.view.RefreshLoadmoreLayout;
import com.hemaapp.jhctm.JhHttpInformation;
import com.hemaapp.jhctm.JhNetTaskExecuteListener;
import com.hemaapp.jhctm.JhNetWorker;
import com.hemaapp.jhctm.JhctmApplication;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.activity.OrderActivity;
import com.hemaapp.jhctm.model.OrderListInfor;
import com.hemaapp.jhctm.model.User;

import java.util.ArrayList;

import xtom.frame.XtomObject;
import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * Created by WangYuxia on 2017/2/10.
 */
public class OrderViewPagerAdapter extends PagerAdapter {

    private ArrayList<Params> paramsList;
    private OrderActivity mContext;
    private Params currParams;
    private String tabTitles[] = new String[]{"全部", "待付款", "待收货", "待评价", "已完成"};
    private String keytype;
    private ArrayList<RefreshLoadmoreLayout> layouts = new ArrayList<>();
    public OrderViewPagerAdapter(OrderActivity mContext, ArrayList<Params> paramsList, String keytype) {
        this.paramsList = paramsList;
        this.mContext = mContext;
        this.keytype = keytype;
    }

    @Override
    public int getCount() {
        return paramsList == null ? 0 : paramsList.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        currParams = paramsList.get(position);
        View view = (View) object;
        if (currParams.isFirstSetPrimary) {
            RefreshLoadmoreLayout layout = (RefreshLoadmoreLayout) view
                    .findViewById(R.id.refreshLoadmoreLayout);
            layout.getOnStartListener().onStartRefresh(layout);
            currParams.isFirstSetPrimary = false;
        }
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = container.getChildAt(position);
        Params params = paramsList.get(position);
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.pageritem_rllistview_progress,
                    null);
            RefreshLoadmoreLayout layout = (RefreshLoadmoreLayout) view
                    .findViewById(R.id.refreshLoadmoreLayout);
            layouts.add(layout);
            JhNetWorker netWorker = new JhNetWorker(mContext);
            netWorker.setOnTaskExecuteListener(new OnTaskExecuteListener(
                    mContext, view, params, netWorker));
            view.setTag(netWorker);
            layout.setOnStartListener(new OnStartListener(params, netWorker,
                    view));
            container.addView(view);
        }
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }

    private class OnTaskExecuteListener extends JhNetTaskExecuteListener {
        private XtomRefreshLoadmoreLayout layout;
        private XtomListView listView;
        private ProgressBar progressBar;

        private ArrayList<OrderListInfor> goodsList = new ArrayList<>();
        private OrderListAdapter adapter;
        private OrderActivity activity;
        private JhNetWorker netWorker;
        private Params params;
        private User user;

        private OnTaskExecuteListener(OrderActivity mContext, View v, Params params,
                                      JhNetWorker netWorker) {
            super(mContext);
            this.activity = mContext;
            this.layout = (XtomRefreshLoadmoreLayout) v
                    .findViewById(R.id.refreshLoadmoreLayout);
            this.progressBar = (ProgressBar) v.findViewById(R.id.progressbar);
            this.listView = (XtomListView) layout.findViewById(R.id.listview);
            this.netWorker = netWorker;
            this.params = params;
            user = JhctmApplication.getInstance().getUser();
        }

        @Override
        public void onPreExecute(HemaNetWorker netWorker, HemaNetTask netTask) {
            JhHttpInformation information = (JhHttpInformation) netTask
                    .getHttpInformation();
            switch (information) {
                case BILL_LIST:
                    break;
                case BILL_SAVEOPERATE:
                    activity.showProgressDialog("正在操作...");
                    break;
            }
        }

        @Override
        public void onPostExecute(HemaNetWorker netWorker, HemaNetTask netTask) {
            JhHttpInformation information = (JhHttpInformation) netTask
                    .getHttpInformation();
            switch (information) {
                case BILL_LIST:
                    progressBar.setVisibility(View.GONE);
                    layout.setVisibility(View.VISIBLE);
                    break;
                case BILL_SAVEOPERATE:
                    activity.cancelProgressDialog();
                    break;
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onServerSuccess(HemaNetWorker netWorker,
                                    HemaNetTask netTask, HemaBaseResult baseResult) {
            JhHttpInformation information = (JhHttpInformation) netTask
                    .getHttpInformation();
            switch (information) {
                case BILL_LIST:
                    String page = netTask.getParams().get("page");
                    HemaPageArrayResult<OrderListInfor> gResult = (HemaPageArrayResult<OrderListInfor>) baseResult;
                    ArrayList<OrderListInfor> notices = gResult.getObjects();
                    if ("0".equals(page)) {// 刷新
                        layout.refreshSuccess();
                        goodsList.clear();
                        goodsList.addAll(notices);

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
                            goodsList.addAll(notices);
                        else {
                            layout.setLoadmoreable(false);
                            XtomToastUtil.showShortToast(mContext, "已经到最后啦");
                        }
                    }
                    freshData();
                    break;
                case BILL_SAVEOPERATE:
                    User user = JhctmApplication.getInstance().getUser();
                    this.netWorker.billList(user.getToken(), keytype, params.type_name, 0);
                    break;
            }
        }

        private void freshData() {
            if (adapter == null) {
                adapter = new OrderListAdapter(activity, goodsList, listView, netWorker, keytype);
                adapter.setEmptyString("该状态订单您已处理完毕。");
                listView.setAdapter(adapter);
            } else {
                adapter.setEmptyString("该状态订单您已处理完毕。");
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onServerFailed(HemaNetWorker netWorker,
                                   HemaNetTask netTask, HemaBaseResult baseResult) {
            JhHttpInformation information = (JhHttpInformation) netTask
                    .getHttpInformation();
            switch (information) {
                case BILL_LIST:
                    String page = netTask.getParams().get("page");
                    if ("0".equals(page)) {// 刷新
                        layout.refreshFailed();
                        freshData();
                    } else {// 更多
                        layout.loadmoreFailed();
                    }
                    break;
                case BILL_SAVEOPERATE:
                    activity.showTextDialog(baseResult.getMsg());
                    break;
            }
        }

        @Override
        public void onExecuteFailed(HemaNetWorker netWorker,
                                    HemaNetTask netTask, int failedType) {
            JhHttpInformation information = (JhHttpInformation) netTask
                    .getHttpInformation();
            switch (information) {
                case BILL_LIST:
                    String page = netTask.getParams().get("page");
                    if ("0".equals(page)) {// 刷新
                        layout.refreshFailed();
                        freshData();
                    } else {// 更多
                        layout.loadmoreFailed();
                    }
                    break;
                case BILL_SAVEOPERATE:
                    activity.showTextDialog("操作失败，请稍后重试");
                    break;
            }
        }
    }

    private class OnStartListener implements
            XtomRefreshLoadmoreLayout.OnStartListener {
        private Integer current_page = 0;
        private Params params;
        private JhNetWorker netWorker;
        private User user;

        private OnStartListener(Params params, HemaNetWorker netWorker, View v) {
            this.params = params;
            this.netWorker = (JhNetWorker) netWorker;
            user = JhctmApplication.getInstance().getUser();
        }

        @Override
        public void onStartRefresh(XtomRefreshLoadmoreLayout v) {
            current_page = 0;
            netWorker.billList(user.getToken(), keytype, params.type_name, current_page);
        }

        @Override
        public void onStartLoadmore(XtomRefreshLoadmoreLayout v) {
            current_page++;
            netWorker.billList(user.getToken(), keytype, params.type_name, current_page);
        }
    }

    public static class Params extends XtomObject {
        boolean isFirstSetPrimary = true;// 第一次显示时需要自动加载数据
        String type_name;

        public Params(String type_name) {
            super();
            this.type_name = type_name;
        }
    }
    public void freshAll() {
        for (RefreshLoadmoreLayout layout : layouts) {
            if (layout != null)
                layout.getOnStartListener().onStartRefresh(layout);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}

