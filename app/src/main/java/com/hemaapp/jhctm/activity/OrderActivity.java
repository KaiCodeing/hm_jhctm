package com.hemaapp.jhctm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.jhctm.JhActivity;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.adapter.OrderViewPagerAdapter;

import java.util.ArrayList;

/**
 * Created by lenovo on 2017/1/5.
 * 订单activity
 * keytype = 1: 易物兑订单, 2:限时兑换订单,3:分享订单
 * name:对应的订单分类名称
 */
public class OrderActivity extends JhActivity {

    private TextView title_text;//
    private ImageButton back_button;
    private Button next_button;

    private TabLayout tabLayout;
    private ViewPager pager;
    private OrderViewPagerAdapter adapter;
    private String keytype, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_order);
        super.onCreate(savedInstanceState);
        init();
    }

    private void init(){
        pager.setOffscreenPageLimit(100);
        adapter = new OrderViewPagerAdapter(this, getParams(), keytype);
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);//将TabLayout和ViewPager关联起来。
        tabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，可以滚动);
        pager.setCurrentItem(0);
    }

    @Override
    protected void onResume() {
        if (adapter!=null)
        adapter.freshAll();
        super.onResume();
    }

    private ArrayList<OrderViewPagerAdapter.Params> getParams() {
        ArrayList<OrderViewPagerAdapter.Params> list = new ArrayList<>();
        list.add(0,new OrderViewPagerAdapter.Params("1")); //全部
        list.add(1,new OrderViewPagerAdapter.Params("2")); //待付款
        list.add(2,new OrderViewPagerAdapter.Params("3")); //待收货
        list.add(3,new OrderViewPagerAdapter.Params("4")); //待评价
        list.add(4,new OrderViewPagerAdapter.Params("5")); //已完成
        return list;
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {

    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {

    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {

    }

    @Override
    protected void findView() {
        title_text = (TextView) findViewById(R.id.title_text);
        back_button = (ImageButton) findViewById(R.id.back_button);
        next_button = (Button) findViewById(R.id.next_button);

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        pager = (ViewPager) findViewById(R.id.viewpager);
    }

    @Override
    protected void getExras() {
        keytype = mIntent.getStringExtra("keytype");
        name = mIntent.getStringExtra("name");
    }

    @Override
    protected void setListener() {
        title_text.setText(name);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        next_button.setText("售后");
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, ShouHouOrderListActivity.class);
                it.putExtra("keytype", keytype);
                startActivity(it);
            }
        });
    }
}
