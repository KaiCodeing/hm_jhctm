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
 * Created by lenovo on 2016/10/17.
 * 评论
 */
public class Reply extends XtomObject implements Serializable {
    private String id;
    private String client_id;
    private String nickname;
    private String avatar;
    private String content;
    private String replytype;
    private String regdate;
    private String parentid;
    private String stars;
    private String parentnickname;
    private ArrayList<Image> imgItems;
    private String score;
    private String keytype;
    private String fee;
    public Reply(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                client_id = get(jsonObject, "client_id");
                nickname = get(jsonObject, "nickname");
                avatar = get(jsonObject, "avatar");
                content = get(jsonObject, "content");
                replytype = get(jsonObject, "replytype");
                regdate = get(jsonObject, "regdate");
                parentid = get(jsonObject, "parentid");
                stars = get(jsonObject,"stars");
                parentnickname = get(jsonObject, "parentnickname");
                score = get(jsonObject,"score");
                keytype = get(jsonObject,"keytype");
                fee = get(jsonObject,"fee");
                if (!jsonObject.isNull("imgItems")
                        && !isNull(jsonObject.getString("imgItems"))) {
                    JSONArray jsonList = jsonObject.getJSONArray("imgItems");
                    int size = jsonList.length();
                    imgItems = new ArrayList<Image>();
                    for (int i = 0; i < size; i++)
                        imgItems
                                .add(new Image(jsonList.getJSONObject(i)));
                }
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "Reply{" +
                "avatar='" + avatar + '\'' +
                ", id='" + id + '\'' +
                ", client_id='" + client_id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", content='" + content + '\'' +
                ", replytype='" + replytype + '\'' +
                ", regdate='" + regdate + '\'' +
                ", parentid='" + parentid + '\'' +
                ", stars='" + stars + '\'' +
                ", parentnickname='" + parentnickname + '\'' +
                ", imgItems=" + imgItems +
                ", score='" + score + '\'' +
                ", keytype='" + keytype + '\'' +
                ", fee='" + fee + '\'' +
                '}';
    }

    public String getFee() {
        return fee;
    }

    public String getKeytype() {
        return keytype;
    }

    public String getScore() {
        return score;
    }

    public String getStars() {
        return stars;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getClient_id() {
        return client_id;
    }

    public String getContent() {
        return content;
    }

    public String getId() {
        return id;
    }

    public ArrayList<Image> getImgItems() {
        return imgItems;
    }

    public String getNickname() {
        return nickname;
    }

    public String getParentid() {
        return parentid;
    }

    public String getParentnickname() {
        return parentnickname;
    }

    public String getRegdate() {
        return regdate;
    }

    public String getReplytype() {
        return replytype;
    }
}
