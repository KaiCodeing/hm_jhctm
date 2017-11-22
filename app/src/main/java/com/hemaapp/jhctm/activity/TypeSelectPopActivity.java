package com.hemaapp.jhctm.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.jhctm.JhActivity;
import com.hemaapp.jhctm.JhHttpInformation;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.adapter.TypeGrid2Adapter;
import com.hemaapp.jhctm.adapter.TypeGridAdapter;
import com.hemaapp.jhctm.model.DistrictInfor;
import com.hemaapp.jhctm.view.JhctmGridView;

import java.util.ArrayList;

/**
 * Created by lenovo on 2017/2/7.
 * 分类商品的选择
 */
public class TypeSelectPopActivity extends JhActivity {
    TextView show_or;//品牌的全部
    TextView show_xia_or;//产品类型的全部
    EditText min_price;//最低价
    EditText max_price;//最高价
    JhctmGridView name_grid;//品牌的grid
    JhctmGridView type_grid;//产品类型的grid
    TextView chongzhi;//重置
    TextView over;//完成
    private TypeGridAdapter adapter;
    private TypeGrid2Adapter adapter2;
    private ArrayList<DistrictInfor> districtInfors1 = new ArrayList<>();
    private ArrayList<DistrictInfor> districtInfors2 = new ArrayList<>();
    private String allshow1="0";
    private String allshow2="0";
    private String type1Id = "";
    private String type2Id = "";
    private String maxPrice = "";
    private String minPrice = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.pop_type_check_view);
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case BLOGTYPE_LIST:
                break;

        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case BLOGTYPE_LIST:
                break;

        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case BLOGTYPE_LIST:
                HemaArrayResult<DistrictInfor> result = (HemaArrayResult<DistrictInfor>) hemaBaseResult;
                String parentid = hemaNetTask.getParams().get("parentid");
                if ("0".equals(parentid)) {
                    districtInfors1.clear();
                    districtInfors1 = result.getObjects();
                    if (districtInfors1 == null || districtInfors1.size() == 0) {
                    } else {
                        if (isNull(type1Id)) {
                            districtInfors1.get(0).setCheck(true);
                            getNetWorker().blogtypeList(districtInfors1.get(0).getId());
                        } else {
                            for (int i = 0; i < districtInfors1.size(); i++) {
                                if (type1Id.equals(districtInfors1.get(i).getId())) {
                                    districtInfors1.get(i).setCheck(true);
                                    getNetWorker().blogtypeList(type1Id);
                                }
                            }
                        }
                    }
                } else {
                    districtInfors2.clear();
                    districtInfors2 = result.getObjects();
                    if (districtInfors2 == null || districtInfors2.size() == 0) {
                    } else {
                        if (isNull(type2Id)) {
                        } else {
                            for (DistrictInfor infor : districtInfors2)
                                if (infor.getId().equals(type2Id))
                                    infor.setCheck(true);
                        }
                    }
                    freshData();
                }
                break;

        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case BLOGTYPE_LIST:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case BLOGTYPE_LIST:
                showTextDialog("获取分类失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        show_or = (TextView) findViewById(R.id.show_or);
        show_xia_or = (TextView) findViewById(R.id.show_xia_or);
        min_price = (EditText) findViewById(R.id.min_price);
        max_price = (EditText) findViewById(R.id.max_price);
        name_grid = (JhctmGridView) findViewById(R.id.name_grid);
        type_grid = (JhctmGridView) findViewById(R.id.type_grid);
        chongzhi = (TextView) findViewById(R.id.chongzhi);
        over = (TextView) findViewById(R.id.over);
    }

    @Override
    protected void getExras() {
        type1Id = mIntent.getStringExtra("type1Id");
        type2Id = mIntent.getStringExtra("type2Id");
        maxPrice = mIntent.getStringExtra("maxPrice");
        minPrice = mIntent.getStringExtra("minPrice");
    }

    @Override
    protected void setListener() {
        if (!isNull(maxPrice))
            max_price.setText(maxPrice);
        if (!isNull(minPrice))
            min_price.setText(minPrice);
        //初始化
        getNetWorker().blogtypeList("0");
        //重置
        chongzhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allshow1="0";
                allshow2="0";
                type1Id="";
                type2Id="";
                maxPrice="";
                minPrice ="";
                min_price.setText("");
                min_price.setHint("最低价");
                max_price.setText("");
                max_price.setHint("最高价");
                getNetWorker().blogtypeList("0");
            }
        });
        //确定
        over.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maxPrice = max_price.getText().toString().trim();
                minPrice = min_price.getText().toString().trim();
                for (DistrictInfor infor:districtInfors1)
                    if (infor.isCheck())
                        type1Id = infor.getId();
                for (DistrictInfor infor:districtInfors2)
                    if (infor.isCheck())
                        type2Id = infor.getId();
                mIntent.putExtra("maxPrice", maxPrice);
                mIntent.putExtra("minPrice", minPrice);
                mIntent.putExtra("type1Id", type1Id);
                mIntent.putExtra("type2Id", type2Id);
                setResult(RESULT_OK, mIntent);
                mInputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),
                        0);
                finish();
            }
        });
        //展示一级
        show_or.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("0".equals(allshow1))
                    allshow1 ="1";
                else
                    allshow1="0";
                freshData();
            }
        });
        //展示二级
        show_xia_or.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("0".equals(allshow2))
                    allshow2 ="1";
                else
                    allshow2="0";
                freshData();
            }
        });
    }

    private void freshData() {
        //商品列表
        if (adapter == null) {
            adapter = new TypeGridAdapter(TypeSelectPopActivity.this, districtInfors1, allshow1);
            adapter.setEmptyString("暂无数据");
            name_grid.setAdapter(adapter);
        } else {
            adapter.setEmptyString("暂无数据");
            adapter.setType(allshow1);
            adapter.setDistrictInfors(districtInfors1);
            adapter.notifyDataSetChanged();
        }
        if (adapter2 == null) {
            adapter2 = new TypeGrid2Adapter(TypeSelectPopActivity.this, districtInfors2, allshow2);
            adapter2.setEmptyString("暂无数据");
            type_grid.setAdapter(adapter2);
        } else {
            adapter2.setType(allshow2);
            adapter2.setDistrictInfors(districtInfors2);
            adapter2.setEmptyString("暂无数据");
            adapter2.notifyDataSetChanged();
        }
    }

    /**
     * 改变数据
     *
     * @param infor
     */
    public void selectData(DistrictInfor infor) {
        for (DistrictInfor disinfor : districtInfors1
                ) {
            if (infor.getId().equals(disinfor.getId())) {
                disinfor.setCheck(true);
            } else
                disinfor.setCheck(false);
        }
        getNetWorker().blogtypeList(String.valueOf(infor.getId()));
    }


}
