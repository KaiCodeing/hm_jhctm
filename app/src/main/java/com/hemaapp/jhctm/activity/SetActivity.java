package com.hemaapp.jhctm.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.jhctm.JhActivity;
import com.hemaapp.jhctm.JhHttpInformation;
import com.hemaapp.jhctm.JhctmApplication;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.model.SysInitInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
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
import xtom.frame.XtomActivityManager;
import xtom.frame.image.cache.XtomImageCache;
import xtom.frame.media.XtomVoicePlayer;
import xtom.frame.util.XtomFileUtil;
import xtom.frame.util.XtomSharedPreferencesUtil;

/**
 * Created by lenovo on 2017/1/3.
 * 设置
 */
public class SetActivity extends JhActivity implements PlatformActionListener {
    private ImageButton back_button;
    private TextView title_text;
    private ImageButton next_button;
    private TextView change_pwd;//修改密码
    private ImageView check_tuisong;//推送消息
    private TextView cache;//清除缓存
    private LinearLayout clear_layout;
    private TextView login_out;//退出登录
    private ViewHolder holder;
    private TextView share_software;
    private OnekeyShare oks;
    private String sys_plugins;
    private String pathWX;
    private ShareHolder shareHolder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_set);
        super.onCreate(savedInstanceState);
        ShareSDK.initSDK(this);
        SysInitInfo initInfo = getApplicationContext()
                .getSysInitInfo();
        sys_plugins = initInfo.getSys_plugins();
        pathWX = sys_plugins + "share/sdk.php?id=0" ;
        log_i("++++++++"+pathWX);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_LOGINOUT:
                showProgressDialog("退出登录");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_LOGINOUT:
                showProgressDialog("退出登录");
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_LOGINOUT:
                JhctmApplication.getInstance().setUser(null);
                XtomSharedPreferencesUtil.save(mContext, "username", "");// 清空用户名
                XtomSharedPreferencesUtil.save(mContext, "password", "");// 青空密码
                //XtomSharedPreferencesUtil.save(getActivity(), "city_name", "");
                XtomSharedPreferencesUtil.save(mContext, "notice", "");// 青空推送
                XtomActivityManager.finishAll();
                Intent it = new Intent(mContext, LoginActivity.class);
                startActivity(it);
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_LOGINOUT:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_LOGINOUT:
                showTextDialog("退出登录失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (ImageButton) findViewById(R.id.next_button);
        change_pwd = (TextView) findViewById(R.id.change_pwd);
        check_tuisong = (ImageView) findViewById(R.id.check_tuisong);
        cache = (TextView) findViewById(R.id.cache);
        clear_layout = (LinearLayout) findViewById(R.id.clear_layout);
        login_out = (TextView) findViewById(R.id.login_out);
        share_software = (TextView) findViewById(R.id.share_software);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        String notice = XtomSharedPreferencesUtil.get(mContext, "notice");
        if (isNull(notice))
            check_tuisong.setImageResource(R.mipmap.tuisong_off_img);
        else
            check_tuisong.setImageResource(R.mipmap.tuisong_on_img);
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_text.setText("设置");
        next_button.setVisibility(View.INVISIBLE);
        //修改登录密码
        change_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetActivity.this, ModifyPWDActivity.class);
                startActivity(intent);
            }
        });
        //接受推送
        check_tuisong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String notice = XtomSharedPreferencesUtil.get(mContext, "notice");
                if (isNull(notice)) {
                    check_tuisong.setImageResource(R.mipmap.tuisong_on_img);
                    XtomSharedPreferencesUtil.save(mContext, "notice", "1");
                } else {
                    check_tuisong.setImageResource(R.mipmap.tuisong_off_img);
                    XtomSharedPreferencesUtil.save(mContext, "notice", "");
                }
            }
        });
        cache.setText(bytes2kb(XtomImageCache.getInstance(mContext).getCacheSize()));
        //清除缓存
        clear_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ClearTask().execute();
            }
        });
        //退出登录
        login_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showClient();
            }
        });
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //分享
        share_software.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShareView();
            }
        });
    }


    private class ClearTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // 删除图片缓存
            XtomImageCache.getInstance(mContext).deleteCache();
            // 删除语音缓存
            XtomVoicePlayer player = XtomVoicePlayer.getInstance(mContext);
            player.deleteCache();
            player.release();
            return null;
        }

        @Override
        protected void onPreExecute() {
            showProgressDialog("正在清除缓存");
        }

        @Override
        protected void onPostExecute(Void result) {
            cancelProgressDialog();
            showTextDialog("缓存清理完毕！");
            cache.setText(bytes2kb(XtomImageCache.getInstance(mContext).getCacheSize()));
        }
    }

    /**
     * byte(字节)根据长度转成kb(千字节)和mb(兆字节)
     *
     * @param bytes
     * @return
     */
    public static String bytes2kb(long bytes) {
        BigDecimal filesize = new BigDecimal(bytes);
        BigDecimal megabyte = new BigDecimal(1024 * 1024);
        float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP)
                .floatValue();
        if (returnValue > 1)
            return (returnValue + "M");
        BigDecimal kilobyte = new BigDecimal(1024);
        returnValue = filesize.divide(kilobyte, 2, BigDecimal.ROUND_UP)
                .floatValue();
        return (returnValue + "K");
    }

    private class ViewHolder {
        TextView content_text;
        TextView content_no;
        TextView content_yes;
    }

    private void showClient() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.pop_left_right_view, null);
        holder = new ViewHolder();
        holder.content_text = (TextView) view.findViewById(R.id.content_text);
        holder.content_no = (TextView) view.findViewById(R.id.content_no);
        holder.content_yes = (TextView) view.findViewById(R.id.content_yes);
        holder.content_text.setText("确定要退出当前账号？");
        final PopupWindow popupWindow = new PopupWindow(view,
                RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT);
        holder.content_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        holder.content_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = JhctmApplication.getInstance().getUser().getToken();
                getNetWorker().clientLoginout(token);
                popupWindow.dismiss();
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
        // popupWindow.showAsDropDown(findViewById(R.id.ll_item));
        popupWindow.setAnimationStyle(R.style.PopupAnimation);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
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
