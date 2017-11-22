package com.hemaapp.jhctm.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2016/10/12.
 */
public class AdList extends XtomObject implements Serializable {
    private String id;
    private String keytype;//1：商品；2：商家；3：webview
    private String keyid;//相关id	keytype=1，商品id；keytype=2,商家id
    private String imgurl;
    private String imgurlbig;
    private String blogtype;//1易物兑;2限时兑;3分享专区
    private String client_id;//1平台自营；非1品牌商
    private String shareflag;
    public AdList(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                keytype = get(jsonObject, "keytype");
                keyid = get(jsonObject, "keyid");

                blogtype = get(jsonObject, "blogtype");
                imgurlbig = get(jsonObject, "imgurlbig");
                imgurl = get(jsonObject, "imgurl");
                client_id = get(jsonObject, "client_id");
                shareflag = get(jsonObject,"shareflag");
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "AdList{" +
                "blogtype='" + blogtype + '\'' +
                ", id='" + id + '\'' +
                ", keytype='" + keytype + '\'' +
                ", keyid='" + keyid + '\'' +
                ", imgurl='" + imgurl + '\'' +
                ", imgurlbig='" + imgurlbig + '\'' +
                ", client_id='" + client_id + '\'' +
                ", shareflag='" + shareflag + '\'' +
                '}';
    }

    public String getShareflag() {
        return shareflag;
    }

    public String getBlogtype() {
        return blogtype;
    }

    public String getClient_id() {
        return client_id;
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

    public String getKeytype() {
        return keytype;
    }
}
