package com.hemaapp.jhctm.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.view.RefreshLoadmoreLayout;
import com.hemaapp.jhctm.JhActivity;
import com.hemaapp.jhctm.JhHttpInformation;
import com.hemaapp.jhctm.JhctmApplication;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.adapter.CarBuyAdapter;
import com.hemaapp.jhctm.model.Cart;
import com.hemaapp.jhctm.model.CartItems;

import java.util.ArrayList;

import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * Created by lenovo on 2017/2/17.
 */
public class CarBuyActivity extends JhActivity {
    private ImageButton back_button;
    private TextView title_text;
    private Button next_button;
    private ProgressBar progressbar;
    private RefreshLoadmoreLayout refreshLoadmoreLayout;
    private XtomListView listview;
    private ImageView all_buy_img;//全选
    private TextView price_jf;//合计
    private TextView buy_text;//去结算
    private ArrayList<CartItems> cartItemses = new ArrayList<>();
    private CarBuyAdapter adapter;
    private String type = "0";//0 未选中 1 选中
    private ViewHolder holder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_buy_car);
        super.onCreate(savedInstanceState);
//        inIt();
    }

    @Override
    public void onResume() {
        super.onResume();
        inIt();
    }

    /**
     * 初始化
     */
    private void inIt() {
        String token = JhctmApplication.getInstance().getUser().getToken();
        getNetWorker().cartList(token);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CART_LIST:
                showProgressDialog("获取购物车商品");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CART_LIST:
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
            case CART_LIST:
                refreshLoadmoreLayout.refreshSuccess();
                HemaArrayResult<Cart> result = (HemaArrayResult<Cart>) hemaBaseResult;
                ArrayList<Cart> carts = result.getObjects();
                if (carts == null || carts.size() == 0) {
                } else {
                    cartItemses = carts.get(0).getChildItems();
                }
                freshData();
                break;
            case CART_SAVEOPERATE:
                String carId = hemaNetTask.getParams().get("id");
                String keytype = hemaNetTask.getParams().get("keytype");
                String num = hemaNetTask.getParams().get("buycount");
                if ("3".equals(keytype)) {
                    for (int i = 0; i < cartItemses.size(); i++) {
                        if (cartItemses.get(i).getId().equals(carId))
                            cartItemses.get(i).setBuycount(num);
                    }
                } else if ("2".equals(keytype)) {
                    cartItemses.clear();
                }
                freshData();
                showP();
                break;
        }
    }

    private void freshData() {
        if (adapter == null) {
            adapter = new CarBuyAdapter(CarBuyActivity.this, cartItemses);
            adapter.setEmptyString("暂无数据");
            listview.setAdapter(adapter);
        } else {
            adapter.setEmptyString("暂无数据");
            adapter.setCartItemses(cartItemses);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CART_LIST:
            case CART_SAVEOPERATE:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CART_LIST:
                refreshLoadmoreLayout.refreshFailed();
                showTextDialog("获取购物车商品失败，请稍后重试");
                break;
            case CART_SAVEOPERATE:
                showTextDialog("购物车操作失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (Button) findViewById(R.id.next_button);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        refreshLoadmoreLayout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        listview = (XtomListView) findViewById(R.id.listview);
        all_buy_img = (ImageView) findViewById(R.id.all_buy_img);
        price_jf = (TextView) findViewById(R.id.price_jf);
        buy_text = (TextView) findViewById(R.id.buy_text);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        back_button.setVisibility(View.INVISIBLE);
        title_text.setText("购物车");
        next_button.setText("清空");
        //清空
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        //全选
        all_buy_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartItemses == null || cartItemses.size() == 0)
                    return;
                if (type.equals("0")) {
                    for (int i = 0; i < cartItemses.size(); i++) {
                        cartItemses.get(i).setCheck(true);
                    }
                    all_buy_img.setImageResource(R.mipmap.fapiao_check_on);
                    type = "1";
                } else {
                    for (int i = 0; i < cartItemses.size(); i++) {
                        cartItemses.get(i).setCheck(false);
                    }
                    all_buy_img.setImageResource(R.mipmap.fapiao_check_off);
                    type = "0";
                }
                freshData();
                showP();
            }
        });
        //清空
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartItemses == null || cartItemses.size() == 0) {
                    showTextDialog("购物车为空，不能清空!");
                    return;
                }
                showView();
            }
        });
        //去结算
        buy_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartItemses == null || cartItemses.size() == 0) {
                    showTextDialog("您还没有添加任何商品至购物车");
                    return;
                }
                int m = 0;
                for (CartItems items : cartItemses) {
                    if (items.isCheck())
                        m++;
                }
                if (m == 0) {
                    showTextDialog("您还没有选择商品哦");
                    return;
                }
                Intent intent = new Intent(CarBuyActivity.this, CarOrderConfirmActivity.class);
                intent.putExtra("cartItemses", cartItemses);
               startActivity(intent);
            }
        });
    }

    /**
     * 购物车操作
     * 1：单个删除
     * 2：清空购物车
     * 3：更改购买计数
     * 4：清空过期购物车
     */
    public void changeNum(String carId, String num, String type) {
        String token = JhctmApplication.getInstance().getUser().getToken();
        getNetWorker().cartSaveoperate(token, carId, type, num);
    }

    //总价  是否全部选中
    private void showP() {
        double price = 0;
        int m = 0;
        for (int i = 0; i < cartItemses.size(); i++) {
            if (cartItemses.get(i).isCheck()) {
                price = (Double.valueOf(cartItemses.get(i).getScore()) * Double.valueOf(cartItemses.get(i).getBuycount())) + price;
                m++;
            }
        }
        if (m == cartItemses.size() && m != 0) {
            all_buy_img.setImageResource(R.mipmap.fapiao_check_on);
            type = "1";
        } else {
            all_buy_img.setImageResource(R.mipmap.fapiao_check_off);
            type = "0";
        }
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        price_jf.setText("合计:" + String.valueOf(df.format(price)) + "积分");
        if (0 == price)
            price_jf.setText("合计:0.00积分");
    }

    //更改选中状态
    public void selectCart(int carid) {
        for (int i = 0; i < cartItemses.size(); i++) {
            if (Integer.valueOf(cartItemses.get(i).getId()) == carid) {
                if (cartItemses.get(i).isCheck())
                    cartItemses.get(i).setCheck(false);
                else
                    cartItemses.get(i).setCheck(true);
            }
        }
        freshData();
        showP();
    }

    private class ViewHolder {
        TextView content_text;
        TextView content_no;//取消
        TextView content_yes;//确定

    }

    private void showView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.pop_left_right_view, null);
        holder = new ViewHolder();
        holder.content_no = (TextView) view.findViewById(R.id.content_no);
        holder.content_yes = (TextView) view.findViewById(R.id.content_yes);
        holder.content_text = (TextView) view.findViewById(R.id.content_text);

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

                changeNum("0", "0", "2");
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
}
