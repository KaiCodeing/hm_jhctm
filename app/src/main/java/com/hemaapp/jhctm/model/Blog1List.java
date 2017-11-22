package com.hemaapp.jhctm.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2017/1/10.
 * 易物兑商品列表接口
 */
public class Blog1List extends XtomObject implements Serializable {
    private String id;
    private String keytype;//1易物兑；2限时兑；3分享专区
    private String name;//相关id	keytype=1，商品id；keytype=2,商家id
    private String score;//商品名称
    private String salecount;//已售数量
    private String good_score;//好评率
    private String client_id;//1平台自营；非1：品牌商专营
    private String imgurl;
    private String imgurlbig;
    private String rebatflag;
    private String gold_score;
    private String leftcount;//库存
    private String starttime;
    private String content;
    private String keyid;
    private boolean check;
    private String share_score;
    public Blog1List(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                keytype = get(jsonObject, "keytype");
                name = get(jsonObject, "name");
                gold_score = get(jsonObject, "gold_score");
                score = get(jsonObject, "score");
                salecount = get(jsonObject, "salecount");
                good_score = get(jsonObject, "good_score");
                client_id = get(jsonObject, "client_id");
                imgurl = get(jsonObject, "imgurl");
                imgurlbig = get(jsonObject, "imgurlbig");
                rebatflag = get(jsonObject,"rebatflag");
                leftcount = get(jsonObject,"leftcount");
                starttime = get(jsonObject,"starttime");
                content = get(jsonObject,"content");
                keyid = get(jsonObject,"keyid");
                share_score = get(jsonObject,"share_score");
                check=false;
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    public boolean isCheck() {
        return check;
    }

    public String getShare_score() {
        return share_score;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    @Override
    public String toString() {
        return "Blog1List{" +
                "check=" + check +
                ", id='" + id + '\'' +
                ", keytype='" + keytype + '\'' +
                ", name='" + name + '\'' +
                ", score='" + score + '\'' +
                ", salecount='" + salecount + '\'' +
                ", good_score='" + good_score + '\'' +
                ", client_id='" + client_id + '\'' +
                ", imgurl='" + imgurl + '\'' +
                ", imgurlbig='" + imgurlbig + '\'' +
                ", rebatflag='" + rebatflag + '\'' +
                ", gold_score='" + gold_score + '\'' +
                ", leftcount='" + leftcount + '\'' +
                ", starttime='" + starttime + '\'' +
                ", content='" + content + '\'' +
                ", keyid='" + keyid + '\'' +
                ", share_score='" + share_score + '\'' +
                '}';
    }

    public String getKeyid() {
        return keyid;
    }

    public String getContent() {
        return content;
    }

    public String getImgurl() {
        return imgurl;
    }

    public String getImgurlbig() {
        return imgurlbig;
    }

    public String getGold_score() {
        return gold_score;
    }

    public String getRebatflag() {
        return rebatflag;
    }

    public String getLeftcount() {
        return leftcount;
    }

    public String getStarttime() {
        return starttime;
    }

    public String getClient_id() {
        return client_id;
    }

    public String getGood_score() {
        return good_score;
    }

    public String getId() {
        return id;
    }

    public String getKeytype() {
        return keytype;
    }

    public String getName() {
        return name;
    }

    public String getSalecount() {
        return salecount;
    }

    public String getScore() {
        return score;
    }
}
