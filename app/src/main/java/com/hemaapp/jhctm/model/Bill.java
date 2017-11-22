package com.hemaapp.jhctm.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2017/1/18.
 * 订单提交之后的返回数据
 */
public class Bill extends XtomObject implements Serializable {
    private String bill_ids;
    private String total_fee;
    private String share_score;
    private String gold_score;
    public Bill(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                bill_ids = get(jsonObject,"bill_ids");
                total_fee = get(jsonObject, "total_fee");
                share_score = get(jsonObject, "share_score");
                gold_score = get(jsonObject, "gold_score");
                //trade_branch_name= get(jsonObject, "trade_branch_name");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public String getGold_scor() {
        return gold_score;
    }

    public String getBill_ids() {
        return bill_ids;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public String getShare_fee() {
        return share_score;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "bill_ids='" + bill_ids + '\'' +
                ", total_fee='" + total_fee + '\'' +
                ", share_fee='" + share_score + '\'' +
                ", gold_scor='" + gold_score + '\'' +
                '}';
    }
}
