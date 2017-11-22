package com.hemaapp.jhctm.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.hm_FrameWork.view.RefreshLoadmoreLayout;
import com.hemaapp.jhctm.JhActivity;
import com.hemaapp.jhctm.JhHttpInformation;
import com.hemaapp.jhctm.JhctmApplication;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.adapter.GoodAdAdapter;
import com.hemaapp.jhctm.adapter.ItemsAdapter;
import com.hemaapp.jhctm.model.Blog1List;
import com.hemaapp.jhctm.model.Merchant;
import com.hemaapp.jhctm.model.SysInitInfo;
import com.hemaapp.jhctm.view.CustomLinearLayoutManager;
import com.hemaapp.jhctm.view.JhViewPager;
import com.hemaapp.jhctm.view.MyListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import xtom.frame.util.XtomFileUtil;
import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * Created by lenovo on 2016/12/22.
 * 品牌商详情
 * BrandbusinessInforAdapter
 */
public class BrandbusinessInforActivity extends JhActivity  implements PlatformActionListener {
    private TextView search_world;//搜索关键词
    private ProgressBar progressbar;
    private RefreshLoadmoreLayout refreshLoadmoreLayout;
    //   private XtomListView listview;
    //  private JhctmGridView gridview;
    private ImageButton back_button;
    private TextView title_text;
    private ImageButton next_button;
    //    private JhViewPager adviewpager;
    private TextView brand_name;//品牌名称
    private TextView brand_money;//保证金
    private LinearLayout goto_all_layout;//查看全部
    private TextView brand_content;//商家介绍
    private TextView all_brand_text;//全部商品
    private TextView all_class_text;//全部分类
    private View line1;
    private View line2;
    private String brandId;
    private RecyclerView recyclerview;
    private Merchant merchant;
    private Integer page = 0;
    private ArrayList<Blog1List> blog1Lists = new ArrayList<>();
    private String type = "0";
    private HomeAdapter homeAdapter;
    private LetterAdapter letterAdapter;
    private GridLayoutManager gridLayoutManager;
    private GoodAdAdapter adAdapter;
    private JhViewPager adviewpager;
    private RadioGroup radiogroup;
    private FrameLayout vp_top;
    private TextView number_all;
    private CustomLinearLayoutManager linearLayoutManager;
    private MyListView mylistview;
    private ItemsAdapter adapter;

