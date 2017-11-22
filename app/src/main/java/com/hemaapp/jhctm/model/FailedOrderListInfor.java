package com.hemaapp.jhctm.model;

import org.json.JSONException;
import org.json.JSONObject;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by WangYuxia on 2017/2/10.
 */

public class FailedOrderListInfor extends XtomObject {

    private String id; //
    private String bill_sn; //
    private String keytype; //
    private String itemtype	; //
    private String name; //
    private String imgurl; //
    private String spec_name; //
    private String buycount; //
    private String score; //
    private String share_score; //
    private String gold_score; //
    private String blog_id; //
    private String bill_id;
    public FailedOrderListInfor(JSONObject jsonObject) throws DataParseException {
        if(jsonObject != null){
            try {
                id = get(jsonObject, "id");
                bill_sn = get(jsonObject, "bill_sn");
                keytype = get(jsonObject, "keytype");
                itemtype = get(jsonObject, "itemtype");
                name = get(jsonObject, "name");
                imgurl = get(jsonObject, "imgurl");
                spec_name = get(jsonObject, "spec_name");
                buycount = get(jsonObject, "buycount");
                score = get(jsonObject, "score");
                share_score = get(jsonObject, "share_score");
                gold_score = get(jsonObject, "gold_score");
                blog_id = get(jsonObject, "blog_id");
                bill_id = get(jsonObject,"bill_id");
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "FailedOrderListInfor{" +
                "bill_id='" + bill_id + '\'' +
                ", id='" + id + '\'' +
                ", bill_sn='" + bill_sn + '\'' +
                ", keytype='" + keytype + '\'' +
                ", itemtype='" + itemtype + '\'' +
                ", name='" + name + '\'' +
                ", imgurl='" + imgurl + '\'' +
                ", spec_name='" + spec_name + '\'' +
                ", buycount='" + buycount + '\'' +
                ", score='" + score + '\'' +
                ", share_score='" + share_score + '\'' +
                ", gold_score='" + gold_score + '\'' +
                ", blog_id='" + blog_id + '\'' +
                '}';
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

    public String getGold_score() {
        return gold_score;
    }

    public String getId() {
        return id;
    }

    public String getImgurl() {
        return imgurl;
    }

    public String getItemtype() {
        return itemtype;
    }

    public String getKeytype() {
        return keytype;
    }

    public String getName() {
        return name;
    }

    public String getScore() {
        return score;
    }

    public String getShare_score() {
        return share_score;
    }

    public String getSpec_name() {
        return spec_name;
    }
}
