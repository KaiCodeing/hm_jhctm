package com.hemaapp.jhctm.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.jhctm.JhActivity;
import com.hemaapp.jhctm.R;

/**
 * Created by lenovo on 2016/12/27.
 * 发票选择
 */
public class InvoiceActivity extends JhActivity {
    private ImageButton back_button;
    private TextView title_text;
    private Button next_button;
    private RadioGroup select_no_yes;//选择是否发票
    private RadioGroup people_or_company;//个人或公司
    private EditText input_name;//输入公司或姓名
    private EditText input_content;//输入发票内容
    private LinearLayout layout1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_invoice);
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
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (Button) findViewById(R.id.next_button);
        select_no_yes = (RadioGroup) findViewById(R.id.select_no_yes);
        people_or_company = (RadioGroup) findViewById(R.id.people_or_company);
        input_name = (EditText) findViewById(R.id.input_name);
        input_content = (EditText) findViewById(R.id.input_content);
        layout1 = (LinearLayout) findViewById(R.id.layout1);
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
        title_text.setText("发票");
        next_button.setText("确定");
        next_button.setTextColor(getResources().getColor(R.color.color_text));
        //选择是否需要发票
        select_no_yes.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    //普通发票
                    case R.id.putong:
                        layout1.setVisibility(View.VISIBLE);
                        break;
                    //不需要
                    case R.id.no_putong:
                        layout1.setVisibility(View.GONE);
                        break;
                }
            }
        });
        //选择发票方式
        people_or_company.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    //个人
                    case R.id.people:
                        input_name.setHint("请输入姓名");

                        break;
                    //公司
                    case R.id.company:
                        input_name.setHint("请输入公司名称");
                        break;
                }
            }
        });
        //确定
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = input_name.getText().toString();
                String content = input_content.getText().toString();
                String type = "0";//开发票
                String p_or_c = "1";
                if (select_no_yes.getCheckedRadioButtonId() == R.id.putong) {
                    if (isNull(username)) {
                        if (people_or_company.getCheckedRadioButtonId() == R.id.people) {
                            showTextDialog("请填写发票人姓名");
                            p_or_c = "1";
                        } else {
                            showTextDialog("请填写公司名称");
                            p_or_c = "2";
                        }
                        return;
                    }
                    if (isNull(content)) {
                        showTextDialog("请填写发票内容");
                        return;
                    }
                    if (people_or_company.getCheckedRadioButtonId() == R.id.people)
                        p_or_c = "1";
                    else
                        p_or_c = "2";

                    type = "0";
                    mIntent.putExtra("type", type);
                    mIntent.putExtra("p_or_c", p_or_c);
                    mIntent.putExtra("username", username);
                    mIntent.putExtra("content", content);
                    setResult(RESULT_OK, mIntent);
                    finish();
                } else {
                    type = "1";
                    mIntent.putExtra("type", type);
                    setResult(RESULT_OK, mIntent);
                    finish();
                }
            }

        });
    }
}
