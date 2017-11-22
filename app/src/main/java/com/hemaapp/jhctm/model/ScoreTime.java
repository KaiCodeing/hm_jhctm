package com.hemaapp.jhctm.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2017/2/13.
 */

public class ScoreTime extends XtomObject implements Serializable {
    private String id;//主键id

    private String client_id;//用户id
    private String keytype;//类型1购物;2购买平台积分;3兑换成兑换金积分;4合成卡;6购买用户积分;7:积分返还;21推荐直奖;22拆卡返还;23卖商品赚取score积分数
    private String regdate;
    private String regdate1;
        private String score;
    private String leftscore;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getKeytype() {
        return keytype;
    }

    public void setKeytype(String keytype) {
        this.keytype = keytype;
    }

    public String getRegdate() {
        return regdate;
    }

    public String getRegdate1() {
        return regdate1;
    }

    public String getLeftscore() {
        return leftscore;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }

    public String getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "ScoreTime{" +
                "client_id='" + client_id + '\'' +
                ", id='" + id + '\'' +
                ", keytype='" + keytype + '\'' +
                ", regdate='" + regdate + '\'' +
                ", regdate1='" + regdate1 + '\'' +
                ", score='" + score + '\'' +
                '}';
    }

    public ScoreTime(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                client_id = get(jsonObject, "client_id");
                keytype = get(jsonObject, "keytype");
                regdate = get(jsonObject,"regdate");
                score = get(jsonObject,"score");
                regdate1 = get(jsonObject,"regdate1");
                leftscore = get(jsonObject,"leftscore");
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }
}
