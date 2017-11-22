package com.hemaapp.jhctm.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.hemaapp.hm_FrameWork.view.HemaWebView;
import com.hemaapp.hm_FrameWork.view.RefreshLoadmoreLayout;
import com.hemaapp.jhctm.JhActivity;
import com.hemaapp.jhctm.JhHttpInformation;
import com.hemaapp.jhctm.JhUtil;
import com.hemaapp.jhctm.JhctmApplication;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.adapter.GoodAdAdapter;
import com.hemaapp.jhctm.adapter.GoodInformationAdapter;
import com.hemaapp.jhctm.model.Blog;
import com.hemaapp.jhctm.model.City;
import com.hemaapp.jhctm.model.Reply;
import com.hemaapp.jhctm.model.SysInitInfo;
import com.hemaapp.jhctm.view.FlowLayout;
import com.hemaapp.jhctm.view.JhViewPager;
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
import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * Created by lenovo on 2016/12/23.
 * 商品详情
 * GoodInformationAdapter
 * pop :pop_guige_view
 */
public class GoodsInformationActivity extends JhActivity implements PlatformActionListener {
    private View holderView;
    private View fooderView;
    private ImageButton back_button;
    private TextView title_text;
    private ImageButton next_button;
    private ProgressBar progressbar;
    private RefreshLoadmoreLayout refreshLoadmoreLayout;
    private XtomListView listview;
    private TextView share_text;//分享币
    private TextView collection_text;//收藏
    private TextView car_text;//购物车
    private TextView add_car_text;//加入购物车
    private TextView goto_apliy;//立即购买
    private String goodId;
    private Blog blogs;
    private GoodAdAdapter adAdapter;
    private ArrayList<Reply> replies = new ArrayList<>();
    private GoodInformationAdapter adapter;
    private PopView popView;
    private OnekeyShare oks;
    private String sys_plugins;
    private String pathWX;
    private ShareHolder shareHolder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_goods_information);
        super.onCreate(savedInstanceState);
        inIt();
        ShareSDK.initSDK(this);
        SysInitInfo initInfo = getApplicationContext()
                .getSysInitInfo();
        sys_plugins = initInfo.getSys_plugins();
        pathWX = sys_plugins + "share/sdk.php?id=" + goodId;
        log_i("++++++++"+pathWX);
    }

    private void inIt() {
        getNetWorker().blogGet(goodId);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case BLOG_GET:
                showProgressDialog("获取商品详情");
                break;
            case REPLY_LIST:
                showProgressDialog("获取商品评价");
                break;
            case FOLLOW_COLLECT_OPERATOR:
                showProgressDialog("保存收藏操作");
                break;
            case CART_ADD:
                showProgressDialog("加入购物车");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case BLOG_GET:
            case FOLLOW_COLLECT_OPERATOR:
            case CART_ADD:
                cancelProgressDialog();
//                refreshLoadmoreLayout.setVisibility(View.VISIBLE);
//                progressbar.setVisibility(View.GONE);
                break;
            case REPLY_LIST:
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
            case BLOG_GET:
                HemaArrayResult<Blog> result = (HemaArrayResult<Blog>) hemaBaseResult;
                blogs = result.getObjects().get(0);
                getNetWorker().replyList("2", goodId, "0");
                break;
            case REPLY_LIST:
                refreshLoadmoreLayout.refreshSuccess();
                HemaPageArrayResult<Reply> result1 = (HemaPageArrayResult<Reply>) hemaBaseResult;
                ArrayList<Reply> replies = result1.getObjects();
                this.replies.clear();
                if ("2".equals(blogs.getKeytype()))
                    replies.clear();
                if (replies == null || replies.size() == 0) {
                    this.replies.addAll(replies);
                } else {
                    if (replies.size() <= 3)
                        this.replies.addAll(replies);
                    else
                        this.replies.addAll(replies.subList(0, 2));
                }
                freshData();
                break;
            case FOLLOW_COLLECT_OPERATOR:
                String keytype = hemaNetTask.getParams().get("oper");
                if ("1".equals(keytype)) {
                    showTextDialog("收藏成功");
                    blogs.setCollectflag("1");
                } else {
                    showTextDialog("取消收藏");
                    blogs.setCollectflag("0");
                }
                freshData();
                break;
            case CART_ADD:
                showTextDialog("购物车添加成功!");

                break;
        }
    }

    private void freshData() {
        //加表头
        addHolder();
        //加表尾
        setFooter();
        if (adapter == null) {
            adapter = new GoodInformationAdapter(mContext, replies);
            adapter.setEmptyString("暂无数据");
            listview.setAdapter(adapter);
        } else {
            adapter.setEmptyString("暂无数据");
            adapter.setReplies(replies);
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case BLOG_GET:
                showTextDialog(hemaBaseResult.getMsg());
                if (hemaBaseResult.getError_code()==404)
                    next_button.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    },1000);
                break;
            case REPLY_LIST:
            case FOLLOW_COLLECT_OPERATOR:
            case CART_ADD:
                showTextDialog(hemaBaseResult.getMsg());
                break;


        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case BLOG_GET:
                showTextDialog("获取商品详情失败，请稍后重试");
                break;
            case REPLY_LIST:
                showTextDialog("获取商品评价失败，请稍后重试");
                break;
            case FOLLOW_COLLECT_OPERATOR:
                showTextDialog("收藏操作失败，请稍后重试");
                break;
            case CART_ADD:
                showTextDialog("添加购物车失败，请稍后重试");
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
        share_text = (TextView) findViewById(R.id.share_text);
        collection_text = (TextView) findViewById(R.id.collection_text);
        car_text = (TextView) findViewById(R.id.car_text);
        add_car_text = (TextView) findViewById(R.id.add_car_text);
        goto_apliy = (TextView) findViewById(R.id.goto_apliy);
    }

    @Override
    protected void getExras() {
        goodId = mIntent.getStringExtra("goodId");
    }

    @Override
    protected void setListener() {
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_text.setText("商品详情");
        next_button.setVisibility(View.INVISIBLE);
        //分享币
        share_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShareView();
            }
        });
        //购物车列表
        car_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoodsInformationActivity.this, CarBuyActivity.class);
                startActivity(intent);
            }
        });
        //收藏
        collection_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //为收藏，添加收藏
                String token = JhctmApplication.getInstance().getUser().getToken();
                if ("0".equals(blogs.getCollectflag()))
                    getNetWorker().goodsOperate(token, "1", "1", goodId);
                else
                    getNetWorker().goodsOperate(token, "1", "2", goodId);
            }
        });

        //加入购物车
        add_car_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStandard("2");
            }
        });
        //立即购买或兑换
        goto_apliy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStandard("3");
            }
        });
        refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                inIt();
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {

            }
        });
        refreshLoadmoreLayout.setLoadmoreable(false);
    }


    /**
     * 加的表头 adapter_good_information
     */
    private class ViewHolder {
        JhViewPager adviewpager;
        RadioGroup radiogroup;
        TextView jifen;
        TextView add_text;
        TextView fenxiang_text;
        TextView good_name;
        TextView pinpai_bz;
        TextView content_text;
        TextView infor_data;
        TextView number_;
        LinearLayout guige_layout;
        TextView guige_text;
        TextView yunfei_text;
        TextView reply_num;
        TextView city_name_text;
        FrameLayout vp_top;
        TextView jffh_text;
        TextView jifen_text;
        LinearLayout yf_layout;
        View view_show;
        LinearLayout select_city_layout;
        TextView number_all;
    }

    private void findView(ViewHolder holder, View view) {
        holder.adviewpager = (JhViewPager) view.findViewById(R.id.adviewpager);
        holder.radiogroup = (RadioGroup) view.findViewById(R.id.radiogroup);
        holder.jifen = (TextView) view.findViewById(R.id.jifen);
        holder.add_text = (TextView) view.findViewById(R.id.add_text);
        holder.fenxiang_text = (TextView) view.findViewById(R.id.fenxiang_text);
        holder.good_name = (TextView) view.findViewById(R.id.good_name);
        holder.pinpai_bz = (TextView) view.findViewById(R.id.pinpai_bz);
        holder.content_text = (TextView) view.findViewById(R.id.content_text);
        holder.infor_data = (TextView) view.findViewById(R.id.infor_data);
        holder.number_ = (TextView) view.findViewById(R.id.number_);
        holder.guige_layout = (LinearLayout) view.findViewById(R.id.guige_layout);
        holder.guige_text = (TextView) view.findViewById(R.id.guige_text);
        holder.yunfei_text = (TextView) view.findViewById(R.id.yunfei_text);
        holder.reply_num = (TextView) view.findViewById(R.id.reply_num);
        holder.city_name_text = (TextView) view.findViewById(R.id.city_name_text);
        holder.number_all = (TextView) view.findViewById(R.id.number_all);
        holder.vp_top = (FrameLayout) view.findViewById(R.id.vp_top);
        holder.jifen_text = (TextView) view.findViewById(R.id.jifen_text);
        holder.jffh_text = (TextView) view.findViewById(R.id.jffh_text);
        holder.yf_layout = (LinearLayout) view.findViewById(R.id.yf_layout);
        holder.view_show = view.findViewById(R.id.view_show);
        holder.select_city_layout = (LinearLayout) view.findViewById(R.id.select_city_layout);
        //  holder.number_all = (TextView) view.findViewById(R.id.number_all);
    }

    //加表头
    private void addHolder() {
        if (holderView == null) {
            holderView = LayoutInflater.from(mContext).inflate(R.layout.adapter_good_information, null);
            ViewHolder holder1 = new ViewHolder();
            findView(holder1, holderView);
            holderView.setTag(R.id.TAG_VIEWHOLDER, holder1);
            setHolderData(holder1);
            listview.addHeaderView(holderView);
        } else {
            ViewHolder holder = (ViewHolder) holderView.getTag(R.id.TAG_VIEWHOLDER);
            setHolderData(holder);
        }

    }

    private void setHolderData(final ViewHolder holder) {
        //广告列表
        double width= JhUtil.getScreenWidth(this);
        double height=width/1*1;
        ViewGroup.LayoutParams params1 =  holder.adviewpager.getLayoutParams();
        params1.width = (int) width;
        params1.height=(int) height;
        holder.adviewpager.setLayoutParams(params1);
        adAdapter = new GoodAdAdapter(mContext, holder.radiogroup, holder.vp_top, blogs.getImgItems());
        holder.adviewpager.setAdapter(adAdapter);
        if (blogs.getImgItems() != null)
            holder.number_all.setText("1/" + blogs.getImgItems().size());
        holder.adviewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (adAdapter != null) {
                    ViewGroup indicator = adAdapter.getIndicator();
                    if (indicator != null) {
                        RadioButton rbt = (RadioButton) indicator.getChildAt(position);
                        if (rbt != null)
                            rbt.setChecked(true);
                        holder.number_all.setText(String.valueOf(position + 1) + "/" + blogs.getImgItems().size());
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        holder.good_name.setText(blogs.getName());
        //判断是什么类型

        if (isNull(blogs.getContent()))
            holder.content_text.setVisibility(View.GONE);
        else
            holder.content_text.setText(blogs.getContent());
        //易物兑
        if ("1".equals(blogs.getKeytype())) {
            holder.add_text.setVisibility(View.GONE);
            holder.fenxiang_text.setVisibility(View.GONE);
            holder.jifen.setText(blogs.getScore());
            holder.jffh_text.setVisibility(View.GONE);
            //是否是品牌商
            //平台自营 不做处理
            if ("1".equals(blogs.getClient_id())) {
                holder.pinpai_bz.setVisibility(View.GONE);
            } else {
                //隐藏购物车
                car_text.setVisibility(View.GONE);
                add_car_text.setVisibility(View.GONE);
                holder.pinpai_bz.setVisibility(View.VISIBLE);
            }
        }
        //2 限时兑
        else if ("2".equals(blogs.getKeytype())) {
            holder.pinpai_bz.setVisibility(View.GONE);
            collection_text.setVisibility(View.GONE);
            car_text.setVisibility(View.GONE);
            add_car_text.setVisibility(View.GONE);
            holder.add_text.setVisibility(View.GONE);

            holder.fenxiang_text.setVisibility(View.GONE);
            holder.jifen.setText("金积分" + blogs.getGold_score());
            //判断是否返还
            if ("0".equals(blogs.getRebatflag())) {
                holder.jffh_text.setVisibility(View.GONE);
            } else {
                holder.jffh_text.setVisibility(View.VISIBLE);
            }
            //判断是否售罄
            String key = "0";
            if ("0".equals(blogs.getLeftcount())) {
                goto_apliy.setEnabled(false);
                goto_apliy.setText("已告罄");
                goto_apliy.setBackgroundResource(R.color.no_enabled);
                key = "1";
            } else {
                goto_apliy.setEnabled(true);
                goto_apliy.setText("立即兑换");
                goto_apliy.setBackgroundResource(R.color.title_backgroid);
            }
            //判断是否失效
            if ("0".equals(key))
                if ("0".equals(blogs.getSaleflag())) {
                    goto_apliy.setEnabled(false);
                    goto_apliy.setText("已失效");
                    goto_apliy.setBackgroundResource(R.color.no_enabled);
                } else {
                    goto_apliy.setEnabled(true);
                    goto_apliy.setText("立即兑换");
                    goto_apliy.setBackgroundResource(R.color.title_backgroid);
                }
            //屏蔽规格，运费，评价
            holder.guige_layout.setVisibility(View.GONE);
            holder.yf_layout.setVisibility(View.VISIBLE);
            holder.view_show.setVisibility(View.GONE);
            holder.select_city_layout.setVisibility(View.GONE);

        }
        //分享币专区
        else {
            //不返还
            holder.pinpai_bz.setVisibility(View.GONE);
            holder.jffh_text.setVisibility(View.GONE);
            holder.jifen.setText("¥"+blogs.getScore());
            holder.jifen_text.setText("元");
            holder.fenxiang_text.setText(blogs.getShare_score()+"分享币" );
            collection_text.setVisibility(View.GONE);
            car_text.setVisibility(View.GONE);
            add_car_text.setVisibility(View.GONE);
        }
        //已售，好评率
        holder.infor_data.setText("已售" + blogs.getSalecount() + "  好评率" + blogs.getGood_score() + "%");
        //库存
        holder.number_.setText("库存" + blogs.getLeftcount());
        //运费  还没写
        holder.yunfei_text.setText("¥ " + blogs.getExpressfee());
        //评论数，好评率
        if ("0".equals(blogs.getReplycount())) {
            if ("2".equals(blogs.getKeytype())) {
                holder.select_city_layout.setVisibility(View.GONE);
            } else {
                holder.select_city_layout.setVisibility(View.VISIBLE);
            }
            holder.reply_num.setText("评价 "+blogs.getReplycount());
            holder.city_name_text.setText(blogs.getGood_score()+"%");
        } else {
            if ("2".equals(blogs.getKeytype())) {
                holder.select_city_layout.setVisibility(View.GONE);
            } else {
                holder.select_city_layout.setVisibility(View.VISIBLE);
            }
            holder.reply_num.setText("评价 " + blogs.getReplycount());
            holder.city_name_text.setText(blogs.getGood_score() + "%");

        }
        holder.select_city_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoodsInformationActivity.this, ReplyListActivity.class);
                intent.putExtra("goodId", goodId);
                intent.putExtra("replycount", blogs.getReplycount());
                intent.putExtra("goodScore", blogs.getGood_score());
                startActivity(intent);
            }
        });
        //收藏操作
        if ("1".equals(blogs.getCollectflag())) {
            collection_text.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.collection_on_img), null, null);
        } else {
            collection_text.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.collection_off_img), null, null);
        }
        //选择规格
        holder.guige_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStandard("1");
            }
        });
    }


    /**
     * 加表尾adapter_good_webview
     */
    private class FooterView {
        ImageView commod_img;
        TextView hotel_name;
        TextView hotel_money;
        HemaWebView webview_aboutwe;
        View view1;
        View view2;
        LinearLayout item_hoetl;
    }

    private void findFooter(FooterView footerView, View view) {
        footerView.commod_img = (ImageView) view.findViewById(R.id.commod_img);
        footerView.hotel_name = (TextView) view.findViewById(R.id.hotel_name);
        footerView.hotel_money = (TextView) view.findViewById(R.id.hotel_money);
        footerView.webview_aboutwe = (HemaWebView) view.findViewById(R.id.webview_aboutwe);
        footerView.view1 = view.findViewById(R.id.view1);
        footerView.view2 = view.findViewById(R.id.view2);
        footerView.item_hoetl = (LinearLayout) view.findViewById(R.id.item_hoetl);
    }

    private void setFooter() {
        if (fooderView == null) {
            fooderView = LayoutInflater.from(mContext).inflate(R.layout.adapter_good_webview, null);
            FooterView holder1 = new FooterView();
            findFooter(holder1, fooderView);
            fooderView.setTag(R.id.TAG, holder1);
            setFooterView(holder1);
            listview.addFooterView(fooderView);
        } else {
            FooterView holder = (FooterView) fooderView.getTag(R.id.TAG);
            setFooterView(holder);
        }
    }

    private void setFooterView(FooterView footerView) {
        //判断是否是品牌商
        //是品牌商
        if (!"1".equals(blogs.getClient_id())) {
            footerView.view1.setVisibility(View.VISIBLE);
            footerView.view2.setVisibility(View.VISIBLE);
            footerView.item_hoetl.setVisibility(View.VISIBLE);
            City city = blogs.getSellerItems().get(0);
            //商品图片
            String path = city.getAvatar();
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.mipmap.defult_gridview_img)
                    .showImageForEmptyUri(R.mipmap.defult_gridview_img)
                    .showImageOnFail(R.mipmap.defult_gridview_img).cacheInMemory(true)
                    .bitmapConfig(Bitmap.Config.RGB_565).build();
            ImageLoader.getInstance().displayImage(path, footerView.commod_img, options);
            footerView.hotel_name.setText(city.getNickname());
            footerView.hotel_money.setText("保证金积分额 ¥" + city.getBail());
            footerView.item_hoetl.setTag(R.id.TAG, city);
            footerView.item_hoetl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    City city1 = (City) v.getTag(R.id.TAG);
                    Intent intent = new Intent(mContext, BrandbusinessInforActivity.class);
                    intent.putExtra("brandId", city1.getId());
                    mContext.startActivity(intent);
                }
            });
        } else {
            footerView.view1.setVisibility(View.GONE);
            footerView.view2.setVisibility(View.GONE);
            footerView.item_hoetl.setVisibility(View.GONE);
        }
        String sys_web_service = getApplicationContext().getSysInitInfo()
                .getSys_web_service();
        String path = sys_web_service + "webview/parm/blogdetail_" + goodId;
        footerView.webview_aboutwe.loadUrl(path);
    }

    private class PopView {
        ImageView commod_img;//商品头像
        ImageView close_pop;//关闭
        TextView hotel_name;//价格
        TextView add_bz;//加的符号
        TextView fenxiang_text;//分享币
        TextView hotel_money;//商品名称
        FlowLayout add_view_guige;//添加的view
        ImageView minus_number;//符号减
        ImageView add_number;//符号加
        TextView number_good;//商品数量
        LinearLayout layout_1;
        TextView heji_text;//合计
        TextView yes_order;//立即购买
        LinearLayout layout_2;
        TextView add_car;//添加都购物车
        TextView goto_pay;//立即购买
        TextView jf_or_yuan;
    }

    /**
     * @param type 1 ： 规格
     *             2：加入购物车
     *             3：立即购买
     */
    private void showStandard(final String type) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.pop_guige_view, null);
        popView = new PopView();
        popView.commod_img = (ImageView) view.findViewById(R.id.commod_img);
        popView.close_pop = (ImageView) view.findViewById(R.id.close_pop);
        popView.hotel_name = (TextView) view.findViewById(R.id.hotel_name);
        popView.add_bz = (TextView) view.findViewById(R.id.add_bz);
        popView.fenxiang_text = (TextView) view.findViewById(R.id.fenxiang_text);
        popView.hotel_money = (TextView) view.findViewById(R.id.hotel_money);
        popView.add_view_guige = (FlowLayout) view.findViewById(R.id.add_view_guige);
        popView.minus_number = (ImageView) view.findViewById(R.id.minus_number);
        popView.add_number = (ImageView) view.findViewById(R.id.add_number);
        popView.number_good = (TextView) view.findViewById(R.id.number_good);
        popView.layout_1 = (LinearLayout) view.findViewById(R.id.layout_1);
        popView.yes_order = (TextView) view.findViewById(R.id.yes_order);
        popView.heji_text = (TextView) view.findViewById(R.id.heji_text);
        popView.layout_2 = (LinearLayout) view.findViewById(R.id.layout_2);
        popView.add_car = (TextView) view.findViewById(R.id.add_car);
        popView.goto_pay = (TextView) view.findViewById(R.id.goto_pay);
        popView.jf_or_yuan = (TextView) view.findViewById(R.id.jf_or_yuan);
        final PopupWindow popupWindow = new PopupWindow(view,
                RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT);
        popView.number_good.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isNull(popView.number_good.getText().toString()))
                    popView.number_good.setText("1");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //判断是易物兑
        if ("1".equals(blogs.getKeytype())) {
            popView.add_bz.setVisibility(View.GONE);
            popView.fenxiang_text.setVisibility(View.GONE);
            popView.hotel_name.setText(blogs.getScore()+"积分");
            //平台自营
            if ("1".equals(blogs.getClient_id())) {
                //规格
                if ("1".equals(type)) {
                    popView.layout_1.setVisibility(View.GONE);
                    popView.layout_2.setVisibility(View.VISIBLE);
                    if ("0".equals(blogs.getLeftcount())) {
                        popView.goto_pay.setText("已售罄");
                        popView.goto_pay.setEnabled(false);
                        popView.goto_pay.setBackgroundResource(R.color.no_enabled);
                    } else {
                        popView.goto_pay.setEnabled(true);
                        popView.goto_pay.setText("立即购买");
                        popView.goto_pay.setBackgroundResource(R.color.title_backgroid);
                    }
                }
                //加入购物车
                else if ("2".equals(type)) {
                    popView.layout_2.setVisibility(View.GONE);
                    popView.layout_1.setVisibility(View.VISIBLE);
                    popView.yes_order.setBackgroundResource(R.color.car_);
                    popView.yes_order.setText("加入购物车");
                    popView.heji_text.setText("合计:" + blogs.getScore() + "积分");
                } else {
                    popView.layout_2.setVisibility(View.GONE);
                    popView.layout_1.setVisibility(View.VISIBLE);
                    popView.yes_order.setBackgroundResource(R.color.title_backgroid);
                    popView.yes_order.setText("立即购买");
                    popView.heji_text.setText("合计:" + blogs.getScore() + "积分");
                }
            }
            //品牌商
            else {
                //隐藏全部
                popView.layout_2.setVisibility(View.GONE);
                popView.layout_1.setVisibility(View.VISIBLE);

                if ("0".equals(blogs.getLeftcount())) {
                    popView.yes_order.setText("已售罄");
                    popView.yes_order.setEnabled(false);
                    popView.yes_order.setBackgroundResource(R.color.no_enabled);
                } else {
                    popView.yes_order.setEnabled(true);
                    popView.yes_order.setText("立即购买");
                    popView.yes_order.setBackgroundResource(R.color.title_backgroid);
                }
                popView.heji_text.setText("合计:" + blogs.getScore() + "积分");
            }
        }
        //限时兑
        else if ("2".equals(blogs.getKeytype())) {
            popView.add_bz.setVisibility(View.GONE);
            popView.layout_2.setVisibility(View.GONE);
            popView.fenxiang_text.setVisibility(View.GONE);
            popView.hotel_name.setText("金积分" + blogs.getGold_score());
            popView.yes_order.setEnabled(true);
            popView.yes_order.setText("立即兑换");
            popView.yes_order.setBackgroundResource(R.color.title_backgroid);
            popView.heji_text.setText("合计:金积分" + blogs.getGold_score() + "");
        }
        //分享币专区
        else {
            //
            popView.hotel_name.setText("¥"+blogs.getScore());
            popView.layout_2.setVisibility(View.GONE);
            popView.jf_or_yuan.setText("元");
            //分享币
            popView.fenxiang_text.setText("分享币" + blogs.getShare_score());
            popView.yes_order.setEnabled(true);
            popView.yes_order.setText("立即购买");
            popView.yes_order.setBackgroundResource(R.color.title_backgroid);
            popView.heji_text.setText("合计:¥" + blogs.getScore() + "元+" + blogs.getShare_score() + "分享币");
        }
        if (blogs.getSpecItems() == null) {
            showTextDialog("没有规格选择");
            return;
        }
        for (int i = 0; i < blogs.getSpecItems().size(); i++) {
            if (blogs.getSpecItems().get(i).isCheck()) {
                popView.hotel_money.setText(blogs.getSpecItems().get(i).getSpec_name());

                if ("1".equals(blogs.getKeytype())) {
                    popView.hotel_name.setText(blogs.getSpecItems().get(i).getScore());
                    popView.heji_text.setText(("合计:" + blogs.getSpecItems().get(i).getScore() + "积分"));

                } else if ("2".equals(blogs.getKeytype())) {
                    popView.hotel_name.setText("金积分" + blogs.getSpecItems().get(i).getGold_score());
                    popView.heji_text.setText("合计:金积分" + blogs.getSpecItems().get(i).getGold_score() + "");
                } else {
                    //
                    popView.hotel_name.setText("¥"+blogs.getSpecItems().get(i).getScore());
                    popView.jf_or_yuan.setText("元");
                    //分享币
                    popView.fenxiang_text.setText("分享币" + blogs.getSpecItems().get(i).getShare_score());
                    popView.heji_text.setText("合计:¥" + blogs.getSpecItems().get(i).getScore() + "元+" + blogs.getSpecItems().get(i).getShare_score() + "分享币");
                }
            }
        }
        String path = blogs.getImgurl();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.defult_gridview_img)
                .showImageForEmptyUri(R.mipmap.defult_gridview_img)
                .showImageOnFail(R.mipmap.defult_gridview_img).cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoader.getInstance().displayImage(path, popView.commod_img, options);
        popView.add_view_guige.removeAllViews();
        popView.add_view_guige.setVisibility(View.VISIBLE);
        ArrayList<TextView> textViews = new ArrayList<>();
        for (int i = 0; i < blogs.getSpecItems().size(); i++) {
            View view1 = LayoutInflater.from(mContext).inflate(R.layout.add_view_guige, null);
            TextView guige_text = (TextView) view1.findViewById(R.id.guige_text);
            textViews.add(guige_text);
            guige_text.setText(blogs.getSpecItems().get(i).getSpec_name());
            if (blogs.getSpecItems().get(i).isCheck()) {
                guige_text.setTextColor(getResources().getColor(R.color.white));
                guige_text.setBackgroundResource(R.color.title_backgroid);
                String path1 = blogs.getSpecItems().get(i).getImgurl();
                DisplayImageOptions options1 = new DisplayImageOptions.Builder()
                        .showImageOnLoading(R.mipmap.defult_gridview_img)
                        .showImageForEmptyUri(R.mipmap.defult_gridview_img)
                        .showImageOnFail(R.mipmap.defult_gridview_img).cacheInMemory(true)
                        .bitmapConfig(Bitmap.Config.RGB_565).build();
                ImageLoader.getInstance().displayImage(path1, popView.commod_img, options1);

            } else {
                guige_text.setTextColor(getResources().getColor(R.color.color_text));
                guige_text.setBackgroundResource(R.color.guige_b);
            }
            //选择
            guige_text.setTag(R.id.TAG, i);
            guige_text.setTag(R.id.TAG_VIEWHOLDER, textViews);
            guige_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i1 = (int) v.getTag(R.id.TAG);
                    ArrayList<TextView> textViews1 = (ArrayList<TextView>) v.getTag(R.id.TAG_VIEWHOLDER);
                    for (int j = 0; j < blogs.getSpecItems().size(); j++) {
                        if (j == i1) {
                            blogs.getSpecItems().get(j).setCheck(true);
                            textViews1.get(j).setTextColor(getResources().getColor(R.color.white));
                            textViews1.get(j).setBackgroundResource(R.color.title_backgroid);
                            popView.hotel_money.setText(blogs.getSpecItems().get(j).getSpec_name());
                            ViewHolder holder = (ViewHolder) holderView.getTag(R.id.TAG_VIEWHOLDER);

                            holder.guige_text.setText(blogs.getSpecItems().get(j).getSpec_name());
                            String path1 = blogs.getSpecItems().get(j).getImgurl();
                            DisplayImageOptions options1 = new DisplayImageOptions.Builder()
                                    .showImageOnLoading(R.mipmap.defult_gridview_img)
                                    .showImageForEmptyUri(R.mipmap.defult_gridview_img)
                                    .showImageOnFail(R.mipmap.defult_gridview_img).cacheInMemory(true)
                                    .bitmapConfig(Bitmap.Config.RGB_565).build();
                            ImageLoader.getInstance().displayImage(path1, popView.commod_img, options1);
                            //修改总计
                            if ("1".equals(blogs.getKeytype())) {
                                int num = Integer.valueOf(popView.number_good.getText().toString());
                                int score = Integer.valueOf(blogs.getSpecItems().get(j).getScore());

                                popView.heji_text.setText(("合计:" + String.valueOf(num * score) + "积分"));
                                popView.hotel_name.setVisibility(View.VISIBLE);
                                popView.hotel_name.setText(blogs.getSpecItems().get(j).getScore());

                                String number = blogs.getSpecItems().get(j).getScore();

                            }
                            //限时兑
                            else if ("2".equals(blogs.getKeytype())) {
                                int num = Integer.valueOf(popView.number_good.getText().toString());
                                int score = Integer.valueOf(blogs.getSpecItems().get(j).getGold_score());
                                popView.heji_text.setText(("合计:金积分" + String.valueOf(num * score) + ""));
                                popView.hotel_name.setVisibility(View.VISIBLE);
                                popView.hotel_name.setText("金积分" + blogs.getSpecItems().get(j).getGold_score());
                            } else {
                                int num = Integer.valueOf(popView.number_good.getText().toString());
                                int score = Integer.valueOf(blogs.getSpecItems().get(j).getScore());
                                int share_score = Integer.valueOf(blogs.getSpecItems().get(j).getShare_score());
                                popView.heji_text.setText("合计:¥" + String.valueOf(num * score) + "元+" + String.valueOf(num * share_score) + "分享币");
                                //
                                popView.hotel_name.setText("¥"+blogs.getSpecItems().get(j).getScore());
                                popView.jf_or_yuan.setText("元");
                                //分享币
                                popView.fenxiang_text.setText(blogs.getSpecItems().get(j).getShare_score()+"分享币" );
                                popView.hotel_name.setVisibility(View.VISIBLE);
                                popView.fenxiang_text.setVisibility(View.VISIBLE);
                            }
                        } else {
                            blogs.getSpecItems().get(j).setCheck(false);
                            textViews1.get(j).setTextColor(getResources().getColor(R.color.color_text));
                            textViews1.get(j).setBackgroundResource(R.color.guige_b);
                        }
                    }
                }
            });
            popView.add_view_guige.addView(view1);
        }
        //减
        popView.minus_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("1".equals(popView.number_good.getText().toString())) {
                } else {
                    int i = Integer.valueOf(popView.number_good.getText().toString()) - 1;
                    popView.number_good.setText(String.valueOf(i));
                    int j = 0;
                    for (int m = 0; m < blogs.getSpecItems().size(); m++) {
                        if (blogs.getSpecItems().get(m).isCheck()) {
                            j++;
                            if ("1".equals(blogs.getKeytype())) {
                                int num = Integer.valueOf(popView.number_good.getText().toString());
                                int score = Integer.valueOf(blogs.getSpecItems().get(m).getScore());
                                popView.heji_text.setText(("合计:" + String.valueOf(num * score) + "积分"));
                            } else if ("2".equals(blogs.getKeytype())) {
                                int num = Integer.valueOf(popView.number_good.getText().toString());
                                int score = Integer.valueOf(blogs.getSpecItems().get(m).getGold_score());
                                popView.heji_text.setText(("合计:金积分" + String.valueOf(num * score) + ""));
                            } else {
                                int num = Integer.valueOf(popView.number_good.getText().toString());
                                int score = Integer.valueOf(blogs.getSpecItems().get(m).getScore());
                                int share_score = Integer.valueOf(blogs.getSpecItems().get(m).getShare_score());
                                popView.heji_text.setText("合计:¥" + String.valueOf(num * score) + "元+" + String.valueOf(num * share_score) + "分享币");
                            }
                        }
                    }
                    if (j == 0) {
                        int num = Integer.valueOf(popView.number_good.getText().toString());
                        if ("1".equals(blogs.getKeytype())) {
                            int score = Integer.valueOf(blogs.getScore());
                            popView.heji_text.setText(("合计:" + String.valueOf(num * score) + "积分"));
                        } else if ("2".equals(blogs.getKeytype())) {
                            int score = Integer.valueOf(blogs.getGold_score());
                            popView.heji_text.setText(("合计:金积分" + String.valueOf(num * score) + ""));
                        } else {
                            int score = Integer.valueOf(blogs.getScore());
                            int share_score = Integer.valueOf(blogs.getShare_score());
                            popView.heji_text.setText("合计:¥" + String.valueOf(num * score) + "元+" + String.valueOf(num * share_score) + "分享币");
                        }
                    }

                }
            }
        });
        //加
        popView.add_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = Integer.valueOf(popView.number_good.getText().toString()) + 1;
                popView.number_good.setText(String.valueOf(i));
                int j = 0;
                for (int m = 0; m < blogs.getSpecItems().size(); m++) {
                    if (blogs.getSpecItems().get(m).isCheck()) {
                        j++;
                        if ("1".equals(blogs.getKeytype())) {
                            int num = Integer.valueOf(popView.number_good.getText().toString());
                            int score = Integer.valueOf(blogs.getSpecItems().get(m).getScore());
                            popView.heji_text.setText(("合计:" + String.valueOf(num * score) + "积分"));
                        } else if ("2".equals(blogs.getKeytype())) {
                            int num = Integer.valueOf(popView.number_good.getText().toString());
                            int score = Integer.valueOf(blogs.getSpecItems().get(m).getGold_score());
                            popView.heji_text.setText(("合计:金积分" + String.valueOf(num * score) + ""));
                        } else {
                            int num = Integer.valueOf(popView.number_good.getText().toString());
                            int score = Integer.valueOf(blogs.getSpecItems().get(m).getScore());
                            int share_score = Integer.valueOf(blogs.getSpecItems().get(m).getShare_score());
                            popView.heji_text.setText("合计:¥" + String.valueOf(num * score) + "元+" + String.valueOf(num * share_score) + "分享币");
                        }
                    }
                }
                if (j == 0) {
                    int num = Integer.valueOf(popView.number_good.getText().toString());
                    if ("1".equals(blogs.getKeytype())) {
                        int score = Integer.valueOf(blogs.getScore());
                        popView.heji_text.setText(("合计:" + String.valueOf(num * score) + "积分"));
                    } else if ("2".equals(blogs.getKeytype())) {
                        int score = Integer.valueOf(blogs.getGold_score());
                        popView.heji_text.setText(("合计:金积分" + String.valueOf(num * score) + ""));
                    } else {
                        int score = Integer.valueOf(blogs.getScore());
                        int share_score = Integer.valueOf(blogs.getShare_score());
                        popView.heji_text.setText("合计:¥" + String.valueOf(num * score) + "元+" + String.valueOf(num * share_score) + "分享币");
                    }
                }

            }
        });
        //关闭
        popView.close_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        //易物兑的平台自营
        //加入购物车
        popView.add_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("-1".equals(checkStand())) {
                    showTextDialog("请选择商品规格");
                    return;
                } else {
                    popupWindow.dismiss();
                    String num = popView.number_good.getText().toString();
                    String brandId = checkStand();
                    String token = JhctmApplication.getInstance().getUser().getToken();
                    getNetWorker().cartAdd(token, goodId, brandId, num);
                }
            }
        });
        //直接购买
        popView.goto_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("-1".equals(checkStand())) {
                    showTextDialog("请选择商品规格");
                    return;
                } else {
                    popupWindow.dismiss();
                    String num = popView.number_good.getText().toString();
                    Intent intent = new Intent(GoodsInformationActivity.this, ConfirmOrderActivity.class);
                    intent.putExtra("good", blogs);
                    intent.putExtra("num", num);
                    startActivity(intent);
                }
            }
        });
        //平台自营中的直接购买或加入购物车
        popView.yes_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("-1".equals(checkStand())) {
                    showTextDialog("请选择商品规格");
                    return;
                } else { //加入购物车
                    if ("2".equals(type)) {
                        popupWindow.dismiss();
                        String num = popView.number_good.getText().toString();
                        String brandId = checkStand();
                        String token = JhctmApplication.getInstance().getUser().getToken();
                        getNetWorker().cartAdd(token, goodId, brandId, num);
                    }
                    //规格，立即购买
                    else {
                        popupWindow.dismiss();
                        String num = popView.number_good.getText().toString();
                        Intent intent = new Intent(GoodsInformationActivity.this, ConfirmOrderActivity.class);
                        intent.putExtra("good", blogs);
                        intent.putExtra("num", num);
                        startActivity(intent);
                    }
                }


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

    //判断是否选中规格
    private String checkStand() {
        String j = "-1";
        for (int i = 0; i < blogs.getSpecItems().size(); i++) {
            if (blogs.getSpecItems().get(i).isCheck())
                j = blogs.getSpecItems().get(i).getId();
        }
        return j;
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
            oks.setText(blogs.getName());
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
