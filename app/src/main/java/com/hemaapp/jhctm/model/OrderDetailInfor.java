package com.hemaapp.jhctm.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by WangYuxia on 2017/2/13.
 */
public class OrderDetailInfor extends XtomObject {
    private String id;
    private String client_id;
    private String bill_sn;
    private String tradetype;
    private String statetype;
    private String sendflag;
    private String returnflag;
    private String consignee;
    private String phone;
    private String address;
    private String shipping_name;
    private String shipping_num;
    private String regdate;
    private String senddate;
    private String total_fee;
    private String goods_score;
    private String goods_share_score;
    private String goods_gold_score;
    private String shipping_fee;
    private String recvtype;
    private String total_buycount;
    private ArrayList<OrderGoodInfor> childItems;
    private ArrayList<FaPiaoInfor> billsItems;
    private String seller_name;
    private String costcode;
    private String mall_name;//自提超市名称
    private String mall_address;//自提详细地址

    public String getMall_name() {
        return mall_name;
    }

    public void setMall_name(String mall_name) {
        this.mall_name = mall_name;
    }

    public String getMall_address() {
        return mall_address;
    }

    public void setMall_address(String mall_address) {
        this.mall_address = mall_address;
    }

    public OrderDetailInfor(JSONObject jsonObject) throws DataParseException {
        if(jsonObject != null){
            try {
                id = get(jsonObject, "id");
                client_id = get(jsonObject, "client_id");
                bill_sn = get(jsonObject, "bill_sn");
                tradetype = get(jsonObject, "tradetype");
                statetype = get(jsonObject, "statetype");
                sendflag = get(jsonObject, "sendflag");
                returnflag = get(jsonObject, "returnflag");
                consignee = get(jsonObject, "consignee");
                phone = get(jsonObject, "phone");

                address = get(jsonObject, "address");
                shipping_name = get(jsonObject, "shipping_name");
                shipping_num = get(jsonObject, "shipping_num");
                regdate = get(jsonObject, "regdate");
                senddate = get(jsonObject, "senddate");
                total_fee = get(jsonObject, "total_fee");
                goods_score = get(jsonObject, "goods_score");
                goods_share_score = get(jsonObject, "goods_share_score");
                goods_gold_score = get(jsonObject, "goods_gold_score");
                shipping_fee = get(jsonObject, "shipping_fee");
                recvtype = get(jsonObject, "recvtype");
                total_buycount = get(jsonObject, "total_buycount");
                seller_name = get(jsonObject, "seller_name");
                costcode = get(jsonObject, "costcode");
                mall_name=get(jsonObject, "mall_name");
                mall_address=get(jsonObject, "mall_address");
                if( !jsonObject.isNull("childItems") &&
                        !isNull(jsonObject.getString("childItems"))){
                    JSONArray jsonList = jsonObject.getJSONArray("childItems");
                    int size = jsonList.length();
                    childItems = new ArrayList<>();
                    for (int i = 0; i < size; i++) {
                        childItems.add(new OrderGoodInfor(jsonList.getJSONObject(i)));
                    }
                }
                if( !jsonObject.isNull("billsItems") &&
                        !isNull(jsonObject.getString("billsItems"))){
                    JSONArray jsonList = jsonObject.getJSONArray("billsItems");
                    int size = jsonList.length();
                    billsItems = new ArrayList<>();
                    for (int i = 0; i < size; i++) {
                        billsItems.add(new FaPiaoInfor(jsonList.getJSONObject(i)));
                    }
                }

                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "OrderDetailInfor{" +
                "address='" + address + '\'' +
                ", id='" + id + '\'' +
                ", client_id='" + client_id + '\'' +
                ", bill_sn='" + bill_sn + '\'' +
                ", tradetype='" + tradetype + '\'' +
                ", statetype='" + statetype + '\'' +
                ", sendflag='" + sendflag + '\'' +
                ", returnflag='" + returnflag + '\'' +
                ", consignee='" + consignee + '\'' +
                ", phone='" + phone + '\'' +
                ", shipping_name='" + shipping_name + '\'' +
                ", shipping_num='" + shipping_num + '\'' +
                ", regdate='" + regdate + '\'' +
                ", senddate='" + senddate + '\'' +
                ", total_fee='" + total_fee + '\'' +
                ", goods_score='" + goods_score + '\'' +
                ", goods_share_score='" + goods_share_score + '\'' +
                ", goods_gold_score='" + goods_gold_score + '\'' +
                ", shipping_fee='" + shipping_fee + '\'' +
                ", recvtype='" + recvtype + '\'' +
                ", total_buycount='" + total_buycount + '\'' +
                ", childItems=" + childItems +
                ", billsItems=" + billsItems +
                ", seller_name='" + seller_name + '\'' +
                ", costcode='" + costcode + '\'' +
                '}';
    }

    public String getAddress() {
        return address;
    }

    public String getBill_sn() {
        return bill_sn;
    }

    public ArrayList<FaPiaoInfor> getBillsItems() {
        return billsItems;
    }

    public ArrayList<OrderGoodInfor> getChildItems() {
        return childItems;
    }

    public String getClient_id() {
        return client_id;
    }

    public String getConsignee() {
        return consignee;
    }

    public String getGoods_gold_score() {
        return goods_gold_score;
    }

    public String getGoods_score() {
        return goods_score;
    }

    public String getGoods_share_score() {
        return goods_share_score;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public String getCostcode() {
        return costcode;
    }

    public String getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public String getRecvtype() {
        return recvtype;
    }

    public String getRegdate() {
        return regdate;
    }

    public String getReturnflag() {
        return returnflag;
    }

    public String getSenddate() {
        return senddate;
    }

    public String getSendflag() {
        return sendflag;
    }

    public String getShipping_fee() {
        return shipping_fee;
    }

    public String getShipping_name() {
        return shipping_name;
    }

    public String getShipping_num() {
        return shipping_num;
    }

    public String getStatetype() {
        return statetype;
    }

    public String getTotal_buycount() {
        return total_buycount;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public String getTradetype() {
        return tradetype;
    }
}
