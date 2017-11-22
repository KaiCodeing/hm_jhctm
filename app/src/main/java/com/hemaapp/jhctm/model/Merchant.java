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
 * Created by lenovo on 2017/1/12.
 * 品牌商详情
 */
public class Merchant extends XtomObject implements Serializable {
    private String id;//
    private String name;
    private String bail;
    private String content;//
    private ArrayList<City> typeItems;
    private ArrayList<Image> imgItems;
    public Merchant(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                name = get(jsonObject, "nickname");
                bail = get(jsonObject, "bail");
                content = get(jsonObject, "content");
                if (!jsonObject.isNull("typeItems")
                        && !isNull(jsonObject.getString("typeItems"))) {
                    JSONArray jsonList = jsonObject.getJSONArray("typeItems");
                    int size = jsonList.length();
                    typeItems = new ArrayList<City>();
                    for (int i = 0; i < size; i++) {
                        typeItems.add(new City(jsonList.getJSONObject(i)));
                    }
                }
                if (!jsonObject.isNull("imgItems")
                        && !isNull(jsonObject.getString("imgItems"))) {
                    JSONArray jsonList = jsonObject.getJSONArray("imgItems");
                    int size = jsonList.length();
                    imgItems = new ArrayList<Image>();
                    for (int i = 0; i < size; i++) {
                        imgItems.add(new Image(jsonList.getJSONObject(i)));
                    }
                }
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "Merchant{" +
                "bail='" + bail + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", typeItems=" + typeItems +
                ", imgItems=" + imgItems +
                '}';
    }

    public ArrayList<Image> getImgItems() {
        return imgItems;
    }

    public String getBail() {
        return bail;
    }

    public String getContent() {
        return content;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<City> getTypeItems() {
        return typeItems;
    }
}
