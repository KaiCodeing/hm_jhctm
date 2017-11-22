package com.hemaapp.jhctm.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.hemaapp.jhctm.adapter.ReplyImageAdapter;
import com.hemaapp.jhctm.config.JhConfig;
import com.hemaapp.jhctm.model.OrderGoodInfor;
import com.hemaapp.jhctm.model.User;
import com.hemaapp.jhctm.view.JhctmGridView;
import com.hemaapp.jhctm.view.JhctmImageWay;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import xtom.frame.image.load.XtomImageTask;
import xtom.frame.util.XtomFileUtil;
import xtom.frame.util.XtomImageUtil;

/**
 * Created by lenovo on 2017/1/6.
 * 评价的activity
 */
public class EvaluateActivity extends JhActivity {
    private TextView title_text;
    private ImageButton back_button;
    private Button next_button;
    private ImageView commod_img;//商品图片
    private TextView hotel_name;//商品名称
    private TextView hotel_attri;//商品规格
    private TextView text_left;//
    private TextView text_middle;//
    private TextView text_right;//
    private ImageView star1;
    private ImageView star2;
    private ImageView star3;
    private ImageView star4;
    private ImageView star5;
    private ArrayList<ImageView> views = new ArrayList<>();
    private EditText content_evaluate;//输入评价
    private JhctmGridView gridview;//评价图片
    private ReplyImageAdapter adapter;

