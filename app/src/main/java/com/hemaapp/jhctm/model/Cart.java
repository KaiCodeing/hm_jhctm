package com.hemaapp.jhctm.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2017/2/7.
 * 购物车一级
 */
public class Cart extends XtomObject implements Serializable {
    private String seller_id;
    private String seller_name;
    private String total_fee;
    private ArrayList<CartItems> childItems;
    public Cart(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                seller_id = get(jsonObject, "seller_id");
                seller_name = get(jsonObject, "seller_name");
                total_fee = get(jsonObject, "total_fee");
                if (!jsonObject.isNull("childItems")
                        && !isNull(jsonObject.getString("childItems"))) {
                    JSONArray jsonList = jsonObject.getJSONArray("childItems");
                    int size = jsonList.length();
                    childItems = new ArrayList<CartItems>();
                    for (int i = 0; i < size; i++) {
                        childItems.add(new CartItems(jsonList.getJSONObject(i)));
                    }
                }
                //trade_branch_name= get(jsonObject, "trade_branch_name");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        return "Cart{" +
                "childItems=" + childItems +
                ", seller_id='" + seller_id + '\'' +
                ", seller_name='" + seller_name + '\'' +
                ", total_fee='" + total_fee + '\'' +
                '}';
    }

    public ArrayList<CartItems> getChildItems() {
        return childItems;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public String getTotal_fee() {
        return total_fee;
    }
}