    private OnekeyShare oks;
    private String sys_plugins;
    private String pathWX;
    private ShareHolder shareHolder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_brand_business_infor);
        super.onCreate(savedInstanceState);
        inIt();
        ShareSDK.initSDK(this);
        SysInitInfo initInfo = getApplicationContext()
                .getSysInitInfo();
        sys_plugins = initInfo.getSys_plugins();
        pathWX = sys_plugins + "share/sdk.php?id=0";
        log_i("++++++++"+pathWX);
    }

    //初始化
    private void inIt() {
        String token = JhctmApplication.getInstance().getUser().getToken();
        getNetWorker().merchantGet(token, brandId);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case MERCHANT_GET:
                showProgressDialog("获取品牌商详情");
                break;
            case BLOG_1_LIST:
                showProgressDialog("获取品牌商商品");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case MERCHANT_GET:
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
            case MERCHANT_GET:
                HemaArrayResult<Merchant> result = (HemaArrayResult<Merchant>) hemaBaseResult;
                merchant = result.getObjects().get(0);
                getNetWorker().blog1List("", "", brandId, "1", String.valueOf(page));
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
                freshData();
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case MERCHANT_GET:
            case BLOG_1_LIST:
                showTextDialog(hemaBaseResult.getMsg());
                if (hemaBaseResult.getError_code()==404)
                    next_button.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    },1000);
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case MERCHANT_GET:
                showTextDialog("获取品牌商详情失败，请稍后重试");
                break;
            case BLOG_1_LIST:
                String page = hemaNetTask.getParams().get("page");
                if ("0".equals(page)) {
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
        adviewpager = (JhViewPager) findViewById(R.id.adviewpager);
        brand_money = (TextView) findViewById(R.id.brand_money);
        goto_all_layout = (LinearLayout) findViewById(R.id.goto_all_layout);
        brand_content = (TextView) findViewById(R.id.brand_content);
        all_brand_text = (TextView) findViewById(R.id.all_brand_text);
        all_class_text = (TextView) findViewById(R.id.all_class_text);
        line1 = findViewById(R.id.line1);
        line2 = findViewById(R.id.line2);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        brand_name = (TextView) findViewById(R.id.brand_name);
        adviewpager = (JhViewPager) findViewById(R.id.adviewpager);
        radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
        vp_top = (FrameLayout) findViewById(R.id.vp_top);
        number_all = (TextView) findViewById(R.id.number_all);
        mylistview = (MyListView) findViewById(R.id.mylistview);
    }

    @Override
    protected void getExras() {
        brandId = mIntent.getStringExtra("brandId");
    }

    @Override
    protected void setListener() {
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_text.setText("详情");
        //分享
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShareView();
            }
        });
        refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page = 0;
                getNetWorker().blog1List("", "", brandId, "1", String.valueOf(page));
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page++;
                getNetWorker().blog1List("", "", brandId, "1", String.valueOf(page));
            }
        });
        //全部商品
        all_brand_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "0";
                recyclerview.setLayoutManager(gridLayoutManager);
                recyclerview.setAdapter(homeAdapter);
                recyclerview.setVisibility(View.VISIBLE);
                mylistview.setVisibility(View.GONE);
                // freshData();
                all_brand_text.setTextColor(getResources().getColor(R.color.title_backgroid));
                all_class_text.setTextColor(getResources().getColor(R.color.infor));
                line1.setVisibility(View.VISIBLE);
                line2.setVisibility(View.INVISIBLE);
            }
        });
        //全部分类
        all_class_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "1";
                if (adapter == null)
                    freshData();
                else
                {
                    adapter = new ItemsAdapter(mContext,merchant.getTypeItems(),brandId);
                    mylistview.setAdapter(adapter);
                    recyclerview.setVisibility(View.GONE);
                    mylistview.setVisibility(View.VISIBLE);
                }
                all_class_text.setTextColor(getResources().getColor(R.color.title_backgroid));
                all_brand_text.setTextColor(getResources().getColor(R.color.infor));
                line2.setVisibility(View.VISIBLE);
                line1.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void freshData() {
        if ("0".equals(type)) {
            //商品列表
            if (homeAdapter == null) {
                recyclerview.setLayoutManager(gridLayoutManager = new GridLayoutManager(this, 2));
                recyclerview.setAdapter(homeAdapter = new HomeAdapter());
            } else {
                homeAdapter.notifyDataSetChanged();
            }
        } else {
            if (adapter == null) {
                adapter = new ItemsAdapter(mContext,merchant.getTypeItems(),brandId);
                mylistview.setAdapter(adapter);
                recyclerview.setVisibility(View.GONE);
                mylistview.setVisibility(View.VISIBLE);
            } else {
                adapter.notifyDataSetChanged();
            }
        }
        //填写各种信息
        brand_name.setText(merchant.getName());
        brand_money.setText("保证金额 ¥" + merchant.getBail());
        brand_content.setText(merchant.getContent());
        if (merchant.getImgItems() != null)
            number_all.setText("1/" + merchant.getImgItems().size());
        //商家介绍全部
        goto_all_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
//广告列表
        adAdapter = new GoodAdAdapter(mContext, radiogroup, vp_top, merchant.getImgItems());
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
                    number_all.setText(String.valueOf(arg0 + 1) + "/" + merchant.getImgItems().size());
                }
            }
        }

    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    BrandbusinessInforActivity.this).inflate(R.layout.item_home_gridview, parent,
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
            if (blog1Lists != null)
                return blog1Lists == null ? 0 : blog1Lists.size();
            else
                return 0;
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

    class LetterAdapter extends RecyclerView.Adapter<LetterAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    BrandbusinessInforActivity.this).inflate(R.layout.adapter_item_brandbusiness_infor, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.tv.setText(merchant.getTypeItems().get(position).getName());
            //跳转分类
            holder.tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showTextDialog(String.valueOf(merchant.getTypeItems().size()));
                }
            });
        }

        @Override
        public int getItemCount() {
            if (merchant != null)
                return merchant.getTypeItems() == null ? 0 : merchant.getTypeItems().size();
            else
                return 0;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView tv;

            public MyViewHolder(View view) {
                super(view);
                tv = (TextView) view.findViewById(R.id.hotel_name);
            }
        }
    }
    //分享
    private class ShareHolder{
        TextView qq_text;
        TextView qq_zone_text;
        TextView wechat_text;
        TextView wechat_friend_text;
        TextView sinbo_text;
        TextView close_pop;
    }
    private void showShareView()
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.pop_share_layout, null);
        shareHolder = new ShareHolder();
        shareHolder.qq_text = (TextView) view.findViewById(R.id.qq_text);
        shareHolder.qq_zone_text = (TextView) view.findViewById(R.id.qq_zone_text);
        shareHolder.wechat_text = (TextView) view.findViewById(R.id.wechat_text);
        shareHolder.wechat_friend_text = (TextView) view.findViewById(R.id.wechat_friend_text);
        shareHolder.sinbo_text = (TextView) view.findViewById(R.id.sinbo_text);
        shareHolder.close_pop = (TextView) view.findViewById(R.id.close_pop);
        final PopupWindow popupWindow = new PopupWindow(view,
                RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT);
        //QQ
        shareHolder.qq_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTextDialog("暂未开通，敬请期待！");
            }
        });
        shareHolder.qq_zone_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTextDialog("暂未开通，敬请期待！");
            }
        });
        shareHolder.sinbo_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTextDialog("暂未开通，敬请期待！");
            }
        });
        //关闭
        shareHolder.close_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        //微信
        shareHolder.wechat_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare(WechatMoments.NAME);
                popupWindow.dismiss();
            }
        });
        shareHolder.wechat_friend_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare(Wechat.NAME);
                popupWindow.dismiss();
            }
        });
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setWidth(RadioGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(RadioGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //   popupWindow.showAsDropDown(findViewById(R.id.pop_layout_bottom));
        popupWindow.setAnimationStyle(R.style.PopupAnimation);
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }
    private void showShare(String platform) {
        if (oks == null) {
            oks = new OnekeyShare();
            oks.setTitleUrl(pathWX); // 标题的超链接
            oks.setTitle("久好C2M");
            oks.setText("久好C2M分享");
            oks.setImageUrl("");
            oks.setFilePath(pathWX);
            //  oks.setImageUrl(goodsGet.getImgurl());
            oks.setImagePath(getLogoImagePath());
            oks.setUrl(pathWX);
            oks.setSiteUrl(pathWX);
            oks.setCallback(this);
        }
        oks.setPlatform(platform);
        oks.show(mContext);
    }
    // 获取软件Logo文件地址
    private String getLogoImagePath() {
        String imagePath;
        try {
            String cachePath_internal = XtomFileUtil.getCacheDir(mContext)
                    + "/images/";// 获取缓存路径
            File dirFile = new File(cachePath_internal);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            imagePath = cachePath_internal + "icondd.png";
            File file = new File(imagePath);
            if (!file.exists()) {
                file.createNewFile();
                Bitmap pic = BitmapFactory.decodeResource(
                        mContext.getResources(), R.mipmap.ic_launcher);
                FileOutputStream fos = new FileOutputStream(file);
                pic.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
            }
        } catch (Throwable t) {
            t.printStackTrace();
            imagePath = null;
        }
        log_i("imagePath=" + imagePath);
        return imagePath;
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(getApplicationContext(), "QQ分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), "微信分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(), "空间分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 4:
                    Toast.makeText(getApplicationContext(), "微博分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 5:
                    Toast.makeText(getApplicationContext(), "微信朋友圈分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 7:
                    Toast.makeText(getApplicationContext(), "分享失败", Toast.LENGTH_LONG).show();
                    break;
                case 6:
                    Toast.makeText(getApplicationContext(), "取消分享", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }

    };
    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        if (platform.getName().equals(QQ.NAME)) {// 判断成功的平台是不是微信
            handler.sendEmptyMessage(1);
        }
        if (platform.getName().equals(Wechat.NAME)) {// 判断成功的平台是不是微信朋友圈
            handler.sendEmptyMessage(2);
        }
        if (platform.getName().equals(QZone.NAME)) {// 判断成功的平台是不是微信朋友圈
            handler.sendEmptyMessage(3);
        }
        if (platform.getName().equals(SinaWeibo.NAME)) {// 判断成功的平台是不是微信朋友圈
            handler.sendEmptyMessage(4);
        }
        if (platform.getName().equals(WechatMoments.NAME)) {// 判断成功的平台是不是微信朋友圈
            handler.sendEmptyMessage(5);
        }
    }

    @Override
    public void onError(Platform arg0, int arg1, Throwable arg2) {
        arg2.printStackTrace();
        Message msg = new Message();
        msg.what = 7;
        msg.obj = arg2.getMessage();
        handler.sendMessage(msg);
    }

    @Override
    public void onCancel(Platform platform, int i) {
        handler.sendEmptyMessage(6);
    }

}
