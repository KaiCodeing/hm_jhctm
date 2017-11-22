package com.hemaapp.jhctm;

import android.content.Context;

import com.hemaapp.hm_FrameWork.HemaNetWorker;
import com.hemaapp.hm_FrameWork.HemaUtil;
import com.hemaapp.jhctm.nettask.AccountGetTask;
import com.hemaapp.jhctm.nettask.AdListTask;
import com.hemaapp.jhctm.nettask.AddressListTask;
import com.hemaapp.jhctm.nettask.AdvertiseListTask;
import com.hemaapp.jhctm.nettask.AfterServiceBillListTask;
import com.hemaapp.jhctm.nettask.AlipayTradeTask;
import com.hemaapp.jhctm.nettask.BillExpressfeeGetTask;
import com.hemaapp.jhctm.nettask.BillGetTask;
import com.hemaapp.jhctm.nettask.BillInforGetTask;
import com.hemaapp.jhctm.nettask.BillListTask;
import com.hemaapp.jhctm.nettask.Blog1ListTask;
import com.hemaapp.jhctm.nettask.CardListTask;
import com.hemaapp.jhctm.nettask.CartAddTask;
import com.hemaapp.jhctm.nettask.CartListTask;
import com.hemaapp.jhctm.nettask.CityListTask;
import com.hemaapp.jhctm.nettask.ClientAddTask;
import com.hemaapp.jhctm.nettask.ClientBankListTask;
import com.hemaapp.jhctm.nettask.ClientLoginTask;
import com.hemaapp.jhctm.nettask.ClientVerifyTask;
import com.hemaapp.jhctm.nettask.CodeGetTask;
import com.hemaapp.jhctm.nettask.CodeVerifyTask;
import com.hemaapp.jhctm.nettask.DistrictAllGetTask;
import com.hemaapp.jhctm.nettask.DistrictListTask;
import com.hemaapp.jhctm.nettask.FileUploadTask;
import com.hemaapp.jhctm.nettask.GetPayTask;
import com.hemaapp.jhctm.nettask.GoldRebatListTask;
import com.hemaapp.jhctm.nettask.GoodsGetTask;
import com.hemaapp.jhctm.nettask.GoodsOperateTask;
import com.hemaapp.jhctm.nettask.InitTask;
import com.hemaapp.jhctm.nettask.MallListTask;
import com.hemaapp.jhctm.nettask.MerchantGetTask;
import com.hemaapp.jhctm.nettask.MerchantListTask;
import com.hemaapp.jhctm.nettask.NoResultReturnTask;
import com.hemaapp.jhctm.nettask.NoticeListTask;
import com.hemaapp.jhctm.nettask.NoticeSaveoperateTask;
import com.hemaapp.jhctm.nettask.PasswordResetTask;
import com.hemaapp.jhctm.nettask.ReplyAddTask;
import com.hemaapp.jhctm.nettask.ReplyListTask;
import com.hemaapp.jhctm.nettask.ShareAdvertiseTask;
import com.hemaapp.jhctm.nettask.TimeTask;
import com.hemaapp.jhctm.nettask.UnionTradeTask;
import com.hemaapp.jhctm.nettask.WeixinTradeTask;
import com.hemaapp.jhctm.nettask.cardGetTask;
import com.hemaapp.jhctm.nettask.scoreBillGetTask;

import java.util.HashMap;

import xtom.frame.util.XtomDeviceUuidFactory;
import xtom.frame.util.XtomSharedPreferencesUtil;

/**
 * Created by lenovo on 2016/9/6.
 */
public class JhNetWorker extends HemaNetWorker {
    /**
     * 实例化网络请求工具类
     *
     * @param mContext
     */
    private Context mContext;

    public JhNetWorker(Context mContext) {

        super(mContext);
        this.mContext = mContext;


    }

    @Override
    public void clientLogin() {

        JhHttpInformation information = JhHttpInformation.CLIENT_LOGIN;
        HashMap<String, String> params = new HashMap<String, String>();
        String username = XtomSharedPreferencesUtil.get(mContext, "username");
        params.put("username", username);// 用户登录名 手机号或邮箱
        String password = XtomSharedPreferencesUtil.get(mContext, "password");
        params.put("password", password); // 登陆密码 服务器端存储的是32位的MD5加密串
        params.put("devicetype", "2"); // 用户登录所用手机类型 1：苹果 2：安卓（方便服务器运维统计）
        String version = HemaUtil.getAppVersionForSever(mContext);
        params.put("lastloginversion", version);// 登陆所用的系统版本号,记录用户的登录版本，方便服务器运维统计

        JhNetTask task = new ClientLoginTask(information, params);
        executeTask(task);

    }

