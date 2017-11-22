package com.hemaapp.jhctm.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.hemaapp.jhctm.activity.LimitExchangeActivity;
import com.hemaapp.jhctm.model.Blog1List;

import java.util.ArrayList;

import xtom.frame.XtomObject;
import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * Created by lenovo on 2016/12/27.
 * 限时购的page
 */
public class LimitExchangePageAdapter extends PagerAdapter {
    private ArrayList<Params> paramses;
    private LimitExchangeActivity activity;
    private ArrayList<Blog1List> blog1Lists = new ArrayList<>();
    private LimitExchangeAdapter adapter;
    public LimitExchangePageAdapter(LimitExchangeActivity activity, ArrayList<Params> paramses) {
        this.activity = activity;
        this.paramses = paramses;
    }

    @Override
    public int getCount() {

        return paramses == null ? 0 : paramses.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
    @Override
    public void destroyItem(View container, int position, Object object) {
        // TODO Auto-generated method stub
        // super.destroyItem(container, position, object);
    }


    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub
        Params params = paramses.get(position);
        if (params.isFirstSetPrimary) {
            View view = (View) object;
            RefreshLoadmoreLayout layout = (RefreshLoadmoreLayout) view
                    .findViewById(R.id.refreshLoadmoreLayout);
            layout.getOnStartListener().onStartRefresh(layout);
            params.isFirstSetPrimary = false;

        }
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO Auto-generated method stub
//        View view = container.getChildAt(position);
//        if (view == null) {
        Log.i("ss", "position---" + position);
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.pageritem_rllistview_progress,
                null);
        RefreshLoadmoreLayout layout = (RefreshLoadmoreLayout) view
                .findViewById(R.id.refreshLoadmoreLayout);
     //   layouts.add(layout);
        Params params = paramses.get(position);
        JhNetWorker netWorker = new JhNetWorker(activity);
        netWorker.setOnTaskExecuteListener(new OnTaskExecuteListener(
                activity, view, params));
        layout.setOnStartListener(new OnStartListener(params, netWorker,
                view));
        container.addView(view);
//        }
        return view;
    }
    private class OnStartListener implements
            xtom.frame.view.XtomRefreshLoadmoreLayout.OnStartListener {

        private Integer current_page = 0;
        private Params params;
        private JhNetWorker netWorker;
        private XtomRefreshLoadmoreLayout layout;
        private XtomListView listView;
        // private GridView gridview;
        private ProgressBar progressBar;
        private LinearLayout layout_show;
        public OnStartListener(Params params, JhNetWorker netWorker, View v) {
            this.params = params;
            this.netWorker = netWorker;
            this.layout = (XtomRefreshLoadmoreLayout) v
                    .findViewById(R.id.refreshLoadmoreLayout);
            this.progressBar = (ProgressBar) v.findViewById(R.id.progressbar);
            this.listView = (XtomListView) layout.findViewById(R.id.listview);

        }

        @Override
        public void onStartRefresh(XtomRefreshLoadmoreLayout v) {
            // TODO Auto-generated method stub
            current_page = 0;
            JhctmApplication application = JhctmApplication.getInstance();
            String token = application.getUser().getToken();
            netWorker.blog2List(params.keytype,String.valueOf(current_page));
        }

        @Override
        public void onStartLoadmore(XtomRefreshLoadmoreLayout v) {
            // TODO Auto-generated method stub
            current_page++;
            JhctmApplication application = JhctmApplication.getInstance();
            String token = application.getUser().getToken();
            netWorker.blog2List(params.keytype,String.valueOf(current_page));
        }

    }

    private class OnTaskExecuteListener extends JhNetTaskExecuteListener {

        private XtomRefreshLoadmoreLayout layout;
        private XtomListView listView;
        private ProgressBar progressBar;
        private Params params;
        // private GridView gridview;
        private ViewPager vp;
        private TextView content;

        private LimitExchangeAdapter adapter;
    //    private ArrayList<OrderList> orderLists = new ArrayList<>();
        private ArrayList<Blog1List> blog1Lists = new ArrayList<>();
        // private View allitem;
        public OnTaskExecuteListener(Context context, View view, Params params) {
            super(context);
            // TODO Auto-generated constructor stub
            layout = (XtomRefreshLoadmoreLayout) view
                    .findViewById(R.id.refreshLoadmoreLayout);
            listView = (XtomListView) view.findViewById(R.id.listview);
            progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
            this.params = params;
            blog1Lists = new ArrayList<Blog1List>();
        }

        @Override
        public void onPreExecute(HemaNetWorker netWorker, HemaNetTask netTask) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPostExecute(HemaNetWorker netWorker, HemaNetTask netTask) {
            // TODO Auto-generated method stub
            JhHttpInformation information = (JhHttpInformation) netTask
                    .getHttpInformation();
            switch (information) {
                case BLOG_2_LIST:
                    progressBar.setVisibility(View.GONE);
                    layout.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onServerSuccess(HemaNetWorker netWorker,
                                    HemaNetTask netTask, HemaBaseResult baseResult) {
            // TODO Auto-generated method stub
            JhHttpInformation information = (JhHttpInformation) netTask
                    .getHttpInformation();
            switch (information) {
                case BLOG_2_LIST:
                    HemaPageArrayResult<Blog1List> resultblog = (HemaPageArrayResult<Blog1List>) baseResult;
                    String pageblog = netTask.getParams().get("page");
                    if ("0".equals(pageblog)) {
                        layout.refreshSuccess();
                        blog1Lists.clear();
                        blog1Lists.addAll(resultblog.getObjects());
                        if (resultblog.getObjects().size() < JhctmApplication
                                .getInstance().getSysInitInfo().getSys_pagesize()) {
                            layout.setLoadmoreable(false);
                        } else {
                            layout.setLoadmoreable(true);
                        }

                    } else { // 更多
                        layout.loadmoreSuccess();
                        if (resultblog.getObjects().size() > 0) {
                            blog1Lists.addAll(resultblog.getObjects());
                        } else {
                            layout.setLoadmoreable(false);
                            XtomToastUtil.showShortToast(activity, "已经到最后啦");
                        }
                    }
                    String keytpe = netTask.getParams().get("keytype");

                    if (adapter == null) {
                        adapter = new LimitExchangeAdapter(activity, blog1Lists,keytpe);
                        adapter.setEmptyString("没有任何商品");
                        listView.setAdapter(adapter);
                    } else {
                        adapter.setKeytype(keytpe);
                        adapter.setBlog1Lists(blog1Lists);
                        adapter.setEmptyString("没有任何商品");
                        adapter.notifyDataSetChanged();
                    }
                    break;

                default:
                    break;
            }
        }

        @Override
        public void onServerFailed(HemaNetWorker netWorker,
                                   HemaNetTask netTask, HemaBaseResult baseResult) {
            // TODO Auto-generated method stub
            JhHttpInformation information = (JhHttpInformation) netTask
                    .getHttpInformation();
            switch (information) {
                case BLOG_2_LIST:
                    activity.showTextDialog(baseResult.getMsg());
                    String page12 = netTask.getParams().get("page");
                    if ("0".equals(page12)) { // 刷新
                        layout.refreshFailed();
                    } else { // 更多
                        layout.loadmoreFailed();
                    }
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onExecuteFailed(HemaNetWorker netWorker,
                                    HemaNetTask netTask, int failedType) {
            // TODO Auto-generated method stub
            JhHttpInformation information = (JhHttpInformation) netTask
                    .getHttpInformation();
            switch (information) {
                case BLOG_2_LIST:
                    activity.showTextDialog("获取商品失败，请稍后重试");
                    String page12 = netTask.getParams().get("page");
                    if ("0".equals(page12)) { // 刷新
                        layout.refreshFailed();
                    } else { // 更多
                        layout.loadmoreFailed();
                    }
                    break;

                default:
                    break;
            }
        }

    }

    public static class Params extends XtomObject {
        boolean isFirstSetPrimary = true;// 第一次显示时需要自动加载数据
        String keytype;
        String keyid;

        public Params(String keytype, String keyid) {
            super();
            this.keytype = keytype;
            this.keyid = keyid;
        }
    }

}
