package com.hemaapp.jhctm.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2017/1/19.
 * 提现银行
 */
public class Bank extends XtomObject implements Serializable {
    private String bankuser;//	银行卡用户名
    private String bankname;//		银行名称
    private String bankcard;//		银行卡号
    private String bankaddress;//		开户行名称
    private String defaultflag;//默认标志	0否;1是

    private String id;//银行卡id
    private String client_id;//用户id
    private String name;
    private boolean check;

    public Bank(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                client_id = get(jsonObject, "client_id");
                bankuser = get(jsonObject, "bankuser");
                bankname = get(jsonObject, "bankname");
                bankcard = get(jsonObject, "bankcard");
                bankaddress = get(jsonObject, "bankaddress");
                defaultflag = get(jsonObject, "defaultflag");
                name = get(jsonObject, "name");
                if ("0".equals(defaultflag))
                    check = false;
                else
                    check = true;
                //trade_branch_name= get(jsonObject, "trade_branch_name");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        return "Bank{" +
                "bankaddress='" + bankaddress + '\'' +
                ", bankuser='" + bankuser + '\'' +
                ", bankname='" + bankname + '\'' +
                ", bankcard='" + bankcard + '\'' +
                ", defaultflag='" + defaultflag + '\'' +
                ", id='" + id + '\'' +
                ", client_id='" + client_id + '\'' +
                ", name='" + name + '\'' +
                ", check=" + check +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getBankaddress() {
        return bankaddress;
    }

    public String getBankcard() {
        return bankcard;
    }

    public String getBankname() {
        return bankname;
    }

    public String getBankuser() {
        return bankuser;
    }

    public boolean isCheck() {
        return check;
    }

    public String getClient_id() {
        return client_id;
    }

    public String getDefaultflag() {
        return defaultflag;
    }

    public String getId() {
        return id;
    }
}
