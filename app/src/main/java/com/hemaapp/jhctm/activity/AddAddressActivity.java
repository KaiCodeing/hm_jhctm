package com.hemaapp.jhctm.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.jhctm.JhActivity;
import com.hemaapp.jhctm.JhHttpInformation;
import com.hemaapp.jhctm.JhctmApplication;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.model.Address;
import com.hemaapp.jhctm.model.CitySan;
import com.hemaapp.jhctm.view.AreaDialog;

/**
 * Created by lenovo on 2016/12/27.
 * 添加收货地址
 */
public class AddAddressActivity extends JhActivity {
    private ImageButton back_button;
    private TextView title_text;
    private Button next_button;
    private EditText input_name;
    private EditText input_tel;
    private EditText input_post;
    private TextView address;
    private EditText input_address_content;
    private Address address_data;
    private AreaDialog areaDialog;
    private String provinceId;//省份id
    private String cityId;//市id；
    private String districtId;//区id
    private LinearLayout select_city_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_address);
        super.onCreate(savedInstanceState);
        inIt();
        if (JhctmApplication.getInstance().getCityInfo() == null)
            getNetWorker().districtAllGet();
    }

    private void inIt() {
        if (address_data != null) {
            //名字
            if (!isNull(address_data.getName()))
                input_name.setText(address_data.getName());
            //地址
            if (!isNull(address_data.getAddress()))
                input_address_content.setText(address_data.getAddress());
            //电话
            if (!isNull(address_data.getTel()))
                input_tel.setText(address_data.getTel());
            //所在城市
            if (!isNull(address_data.getNamepath()))
                address.setText(address_data.getNamepath());
            //邮政编码
            if (!isNull(address_data.getZipcode()))
                input_post.setText(address_data.getZipcode());
            provinceId = address_data.getProvince_id();
            cityId = address_data.getCity_id();
            districtId = address_data.getDistrict_id();
        }
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case DISTRICT_ALL_GET:
                showProgressDialog("获取地区列表");
                break;
            case ADDRESS_SAVE:
                showProgressDialog("保存收货地址..");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case DISTRICT_ALL_GET:
            case ADDRESS_SAVE:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case DISTRICT_ALL_GET:
                HemaArrayResult<CitySan> result1 = (HemaArrayResult<CitySan>) hemaBaseResult;
            //    CitySan citySan = result1.getObjects().get(0);
//                ArrayList<CitySan> citySanArrayList = ;

                CitySan citySan = result1.getObjects().get(0);
                getApplicationContext().setCityInfo(citySan);
                break;
            case ADDRESS_SAVE:
                if (address_data==null)
                showTextDialog("保存收货地址成功");
                else
                showTextDialog("编辑收货地址成功");
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
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case DISTRICT_ALL_GET:
                showTextDialog(hemaBaseResult.getMsg());

                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case DISTRICT_ALL_GET:
                showTextDialog("获取地区列表失败，请稍后重试");

                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (Button) findViewById(R.id.next_button);
        input_name = (EditText) findViewById(R.id.input_name);
        input_tel = (EditText) findViewById(R.id.input_tel);
        input_post = (EditText) findViewById(R.id.input_post);
        address = (TextView) findViewById(R.id.address);
        input_address_content = (EditText) findViewById(R.id.input_address_content);
        select_city_layout = (LinearLayout) findViewById(R.id.select_city_layout);
    }

    @Override
    protected void getExras() {
        address_data = (Address) mIntent.getSerializableExtra("address");
    }

    @Override
    protected void setListener() {
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (address_data == null)
            title_text.setText("添加收货地址");
        else
            title_text.setText("编辑收货地址");
        next_button.setText("保存");
        next_button.setTextColor(getResources().getColor(R.color.color_text));
        select_city_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCity();
            }
        });
        //保存 添加或编辑
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = input_name.getText().toString().trim();
                String tel = input_tel.getText().toString().trim();
                String post = input_post.getText().toString().trim();
                String address_text = input_address_content.getText().toString().trim();
                String city = address.getText().toString().trim();
                if (isNull(username))
                {
                    showTextDialog("请填写联系人姓名");
                    return;
                }
                if (isNull(tel))
                {
                    showTextDialog("请填写联系人电话");
                    return;
                }
                if (isNull(post))
                {
                    showTextDialog("请填写联系人邮编");
                    return;
                }
                if (isNull(address_text))
                {
                    showTextDialog("请填写联系人详细地址");
                    return;
                }
                if (isNull(city))
                {
                    showTextDialog("请选择联系人所在地区");
                    return;
                }
                String token = JhctmApplication.getInstance().getUser().getToken();
                String id="0";
                String defaultflag="0";
                if (address_data!=null) {
                    id = address_data.getId();
                    defaultflag = address_data.getDefaultflag();
                }
                getNetWorker().addressSave(token,id,username,tel,provinceId,cityId,districtId,address_text,post,defaultflag);
            }
        });
    }

    private void showCity() {
        if (areaDialog == null) {
            areaDialog = new AreaDialog(mContext);

            areaDialog.setButtonListener(new onbutton());
            return;
        }
        areaDialog.show();
    }

    private class onbutton implements AreaDialog.OnButtonListener {

        @Override
        public void onLeftButtonClick(AreaDialog dialog) {
            // TODO Auto-generated method stub

            areaDialog.cancel();
        }


        @Override
        public void onRightButtonClick(AreaDialog dialog) {
            // TODO Auto-generated method stub
            address.setText(areaDialog.getText());
            //  cityName = city_text.getText().toString();
//            homecity = home_text.getText().toString();
//            home_text.setTag(areaDialog.getId());
            String[] cityid = areaDialog.getId().split(",");
            provinceId = cityid[0];
            cityId = cityid[1];
            districtId = cityid[2];
            areaDialog.cancel();
        }

    }
}
