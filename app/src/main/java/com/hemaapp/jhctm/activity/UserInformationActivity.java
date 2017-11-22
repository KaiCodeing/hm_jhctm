package com.hemaapp.jhctm.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.jhctm.JhActivity;
import com.hemaapp.jhctm.JhHttpInformation;
import com.hemaapp.jhctm.JhctmApplication;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.adapter.UserInformationAdapter;
import com.hemaapp.jhctm.config.JhConfig;
import com.hemaapp.jhctm.model.Bank;
import com.hemaapp.jhctm.model.User;
import com.hemaapp.jhctm.view.JhctmImageWay;
import com.hemaapp.jhctm.view.MyListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import xtom.frame.image.load.XtomImageTask;
import xtom.frame.util.XtomBaseUtil;
import xtom.frame.util.XtomFileUtil;

/**
 * Created by lenovo on 2017/1/3.
 * 修改个人信息
 * UserInformationAdapter
 */
public class UserInformationActivity extends JhActivity {
    private ImageButton back_button;
    private TextView title_text;
    private ImageButton next_button;
    private ProgressBar progressbar;
    //    private RefreshLoadmoreLayout refreshLoadmoreLayout;
    private MyListView listview;
    private ImageView user_img;//头像
    private LinearLayout crame_layout;//照相
    private TextView user_name;//用户昵称
    private TextView user_id_number;//身份证号
    private TextView user_tel;//手机号
    private ImageView add_bank;//添加银行卡号
    private TextView up_text;//提交
    private User user;
    private ArrayList<Bank> banks = new ArrayList<>();
    private UserInformationAdapter adapter;
    private JhctmImageWay imageWay;
    private String imagePathCamera;
    private String tempPath = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_user_information);
        super.onCreate(savedInstanceState);
        inIt();
        if (savedInstanceState == null) {
            imageWay = new JhctmImageWay(mContext, 1, 2);
        } else {
            imagePathCamera = savedInstanceState.getString("imagePathCamera");
            imageWay = new JhctmImageWay(mContext, 1, 2);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String token = JhctmApplication.getInstance().getUser().getToken();
        getNetWorker().clientBankList(token);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
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

    private void inIt() {
        String token = JhctmApplication.getInstance().getUser().getToken();
        String userId = JhctmApplication.getInstance().getUser().getId();
        getNetWorker().clientGet(token, userId);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_GET:
                showProgressDialog("获取用户信息");
                break;
            case CLIENT_BANK_LIST:
                showProgressDialog("获取用户银行信息");
                break;
            case CLIENT_BANK_SAVE:
                showProgressDialog("保存银行信息");
                break;
            case FILE_UPLOAD:
                showProgressDialog("保存头像");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_GET:
                cancelProgressDialog();
                progressbar.setVisibility(View.GONE);
                //     refreshLoadmoreLayout.setVisibility(View.VISIBLE);
                break;
            case CLIENT_BANK_LIST:
            case FILE_UPLOAD:
            case CLIENT_BANK_SAVE:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_GET:
                HemaArrayResult<User> result = (HemaArrayResult<User>) hemaBaseResult;
                user = result.getObjects().get(0);
                String token = JhctmApplication.getInstance().getUser().getToken();
                setData();
                getNetWorker().clientBankList(token);

                break;
            case CLIENT_BANK_LIST:
                HemaArrayResult<Bank> result1 = (HemaArrayResult<Bank>) hemaBaseResult;
                banks.clear();
                banks = result1.getObjects();
                freshData();

                break;
            case FILE_UPLOAD:
                for (Bank bank : banks) {
                    if (bank.isCheck()) {
                        String token1 = JhctmApplication.getInstance().getUser().getToken();
                        String bankName = bank.getBankname();
                        String bankNum = bank.getBankcard();
                        String bankUser = bank.getBankuser();
                        String kaiBank = bank.getBankaddress();
                        String bankId = bank.getId();
                        getNetWorker().clientBankSave(token1, bankId, bankName, bankUser, bankNum, kaiBank, "1");
                    }
                }
                break;
            case CLIENT_BANK_SAVE:
                showTextDialog("修改成功");
                next_button.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                },1000);
                break;
        }
    }

    private void freshData() {
        if (adapter == null) {
            adapter = new UserInformationAdapter(UserInformationActivity.this, banks);
            adapter.setEmptyString("暂无数据");
            listview.setAdapter(adapter);
        } else {
            adapter.setEmptyString("暂无数据");
            adapter.setBanks(banks);
            adapter.notifyDataSetChanged();
        }
    }

    private void setData() {
        //头像
        String path = user.getAvatar();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.defult_gridview_img)
                .showImageForEmptyUri(R.mipmap.defult_gridview_img)
                .showImageOnFail(R.mipmap.defult_gridview_img).cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoader.getInstance().displayImage(path, user_img, options);
        user_name.setText(user.getNickname());
        user_id_number.setText(user.getContisignintimes());
        user_tel.setText(user.getMobile());

    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_GET:
            case CLIENT_BANK_LIST:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_GET:
                showTextDialog("获取用户信息失败，请稍后重试");
                break;
            case CLIENT_BANK_LIST:
                showTextDialog("获取银行卡信息失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (ImageButton) findViewById(R.id.next_button);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        //    refreshLoadmoreLayout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        listview = (MyListView) findViewById(R.id.listview);
        user_img = (ImageView) findViewById(R.id.user_img);
        crame_layout = (LinearLayout) findViewById(R.id.crame_layout);
        user_id_number = (TextView) findViewById(R.id.user_id_number);
        user_name = (TextView) findViewById(R.id.user_name);
        user_tel = (TextView) findViewById(R.id.user_tel);
        add_bank = (ImageView) findViewById(R.id.add_bank);
        up_text = (TextView) findViewById(R.id.up_text);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });
        title_text.setText("修改个人信息");
        next_button.setVisibility(View.INVISIBLE);
        //添加银行卡
        add_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInformationActivity.this, AddBankActivity.class);
                startActivity(intent);
            }
        });
        //提交
        up_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = JhctmApplication.getInstance().getUser().getToken();
                if (isNull(tempPath)) {
                    for (Bank bank : banks) {
                        if (bank.isCheck()) {
                            String bankName = bank.getName();
                            String bankNum = bank.getBankcard();
                            String bankUser = bank.getBankuser();
                            String kaiBank = bank.getBankaddress();
                            String bankId = bank.getId();
                            getNetWorker().clientBankSave(token, bankId, bankName, bankUser, bankNum, kaiBank, "1");
                        }
                    }
                } else {
                    getNetWorker().fileUpload(token,"1","0","0","0","无",tempPath);
                }
            }
        });
        //照片
        user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),
                        0);
                imageWay.show();
            }
        });
    }
    //修改默认
    public void changeData(int m) {
        for (int i = 0; i < banks.size(); i++) {
            if (i == m) {
                banks.get(i).setCheck(true);
            } else {
                banks.get(i).setCheck(false);
            }
        }
        freshData();
    }
}
