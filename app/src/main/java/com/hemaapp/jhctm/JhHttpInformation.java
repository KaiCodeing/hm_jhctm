package com.hemaapp.jhctm;

import com.hemaapp.HemaConfig;
import com.hemaapp.hm_FrameWork.HemaHttpInfomation;
import com.hemaapp.jhctm.config.JhConfig;
import com.hemaapp.jhctm.model.SysInitInfo;

/**
 * Created by lenovo on 2016/9/6.
 */
public  enum JhHttpInformation implements HemaHttpInfomation {
    /**
     * 登录
     */
    CLIENT_LOGIN(HemaConfig.ID_LOGIN, "client_login", "登录", false),
    // 注意登录接口id必须为HemaConfig.ID_LOGIN

    /**
     * 后台服务接口根路径
     */
    SYS_ROOT(0, JhConfig.SYS_ROOT, "后台服务接口根路径", true),
    /**
     * 初始化
     */
    INIT(1, "index.php/webservice/index/init", "初始化", false),
    /**
     * code_get发送验证码
     */
    CODE_GET(2, "code_get", "发送验证码", false),
    /**
     * code_verify验证验证码
     */
    CODE_VERIFY(3, "code_verify", "验证验证码", false),
    /**
     * client_verify验证用户是否注册
     */
    CLIENT_VERIFY(4, "client_verify", "验证用户是否注册", false),
    /**
     * 密码重设password_reset
     */
    PASSWORD_RESET(5, "password_reset", "密码重设", false),
    /**
     * 支付宝alipay
     */
    ALIPAY(7,"OnlinePay/Alipay/alipaysign_get.php","支付宝获取串口号",false),
    /**
     * 银联支付unionpay
     */
    UNIONPAY(8,"OnlinePay/Unionpay/unionpay_get.php","银联获取串口号",false),
    /**
     * 微信支付wxpayment
     */
    WXPAYMENT(9,"OnlinePay/Weixinpay/weixinpay_get.php","微信获取串口号",false),
    /**
     * 超市列表mall_list
     */
    MALL_LIST(10,"mall_list","超市列表",false),
    /**
     * 城市列表district_list
     * 0：表示获取第一级别（省份或直辖市或自治区）
     -1：表示获取所有地级以上级别城市（含地级）
     */
    DISTRICT_LIST(11,"district_list","地区列表",false),
    /**
     * 所有地区列表district_all_get
     */
    DISTRICT_ALL_GET(12,"district_all_get","所有地区列表",false),
    /**
     * city_list热门城市列表
     */
    CITY_LIST(13,"city_list","热门城市列表",false),
    /**
     * client_add注册
     */
    CLIENT_ADD(14,"client_add","注册",false),
    /**
     * file_upload上传文件
     */
    FILE_UPLOAD(15,"file_upload","上传文件",false),
    /**
     * device_save硬件保存
     */
    DEVICE_SAVE(16,"device_save","硬件保存",false),
    /**
     * ad_list广告页
     */
    AD_LIST(17,"ad_list","广告页",false),
    /**
     * client_get 获取用户详情
     */
    CLIENT_GET(18,"client_get","用户详情",false),
    /**
     * blog_1_list 精品推荐
     */
    BLOG_1_LIST(19,"blog_1_list","精品推荐",false),
    /**
     * blog_get 商品详情
     */
    BLOG_GET(20,"blog_get","商品详情",false),
    /**
     * blog_3_list分享专区商品
     */
    BLOG_3_LIST(21,"blog_3_list","分享专区",false),
    /**
     * blog_2_list限时兑换
     */
    BLOG_2_LIST(22,"blog_2_list","限时兑换",false),
    /**
     * merchant_list品牌商列表
     */
    MERCHANT_LIST(23,"merchant_list","品牌商列表",false),
    /**
     * 品牌商详情merchant_get
     */
    MERCHANT_GET(24,"merchant_get","品牌商详情",false),
    /**
     * reply_list评论列表
     */
    REPLY_LIST(25,"reply_list","评论列表",false),
    /**
     * follow_collect_operator 收藏操作
     */
    FOLLOW_COLLECT_OPERATOR(26,"follow_collect_operator","收藏操作",false),
    /**
     * cart_add加入购物车
     */
    CART_ADD(27,"cart_add","加入购物车",false),
    /**
     *   address_list收货地址列表
     */
    ADDRESS_LIST(28,"address_list","收货地址列表",false),
    /**
     * address_save 保存收货地址
     */
    ADDRESS_SAVE(29,"address_save","保存或编辑收货地址",false),
    /**
     * address_remove删除收货地址
     */
    ADDRESS_REMOVE(30,"address_remove","删除收货地址",false),
    /**
     * bill_bills_save/保存订单发票
     */
    BILL_BILLS_SAVE(31,"bill_bills_save","保存订单发票",false),
    /**
     * bill_save 提交订单
     */
    BILL_SAVE(32,"bill_save","提交订单",false),
    /**
     * client_loginout退出登录
     */
    CLIENT_LOGINOUT(34,"client_loginout","退出登录",false),
    /**
     * 修改登录密码password_save
     */
    PASSWORD_SAVE(33,"password_save","修改密码",false),
    /**
     * follow_collect_list我的收藏
     */
    FOLLOW_COLLECT_LIST(34,"follow_collect_list","我的收藏",false),
    /**
     * client_bank_list 提现银行列表
     */
    CLIENT_BANK_LIST(35,"client_bank_list","提现银行列表",false),
    /**
     * bank_list 获取银行列表
     */
    BANK_LIST(36,"bank_list","获取银行列表",false),
    /**
     * client_bank_save银行卡信息保存
     */
    CLIENT_BANK_SAVE(37,"client_bank_save","银行卡信息保存",false),
    /**
     * blogtype_list 分类
     */
    BLOGTYPE_LIST(38,"blogtype_list","分类",false),

