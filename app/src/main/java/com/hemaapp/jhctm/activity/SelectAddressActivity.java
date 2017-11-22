package com.hemaapp.jhctm.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.view.RefreshLoadmoreLayout;
import com.hemaapp.jhctm.JhActivity;
import com.hemaapp.jhctm.JhHttpInformation;
import com.hemaapp.jhctm.JhctmApplication;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.adapter.SelectAddressAdapter;
import com.hemaapp.jhctm.model.Address;

import java.util.ArrayList;

import xtom.frame.view.XtomListView;

/**
 * Created by lenovo on 2016/12/28.
 * 选择地址
 */
public class SelectAddressActivity extends JhActivity {
    private ImageButton back_button;
    private TextView title_text;
    private ImageButton next_button;
    private ProgressBar progressbar;
    private RefreshLoadmoreLayout refreshLoadmoreLayout;
    private XtomListView listview;
 //   private LinearLayout add_layout;//添加地址的layout
 //   private TextView add_address_text;//添加地址
    private ArrayList<Address> addresses = new ArrayList<>();
    private SelectAddressAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_select_address);
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onResume() {
        inIt();
        super.onResume();
    }

    //初始化
    private void inIt()
    {
        String token = JhctmApplication.getInstance().getUser().getToken();
        getNetWorker().addressList(token);
    }
    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ADDRESS_LIST:
                showProgressDialog("获取收货地址");
                break;
            case ADDRESS_REMOVE:
                showProgressDialog("删除收货地址中");
                break;
            case ADDRESS_SAVE:
                showProgressDialog("设置默认地址中");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ADDRESS_LIST:
                cancelProgressDialog();
                progressbar.setVisibility(View.GONE);
                break;
            case ADDRESS_REMOVE:
            case ADDRESS_SAVE:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ADDRESS_LIST:
                HemaArrayResult<Address> result = (HemaArrayResult<Address>) hemaBaseResult;
                addresses.clear();
                addresses = result.getObjects();
                freshData();
                break;
            case ADDRESS_REMOVE:
            case ADDRESS_SAVE:
                inIt();
                break;
        }
    }
    private void freshData() {
        if (adapter == null) {
            adapter = new SelectAddressAdapter(SelectAddressActivity.this, addresses);
            listview.setAdapter(adapter);
        } else {
            adapter.setAddresses(addresses);
            adapter.notifyDataSetChanged();

        }
    }
    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ADDRESS_LIST:
            case ADDRESS_REMOVE:
            case ADDRESS_SAVE:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ADDRESS_LIST:
                showTextDialog("获取地址失败，请稍后重试");
                break;
            case ADDRESS_REMOVE:
                showTextDialog("删除地址失败，请稍后重试");
                break;
            case ADDRESS_SAVE:
                showTextDialog("设置默认地址失败，请稍后重试");
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
        title_text.setText("收货地址");
        next_button.setVisibility(View.INVISIBLE);

    }
    public void deleteAddress(String id)
    {
        String token = JhctmApplication.getInstance().getUser().getToken();
        getNetWorker().addressRemove(token,id);
    }
    public void changeAddress(Address address)
    {
        String token = JhctmApplication.getInstance().getUser().getToken();
        String id = address.getId();
        String name = address.getName();
        String tel = address.getTel();
        String province_id = address.getProvince_id();
        String city_id = address.getCity_id();
        String district_id = address.getDistrict_id();
        String address_text = address.getAddress();
        String zipcode = address.getZipcode();
        getNetWorker().addressSave(token,id,name,tel,province_id,city_id,district_id,address_text,zipcode,"1");
    }
}