    @Override
    public boolean thirdSave() {
        return false;
    }

    /**
     * @param
     * @param
     * @方法名称: inIt
     * @功能描述: TODO初始化
     * @返回值: void
     */
    public void inIt() {
        JhHttpInformation information = JhHttpInformation.INIT;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("lastloginversion", HemaUtil.getAppVersionForSever(mContext));// 版本号
        params.put("device_sn", XtomDeviceUuidFactory.get(mContext));
        JhNetTask netTask = new InitTask(information, params);
        executeTask(netTask);
    }

    /**
     * 登录
     *
     * @param username
     * @param password
     */
    public void clientLogin(String username, String password) {
        JhHttpInformation information = JhHttpInformation.CLIENT_LOGIN;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", username);// 版本号
        params.put("password", password);
        params.put("devicetype", "2"); // 用户登录所用手机类型 1：苹果 2：安卓（方便服务器运维统计）
        String version = HemaUtil.getAppVersionForSever(mContext);
        params.put("lastloginversion", version);// 登陆所用的系统版本号,记录用户的登录版本，方便服务器运维统计
        JhNetTask netTask = new ClientLoginTask(information, params);
        executeTask(netTask);
    }

    /**
     * 用户详情
     *
     * @param //username
     * @param //password
     */
    public void clientGet(String token, String id) {
        JhHttpInformation information = JhHttpInformation.CLIENT_GET;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);// 版本号
        params.put("id", id);

