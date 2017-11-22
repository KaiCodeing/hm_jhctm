package com.hemaapp.jhctm.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2017/4/12.
 * 冻结积分列表
 */
public class Rebat extends XtomObject implements Serializable {
    private String id;
    private String client_id;
    private String keytype;
    private String score;
    private String regdate;
    private String regdate1;
    private String totalscore;
    private String leftcount;
    private String totalcount;
    private String status;
    public Rebat(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                client_id = get(jsonObject, "client_id");
                keytype = get(jsonObject, "keytype");
                score = get(jsonObject, "score");
                regdate = get(jsonObject, "regdate");
                regdate1 = get(jsonObject, "regdate1");
                totalscore = get(jsonObject, "totalscore");
                leftcount = get(jsonObject, "leftcount");
                totalcount = get(jsonObject,"totalcount");
                status = get(jsonObject,"status");
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "Rebat{" +
                "client_id='" + client_id + '\'' +
                ", id='" + id + '\'' +
                ", keytype='" + keytype + '\'' +
                ", score='" + score + '\'' +
                ", regdate='" + regdate + '\'' +
                ", regdate1='" + regdate1 + '\'' +
                ", totalscore='" + totalscore + '\'' +
                ", leftcount='" + leftcount + '\'' +
                ", totalcount='" + totalcount + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public String getStatus() {
        return status;
    }

    public String getClient_id() {
        return client_id;
    }

    public String getId() {
        return id;
    }

    public String getKeytype() {
        return keytype;
    }

    public String getLeftcount() {
        return leftcount;
    }

    public String getRegdate1() {
        return regdate1;
    }

    public String getRegdate() {
        return regdate;
    }

    public String getScore() {
        return score;
    }

    public String getTotalcount() {
        return totalcount;
    }

    public String getTotalscore() {
        return totalscore;
    }

}
