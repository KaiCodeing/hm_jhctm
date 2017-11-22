package com.hemaapp.jhctm.model;

import org.json.JSONException;
import org.json.JSONObject;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by WangYuxia on 2017/2/13.
 */

public class FaPiaoInfor extends XtomObject {

    private String id;
    private String bills_type;
    private String bills_head;
    private String bills_content;

    public FaPiaoInfor(JSONObject jsonObject) throws DataParseException {
        if(jsonObject != null){
            try {
                id = get(jsonObject, "id");
                bills_type = get(jsonObject, "bills_type");
                bills_head = get(jsonObject, "bills_head");
                bills_content = get(jsonObject, "bills_content");

                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "FaPiaoInfor{" +
                "bills_content='" + bills_content + '\'' +
                ", id='" + id + '\'' +
                ", bills_type='" + bills_type + '\'' +
                ", bills_head='" + bills_head + '\'' +
                '}';
    }

    public String getBills_content() {
        return bills_content;
    }

    public String getBills_head() {
        return bills_head;
    }

    public String getBills_type() {
        return bills_type;
    }

    public String getId() {
        return id;
    }
}
