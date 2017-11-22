package com.hemaapp.jhctm.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.jhctm.JhActivity;
import com.hemaapp.jhctm.R;

/**
 * Created by lenovo on 2016/12/21.
 * 搜索商铺或商家
 */
public class SearchActivity extends JhActivity {
    private ImageButton back_button;
    private TextView select_class;//选择商品
    private EditText search_log;//输入搜索
    private TextView message_view;//搜索
    private ViewHolder holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_search);
        super.onCreate(savedInstanceState);
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
        back_button = (ImageButton) findViewById(R.id.back_button);
        select_class = (TextView) findViewById(R.id.select_class);
        search_log = (EditText) findViewById(R.id.search_log);
        message_view = (TextView) findViewById(R.id.message_view);

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
        //选择商品或商店
        select_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(SearchActivity.this).inflate(
                        R.layout.pop_select_search, null);
                holder = new ViewHolder();
                holder.search_com = (RadioGroup) view.findViewById(R.id.search_com);
                holder.commodity = (RadioButton) view.findViewById(R.id.commodity);
                holder.merchant = (RadioButton) view.findViewById(R.id.merchant);
                if ("商品".equals(select_class.getText().toString().trim())) {
                    holder.commodity.setChecked(true);
                } else {
                    holder.merchant.setChecked(true);
                }
                final PopupWindow popupWindow = new PopupWindow(view,
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                holder.search_com.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.commodity:
                                select_class.setText("商品");
                                popupWindow.dismiss();
                                break;
                            case R.id.merchant:
                                select_class.setText("商家");
                                popupWindow.dismiss();
                                break;
                        }
                    }
                });
                popupWindow.setOutsideTouchable(true);
                popupWindow.setFocusable(true);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.showAsDropDown(findViewById(R.id.select_class));
            }

        });
        //搜索
        message_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search = search_log.getText().toString().trim();
                if (isNull(search)) {
                    showTextDialog("请填写搜索关键字");
                    return;
                } else {
                    String text = select_class.getText().toString();
                    Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                    if ("商品".equals(text))
                        intent.putExtra("type", "1");
                    else
                        intent.putExtra("type", "0");
                    intent.putExtra("search", search);
                    startActivity(intent);
                }
            }
        });
    }

    private class ViewHolder {
        RadioGroup search_com;
        RadioButton commodity;
        RadioButton merchant;
    }
}
