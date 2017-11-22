package com.hemaapp.jhctm.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.jhctm.JhActivity;
import com.hemaapp.jhctm.JhHttpInformation;
import com.hemaapp.jhctm.JhctmApplication;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.config.JhConfig;
import com.hemaapp.jhctm.model.User;
import com.hemaapp.jhctm.view.JhctmImageWay;

import java.io.File;
import java.io.IOException;

import xtom.frame.XtomActivityManager;
import xtom.frame.XtomConfig;
import xtom.frame.image.load.XtomImageTask;
import xtom.frame.util.Md5Util;
import xtom.frame.util.XtomBaseUtil;
import xtom.frame.util.XtomFileUtil;
import xtom.frame.util.XtomSharedPreferencesUtil;

/**
 * Created by lenovo on 2016/12/20.
 * 注册 填写个人信息
 */
public class RegisterActivity extends JhActivity {
    private ImageButton back_button;
    private TextView title_text;
    private ImageButton next_button;
    private ImageView user_img;//头像
    private EditText user_name;//姓名
    private EditText user_num;//身份证号
    private TextView up_text;//提交
    //接收到 的 字段
    private String username;//手机号
    private String password;//密码
    private String tempToken;//临时token
    private String supid;//超市id
    private String share_tel;//分享手机号

    private JhctmImageWay imageWay;
    private String imagePathCamera;
    private String tempPath = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            imageWay = new JhctmImageWay(mContext, 1, 2);
        } else {
            imagePathCamera = savedInstanceState.getString("imagePathCamera");
            imageWay = new JhctmImageWay(mContext, 1, 2);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK != resultCode) {
            return;
        }
        switch (requestCode) {
            case 1:
                album(data);
                break;
            case 2:
                camera();
                break;
            case 3:
                imageWorker.loadImage(new XtomImageTask(user_img, tempPath,
                        mContext, new XtomImageTask.Size(180, 180)));
                break;

            default:
                break;
        }
    }
    private void album(Intent data) {
        if (data == null)
            return;
        Uri selectedImageUri = data.getData();
        startPhotoZoom(selectedImageUri, 3);
    }

    private void editImage(String path, int requestCode) {
        File file = new File(path);
        startPhotoZoom(Uri.fromFile(file), requestCode);
    }

    private void startPhotoZoom(Uri uri, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", false);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", JhConfig.IMAGE_WIDTH);
        intent.putExtra("aspectY", JhConfig.IMAGE_WIDTH);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", JhConfig.IMAGE_WIDTH);
        intent.putExtra("outputY", JhConfig.IMAGE_WIDTH);
        intent.putExtra("return-data", false);
        startActivityForResult(intent, requestCode);
    }

    private Uri getTempUri() {
        return Uri.fromFile(getTempFile());
    }

    private File getTempFile() {
        String savedir = XtomFileUtil.getTempFileDir(mContext);
        File dir = new File(savedir);
        if (!dir.exists())
            dir.mkdirs();
        // 保存入sdCard
        tempPath = savedir + XtomBaseUtil.getFileName() + ".jpg";// 保存路径
        File file = new File(tempPath);
        try {
            file.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        return file;
    }

    private void camera() {
//		imagePathCamera = null;
//		if (imagePathCamera == null) {
//			imagePathCamera = imageWay.getCameraImage();
//		}
        String path = imageWay.getCameraImage();
        if (!isNull(path))
            imagePathCamera = path;
        log_i("imagePathCamera=" + imagePathCamera);
        editImage(imagePathCamera, 3);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (imageWay != null)
            outState.putString("imagePathCamera", imageWay.getCameraImage());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_ADD:
                showProgressDialog("上传个人信息");
                break;
            case FILE_UPLOAD:
                showProgressDialog("上传个人头像");
                break;
            case CLIENT_LOGIN:
                showProgressDialog("登录...");
                break;

            default:
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_ADD:
               cancelProgressDialog();
                break;
            case FILE_UPLOAD:
                cancelProgressDialog();
                break;
            case CLIENT_LOGIN:
                cancelProgressDialog();
                break;

            default:
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_ADD:
                HemaArrayResult<String> result = (HemaArrayResult<String>) hemaBaseResult;
                String token = result.getObjects().get(0);
                if (isNull(tempPath) || tempPath.equals("")) {
                    getNetWorker().clientLogin(username, Md5Util.getMd5(XtomConfig.DATAKEY
                            + Md5Util.getMd5(password)));
                } else {
                    getNetWorker().fileUpload(token,"1","0","0","0","无",tempPath);
                }
                break;
            case FILE_UPLOAD:
                    getNetWorker().clientLogin(username, Md5Util.getMd5(XtomConfig.DATAKEY
                            + Md5Util.getMd5(password)));
                break;
            case CLIENT_LOGIN:
                HemaArrayResult<User> result2 = (HemaArrayResult<User>) hemaBaseResult;
                User user = result2.getObjects().get(0);
                JhctmApplication.getInstance().setUser(user);
                XtomSharedPreferencesUtil.save(mContext, "username", username);
                XtomSharedPreferencesUtil.save(mContext, "password", Md5Util.getMd5(XtomConfig.DATAKEY
                        + Md5Util.getMd5(password)));

                XtomActivityManager.finishAll();
                Intent it1 = new Intent(mContext, MainActivity.class);
                startActivity(it1);
                break;
            default:
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_ADD:
            case FILE_UPLOAD:
            case CLIENT_LOGIN:
                showTextDialog(hemaBaseResult.getMsg());

                break;
            default:
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_ADD:
                showTextDialog("添加个人信息失败，请稍后重试");
                break;
            case FILE_UPLOAD:
                showTextDialog("添加个人头像失败，请稍后重试");
                break;
            case CLIENT_LOGIN:
                showTextDialog("登录失败，请稍后重试");
                break;
            default:
                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (ImageButton) findViewById(R.id.next_button);
        user_img = (ImageView) findViewById(R.id.user_img);
        user_name = (EditText) findViewById(R.id.user_name);
        user_num = (EditText) findViewById(R.id.user_num);
        up_text = (TextView) findViewById(R.id.up_text);
    }

    @Override
    protected void getExras() {
        username = mIntent.getStringExtra("username");
        password = mIntent.getStringExtra("password");
        tempToken = mIntent.getStringExtra("tempToken");
        supid = mIntent.getStringExtra("supid");
        share_tel = mIntent.getStringExtra("share_tel");
    }

    @Override
    protected void setListener() {
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_text.setText("完善个人信息");
        next_button.setVisibility(View.INVISIBLE);
        /**
         * 照片
         */
        user_img.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                mInputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),
                        0);
                imageWay.show();

            }
        });
        //提交
        up_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNull(tempPath))
                {
                    showTextDialog("请完善您的头像");
                    return;
                }
                String userName = user_name.getText().toString().trim();
                String userNum = user_num.getText().toString().trim();
                if (isNull(userName))
                {
                    showTextDialog("请完善您的姓名");
                    return;
                }
                if (isNull(userNum))
                {
                    showTextDialog("请完善您的身份证号");
                    return;
                }
            getNetWorker().clientAdd(tempToken,username, Md5Util.getMd5(XtomConfig.DATAKEY
                    + Md5Util.getMd5(password)),userName,userNum,supid,share_tel);
            }
        });
    }
}