        String version = HemaUtil.getAppVersionForSever(mContext);
        JhNetTask netTask = new ClientLoginTask(information, params);
        executeTask(netTask);
    }

    /**
     * @param username
     * @param code
     * @方法名称: codeVerify
     * @功能描述: TODO验证验证码
     * @返回值: void
     */
    public void codeVerify(String username, String code) {
        JhHttpInformation information = JhHttpInformation.CODE_VERIFY;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("code", code);
        JhNetTask task = new CodeVerifyTask(information, params);
        executeTask(task);
    }

    /**
     * @param username
     * @方法名称: clientVerify
     * @功能描述: TODO验证用户是否注册过
     * @返回值: void
     */
    public void clientVerify(String username) {
        JhHttpInformation information = JhHttpInformation.CLIENT_VERIFY;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", username);

        JhNetTask task = new ClientVerifyTask(information, params);
        executeTask(task);
    }

    /**
     * @param username
     * @方法名称: codeGet
     * @功能描述: TODO发送验证码
     * @返回值: void
     */
    public void codeGet(String username) {
        JhHttpInformation information = JhHttpInformation.CODE_GET;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        JhNetTask task = new CodeGetTask(information, params);
        executeTask(task);
    }

    /**
     * 超市列表
     * @param district_name
     */
    public void mallList(String district_name,String keyword)
    {
        JhHttpInformation information = JhHttpInformation.MALL_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("district_name", district_name);// 版本号
        params.put("keyword", keyword);// 版本号
        JhNetTask netTask = new MallListTask(information, params);
        executeTask(netTask);
    }

    /**
     * 热门城市
     */
    public void cityList()
    {
        JhHttpInformation information = JhHttpInformation.CITY_LIST;
        HashMap<String, String> params = new HashMap<String, String>();

        JhNetTask netTask = new CityListTask(information, params);
        executeTask(netTask);
    }
    /**
     * 地区列表district_list
     */
    public void districtList(String parentid)
    {
        JhHttpInformation information = JhHttpInformation.DISTRICT_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("parentid", parentid);// 版本号
        JhNetTask netTask = new DistrictListTask(information, params);
        executeTask(netTask);
    }

    /**
     * 所有地区列表
     */
    public void districtAllGet()
    {
        JhHttpInformation information = JhHttpInformation.DISTRICT_ALL_GET;
        HashMap<String, String> params = new HashMap<String, String>();

        JhNetTask netTask = new DistrictAllGetTask(information, params);
        executeTask(netTask);
    }
    /**
     * @param temp_token
     * @param keytype
     * @param new_password
     * @方法名称: passwordReset
     * @功能描述: TODO修改密码
     * @返回值: void
     */
    public void passwordReset(String temp_token, String keytype,
                              String new_password) {
        JhHttpInformation information = JhHttpInformation.PASSWORD_RESET;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("temp_token", temp_token);
        params.put("keytype", keytype);
        params.put("new_password", new_password);
        JhNetTask task = new PasswordResetTask(information, params);
        executeTask(task);
    }
    /**
     * @param temp_token
     * @param username
     * @param password
     * @方法名称: clientAdd
     * @功能描述: TODO 用户注册
     * @返回值: void
     */
    public void clientAdd(String temp_token, String username, String password,
                          String nickname, String personid,String mall_id,String invite_username) {
        JhHttpInformation information = JhHttpInformation.CLIENT_ADD;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("temp_token", temp_token);
        params.put("username", username);
        params.put("password", password);
        params.put("nickname", nickname);
        params.put("personid", personid);
        params.put("mall_id", mall_id);
        params.put("invite_username", invite_username);
        JhNetTask task = new ClientAddTask(information, params);
        executeTask(task);
    }
    /**
     * @param token
     * @param keytype
     * @param keyid
     * @param orderby
     * @param temp_file
     * @方法名称: fileUpload
     * @功能描述: TODO上传文件
     * @返回值: void
     */
    public void fileUpload(String token, String keytype, String keyid,
                           String duration, String orderby, String content, String temp_file) {
        JhHttpInformation information = JhHttpInformation.FILE_UPLOAD;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("keyid", keyid);
        params.put("orderby", orderby);
        params.put("duration", duration);
        params.put("content", content);
        HashMap<String, String> files = new HashMap<String, String>();
        files.put("temp_file", temp_file);
        JhNetTask task = new FileUploadTask(information, params, files);
        executeTask(task);
    }
    /**
     *
     * @方法名称: deviceSave
     * @功能描述: TODO硬件保存
     * @param token
     * @param deviceid
     * @param devicetype
     * @param channelid
     * @返回值: void
     */
    public void deviceSave(String token, String deviceid, String devicetype,
                           String channelid) {
        JhHttpInformation information = JhHttpInformation.DEVICE_SAVE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("deviceid", deviceid);
        params.put("devicetype", devicetype);
        params.put("channelid", channelid);

        JhNetTask task = new CodeGetTask(information, params);
        executeTask(task);
    }
    /**
     * 广告页
     */
    public void adList() {
        JhHttpInformation information = JhHttpInformation.AD_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        JhNetTask task = new AdListTask(information, params);
        executeTask(task);
    }
    /**
     * 商品列表易物兑
     */
    public void blog1List(String topflag,String typeid,String merchant_id,String orderby,String page) {
        JhHttpInformation information = JhHttpInformation.BLOG_1_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("topflag", topflag);
        params.put("typeid", typeid);
        params.put("merchant_id", merchant_id);
        params.put("lng", XtomSharedPreferencesUtil.get(mContext,"lng"));
        params.put("lat", XtomSharedPreferencesUtil.get(mContext,"lat"));
        params.put("orderby", orderby);
        params.put("page", page);
        JhNetTask task = new Blog1ListTask(information, params);
        executeTask(task);
    }
    /**
     * 商品列表易物兑
     */
    public void blog1List2(String topflag,String typeid,String merchant_id,String orderby,String start_score,
                           String end_score,String keyword,String page) {
        JhHttpInformation information = JhHttpInformation.BLOG_1_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("topflag", topflag);
        params.put("typeid", typeid);
        params.put("lng", XtomSharedPreferencesUtil.get(mContext,"lng"));
        params.put("lat", XtomSharedPreferencesUtil.get(mContext,"lat"));
        params.put("merchant_id", merchant_id);
        params.put("orderby", orderby);
        params.put("start_score", start_score);
        params.put("end_score", end_score);
        params.put("keyword", keyword);
        params.put("page", page);
        JhNetTask task = new Blog1ListTask(information, params);
        executeTask(task);
    }
    /**
     * 商品列表易物兑
     */
    public void blog1List3(String topflag,String typeid,String merchant_id,String orderby,String ordertype,String start_score,
                           String end_score,String keyword,String page) {
        JhHttpInformation information = JhHttpInformation.BLOG_1_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("topflag", topflag);
        params.put("typeid", typeid);
        params.put("merchant_id", merchant_id);
        params.put("lng", XtomSharedPreferencesUtil.get(mContext,"lng"));
        params.put("lat", XtomSharedPreferencesUtil.get(mContext,"lat"));
        params.put("orderby", orderby);
        params.put("start_score", start_score);
        params.put("end_score", end_score);
        params.put("keyword", keyword);
        params.put("page", page);
        params.put("ordertype", ordertype);
        JhNetTask task = new Blog1ListTask(information, params);
        executeTask(task);
    }
    /**
     * 分享专区
     */
    public void blog3List(String page) {
        JhHttpInformation information = JhHttpInformation.BLOG_3_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("page", page);
        JhNetTask task = new Blog1ListTask(information, params);
        executeTask(task);
    }
    /**
     * 限时兑换
     */
    public void blog2List(String keytype,String page) {
        JhHttpInformation information = JhHttpInformation.BLOG_2_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("keytype", keytype);
        params.put("page", page);
        JhNetTask task = new Blog1ListTask(information, params);
        executeTask(task);
    }
    /**
     * 商品详情
     */
    public void blogGet(String id) {
        JhHttpInformation information = JhHttpInformation.BLOG_GET;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        JhNetTask task = new GoodsGetTask(information, params);
        executeTask(task);
    }
    /**
     * 商品详情
     */
    public void merchantList(String keyword,String page) {
        JhHttpInformation information = JhHttpInformation.MERCHANT_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("keyword", keyword);
        params.put("page", page);
        JhNetTask task = new MerchantListTask(information, params);
        executeTask(task);
    }
    /**
     * 品牌商商品详情
     */
    public void merchantGet(String token,String keyid) {
        JhHttpInformation information = JhHttpInformation.MERCHANT_GET;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("keyid", keyid);
        JhNetTask task = new MerchantGetTask(information, params);
        executeTask(task);
    }
    /**
     * 评价列表
     */
    public void replyList(String keytype,String keyid,String page) {
        JhHttpInformation information = JhHttpInformation.REPLY_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("keytype", keytype);
        params.put("keyid", keyid);
        params.put("page", page);
        JhNetTask task = new ReplyListTask(information, params);
        executeTask(task);
    }
    /**
     * 收藏操作follow_collect_operator
     */
    public void goodsOperate(String token, String keytype, String oper,String keyid) {
        JhHttpInformation information = JhHttpInformation.FOLLOW_COLLECT_OPERATOR;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("oper", oper);
        params.put("keyid", keyid);
        JhNetTask task = new GoodsOperateTask(information, params);
        executeTask(task);
    }
    /**
     * 加入购物车
     */
    public void cartAdd(String token, String keyid, String specid, String buycount) {
        JhHttpInformation information = JhHttpInformation.CART_ADD;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("buycount", buycount);
        params.put("keyid", keyid);
        params.put("specid", specid);
        JhNetTask task = new CartAddTask(information, params);
        executeTask(task);
    }
    /**
     * 获取收货地址
     */
    public void addressList(String token) {
        JhHttpInformation information = JhHttpInformation.ADDRESS_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        JhNetTask task = new AddressListTask(information, params);
        executeTask(task);
    }

    /**
     * 编辑或添加收货地址
     */
    public void addressSave(String token,String id,String name,String tel,String province_id,String city_id,String district_id,String address
    ,String zipcode,String defaultflag) {
        JhHttpInformation information = JhHttpInformation.ADDRESS_SAVE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("id", id);
        params.put("name", name);
        params.put("tel", tel);
        params.put("province_id", province_id);
        params.put("city_id", city_id);
        params.put("district_id", district_id);
        params.put("address", address);
        params.put("zipcode", zipcode);
        params.put("defaultflag", defaultflag);
        JhNetTask task = new CartAddTask(information, params);
        executeTask(task);
    }
    /**
     * 删除收货地址
     */
    public void addressRemove(String token,String id) {
        JhHttpInformation information = JhHttpInformation.ADDRESS_REMOVE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("id", id);
        JhNetTask task = new CartAddTask(information, params);
        executeTask(task);
    }
    /**
     * 保存发票订单
     */
    public void billBillsSave(String token,String bill_id,String bills_type,String bills_head,String bills_content,String bills_fee) {
        JhHttpInformation information = JhHttpInformation.BILL_BILLS_SAVE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("id", "0");
        params.put("bill_id", bill_id);
        params.put("bills_type", bills_type);
        params.put("bills_head", bills_head);
        params.put("bills_content", bills_content);
        params.put("bills_fee", bills_fee);
        JhNetTask task = new CartAddTask(information, params);
        executeTask(task);
    }
    /**
     * 提交订单
     */
    public void billSave(String token,String keytype,String keyid,String address_id,String specid,String buycount
    ,String recvtype,String mall_id) {
        JhHttpInformation information = JhHttpInformation.BILL_SAVE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("keyid", keyid);
        params.put("address_id", address_id);
        params.put("specid", specid);
        params.put("buycount", buycount);
        params.put("recvtype", recvtype);
        params.put("mall_id", mall_id);
        JhNetTask task = new BillGetTask(information, params);
        executeTask(task);
    }
    /**
     * 修改密码
     */
    public void passwordSave(String token,String keytype,String old_password,String new_password) {
        JhHttpInformation information = JhHttpInformation.PASSWORD_SAVE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("old_password", old_password);
        params.put("new_password", new_password);
        JhNetTask task = new CartAddTask(information, params);
        executeTask(task);
    }
    /**
     * 退出登录
     */
    public void clientLoginout(String token) {
        JhHttpInformation information = JhHttpInformation.CLIENT_LOGINOUT;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        JhNetTask task = new CartAddTask(information, params);
        executeTask(task);
    }
    /**
     * 获取收藏列表
     */
    public void followCollectList(String token,String keytype,String keyid,String page) {
        JhHttpInformation information = JhHttpInformation.FOLLOW_COLLECT_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("keyid", keyid);
        params.put("page", page);
        JhNetTask task = new Blog1ListTask(information, params);
        executeTask(task);
    }
    /**
     * 银行提现列表
     */
    public void clientBankList(String token) {
        JhHttpInformation information = JhHttpInformation.CLIENT_BANK_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        JhNetTask task = new ClientBankListTask(information, params);
        executeTask(task);
    }
    /**
     * 银行提现列表
     */
    public void bankList(String token) {
        JhHttpInformation information = JhHttpInformation.BANK_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        JhNetTask task = new ClientBankListTask(information, params);
        executeTask(task);
    }
    /**
     * 银行卡信息保存
     */
    public void clientBankSave(String token,String id,String bankname,String bankuser,String bankcard,String bankaddress,String defaultflag) {
        JhHttpInformation information = JhHttpInformation.CLIENT_BANK_SAVE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("id", id);
        params.put("bankname", bankname);
        params.put("bankuser", bankuser);
        params.put("bankcard", bankcard);
        params.put("bankaddress", bankaddress);
        params.put("defaultflag", defaultflag);

        JhNetTask task = new CartAddTask(information, params);
        executeTask(task);
    }

    /**
     * 商品分类列表
     */
    public void blogtypeList(String parentid) {
        JhHttpInformation information = JhHttpInformation.BLOGTYPE_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("parentid", parentid);
        JhNetTask task = new CityListTask(information, params);
        executeTask(task);
    }
    /**
     * 评价列表
     */
    public void tradelist(String token,String page) {
        JhHttpInformation information = JhHttpInformation.TRADE_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("page", page);
        JhNetTask task = new ReplyListTask(information, params);
        executeTask(task);
    }
    /**
     * 提现申请
     */
    public void cashAdd(String token,String keytype,String applyfee,String password) {
        JhHttpInformation information = JhHttpInformation.CASH_ADD;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("applyfee", applyfee);
        params.put("password", password);
        JhNetTask task = new CartAddTask(information, params);
        executeTask(task);
    }
    /**
     * 我的卡包
     */
    public void cardList(String token,String page) {
        JhHttpInformation information = JhHttpInformation.CARD_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("page", page);
        JhNetTask task = new CardListTask(information, params);
        executeTask(task);
    }
    /**
     * 出售卡包
     */
    public void cashSell(String token,String keyid,String username) {
        JhHttpInformation information = JhHttpInformation.CARD_SELL;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("keyid", keyid);
        params.put("username", username);
        JhNetTask task = new CartAddTask(information, params);
        executeTask(task);
    }
    /**
     * 出售卡包
     */
    public void cardRemove(String token,String keyid) {
        JhHttpInformation information = JhHttpInformation.CARD_REMOVE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("keyid", keyid);
        JhNetTask task = new CartAddTask(information, params);
        executeTask(task);
    }
    /**
     * 账户详情
     */
    public void accountGet(String token) {
        JhHttpInformation information = JhHttpInformation.ACCOUNT_GET;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        JhNetTask task = new AccountGetTask(information, params);
        executeTask(task);
    }
    /**
     * 余额支付
     */
    public void feeaccountRemove(String token,String keytype,String keyid,String paypassword,String total_fee,String total_score,String total_share_score) {
        JhHttpInformation information = JhHttpInformation.FEEACCOUNT_REMOVE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("keyid", keyid);
        params.put("paypassword", paypassword);
        params.put("total_fee", total_fee);
        params.put("total_score", total_score);
        params.put("total_share_score", total_share_score);
        JhNetTask task = new CartAddTask(information, params);
        executeTask(task);
    }
    /**
     * 兑换券金积分明细接口
     */
    public void goldScoreList(String token,String page) {
        JhHttpInformation information = JhHttpInformation.GOLD_SCORE_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("page", page);
        JhNetTask task = new ReplyListTask(information, params);
        executeTask(task);
    }
    /**
     * 兑换券金积分明细接口gold_score_buy_list
     */
    public void goldScoreBuyList(String token,String page) {
        JhHttpInformation information = JhHttpInformation.GOLD_SCORE_BUY_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("page", page);
        JhNetTask task = new ReplyListTask(information, params);
        executeTask(task);
    }
    /**
     * 出售金积分goldscore_sell
     */
    public void goldScoreSell(String token,String gold_score,String username,String fee) {
        JhHttpInformation information = JhHttpInformation.GOLDSCORE_SELL;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("gold_score", gold_score);
        params.put("username", username);
        params.put("fee", fee);
        JhNetTask task = new CartAddTask(information, params);
        executeTask(task);
    }
    /**
     * 兑换币明细接口share_score_list
     */
    public void shareScoreList(String token,String page) {
        JhHttpInformation information = JhHttpInformation.SHARE_SCORE_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("page", page);
        JhNetTask task = new ReplyListTask(information, params);
        executeTask(task);
    }
    /**
     * 购物车
     */
    public void cartList(String token) {
        JhHttpInformation information = JhHttpInformation.CART_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        JhNetTask task = new CartListTask(information, params);
        executeTask(task);
    }
    /**
     * 购物车操作
     */
    public void cartSaveoperate(String token,String id,String keytype,String buycount) {
        JhHttpInformation information = JhHttpInformation.CART_SAVEOPERATE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("id", id);
        params.put("keytype", keytype);
        params.put("buycount", buycount);

        JhNetTask task = new CartAddTask(information, params);
        executeTask(task);
    }
    /**
     *bill_expressfee_get 订单总运费
     */
    public void billExpressfeeGet(String token,String blog_ids,String spec_ids,String buycounts,String address_id) {
        JhHttpInformation information = JhHttpInformation.BILL_EXPRESSFEE_GET;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("blog_ids", blog_ids);
        params.put("spec_ids", spec_ids);
        params.put("buycounts", buycounts);
        params.put("address_id", address_id);
        JhNetTask task = new BillExpressfeeGetTask(information, params);
        executeTask(task);
    }
    /**
     * 获取用户通知列表
     */
    public void noticeList(String token, String noticetype, String page) {
        JhHttpInformation information = JhHttpInformation.NOTICE_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);// 登陆令牌
        params.put("noticetype", noticetype);// /获取类型 1：系统通知2：评论回复3: 聊天人员列表页
        params.put("page", page);// 当前列表翻页索引 第一页时请传递page=0，翻页时依次递增。
        JhNetTask task = new NoticeListTask(information, params);
        executeTask(task);
    }

    /**
     * 保存用户通知操作
     */
    public void noticeSaveoperate(String token, String id,String keytype,String keyid,String operatetype) {
        JhHttpInformation information = JhHttpInformation.NOTICE_SAVEOPERATE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);// 登陆令牌
        params.put("id", id);// 通知主键id 从 通知列表 获取
        params.put("keytype", keytype);// 操作类型
        params.put("keyid", keyid);
        params.put("operatetype", operatetype);// 操作类型										// 1：置为已读2：全部已3:删除单条4：删除全部（清空）5：同意好友申请其余待扩展...
        JhNetTask task = new NoticeSaveoperateTask(information, params);
        executeTask(task);
    }
    /**
     * 获取支付宝交易签名串
     */
    public void alipay(String token, String keytype,
                       String keyid, String total_fee,String total_score,String total_share_score) {
        JhHttpInformation information = JhHttpInformation.ALIPAY;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);// 登陆令牌
        params.put("paytype", "1");// 支付类型 固定传2
        params.put("keytype", keytype);// 业务类型,1：账户余额充值2：商品立即购买
        params.put("keyid", keyid);// 业务相关,id当keytype=1时,keyid=0当keytype=2时,keyid=blog_id
        params.put("total_fee", total_fee);// 支付交易金额,单位：元(测试时统一传递0.01元)
        params.put("total_score", total_score);// 支付交易金额,单位：元(测试时统一传递0.01元)
        params.put("total_share_score", total_share_score);// 支付交易金额,单位：元(测试时统一传递0.01元)
        JhNetTask task = new AlipayTradeTask(information, params);
        executeTask(task);
    }

    /**
     * 获取银联交易签名串
     */
    public void unionpay(String token, String keytype,
                         String keyid, String total_fee,String total_score,String total_share_score) {
        JhHttpInformation information = JhHttpInformation.UNIONPAY;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);// 登陆令牌
        params.put("paytype", "2");// 支付类型 固定传2
        params.put("keytype", keytype);// 业务类型,1：账户余额充值2：商品立即购买
        params.put("keyid", keyid);// 业务相关,id当keytype=1时,keyid=0当keytype=2时,keyid=blog_id
        params.put("total_fee", total_fee);// 支付交易金额,单位：元(测试时统一传递0.01元)
        params.put("total_score", total_score);// 支付交易金额,单位：元(测试时统一传递0.01元)
        params.put("total_share_score", total_share_score);// 支付交易金额,单位：元(测试时统一传递0.01元)
        JhNetTask task = new UnionTradeTask(information, params);
        executeTask(task);
    }

    /**
     * 获取微信预支付交易会话标识
     */
    public void weixinpay(String token,String keytype,
                          String keyid,String total_fee,String total_score,String total_share_score) {
        JhHttpInformation information = JhHttpInformation.WXPAYMENT;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);// 登陆令牌
        params.put("keytype", keytype);// 业务类型,1：账户余额充值2：商品立即购买
        params.put("keyid", keyid);// 业务相关,id当keytype=1时,keyid=0当keytype=2时,keyid=blog_id
        params.put("paytype", "3");
        params.put("total_fee", total_fee);
        params.put("total_score", total_score);
        params.put("total_share_score", total_share_score);
        JhNetTask task = new WeixinTradeTask(information, params);
        executeTask(task);
    }
    //余额支付
    public void feeaccount_remove(String token,String keytype,String keyid,String total_fee,String total_score,String total_share_score)
    {
        JhHttpInformation information = JhHttpInformation.FEEACCOUNT_REMOVE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("keytype",keytype);
        params.put("keyid",keyid);
        //params.put("paypassword", Md5Util.getMd5(XtomConfig.DATAKEY + Md5Util.getMd5(paypassword))); // 登陆密码 服务器端存储的是32位的MD5加密串Md5Util.getMd5(XtomConfig.DATAKEY + Md5Util.getMd5(password)paypassword);
        params.put("total_fee",total_fee);
        params.put("total_score",total_score);
        params.put("total_share_score",total_share_score);
        JhNetTask task = new GetPayTask(information, params);
        executeTask(task);
    }
    //兑换券金积分购买接口
    public void gold_score_remove(String token,String keytype,String keyid,String gold_score)
    {
        JhHttpInformation information = JhHttpInformation.GOLD_SCORE_REMOVE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("keyid", keyid);
        params.put("keytype", keytype);
        params.put("gold_score", gold_score);
        JhNetTask task = new GetPayTask(information, params);
        executeTask(task);
    }
    //积分返还明细
    public void score_reabt_list(String token,String page)
    {
        JhHttpInformation information = JhHttpInformation.SCORE_REABT_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("page",page);
        JhNetTask task = new TimeTask(information, params);
        executeTask(task);
    }
    //兑换券金积分返还记录接口
    public void scoreReabtList(String token,String page)
    {
        JhHttpInformation information = JhHttpInformation.SCORE_REABT_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("page",page);
        JhNetTask task = new TimeTask(information, params);
        executeTask(task);
    }
    //积分明细
    public void score_list(String token,String page)
    {
        JhHttpInformation information = JhHttpInformation.SCORE_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("page", page);
        JhNetTask task = new ReplyListTask(information, params);
        executeTask(task);
    }

    public void score_to_goldscore(String token,String gold_score)
    {
        JhHttpInformation information = JhHttpInformation.SCORE_TO_GOLDSCORE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("gold_score", gold_score);
        JhNetTask task = new PasswordResetTask(information, params);
        executeTask(task);
    }


    /**
     * 订单列表接口
     * */
    public void billList(String token, String billtype, String keytype, int page){
        JhHttpInformation information = JhHttpInformation.BILL_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("billtype", billtype);
        params.put("keytype", keytype);
        params.put("page", String.valueOf(page));

        JhNetTask task = new BillListTask(information, params);
        executeTask(task);
    }

    /**
     * 订单操作接口
     * */
    public void billSaveOperate(String token, String keyid, String keytype, String reason, String discription){
        JhHttpInformation information = JhHttpInformation.BILL_SAVEOPERATE;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("keyid", keyid);
        params.put("keytype", keytype);
        params.put("reason", reason);
        params.put("discription", discription);

        JhNetTask task = new NoResultReturnTask(information, params);
        executeTask(task);
    }

    /**
     * 问题订单列表接口
     * */
    public void afterServiceBillList(String token, String billtype, int page){
        JhHttpInformation information = JhHttpInformation.AFTERSERVICE_BILL_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("billtype", billtype);
        params.put("page", String.valueOf(page));

        JhNetTask task = new AfterServiceBillListTask(information, params);
        executeTask(task);
    }

    /**
     * 订单详情接口
     * */
    public void billGet(String token, String id){
        JhHttpInformation information = JhHttpInformation.BILL_GET;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("id", id);

        JhNetTask task = new BillInforGetTask(information, params);
        executeTask(task);
    }

    /**
     * 添加订单商品评论接口
     * */
    public void replyAdd(String token, String keytype, String keyid, String content, String stars,
                         String parentid){
        JhHttpInformation information = JhHttpInformation.REPLY_ADD;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("keyid", keyid);
        params.put("content", content);
        params.put("stars", stars);
        params.put("parentid", parentid);

        JhNetTask task = new ReplyAddTask(information, params);
        executeTask(task);
    }
    /**
     * @param token
     * @param paytype
     * @param keytype
     * @param keyid
     * @param total_fee
     * @方法名称: wxpayment
     * @功能描述: TODO微信支付
     * @返回值: void
     */
    public void wxpayment(String token, String paytype, String keytype,
                          String keyid, String total_fee) {
        JhHttpInformation information = JhHttpInformation.WXPAYMENT;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("paytype", paytype);
        params.put("keytype", keytype);
        params.put("keyid", keyid);
        params.put("total_fee", total_fee);
        JhNetTask task = new WeixinTradeTask(information, params);
        executeTask(task);
    }

    /**
     * @param token
     * @param paytype
     * @param keytype
     * @param keyid
     * @param total_fee
     * @param //extra_param
     * @方法名称: alipayGet
     * @功能描述: TODO支付宝支付
     * @返回值: void
     */
    public void alipayGet(String token, String paytype, String keytype,
                          String keyid, String total_fee) {
        JhHttpInformation information = JhHttpInformation.ALIPAY;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("paytype", paytype);
        params.put("keytype", keytype);
        params.put("keyid", keyid);
        params.put("total_fee", total_fee);

        JhNetTask task = new AlipayTradeTask(information, params);
        executeTask(task);
    }
    /**
     *卡包详情
     * */
    public void cardGet(String token,String id){
        JhHttpInformation information = JhHttpInformation.CARD_GET;
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("token", token);
        JhNetTask task = new cardGetTask(information, params);
        executeTask(task);
    }
    /**
     *积分返还时间轴
     * GOLD_SCORE_REBAT_LIST
     * */
    public void goldScoreRebatList(String token,String page){
        JhHttpInformation information = JhHttpInformation.GOLD_SCORE_REBAT_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("page", page);
        JhNetTask task = new TimeTask(information, params);
        executeTask(task);
    }
    /**
     *金积分倍增
     * GILDSCORE_DOUBLY
     * */
    public void goldScoreDoubly(String token,String gold_score){
        JhHttpInformation information = JhHttpInformation.GOLDSCORE_DOUBLY;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("gold_score", gold_score);
        JhNetTask task = new CartAddTask(information, params);
        executeTask(task);
    }
    /**
     *积分交易详情
     * score_bill_get
     * */
    public void scoreBillGet(String token,String keyid){
        JhHttpInformation information = JhHttpInformation.SCORE_BILL_GET;
        HashMap<String, String> params = new HashMap<>();
        params.put("keyid", keyid);
        params.put("token", token);
        JhNetTask task = new scoreBillGetTask(information, params);
        executeTask(task);
    }
    /**
     *积分交易详情
     * bank_save
     * */
    public void bankSave(String token,String bankuser,String bankname,String bankcard,String bankaddress){
        JhHttpInformation information = JhHttpInformation.BANK_SAVE;
        HashMap<String, String> params = new HashMap<>();
        params.put("bankuser", bankuser);
        params.put("token", token);
        params.put("bankname", bankname);
        params.put("bankcard", bankcard);
        params.put("bankaddress", bankaddress);
        JhNetTask task = new CartAddTask(information, params);
        executeTask(task);
    }
    /**
     *冻结积分gold_rebat_list
     * bank_save
     * */
    public void goldRebatList(String token,String keytype,String page){
        JhHttpInformation information = JhHttpInformation.GOLD_REBAT_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("keytype", keytype);
        params.put("token", token);
        params.put("page", page);
        JhNetTask task = new GoldRebatListTask(information, params);
        executeTask(task);
    }
    /**
     *advertise_list 广告推广
     * bank_save
     * */
    public void advertiseList(String token,String id,String page){
        JhHttpInformation information = JhHttpInformation.ADVERTISE_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("token", token);
        params.put("page", page);
        JhNetTask task = new AdvertiseListTask(information, params);
        executeTask(task);
    }
    /**
     *share_advertise 分享广告
     * bank_save
     * */
    public void shareAdvertise(String token,String advertise_id){
        JhHttpInformation information = JhHttpInformation.SHARE_ADVERTISE;
        HashMap<String, String> params = new HashMap<>();
        params.put("advertise_id", advertise_id);
        params.put("token", token);
        JhNetTask task = new ShareAdvertiseTask(information, params);
        executeTask(task);
    }
}
