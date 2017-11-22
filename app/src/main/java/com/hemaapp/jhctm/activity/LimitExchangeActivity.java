package com.hemaapp.jhctm.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.jhctm.JhActivity;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.adapter.LimitExchangePageAdapter;

import java.util.ArrayList;

/**
 * Created by lenovo on 2016/12/27.
 * 限时兑换
 */
public class LimitExchangeActivity extends JhActivity {
//   private TableLayout tab_FindFragment_title;
    private ViewPager viewpager;
    private ImageButton back_button;
    private TextView title_text;
    private ImageButton next_button;
    private RadioGroup radio_selete;
    private View view1;
    private View view2;
    private View view3;
    private LimitExchangePageAdapter pageAdapter;
    private String type = "0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_limit_exchange);
        super.onCreate(savedInstanceState);
        inIt(Integer.valueOf(type));
    }
    /**
     * @方法名称: inIt
     * @功能描述: TODO初始化
     * @返回值: void
     */
    private void inIt(int type) {
        pageAdapter = new LimitExchangePageAdapter(this, getParams());
        viewpager.setAdapter(pageAdapter);
        viewpager.setCurrentItem(type);
    }
    private ArrayList<LimitExchangePageAdapter.Params> getParams() {
        ArrayList<LimitExchangePageAdapter.Params> ps = new ArrayList<LimitExchangePageAdapter.Params>();
        ps.add(new LimitExchangePageAdapter.Params("2", ""));
        ps.add(new LimitExchangePageAdapter.Params("3", ""));
        ps.add(new LimitExchangePageAdapter.Params("1", ""));
        return ps;
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
     //   tab_FindFragment_title = (TableLayout) findViewById(R.id.tab_FindFragment_title);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (ImageButton) findViewById(R.id.next_button);
        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        view3 = findViewById(R.id.view3);
        radio_selete = (RadioGroup) findViewById(R.id.radio_selete);
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
        title_text.setText("限时秒杀");
        next_button.setVisibility(View.INVISIBLE);
        //选择类型
        radio_selete.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    case R.id.all_sp:
                        view1.setVisibility(View.VISIBLE);
                        view2.setVisibility(View.INVISIBLE);
                        view3.setVisibility(View.INVISIBLE);
                        viewpager.setCurrentItem(2);
                        break;
                    case R.id.wait_buy_sp:
                        view2.setVisibility(View.VISIBLE);
                        view1.setVisibility(View.INVISIBLE);
                        view3.setVisibility(View.INVISIBLE);
                        viewpager.setCurrentItem(0);
                        break;
                    case R.id.wait_go_sp:
                        view3.setVisibility(View.VISIBLE);
                        view1.setVisibility(View.INVISIBLE);
                        view2.setVisibility(View.INVISIBLE);
                        viewpager.setCurrentItem(1);
                        break;
                }
            }
        });
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        radio_selete.check(R.id.wait_buy_sp);
                        break;
                    case 1:
                        radio_selete.check(R.id.wait_go_sp);
                        break;
                    case 2:
                        radio_selete.check(R.id.all_sp);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewpager.setOffscreenPageLimit(3);
    }
}
