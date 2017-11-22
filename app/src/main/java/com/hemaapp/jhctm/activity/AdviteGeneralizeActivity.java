package com.hemaapp.jhctm.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.hm_FrameWork.view.RefreshLoadmoreLayout;
import com.hemaapp.jhctm.JhActivity;
import com.hemaapp.jhctm.JhHttpInformation;
import com.hemaapp.jhctm.JhctmApplication;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.adapter.AdviteGeneralizeAdapter;
import com.hemaapp.jhctm.model.AdList;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import xtom.frame.util.XtomFileUtil;
import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * Created by lenovo on 2017/4/10.
 * 广告推广
 */
public class AdviteGeneralizeActivity extends JhActivity implements PlatformActionListener {
    private ImageButton back_button;
    private TextView title_text;
    private ImageButton next_button;
    private RefreshLoadmoreLayout refreshLoadmoreLayout;
    private XtomListView listview;
    private ProgressBar progressbar;
    private Integer page = 0;
    private AdviteGeneralizeAdapter adapter;
    private ArrayList<AdList> adLists = new ArrayList<>();
    private String rebatid;
    private OnekeyShare oks;
    private String sys_plugins;
    private String pathWX;
    private ShareHolder shareHolder;
    private String shareType = "0";

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
        getNetWorker().advertiseList(token, rebatid, String.valueOf(page));
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ADVERTISE_LIST:
                showProgressDialog("获取广告信息");
                break;
            case SHARE_ADVERTISE:
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ADVERTISE_LIST:
                progressbar.setVisibility(View.GONE);
                refreshLoadmoreLayout.setVisibility(View.VISIBLE);
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ADVERTISE_LIST:
                HemaPageArrayResult<AdList> result3 = (HemaPageArrayResult<AdList>) hemaBaseResult;
                ArrayList<AdList> adLists = result3.getObjects();
                String page2 = hemaNetTask.getParams().get("page");
                if ("0".equals(page2)) {// 刷新
                    refreshLoadmoreLayout.refreshSuccess();
                    this.adLists.clear();
                    this.adLists.addAll(adLists);

                    JhctmApplication application = JhctmApplication.getInstance();
                    int sysPagesize = application.getSysInitInfo()
                            .getSys_pagesize();
                    if (adLists.size() < sysPagesize) {
                        refreshLoadmoreLayout.setLoadmoreable(false);
                        // leftRE = false;
                    } else {
                        refreshLoadmoreLayout.setLoadmoreable(true);
                        // leftRE = true;
                    }
                } else {// 更多
                    refreshLoadmoreLayout.loadmoreSuccess();
                    if (adLists.size() > 0)
                        this.adLists.addAll(adLists);
                    else {
                        refreshLoadmoreLayout.setLoadmoreable(false);
                        // leftRE = false;
                        XtomToastUtil.showShortToast(mContext, "已经到最后啦");
                    }
                }
                freshData();
                break;
            case SHARE_ADVERTISE:
                if ("0".equals(shareType)) {
                } else if ("1".equals(shareType)) {
                } else if ("2".equals(shareType)) {
                } else if ("3".equals(shareType)) {
                    showShare(WechatMoments.NAME);
                } else if ("4".equals(shareType)) {
                    showShare(Wechat.NAME);
                }
                break;
        }
    }

    private void freshData() {
        if (adapter == null) {
            adapter = new AdviteGeneralizeAdapter(mContext, adLists);
            adapter.setEmptyString("暂无数据");
            listview.setAdapter(adapter);
        } else {
            adapter.setEmptyString("暂无数据");
            adapter.setAdLists(adLists);
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ADVERTISE_LIST:
            case SHARE_ADVERTISE:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ADVERTISE_LIST:
                String page = hemaNetTask.getParams().get("page");
                if ("0".equals(page)) {
                    refreshLoadmoreLayout.refreshFailed();
                } else {
                    refreshLoadmoreLayout.loadmoreFailed();
                }
                showProgressDialog("获取广告列表失败，请稍后重试");
                break;
            case SHARE_ADVERTISE:
                showTextDialog("分享失败，请稍后重试");
                break;
        }
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
        rebatid = mIntent.getStringExtra("rebatid");
    }

    @Override
    protected void setListener() {
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_text.setText("广告推广");
        next_button.setVisibility(View.INVISIBLE);
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

    //分享
    //分享
    private class ShareHolder {
        TextView qq_text;
        TextView qq_zone_text;
        TextView wechat_text;
        TextView wechat_friend_text;
        TextView sinbo_text;
        TextView close_pop;
    }

    public void showShareView(final String id,String path) {
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
        final String token = JhctmApplication.getInstance().getUser().getToken();
        pathWX = path;
        //QQ
        shareHolder.qq_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareType = "0";
                showTextDialog("暂未开通，敬请期待！");
            }
        });
        shareHolder.qq_zone_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTextDialog("暂未开通，敬请期待！");
                shareType = "1";
            }
        });
        shareHolder.sinbo_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTextDialog("暂未开通，敬请期待！");
                shareType = "2";
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
                shareType = "3";
                getNetWorker().shareAdvertise(token, id);

                popupWindow.dismiss();
            }
        });
        shareHolder.wechat_friend_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareType = "4";
                getNetWorker().shareAdvertise(token, id);
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
