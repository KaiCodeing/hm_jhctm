package com.hemaapp.jhctm.activity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.jhctm.JhActivity;
import com.hemaapp.jhctm.JhHttpInformation;
import com.hemaapp.jhctm.R;
import com.hemaapp.jhctm.model.Notice;
import com.hemaapp.jhctm.model.User;
import com.hemaapp.jhctm.swipemenulistview.SwipeMenu;
import com.hemaapp.jhctm.swipemenulistview.SwipeMenuCreator;
import com.hemaapp.jhctm.swipemenulistview.SwipeMenuItem;
import com.hemaapp.jhctm.swipemenulistview.SwipeMenuListView;
import com.hemaapp.jhctm.util.BaseUtil;

import java.util.ArrayList;
import java.util.List;

import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomRefreshLoadmoreLayout;
import xtom.frame.view.XtomRefreshLoadmoreLayout.OnStartListener;
/**
 * 我的消息
 */
public class MyXiaoXi extends JhActivity implements OnClickListener {
    private ImageButton left;
    private TextView title;
    private Button next_button;
    private ListView selectList;
    private List<ApplicationInfo> mAppList;
    private AppAdapter mAdapter;
    private SwipeMenuListView mListView;
    private Intent intent;
    private XtomRefreshLoadmoreLayout layout;
    private TextView alert;
    private User user;
    private Integer currentPage = 0;
    private ArrayList<Notice> notices = new ArrayList<Notice>();
    private int tag = 1;
    private View selectView;
    private String currentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        setContentView(R.layout.activity_xiaoxi_list);
        super.onCreate(savedInstanceState);
//        BaseUtil.setColor(this,getResources().getColor(R.color.titlecolor));
        user = getApplicationContext().getUser();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        getList(currentPage.toString(), tag);
        super.onResume();
    }

    private void getList(String page, int i) {
        getNetWorker().noticeList(user.getToken(), i + "", page);
        //getNetWorker().noticeList(user.getToken(),"6",page);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask netTask) {
        // TODO Auto-generated method stub
        showProgressDialog("加载中");
    }

    @Override
    protected void callAfterDataBack(HemaNetTask netTask) {
        // TODO Auto-generated method stub
        cancelProgressDialog();
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask netTask,
                                            HemaBaseResult baseResult) {
        // TODO Auto-generated method stub
        JhHttpInformation information = (JhHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case NOTICE_LIST:
                HemaPageArrayResult<Notice> iResult = (HemaPageArrayResult<Notice>) baseResult;
                ArrayList<Notice> notices1 = iResult.getObjects();
                notices.addAll(iResult.getObjects());
                if ("0".equals(currentPage.toString())) {
                    layout.refreshSuccess();
                    notices.clear();
                    notices.addAll(iResult.getObjects());
                    if (notices.size() == 0) {
                        alert.setVisibility(View.VISIBLE);

                    } else {
                        alert.setVisibility(View.GONE);
                    }
                    int sysPagesize = getApplicationContext().getSysInitInfo()
                            .getSys_pagesize();
                    if (notices1.size() < sysPagesize)
                        layout.setLoadmoreable(false);
                    else
                        layout.setLoadmoreable(true);
                    //}
                } else {// 更多
                    layout.loadmoreSuccess();
                    if (notices1.size() > 0)
                        this.notices.addAll(iResult.getObjects());
                    else {
                        layout.setLoadmoreable(false);
                        XtomToastUtil.showShortToast(mContext, "已经到最后啦");
                    }
                }
                freshData();
                break;
            case NOTICE_SAVEOPERATE:
                String keytype = netTask.getParams().get("operatetype");
                String id = netTask.getParams().get("keyid");
                if ("3".equals(keytype)) {
                    showTextDialog("删除成功");
                }
                currentPage = 0;
                getList(currentPage.toString(), tag);
                break;
//            case task_recieve:
//                getNetWorker().noticeSaveoperate(user.getToken(),
//                        currentId,"2");
//                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask netTask,
                                           HemaBaseResult baseResult) {
        showTextDialog(baseResult.getMsg());
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask netTask, int failedType) {
        showTextDialog("网络错误");
    }

    @Override
    protected void findView() {
        // TODO Auto-generated method stub
        title = (TextView) findViewById(R.id.title_text);
        title.setText("消息");
        alert = (TextView) findViewById(R.id.alert);
        next_button = (Button) findViewById(R.id.next_button);
        next_button.setVisibility(View.GONE);
        layout = (XtomRefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        left = (ImageButton) findViewById(R.id.back_button);
        mListView = (SwipeMenuListView) findViewById(R.id.listView);
    }

    @Override
    protected void getExras() {
        // TODO Auto-generated method stub
    }

    public void setUi() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(241,
                        2, 21)));
                // set item width
                deleteItem.setWidth(dp2px(70));
                // set a icon
                deleteItem.setIcon(R.mipmap.delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        mListView.setMenuCreator(creator);
    }

    @Override
    protected void setListener() {
        // TODO Auto-generated method stub
        left.setOnClickListener(this);
        layout.setOnStartListener(new OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout v) {
                currentPage = 0;
                getList(currentPage.toString(), tag);
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout v) {
                currentPage++;
                getList(currentPage.toString(), tag);
            }
        });
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        getNetWorker().noticeSaveoperate(user.getToken(),
                                notices.get(position).getId(), notices.get(position).getKeytype(), notices.get(position).getId(), "3");
                        break;
                }
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ("2".equals(notices.get(position).getLooktype())) {
                    if ("7".equals(notices.get(position).getKeytype()) || "8".equals(notices.get(position).getKeytype())) {
                        Intent intent = new Intent(MyXiaoXi.this, NewsToPay.class);
                        intent.putExtra("keyid",notices.get(position).getKeyid());
                        intent.putExtra("keytype",notices.get(position).getKeytype());
                        startActivity(intent);
                    }
                } else {
                    getNetWorker().noticeSaveoperate(user.getToken(),
                            notices.get(position).getId(), notices.get(position).getKeytype(), notices.get(position).getId(), "1");
                }
            }
        });
        // set SwipeListener
        mListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.back_button:
                MyXiaoXi.this.finish();
                break;
            default:
                break;
        }
    }

    class AppAdapter extends HemaAdapter {
        private ArrayList<Notice> adapternotices;

        public AppAdapter(ArrayList<Notice> notices) {
            super(MyXiaoXi.this);
            this.adapternotices = notices;
        }

        @Override
        public int getCount() {
            int size = adapternotices == null ? 0 : adapternotices.size();
            return size == 0 ? 0 : adapternotices.size();
        }

        @Override
        public boolean isEmpty() {
            int size = adapternotices == null ? 0 : adapternotices.size();
            return size == 0;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (isEmpty()) {
                return getEmptyView(parent);
            }
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(),
                        R.layout.activity_xiaoxi_listitem, null);
                new ViewHolder(convertView, position);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            if ("1".equals(adapternotices.get(position).getLooktype())) {
                holder.agree.setVisibility(View.VISIBLE);
            } else
                holder.agree.setVisibility(View.GONE);
            holder.xiaoxi_time.setText(BaseUtil.TransTime(adapternotices.get(position).getRegdate(),
                    "yyyy.MM.dd HH:mm"));
            holder.tv_content
                    .setText(adapternotices.get(position).getContent());
            return convertView;
        }

        class ViewHolder {
            TextView tv_content;
            TextView xiaoxi_time;
            ImageView agree;

            public ViewHolder(View view, final int position) {
                xiaoxi_time = (TextView) view.findViewById(R.id.xiaoxi_title);
                tv_content = (TextView) view.findViewById(R.id.xiaoxi_content);
                agree = (ImageView) view.findViewById(R.id.count);
                view.setTag(this);
            }
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    private void freshData() {
        if (mAdapter == null) {
            mAdapter = new AppAdapter(notices);
            mListView.setAdapter(mAdapter);
            setUi();
        } else {
            // adapter.setEmptyString("您还没有保修记录");
            mAdapter.notifyDataSetChanged();
            setUi();
        }
    }

}