    private OrderGoodInfor infor;
    private String keytype, star="5", content, reply_id;
    private JhctmImageWay imageWay;
    private ArrayList<String> images = new ArrayList<>();
    private User user;
    private int orderby = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_evaluate);
        super.onCreate(savedInstanceState);
        user = JhctmApplication.getInstance().getUser();
        imageWay = new JhctmImageWay(mContext, 1, 2){
            @Override
            public void album() {
                Intent it = new Intent(mContext, AlbumActivity.class);
                it.putExtra("limitCount", 4 - images.size());// 图片选择张数限制
                startActivityForResult(it, albumRequestCode);
            }
        };
        adapter = new ReplyImageAdapter(mContext, images);
        gridview.setAdapter(adapter);
        setdata();
    }

    public void showImageWay() {
        imageWay.show();
    }

    @Override
    protected void onDestroy() {
        deleteCompressPics();
        super.onDestroy();
    }

    // 删除临时图片文件
    private void deleteCompressPics() {
        if(images.size() > 0)
            for (String string : images) {
                File file = new File(string);
                file.delete();
            }
    }

    private void camera() {
        String imagepath = imageWay.getCameraImage();
        new CompressPicTask().execute(imagepath);
    }

    // 自定义相册选择时处理方法
    private void album(Intent data) {
        if (data == null)
            return;
        ArrayList<String> imgList = data.getStringArrayListExtra("images");
        if (imgList == null)
            return;
        for (String img : imgList) {
            new CompressPicTask().execute(img);
        }
    }

    /**
     * 压缩图片
     */
    private class CompressPicTask extends AsyncTask<String, Void, Integer> {
        String compressPath;

        @Override
        protected Integer doInBackground(String... params) {
            try {
                String path = params[0];
                String savedir = XtomFileUtil.getTempFileDir(mContext);
                compressPath = XtomImageUtil.compressPictureWithSaveDir(path,
                        JhConfig.IMAGE_WIDTH, JhConfig.IMAGE_WIDTH,
                        JhConfig.IMAGE_QUALITY, savedir, mContext);
                return 0;
            } catch (IOException e) {
                return 1;
            }
        }

        @Override
        protected void onPreExecute() {
            showProgressDialog("正在压缩图片");
        }

        @Override
        protected void onPostExecute(Integer result) {
            cancelProgressDialog();
            switch (result) {
                case 0:
                    images.add(compressPath);
                    adapter.notifyDataSetChanged();
                    break;
                case 1:
                    showTextDialog("图片压缩失败");
                    break;
            }
        }
    }


    private void setdata(){
        try {
            URL url = new URL(infor.getImgurl());
            imageWorker.loadImage(new XtomImageTask(commod_img, url, mContext));
        } catch (MalformedURLException e) {
            commod_img.setImageResource(R.mipmap.hotel_defult_img);
        }
        hotel_name.setText(infor.getName());
        hotel_attri.setText("规格："+infor.getSpec_name()+";数量："+infor.getBuycount());
        if("1".equals(keytype)){
            text_left.setVisibility(View.GONE);
            text_middle.setText(infor.getScore());
            text_right.setVisibility(View.VISIBLE);
            text_right.setText("消费积分");
        }else if("2".equals(keytype)){
            text_left.setVisibility(View.VISIBLE);
            text_left.setText("金积分");
            text_middle.setText(infor.getGold_score());
            text_right.setVisibility(View.GONE);
        }else{
            text_left.setVisibility(View.GONE);
            text_middle.setVisibility(View.VISIBLE);
            text_middle.setText(infor.getScore());
            text_right.setVisibility(View.VISIBLE);
            text_right.setText("消费积分+分享币"+infor.getShare_score());
        }

    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information){
            case REPLY_ADD:
                showProgressDialog("正在提交...");
                break;
            case FILE_UPLOAD:
                showProgressDialog("正在保存图片...");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information){
            case REPLY_ADD:
            case FILE_UPLOAD:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information){
            case REPLY_ADD:
                HemaArrayResult<String> sReuslt = (HemaArrayResult<String>) hemaBaseResult;
                reply_id = sReuslt.getObjects().get(0);
                if(images.size() > 0)
                    fileUpdate();
                else
                {
                    showTextDialog("评论成功");
                    next_button.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setResult(RESULT_OK, mIntent);
                            finish();
                        }
                    },1000);
                }
                break;
            case FILE_UPLOAD:
                if(images.size() > 0)
                    fileUpdate();
                else{
                    showTextDialog("提交成功");
                    title_text.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setResult(RESULT_OK, mIntent);
                            finish();
                        }
                    }, 1000);
                }
                break;
        }
    }

    private void fileUpdate(){
        getNetWorker().fileUpload(user.getToken(), "11", reply_id, "0", String.valueOf(orderby), "无", images.get(0));
        images.remove(0);
        orderby++;
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information){
            case REPLY_ADD:
            case FILE_UPLOAD:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information){
            case REPLY_ADD:
                showTextDialog("提交失败，请稍后重试");
                break;
            case FILE_UPLOAD:
                showTextDialog("保存图片失败");
                break;
        }
    }

    @Override
    protected void findView() {
        title_text = (TextView) findViewById(R.id.title_text);
        back_button = (ImageButton) findViewById(R.id.back_button);
        next_button = (Button) findViewById(R.id.next_button);
        commod_img = (ImageView) findViewById(R.id.imageview);
        hotel_name = (TextView) findViewById(R.id.textview);
        hotel_attri = (TextView) findViewById(R.id.textview_0);
        text_left = (TextView) findViewById(R.id.textview_1);
        text_middle = (TextView) findViewById(R.id.textview_3);
        text_right = (TextView) findViewById(R.id.textview_2);
        star1 = (ImageView) findViewById(R.id.star1);
        star2 = (ImageView) findViewById(R.id.star2);
        star3 = (ImageView) findViewById(R.id.star3);
        star4 = (ImageView) findViewById(R.id.star4);
        star5 = (ImageView) findViewById(R.id.star5);
        content_evaluate = (EditText) findViewById(R.id.content_evaluate);
        gridview = (JhctmGridView) findViewById(R.id.gridview);
        views.add(0, star1);
        views.add(1, star2);
        views.add(2, star3);
        views.add(3, star4);
        views.add(4, star5);
    }

    @Override
    protected void getExras() {
        infor = (OrderGoodInfor) mIntent.getSerializableExtra("good");
        keytype = mIntent.getStringExtra("keytype");
    }

    @Override
    protected void setListener() {
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        next_button.setText("提交");
        title_text.setText("评价");
        setListener(star1);
        setListener(star2);
        setListener(star3);
        setListener(star4);
        setListener(star5);
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = content_evaluate.getText().toString();
                if(isNull(content)){
                    showTextDialog("请输入评价的内容");
                    return;
                }

                getNetWorker().replyAdd(user.getToken(), "2", infor.getId(), content, star, "0");
            }
        });
    }

    private void setListener(View view){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.star1:
                        setImageView(1);
                        break;
                    case R.id.star2:
                        setImageView(2);
                        break;
                    case R.id.star3:
                        setImageView(3);
                        break;
                    case R.id.star4:
                        setImageView(4);
                        break;
                    case R.id.star5:
                        setImageView(5);
                        break;
                }
            }
        });
    }

    private void setImageView(int position){
        star = String.valueOf(position);
        for(int i = 0; i < views.size(); i++){
            if(i < position){
                views.get(i).setImageResource(R.mipmap.evaluate_star_liang_img);
            }else
                views.get(i).setImageResource(R.mipmap.evaluate_star_an_img);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_OK){
            return;
        }
        switch (requestCode){
            case 1: //相册
                album(data);
                break;
            case 2:
                camera();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
