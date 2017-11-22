package com.hemaapp.jhctm.model;

import com.hemaapp.hm_FrameWork.model.Image;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2017/1/11.
 * 商品详情
 */
public class Blog extends XtomObject implements Serializable {
    private String id;//
    private String name;//
    private String score;//积分
    private String share_score;//
    private String gold_score;//
    private String salecount;//
    private String imgurl;//
    private String imgurlbig;//
    private ArrayList<Image> imgItems;//
    private String collectflag;//
    private String client_id;//
    private String keytype;//
    private String good_score;//
    private String leftcount;//
    private String rebatflag;//
    private String expressfee;//
    private String modid;//
    private String typeid;//
    private String parant_typeid;//
    private String saleflag;//
    private String topflag;//
    private String content;//
    private String validflag;
    private ArrayList<Spec> specItems;//
    private ArrayList<City> sellerItems;
    private String replycount;

    public Blog(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                name = get(jsonObject, "name");
                score = get(jsonObject, "score");
                share_score = get(jsonObject, "share_score");
                gold_score = get(jsonObject, "gold_score");
                salecount = get(jsonObject, "salecount");
                imgurl = get(jsonObject, "imgurl");
                imgurlbig = get(jsonObject, "imgurlbig");
                collectflag = get(jsonObject, "collectflag");
                keytype = get(jsonObject, "keytype");
                good_score = get(jsonObject, "good_score");
                leftcount = get(jsonObject, "leftcount");
                rebatflag = get(jsonObject, "rebatflag");
                expressfee = get(jsonObject, "expressfee");
                modid = get(jsonObject, "modid");
                client_id = get(jsonObject,"client_id");
                typeid = get(jsonObject, "typeid");
                parant_typeid = get(jsonObject, "parant_typeid");
                saleflag = get(jsonObject, "saleflag");
                topflag = get(jsonObject, "topflag");
                content = get(jsonObject, "content");
                validflag = get(jsonObject,"validflag");
                replycount = get(jsonObject,"replycount");
                if (!jsonObject.isNull("imgItems")
                        && !isNull(jsonObject.getString("imgItems"))) {
                    JSONArray jsonList = jsonObject.getJSONArray("imgItems");
                    int size = jsonList.length();
                    imgItems = new ArrayList<Image>();
                    for (int i = 0; i < size; i++) {
                        imgItems.add(new Image(jsonList.getJSONObject(i)));
                    }
                }
                if (!jsonObject.isNull("specItems")
                        && !isNull(jsonObject.getString("specItems"))) {
                    JSONArray jsonList = jsonObject.getJSONArray("specItems");
                    int size = jsonList.length();
                    specItems = new ArrayList<Spec>();
                    for (int i = 0; i < size; i++) {
                        specItems.add(new Spec(jsonList.getJSONObject(i)));
                    }
                }
                if (!jsonObject.isNull("sellerItems")
                        && !isNull(jsonObject.getString("sellerItems"))) {
                    JSONArray jsonList = jsonObject.getJSONArray("sellerItems");
                    int size = jsonList.length();
                    sellerItems = new ArrayList<City>();
                    for (int i = 0; i < size; i++) {
                        sellerItems.add(new City(jsonList.getJSONObject(i)));
                    }
                }
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    public String getReplycount() {
        return replycount;
    }

    public ArrayList<City> getSellerItems() {
        return sellerItems;
    }

    public String getContent() {
        return content;
    }

    public String getValidflag() {
        return validflag;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "client_id='" + client_id + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", score='" + score + '\'' +
                ", share_score='" + share_score + '\'' +
                ", gold_score='" + gold_score + '\'' +
                ", salecount='" + salecount + '\'' +
                ", imgurl='" + imgurl + '\'' +
                ", imgurlbig='" + imgurlbig + '\'' +
                ", imgItems=" + imgItems +
                ", collectflag='" + collectflag + '\'' +
                ", keytype='" + keytype + '\'' +
                ", good_score='" + good_score + '\'' +
                ", leftcount='" + leftcount + '\'' +
                ", rebatflag='" + rebatflag + '\'' +
                ", expressfee='" + expressfee + '\'' +
                ", modid='" + modid + '\'' +
                ", typeid='" + typeid + '\'' +
                ", parant_typeid='" + parant_typeid + '\'' +
                ", saleflag='" + saleflag + '\'' +
                ", topflag='" + topflag + '\'' +
                ", content='" + content + '\'' +
                ", validflag='" + validflag + '\'' +
                ", specItems=" + specItems +
                ", sellerItems=" + sellerItems +
                ", replycount='" + replycount + '\'' +
                '}';
    }

    public void setCollectflag(String collectflag) {
        this.collectflag = collectflag;
    }

    public String getClient_id() {
        return client_id;
    }

    public String getCollectflag() {
        return collectflag;
    }

    public String getExpressfee() {
        return expressfee;
    }

    public String getGold_score() {
        return gold_score;
    }

    public String getGood_score() {
        return good_score;
    }

    public String getId() {
        return id;
    }

    public ArrayList<Image> getImgItems() {
        return imgItems;
    }

    public String getImgurl() {
        return imgurl;
    }

    public String getImgurlbig() {
        return imgurlbig;
    }

    public String getKeytype() {
        return keytype;
    }

    public String getLeftcount() {
        return leftcount;
    }

    public String getModid() {
        return modid;
    }

    public String getName() {
        return name;
    }

    public String getParant_typeid() {
        return parant_typeid;
    }

    public String getRebatflag() {
        return rebatflag;
    }

    public String getSalecount() {
        return salecount;
    }

    public String getSaleflag() {
        return saleflag;
    }

    public String getScore() {
        return score;
    }

    public String getShare_score() {
        return share_score;
    }

    public ArrayList<Spec> getSpecItems() {
        return specItems;
    }

    public String getTopflag() {
        return topflag;
    }

    public String getTypeid() {
        return typeid;
    }
}
