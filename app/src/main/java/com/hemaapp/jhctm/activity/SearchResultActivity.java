package com.hemaapp.jhctm.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.hemaapp.jhctm.model.Blog1List;
import com.hemaapp.jhctm.model.City;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * Created by lenovo on 2016/12/21.
 * 首页搜索结果
 * SearchResultAdapter
 * HomeGridAdapter
 */
public class SearchResultActivity extends JhActivity {
    private TextView search_world;//搜索关键词
    private ProgressBar progressbar;
    private RefreshLoadmoreLayout refreshLoadmoreLayout;
    // private XtomListView listview;
    // private JhctmGridView gridview;
    private RecyclerView recyclerview;
    private ImageButton back_button;
    private TextView title_text;
    private ImageButton next_button;
    private String search;
    private String type;//0 商品，1 商家
    private ArrayList<City> cities = new ArrayList<>();
    private ArrayList<Blog1List> blog1Lists = new ArrayList<>();
    private Integer page = 0;
    private BrandAdapter brandAdapter;
    private GoodAdapter goodAdapter;
    private GridLayoutManager gridLayoutManager;
    private LinearLayout no_search_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_result_search);
        super.onCreate(savedInstanceState);
        inIt();
    }

    private void inIt() {
        String token = JhctmApplication.getInstance().getUser().getToken();
        if ("0".equals(type))
            getNetWorker().merchantList(search, String.valueOf(page));
        else
            getNetWorker().blog1List2("", "", "", "", "", "", search, String.valueOf(page));
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case MERCHANT_LIST:
                showProgressDialog("获取商家列表");
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
            case MERCHANT_LIST:
                cancelProgressDialog();
                progressbar.setVisibility(View.GONE);
                refreshLoadmoreLayout.setVisibility(View.VISIBLE);
                break;
            case BLOG_1_LIST:
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
            case MERCHANT_LIST:
                HemaPageArrayResult<City> result3 = (HemaPageArrayResult<City>) hemaBaseResult;
                ArrayList<City> cities = result3.getObjects();
                String page2 = hemaNetTask.getParams().get("page");
                if ("0".equals(page2)) {// 刷新
                    refreshLoadmoreLayout.refreshSuccess();
                    this.cities.clear();
                    this.cities.addAll(cities);

                    JhctmApplication application = JhctmApplication.getInstance();
                    int sysPagesize = application.getSysInitInfo()
                            .getSys_pagesize();
                    if (cities.size() < sysPagesize) {
                        refreshLoadmoreLayout.setLoadmoreable(false);
                        // leftRE = false;
                    } else {
                        refreshLoadmoreLayout.setLoadmoreable(true);
                        // leftRE = true;
                    }
                } else {// 更多
                    refreshLoadmoreLayout.loadmoreSuccess();
                    if (cities.size() > 0)
                        this.cities.addAll(cities);
                    else {
                        refreshLoadmoreLayout.setLoadmoreable(false);
                        // leftRE = false;
                        XtomToastUtil.showShortToast(mContext, "已经到最后啦");
                    }
                }
                if (this.cities == null || this.cities.size() == 0) {
                    search_world.setVisibility(View.GONE);
                    no_search_data.setVisibility(View.VISIBLE);
                } else {
                    search_world.setVisibility(View.VISIBLE);
                    no_search_data.setVisibility(View.GONE);
                    if (brandAdapter == null) {
                        recyclerview.setLayoutManager(new LinearLayoutManager(this));
                        recyclerview.setAdapter(brandAdapter = new BrandAdapter());
                    } else
                        brandAdapter.notifyDataSetChanged();
                }
                break;
            case BLOG_1_LIST:
                HemaPageArrayResult<Blog1List> result = (HemaPageArrayResult<Blog1List>) hemaBaseResult;
                ArrayList<Blog1List> blog1Lists = result.getObjects();
                String page1 = hemaNetTask.getParams().get("page");
                if ("0".equals(page1)) {// 刷新
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
                if (this.blog1Lists == null || this.blog1Lists.size() == 0) {
                    search_world.setVisibility(View.GONE);
                    no_search_data.setVisibility(View.VISIBLE);
                } else {
                    search_world.setVisibility(View.VISIBLE);
                    no_search_data.setVisibility(View.GONE);
                    if (goodAdapter == null) {
                        recyclerview.setLayoutManager(gridLayoutManager = new GridLayoutManager(this, 2));
                        recyclerview.setAdapter(goodAdapter = new GoodAdapter());
                    } else
                        goodAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case MERCHANT_LIST:
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
            case MERCHANT_LIST:
                showTextDialog("获取商家列表失败，请稍后重试");
                String page = hemaNetTask.getParams().get("page");
                if ("0".equals(page)) {
                    refreshLoadmoreLayout.refreshFailed();
                } else {
                    refreshLoadmoreLayout.loadmoreFailed();
                }
                break;
            case BLOG_1_LIST:
                showTextDialog("获取商品列表失败，请稍后重试");
                String page1 = hemaNetTask.getParams().get("page");
                if ("0".equals(page1)) {
                    refreshLoadmoreLayout.refreshFailed();
                } else {
                    refreshLoadmoreLayout.loadmoreFailed();
                }
                break;
        }
    }

    @Override
    protected void findView() {
        search_world = (TextView) findViewById(R.id.search_world);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        refreshLoadmoreLayout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        //   listview = (XtomListView) findViewById(R.id.listview);
        //   gridview = (JhctmGridView) findViewById(R.id.gridview);
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (ImageButton) findViewById(R.id.next_button);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        no_search_data = (LinearLayout) findViewById(R.id.no_search_data);
    }

    @Override
    protected void getExras() {
        type = mIntent.getStringExtra("type");
        search = mIntent.getStringExtra("search");
    }

    @Override
    protected void setListener() {
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_text.setText("搜索结果");
        next_button.setVisibility(View.INVISIBLE);
        search_world.setText("当前搜索: " + search);
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

    class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    SearchResultActivity.this).inflate(R.layout.item_home_gridview, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            City city = cities.get(position);
            //商品图片
            String path = city.getAvatar();
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.mipmap.defult_gridview_img)
                    .showImageForEmptyUri(R.mipmap.defult_gridview_img)
                    .showImageOnFail(R.mipmap.defult_gridview_img).cacheInMemory(true)
                    .bitmapConfig(Bitmap.Config.RGB_565).build();
            ImageLoader.getInstance().displayImage(path, holder.commod_img, options);
            holder.hotel_name.setText(city.getNickname());
            holder.hotel_money.setText("保证金额 ¥" + city.getBail());
            holder.item_hoetl.setTag(R.id.TAG, city);
            holder.item_hoetl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    City city1 = (City) v.getTag(R.id.TAG);
                    Intent intent = new Intent(mContext, BrandbusinessInforActivity.class);
                    intent.putExtra("brandId", city1.getId());
                    mContext.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return cities.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView commod_img;
            TextView hotel_name;
            TextView hotel_money;
            LinearLayout item_hoetl;

            public MyViewHolder(View view) {
                super(view);
                commod_img = (ImageView) view.findViewById(R.id.commod_img);
                hotel_name = (TextView) view.findViewById(R.id.hotel_name);
                hotel_money = (TextView) view.findViewById(R.id.hotel_money);
                item_hoetl = (LinearLayout) view.findViewById(R.id.item_hoetl);
            }
        }
    }

    class GoodAdapter extends RecyclerView.Adapter<GoodAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    SearchResultActivity.this).inflate(R.layout.item_home_gridview, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Blog1List blog1List = blog1Lists.get(position);
            //商品图片
            String path = blog1List.getImgurl();
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.mipmap.defult_gridview_img)
                    .showImageForEmptyUri(R.mipmap.defult_gridview_img)
                    .showImageOnFail(R.mipmap.defult_gridview_img).cacheInMemory(true)
                    .bitmapConfig(Bitmap.Config.RGB_565).build();
            ImageLoader.getInstance().displayImage(path, holder.commind_img, options);
            //名称
            if (!isNull(blog1List.getName()))
                holder.commod_name.setText(blog1List.getName());
            //判断是否显示分享积分
            //分享
            if ("3".equals(blog1List.getKeytype())) {
                holder.add_bz.setVisibility(View.VISIBLE);
                holder.fenxiang_text.setVisibility(View.VISIBLE);

            } else {
                holder.add_bz.setVisibility(View.GONE);
                holder.fenxiang_text.setVisibility(View.GONE);
            }
            //积分
            if (!isNull(blog1List.getScore()))
                holder.num_credit.setText(blog1List.getScore());
            //分享

            //判断是否是品牌商
            //平台自营
            if ("1".equals(blog1List.getClient_id()))
                holder.pinpai_name.setVisibility(View.GONE);
            else
                holder.pinpai_name.setVisibility(View.VISIBLE);
            //销量 好评率
            holder.commod_content.setText("已售" + blog1List.getSalecount() + "  好评率" + blog1List.getGood_score());
            //点击查看详情
            holder.layout_item.setTag(R.id.TAG, blog1List);
            holder.layout_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Blog1List blog1List1 = (Blog1List) v.getTag(R.id.TAG);
                    Intent intent = new Intent(mContext, GoodsInformationActivity.class);
                    intent.putExtra("goodId", blog1List1.getId());
                    mContext.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return blog1Lists.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView commind_img;
            TextView commod_name;//商品名称
            TextView num_credit;//积分
            TextView commod_content;//商品简介
            TextView add_bz;
            TextView fenxiang_text;
            TextView pinpai_name;
            LinearLayout layout_item;

            public MyViewHolder(View view) {
                super(view);
                commind_img = (ImageView) view.findViewById(R.id.commind_img);
                commod_name = (TextView) view.findViewById(R.id.commod_name);
                num_credit = (TextView) view.findViewById(R.id.num_credit);
                commod_content = (TextView) view.findViewById(R.id.commod_content);
                add_bz = (TextView) view.findViewById(R.id.add_bz);
                fenxiang_text = (TextView) view.findViewById(R.id.fenxiang_text);
                pinpai_name = (TextView) view.findViewById(R.id.pinpai_name);
                layout_item = (LinearLayout) view.findViewById(R.id.layout_item);
            }
        }
    }

}
