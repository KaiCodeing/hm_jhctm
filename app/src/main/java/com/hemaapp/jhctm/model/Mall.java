package com.hemaapp.jhctm.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2017/1/9.
 *超市
 */
public class Mall extends XtomObject implements Serializable {
    private String id;//
    private String nickname;
    private String charindex;
    private String address;

    public Mall(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                nickname = get(jsonObject, "nickname");
                charindex = get(jsonObject, "charindex");
                address = get(jsonObject,"district_name");
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "Mall{" +
                "address='" + address + '\'' +
                ", id='" + id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", charindex='" + charindex + '\'' +
                '}';
    }

    public String getAddress() {
        return address;
    }

    public String getCharindex() {
        return charindex;
    }

    public String getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }
}