    /**
     * trade_list 交易明细
     */
    TRADE_LIST(39,"trade_list","交易明细",false),
    /**
     * cash_add 提现申请
     */
    CASH_ADD(40,"cash_add","提现申请",false),
    /**
     * card_list我的卡包
     */
    CARD_LIST(41,"card_list","我的卡包",false),
    /**
     * card_sell 出售卡包
     */
    CARD_SELL(42,"card_sell","出售卡包",false),
    /**
     * card_remove 拆分
     */
    CARD_REMOVE(43,"card_remove","拆分",false),
    /**
     * account_get 账户详情
     */
    ACCOUNT_GET(44,"account_get","账户详情",false),
    /**
     * feeaccount_remove 余额支付
     */
    FEEACCOUNT_REMOVE(45,"feeaccount_remove","余额支付",false),
    /**
     * gold_score_list 兑换券金积分明细
     */
    GOLD_SCORE_LIST(46,"gold_score_list","兑换券金积分明细",false),
    /**
     * gold_score_buy_list 平台购买记录
     */
    GOLD_SCORE_BUY_LIST(47,"gold_score_buy_list","平台购买记录",false),
    /**
     * goldscore_sell出售金积分
     */
    GOLDSCORE_SELL(48,"goldscore_sell","出售金积分",false),
    /**
     * share_score_list 分享积分兑换币积分明细
     */
    SHARE_SCORE_LIST(49,"share_score_list","兑换币积分明细",false),
    /**
     * cart_list购物车
     */
    CART_LIST(50,"cart_list","购物车",false),
    /**
     * cart_saveoperate购物车操作
     *
     */
    CART_SAVEOPERATE(51,"cart_saveoperate","购物车操作",false),
    /**
     * bill_expressfee_get 获取订单中的运费
     */
    BILL_EXPRESSFEE_GET(52,"bill_expressfee_get","获取订单中的运费",false),
    /**
     * 获取用户通知列表
     */
    NOTICE_LIST(53, "notice_list", "获取用户通知列表", false),
    /**
     * 保存用户通知操作
     */
    NOTICE_SAVEOPERATE(54, "notice_saveoperate", "保存用户通知操作", false),
    GOLD_SCORE_REMOVE(56,"gold_score_remove","兑换券积分支付",false),
    BILL_LIST(57,"bill_list","订单列表",false),
    SCORE_REABT_LIST(58,"score_reabt_list","积分返还明细",false),
    SCORE_LIST(59,"score_list","积分明细",false),
    SCORE_TO_GOLDSCORE(60,"score_to_goldscore","积分换成兑换币",false),
    /**
     * 订单详情接口
     * */
    BILL_GET(61, "bill_get", "订单详情接口", false),
    /**
     * 订单操作接口
     * */
    BILL_SAVEOPERATE(62, "bill_saveoperate", "订单操作接口", false),
    /**
     * 问题订单列表接口
     * */
    AFTERSERVICE_BILL_LIST(63, "afterservice_bill_list", "问题订单列表接口", false),
    /**
     * 添加订单商品评论接口
     * */
    REPLY_ADD(64, "reply_add", "添加订单商品评论接口", false),
    /**
     * gold_score_rebat_list 兑换券金积分返还记录接口
     */
    GOLD_SCORE_REBAT_LIST(65,"gold_score_rebat_list","兑换券金积分返还记录接口",false),
    /**
     * card_get卡包详情
     */
    CARD_GET(66,"card_get","卡包详情",false),
    /**
     * goldscore_doubly 金积分倍增
     */
    GOLDSCORE_DOUBLY(67,"goldscore_doubly","金积分倍增",false),
    /**
     * score_bill_get积分交易详情
     */
    SCORE_BILL_GET(68,"score_bill_get","积分交易详情", false),
    /**
     * bank_save银行卡信息保存
     */
    BANK_SAVE(69,"bank_save","银行卡信息保存", false),
    /**
     * 冻结积分列表gold_rebat_list
     */
    GOLD_REBAT_LIST(70,"gold_rebat_list","冻结积分列表",false),
    /**
     * advertise_list 广告推广
     */
    ADVERTISE_LIST(71,"advertise_list","广告推广",false),
    /**
     * share_advertise 分享广告
     */
    SHARE_ADVERTISE(72,"share_advertise","分享广告",false);
    private int id;// 对应NetTask的id
    private String urlPath;// 请求地址
    private String description;// 请求描述
    private boolean isRootPath;// 是否是根路径

    private JhHttpInformation(int id, String urlPath, String description,
                                boolean isRootPath) {
        this.id = id;
        this.urlPath = urlPath;
        this.description = description;
        this.isRootPath = isRootPath;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getUrlPath() {
        if (isRootPath)
            return urlPath;

        String path = SYS_ROOT.urlPath + urlPath;

        if (this.equals(INIT))
            return path;

        JhctmApplication application = JhctmApplication.getInstance();
        SysInitInfo info = application.getSysInitInfo();
        path = info.getSys_web_service() + urlPath;
         if (this.equals(ALIPAY))
         path = info.getSys_plugins() + urlPath;
         if (this.equals(UNIONPAY))
         path = info.getSys_plugins() + urlPath;
        if (this.equals(WXPAYMENT))
            path = info.getSys_plugins() + urlPath;
        return path;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean isRootPath() {
        return isRootPath;
    }

}
