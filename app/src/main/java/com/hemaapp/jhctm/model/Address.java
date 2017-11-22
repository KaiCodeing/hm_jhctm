package com.hemaapp.jhctm.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2017/1/17.
 *收货地址列表
 */
public class Address extends XtomObject implements Serializable{
    private String id;//
    private String client_id;//
    private String name;//积分
    private String tel;//
    private String province_id;//
    private String city_id;//
    private String namepath;//
    private String district_id;//
    private String position;//
    private String position2;//
    private String address;//
    private String zipcode;//
    private String defaultflag;//
    public Address(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                client_id = get(jsonObject, "client_id");
                name = get(jsonObject, "name");
                tel = get(jsonObject, "tel");
                province_id = get(jsonObject, "province_id");
                city_id = get(jsonObject, "city_id");
                namepath = get(jsonObject, "namepath");
                district_id = get(jsonObject, "district_id");
                position = get(jsonObject, "position");
                position2 = get(jsonObject, "position2");
                address = get(jsonObject,"address");
                zipcode = get(jsonObject,"zipcode");
                defaultflag = get(jsonObject,"defaultflag");
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "Address{" +
                "address='" + address + '\'' +
                ", id='" + id + '\'' +
                ", client_id='" + client_id + '\'' +
                ", name='" + name + '\'' +
                ", tel='" + tel + '\'' +
                ", province_id='" + province_id + '\'' +
                ", city_id='" + city_id + '\'' +
                ", namepath='" + namepath + '\'' +
                ", district_id='" + district_id + '\'' +
                ", position='" + position + '\'' +
                ", position2='" + position2 + '\'' +
                ", zipcode='" + zipcode + '\'' +
                ", defaultflag='" + defaultflag + '\'' +
                '}';
    }

    public String getAddress() {
        return address;
    }

    public String getCity_id() {
        return city_id;
    }

    public String getClient_id() {
        return client_id;
    }

    public String getDefaultflag() {
        return defaultflag;
    }

    public String getDistrict_id() {
        return district_id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNamepath() {
        return namepath;
    }

    public String getPosition2() {
        return position2;
    }

    public String getPosition() {
        return position;
    }

    public String getProvince_id() {
        return province_id;
    }

    public String getTel() {
        return tel;
    }

    public String getZipcode() {
        return zipcode;
    }
}
