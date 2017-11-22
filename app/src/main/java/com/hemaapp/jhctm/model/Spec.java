package com.hemaapp.jhctm.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2017/1/11.
 * 规格
 */
public class Spec extends XtomObject implements Serializable {
    private String id;//
    private String blog_id;//
    private String spec_name;//
    private String leftcount;//
    private String modid;//
    private String imgurl;//
    private String imgurlbig;//
    private String score;//
    private String share_score;//
    private String gold_score;//
    private boolean check;
    public Spec(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                blog_id = get(jsonObject, "blog_id");
                spec_name = get(jsonObject, "spec_name");
                leftcount = get(jsonObject, "leftcount");
                modid = get(jsonObject, "modid");
                imgurl = get(jsonObject, "imgurl");
                imgurlbig = get(jsonObject, "imgurlbig");
                score = get(jsonObject, "score");
                share_score = get(jsonObject, "share_score");
                gold_score = get(jsonObject, "gold_score");
                check=false;
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "Spec{" +
                "blog_id='" + blog_id + '\'' +
                ", id='" + id + '\'' +
                ", spec_name='" + spec_name + '\'' +
                ", leftcount='" + leftcount + '\'' +
                ", modid='" + modid + '\'' +
                ", imgurl='" + imgurl + '\'' +
                ", imgurlbig='" + imgurlbig + '\'' +
                ", score='" + score + '\'' +
                ", share_score='" + share_score + '\'' +
                ", gold_score='" + gold_score + '\'' +
                ", check=" + check +
                '}';
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getBlog_id() {
        return blog_id;
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

    public String getLeftcount() {
        return leftcount;
    }

    public String getModid() {
        return modid;
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
