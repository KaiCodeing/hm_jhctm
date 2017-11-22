package com.hemaapp.jhctm.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by WangYuxia on 2017/2/10.
 */

public class OrderGoodInfor extends XtomObject implements Serializable{

    private String id; //货品id
    private String blog_id; //商品id
    private String client_id; //用户id
    private String name; //商品名称
    private String specid; //规格id
    private String score; //积分单价
    private String share_score; //兑换币单价
    private String gold_score; //兑换金积分单价
    private String buycount; //购买数量
    private String imgurl; //商品图片
    private String imgurlbig; //商品大图
    private String bill_id; //订单id
    private String bill_sn; //订单编号
    private String regdate; //下单时间
    private String expressfee; //下单时，单个商品运费
    private String shipping_fee; //该商品实际运费
    private String memo; //处理说明
    private String reply_id; //订单评价id
    private String rebateflag; //是否返还，0不返还；1返还
    private String spec_name;
    private String servicetype; //0:否， 1：是
    private String itemtype; //退货状态，0无退货;1待处理,2已同意，3拒绝
    private String reason; //退货原因
    private String seller_name;
    private String replytype;
    private String applydate;//申请售后时间
    private String handledate;//售后处理时间
    public OrderGoodInfor(JSONObject jsonObject) throws DataParseException {
        if(jsonObject != null){
            try {
                id = get(jsonObject, "id");
                replytype = get(jsonObject, "replytype");
                blog_id = get(jsonObject, "blog_id");
                client_id = get(jsonObject, "client_id");
                name = get(jsonObject, "name");
                specid = get(jsonObject, "specid");
                score = get(jsonObject, "score");
                share_score = get(jsonObject, "share_score");
                gold_score = get(jsonObject, "gold_score");
                buycount = get(jsonObject, "buycount");
                imgurl = get(jsonObject, "imgurl");
                imgurlbig = get(jsonObject, "imgurlbig");
                bill_id = get(jsonObject, "bill_id");
                bill_sn = get(jsonObject, "bill_sn");
                regdate = get(jsonObject, "regdate");
                expressfee = get(jsonObject, "expressfee");
                shipping_fee = get(jsonObject, "shipping_fee");
                memo = get(jsonObject, "memo");
                reply_id = get(jsonObject, "reply_id");
                rebateflag = get(jsonObject, "rebateflag");
                spec_name = get(jsonObject, "spec_name");
                servicetype = get(jsonObject, "servicetype");
                itemtype = get(jsonObject, "itemtype");
                reason = get(jsonObject, "reason");
                seller_name = get(jsonObject, "seller_name");
                applydate = get(jsonObject, "applydate");
                handledate = get(jsonObject, "handledate");
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "OrderGoodInfor{" +
                "applydate='" + applydate + '\'' +
                ", id='" + id + '\'' +
                ", blog_id='" + blog_id + '\'' +
                ", client_id='" + client_id + '\'' +
                ", name='" + name + '\'' +
                ", specid='" + specid + '\'' +
                ", score='" + score + '\'' +
                ", share_score='" + share_score + '\'' +
                ", gold_score='" + gold_score + '\'' +
                ", buycount='" + buycount + '\'' +
                ", imgurl='" + imgurl + '\'' +
                ", imgurlbig='" + imgurlbig + '\'' +
                ", bill_id='" + bill_id + '\'' +
                ", bill_sn='" + bill_sn + '\'' +
                ", regdate='" + regdate + '\'' +
                ", expressfee='" + expressfee + '\'' +
                ", shipping_fee='" + shipping_fee + '\'' +
                ", memo='" + memo + '\'' +
                ", reply_id='" + reply_id + '\'' +
                ", rebateflag='" + rebateflag + '\'' +
                ", spec_name='" + spec_name + '\'' +
                ", servicetype='" + servicetype + '\'' +
                ", itemtype='" + itemtype + '\'' +
                ", reason='" + reason + '\'' +
                ", seller_name='" + seller_name + '\'' +
                ", replytype='" + replytype + '\'' +
                ", handledate='" + handledate + '\'' +
                '}';
    }

    public String getApplydate() {
        return applydate;
    }

    public String getHandledate() {
        return handledate;
    }

    public String getReplytype() {
        return replytype;
    }

    public String getBill_id() {
        return bill_id;
    }

    public String getBill_sn() {
        return bill_sn;
    }

    public String getBlog_id() {
        return blog_id;
    }

    public String getBuycount() {
        return buycount;
    }

    public String getClient_id() {
        return client_id;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public String getExpressfee() {
        return expressfee;
    }

    public String getSpec_name() {
        return spec_name;
    }

    public String getServicetype() {
        return servicetype;
    }

    public String getItemtype() {
        return itemtype;
    }

    public String getReason() {
        return reason;
    }

    public String getGold_score() {
        return gold_score;
    }

    public String getId() {
        return id;
    }

    public String getImgurl() {
        return imgurl;
    }

    public String getImgurlbig() {
        return imgurlbig;
    }

    public String getMemo() {
        return memo;
    }

    public String getName() {
        return name;
    }

    public String getRebateflag() {
        return rebateflag;
    }

    public String getRegdate() {
        return regdate;
    }

    public String getReply_id() {
        return reply_id;
    }

    public String getScore() {
        return score;
    }

    public String getShare_score() {
        return share_score;
    }

    public String getShipping_fee() {
        return shipping_fee;
    }

    public String getSpecid() {
        return specid;
    }
}
