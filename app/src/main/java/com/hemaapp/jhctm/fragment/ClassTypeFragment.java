package com.hemaapp.jhctm.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.jhctm.JhFragment;
import com.hemaapp.jhctm.JhHttpInformation;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.adapter.ClassTypeGridAdapter;
import com.hemaapp.jhctm.adapter.ClassTypeListAdapter;
import com.hemaapp.jhctm.model.DistrictInfor;

import java.util.ArrayList;

/**
 * Created by lenovo on 2016/12/28.
 * 分类
 * ClassTypeGridAdapter
 */
public class ClassTypeFragment extends JhFragment {
    private ListView left_list;
    private GridView right_list;
    private ArrayList<DistrictInfor> districtInfors1 = new ArrayList<>();
    private ArrayList<DistrictInfor> districtInfors2 = new ArrayList<>();
    private ClassTypeGridAdapter gridAdapter;
    private ClassTypeListAdapter listAdapter;
    private ImageButton back_button;
    private TextView title_text;
    private Button next_button;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_classtype);
        super.onCreate(savedInstanceState);
        inIt();
    }

    /**
     * 初始化
     */
    private void inIt() {
        getNetWorker().blogtypeList("0");
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
                        districtInfors1.get(0).setCheck(true);
                        getNetWorker().blogtypeList(districtInfors1.get(0).getId());
                    }
                } else {
                    districtInfors2.clear();
                    districtInfors2 = result.getObjects();
                    freshData();
                }
                break;
        }
    }
    private void freshData() {
        //商品列表
        if (listAdapter == null) {
            listAdapter = new ClassTypeListAdapter(ClassTypeFragment.this, districtInfors1);
            listAdapter.setEmptyString("暂无数据");
            left_list.setAdapter(listAdapter);
        } else {
            listAdapter.setEmptyString("暂无数据");
            listAdapter.setInfors(districtInfors1);
            listAdapter.notifyDataSetChanged();
        }
        if (gridAdapter == null) {
            gridAdapter = new ClassTypeGridAdapter(getContext(),districtInfors2);
            gridAdapter.setEmptyString("暂无数据");
            right_list.setAdapter(gridAdapter);
        } else {
            gridAdapter.setEmptyString("暂无数据");
            gridAdapter.setInfors(districtInfors2);
            gridAdapter.notifyDataSetChanged();
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
        left_list = (ListView) findViewById(R.id.left_list);
        right_list = (GridView) findViewById(R.id.right_list);
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (Button) findViewById(R.id.next_button);
    }

    @Override
    protected void setListener() {
        next_button.setVisibility(View.INVISIBLE);
        title_text.setText("分类");
        back_button.setVisibility(View.INVISIBLE);
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
