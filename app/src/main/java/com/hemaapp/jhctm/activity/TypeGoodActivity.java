package com.hemaapp.jhctm.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.hm_FrameWork.view.RefreshLoadmoreLayout;
import com.hemaapp.jhctm.JhActivity;
import com.hemaapp.jhctm.JhHttpInformation;
import com.hemaapp.jhctm.JhctmApplication;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.adapter.HomeGridAdapter;
import com.hemaapp.jhctm.adapter.TypeGrid2Adapter;
import com.hemaapp.jhctm.adapter.TypeGridAdapter;
import com.hemaapp.jhctm.model.Blog1List;
import com.hemaapp.jhctm.model.DistrictInfor;
import com.hemaapp.jhctm.view.JhctmGridView;

import java.util.ArrayList;

import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * Created by lenovo on 2016/12/28.
 * \分类中的商品筛选
 */
public class TypeGoodActivity extends JhActivity {
    private ImageButton back_button;
    private TextView title_text;
    private ImageButton next_button;
    private ProgressBar progressbar;
    private RefreshLoadmoreLayout refreshLoadmoreLayout;
    private JhctmGridView gridview;
    private TextView text1;//综合
    private TextView text2;//价格
    private TextView text3;//销量
    private TextView text4;//筛选
    private DistrictInfor infor;
    private Integer page = 0;
    private String maxPrice = "";
    private String minPrice = "";
    private String type = "";//0综合1：价格升2：价格降3：销量升4：销量降
    private ArrayList<DistrictInfor> districtInfors1 = new ArrayList<>();
    private ArrayList<DistrictInfor> districtInfors2 = new ArrayList<>();
    private String allshow1 = "0";
    private String allshow2 = "0";
    private ViewHolder holder;
    private PopupWindow popupWindow;
    private TypeGridAdapter adapter;
    private TypeGrid2Adapter adapter2;
    private String blogId = "";
    private HomeGridAdapter gridAdapter;
    private ArrayList<Blog1List> blog1Lists = new ArrayList<>();
    private String blogId2 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_type_good);
        super.onCreate(savedInstanceState);
        inIt();
    }

    /**
     * 初始化
     */
    private void inIt() {
        String keytype = "";
        String orderby = "";
        if ("0".equals(type))
            keytype = "1";
        else if ("1".equals(type)) {
            keytype = "3";
            orderby = "1";
        } else if ("2".equals(type)) {
            keytype = "3";
            orderby = "2";
        } else if ("3".equals(type)) {
            keytype = "2";
            orderby = "1";
        } else if ("4".equals(type)) {
            keytype = "2";
            orderby = "2";
        }
        if (isNull(blogId2))
            getNetWorker().blog1List3("", infor.getId(), "", keytype, orderby, minPrice, maxPrice, "", String.valueOf(page));
        else
            getNetWorker().blog1List3("", blogId2, "", keytype, orderby, minPrice, maxPrice, "", String.valueOf(page));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK != resultCode) {
            return;
        }
        switch (requestCode) {
            case 1:
                minPrice = data.getStringExtra("minPrice");
                maxPrice = data.getStringExtra("maxPrice");
                blogId = data.getStringExtra("type1Id");
                blogId2 = data.getStringExtra("type2Id");
                type = "0";
                text2.setTextColor(getResources().getColor(R.color.shaixuan));
                text2.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.defult_jiao_img), null);
                text3.setTextColor(getResources().getColor(R.color.shaixuan));
                text3.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.defult_jiao_img), null);
                text1.setTextColor(getResources().getColor(R.color.title_backgroid));
                text1.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.only_xia_img), null);
                text4.setTextColor(getResources().getColor(R.color.shaixuan));
                text4.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.check_img_class), null);
                page = 0;
                inIt();
                break;
        }
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case BLOGTYPE_LIST:
                break;
            case BLOG_1_LIST:
                showProgressDialog("获取商品列表");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case BLOGTYPE_LIST:
                break;
            case BLOG_1_LIST:
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
                }
                break;
            case BLOG_1_LIST:
                HemaPageArrayResult<Blog1List> result3 = (HemaPageArrayResult<Blog1List>) hemaBaseResult;
                ArrayList<Blog1List> blog1Lists = result3.getObjects();
                String page2 = hemaNetTask.getParams().get("page");
                if ("0".equals(page2)) {// 刷新
                    refreshLoadmoreLayout.refreshSuccess();
                    this.blog1Lists.clear();
                    this.blog1Lists.addAll(blog1Lists);
                    JhctmApplication application = JhctmApplication.getInstance();
                    int sysPagesize = application.getSysInitInfo()
                            .getSys_pagesize();
                    if (blog1Lists.size() < sysPagesize) {
                        refreshLoadmoreLayout.setLoadmoreable(false);
                        // leftRE = false;
                    } else {
                        refreshLoadmoreLayout.setLoadmoreable(true);
                        // leftRE = true;
                    }
                } else {// 更多
                    refreshLoadmoreLayout.loadmoreSuccess();
                    if (blog1Lists.size() > 0)
                        this.blog1Lists.addAll(blog1Lists);
                    else {
                        refreshLoadmoreLayout.setLoadmoreable(false);
                        // leftRE = false;
                        XtomToastUtil.showShortToast(mContext, "已经到最后啦");
                    }
                }
                freshData1();
                break;
        }
    }

    private void freshData1() {
        //商品列表
        if (gridAdapter == null) {
            gridAdapter = new HomeGridAdapter(mContext, blog1Lists);
            gridAdapter.setEmptyString("暂无数据");
            gridview.setAdapter(gridAdapter);
        } else {
            gridAdapter.setEmptyString("暂无数据");
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
            case BLOG_1_LIST:
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
            case BLOG_1_LIST:
                String page = hemaNetTask.getParams().get("page");
                if ("0".equals(page)) {
                    refreshLoadmoreLayout.refreshFailed();
                } else {
                    refreshLoadmoreLayout.loadmoreFailed();
                }
                showTextDialog("获取商品失败，请稍后重试");
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
        gridview = (JhctmGridView) findViewById(R.id.gridview);
        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);
        text3 = (TextView) findViewById(R.id.text3);
        text4 = (TextView) findViewById(R.id.text4);
    }

    @Override
    protected void getExras() {
        infor = (DistrictInfor) mIntent.getSerializableExtra("infor");
    }

    @Override
    protected void setListener() {
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_text.setText(infor.getName());
        next_button.setVisibility(View.INVISIBLE);
        //选择综合
        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "0";
                text2.setTextColor(getResources().getColor(R.color.shaixuan));
                text2.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.defult_jiao_img), null);
                text3.setTextColor(getResources().getColor(R.color.shaixuan));
                text3.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.defult_jiao_img), null);
                text1.setTextColor(getResources().getColor(R.color.title_backgroid));
                text1.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.only_xia_img), null);
                text4.setTextColor(getResources().getColor(R.color.shaixuan));
                text4.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.check_img_class), null);
                page = 0;
                inIt();
            }
        });
        //价格
        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //降序
                if ("1".equals(type)) {
                    type = "2";
                    text1.setTextColor(getResources().getColor(R.color.shaixuan));
                    text1.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.only_jiao_img), null);
                    text3.setTextColor(getResources().getColor(R.color.shaixuan));
                    text3.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.defult_jiao_img), null);
                    text2.setTextColor(getResources().getColor(R.color.title_backgroid));
                    text2.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.min_check_img), null);
                    text4.setTextColor(getResources().getColor(R.color.shaixuan));
                    text4.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.check_img_class), null);
                } else {
                    type = "1";
                    text1.setTextColor(getResources().getColor(R.color.shaixuan));
                    text1.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.only_jiao_img), null);
                    text3.setTextColor(getResources().getColor(R.color.shaixuan));
                    text3.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.defult_jiao_img), null);
                    text2.setTextColor(getResources().getColor(R.color.title_backgroid));
                    text2.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.max_check_img), null);
                    text4.setTextColor(getResources().getColor(R.color.shaixuan));
                    text4.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.check_img_class), null);
                }
                page = 0;
                inIt();
            }
        });
        //销量
        text3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("3".equals(type)) {
                    type = "4";//降序
                    text1.setTextColor(getResources().getColor(R.color.shaixuan));
                    text1.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.only_jiao_img), null);
                    text2.setTextColor(getResources().getColor(R.color.shaixuan));
                    text2.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.defult_jiao_img), null);
                    text3.setTextColor(getResources().getColor(R.color.title_backgroid));
                    text3.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.min_check_img), null);
                    text4.setTextColor(getResources().getColor(R.color.shaixuan));
                    text4.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.check_img_class), null);
                } else {
                    type = "3";
                    text1.setTextColor(getResources().getColor(R.color.shaixuan));
                    text1.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.only_jiao_img), null);
                    text2.setTextColor(getResources().getColor(R.color.shaixuan));
                    text2.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.defult_jiao_img), null);
                    text3.setTextColor(getResources().getColor(R.color.title_backgroid));
                    text3.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.max_check_img), null);
                    text4.setTextColor(getResources().getColor(R.color.shaixuan));
                    text4.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.check_img_class), null);
                }
                page = 0;
                inIt();
            }

        });
        //筛选
        text4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TypeGoodActivity.this, TypeSelectPopActivity.class);
                intent.putExtra("type1Id", blogId);
                intent.putExtra("type2Id", blogId2);
                intent.putExtra("maxPrice", maxPrice);
                intent.putExtra("minPrice", minPrice);
                startActivityForResult(intent, 1);
            }
        });
        refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page = 0;
                inIt();
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page++;
                inIt();
            }
        });
    }

    /**
     * 筛选的pop pop_type_check
     */
    private class ViewHolder {
        TextView show_or;//品牌的全部
        TextView show_xia_or;//产品类型的全部
        EditText min_price;//最低价
        EditText max_price;//最高价
        GridView name_grid;//品牌的grid
        GridView type_grid;//产品类型的grid
        TextView chongzhi;//重置
        TextView over;//完成
    }

    private void showSelect() {
        View view = LayoutInflater.from(TypeGoodActivity.this).inflate(
                R.layout.pop_type_check_view, null);
        if (holder == null) {
            holder = new ViewHolder();
            holder.show_or = (TextView) view.findViewById(R.id.show_or);
            holder.show_xia_or = (TextView) view.findViewById(R.id.show_xia_or);
            holder.min_price = (EditText) view.findViewById(R.id.min_price);
            holder.max_price = (EditText) view.findViewById(R.id.max_price);
            holder.name_grid = (GridView) view.findViewById(R.id.name_grid);
            holder.type_grid = (GridView) view.findViewById(R.id.type_grid);
            holder.chongzhi = (TextView) view.findViewById(R.id.chongzhi);
            holder.over = (TextView) view.findViewById(R.id.over);
        }
        if ("0".equals(allshow1)) {
            //holder.show_or.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,);
        } else {
        }
        //是否显示
        holder.show_or.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("0".equals(allshow1))
                    allshow1 = "1";
                else
                    allshow1 = "0";
            }
        });
        holder.show_xia_or.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("0".equals(allshow2))
                    allshow2 = "1";
                else
                    allshow2 = "0";
            }
        });
        //重置
        holder.chongzhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (DistrictInfor infor : districtInfors1)
                    infor.setCheck(false);
                for (DistrictInfor infor : districtInfors2)
                    infor.setCheck(false);
                allshow1 = "0";
                allshow2 = "0";
                holder.min_price.setText("");
                holder.min_price.setHint("最低价");
                holder.max_price.setText("");
                holder.max_price.setHint("最高价");
            }
        });
        //确定
        holder.over.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = 0;
                for (int i = 0; i < districtInfors2.size(); i++) {
                    if (districtInfors2.get(i).isCheck()) {
                        blogId = districtInfors2.get(i).getId();
                        num++;
                    }
                }
                if (num == 0)
                    blogId = "";
                maxPrice = holder.max_price.getText().toString().trim();
                minPrice = holder.min_price.getText().toString().trim();
                type = "0";
                text2.setTextColor(getResources().getColor(R.color.shaixuan));
                text2.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.defult_jiao_img), null);
                text3.setTextColor(getResources().getColor(R.color.shaixuan));
                text3.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.defult_jiao_img), null);
                text1.setTextColor(getResources().getColor(R.color.title_backgroid));
                text1.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.only_xia_img), null);
                text4.setTextColor(getResources().getColor(R.color.shaixuan));
                text4.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.check_img_class), null);
                page = 0;
                inIt();
                popupWindow.dismiss();
            }
        });
        if (popupWindow == null) {
            popupWindow = new PopupWindow(view,
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        }
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setWidth(RadioGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(RadioGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new
                BitmapDrawable()
        );
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //   popupWindow.showAsDropDown(findViewById(R.id.layout_view));
        popupWindow.setAnimationStyle(R.style.PopupAnimation);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
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
