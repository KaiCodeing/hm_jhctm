package com.hemaapp.jhctm.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2017/2/23.
 * 积分交易详情
 */
public class ScoreBill  extends XtomObject implements Serializable {
    private String id;
    private String keytype;
    private String client_id;
    private String keyid;
    private String fee;
    private String statetype;
    private String regdate;
    private String seller_id;
    private String compeletedate;
    private String username;
    private String nickname;
    public ScoreBill(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                client_id = get(jsonObject, "client_id");
                nickname = get(jsonObject, "nickname");
                keytype = get(jsonObject, "keytype");
                keyid = get(jsonObject, "keyid");
                fee = get(jsonObject, "fee");
                regdate = get(jsonObject, "regdate");
                statetype = get(jsonObject, "statetype");
                seller_id = get(jsonObject,"seller_id");
                compeletedate = get(jsonObject, "compeletedate");
                username = get(jsonObject,"username");
                nickname = get(jsonObject,"nickname");

                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "ScoreBill{" +
                "client_id='" + client_id + '\'' +
                ", id='" + id + '\'' +
                ", keytype='" + keytype + '\'' +
                ", keyid='" + keyid + '\'' +
                ", fee='" + fee + '\'' +
                ", statetype='" + statetype + '\'' +
                ", regdate='" + regdate + '\'' +
                ", seller_id='" + seller_id + '\'' +
                ", compeletedate='" + compeletedate + '\'' +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }

    public String getClient_id() {
        return client_id;
    }

    public String getCompeletedate() {
        return compeletedate;
    }

    public String getFee() {
        return fee;
    }

    public String getId() {
        return id;
    }

    public String getKeyid() {
        return keyid;
    }

    public String getKeytype() {
        return keytype;
    }

    public String getNickname() {
        return nickname;
    }

    public String getRegdate() {
        return regdate;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public String getStatetype() {
        return statetype;
    }

    public String getUsername() {
        return username;
    }
}
