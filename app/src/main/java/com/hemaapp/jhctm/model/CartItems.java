package com.hemaapp.jhctm.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2017/2/7.
 */
public class CartItems extends XtomObject implements Serializable {
    private String id;
    private String client_id;
    private String keyid;
    private String specid;
    private String spec_name;
    private String name;
    private String imgurl;
    private String imgurlbig;
    private String score;
    private String buycount;
    private String subtotal;
    private String regdate;
    private String enddate;
    private boolean check;
    public CartItems(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject,"id");
                client_id = get(jsonObject, "client_id");
                keyid = get(jsonObject, "keyid");
                specid = get(jsonObject, "specid");
                spec_name = get(jsonObject, "spec_name");
                name = get(jsonObject, "name");
                imgurl = get(jsonObject, "imgurl");
                imgurlbig = get(jsonObject, "imgurlbig");
                score = get(jsonObject, "score");
                buycount = get(jsonObject, "buycount");
                subtotal = get(jsonObject, "subtotal");
                regdate = get(jsonObject, "regdate");
                enddate = get(jsonObject, "enddate");
                check =false;
                //trade_branch_name= get(jsonObject, "trade_branch_name");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        return "CartItems{" +
                "buycount='" + buycount + '\'' +
                ", id='" + id + '\'' +
                ", client_id='" + client_id + '\'' +
                ", keyid='" + keyid + '\'' +
                ", specid='" + specid + '\'' +
                ", spec_name='" + spec_name + '\'' +
                ", name='" + name + '\'' +
                ", imgurl='" + imgurl + '\'' +
                ", imgurlbig='" + imgurlbig + '\'' +
                ", score='" + score + '\'' +
                ", subtotal='" + subtotal + '\'' +
                ", regdate='" + regdate + '\'' +
                ", enddate='" + enddate + '\'' +
                ", check=" + check +
                '}';
    }

    public void setBuycount(String buycount) {
        this.buycount = buycount;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getBuycount() {
        return buycount;
    }

    public String getClient_id() {
        return client_id;
    }

    public String getEnddate() {
        return enddate;
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

    public String getKeyid() {
        return keyid;
    }

    public String getName() {
        return name;
    }

    public String getRegdate() {
        return regdate;
    }

    public String getScore() {
        return score;
    }

    public String getSpec_name() {
        return spec_name;
    }

    public String getSpecid() {
        return specid;
    }

    public String getSubtotal() {
        return subtotal;
    }
}
