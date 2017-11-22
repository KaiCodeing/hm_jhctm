package com.hemaapp.jhctm.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2017/2/5.
 * 我的卡包
 */
public class Card extends XtomObject implements Serializable {
    private String id;
    private String client_id;//用户id
    private String sn;//编号
    private String saleflag;//出售状态	0未出售；1出售中
    private String regdate;//合成时间
    private String username;//卖家账号
    private String nickname;//卖家名称
    public Card(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                client_id = get(jsonObject, "client_id");
                sn = get(jsonObject, "sn");
                saleflag = get(jsonObject, "saleflag");
                regdate = get(jsonObject, "regdate");
                username = get(jsonObject, "username");
                nickname = get(jsonObject, "nickname");
                //trade_branch_name= get(jsonObject, "trade_branch_name");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        return "Card{" +
                "client_id='" + client_id + '\'' +
                ", id='" + id + '\'' +
                ", sn='" + sn + '\'' +
                ", saleflag='" + saleflag + '\'' +
                ", regdate='" + regdate + '\'' +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }

    public String getNickname() {
        return nickname;
    }

    public String getUsername() {
        return username;
    }

    public String getClient_id() {
        return client_id;
    }

    public String getId() {
        return id;
    }

    public String getRegdate() {
        return regdate;
    }

    public String getSaleflag() {
        return saleflag;
    }

    public String getSn() {
        return sn;
    }
}
