package com.hemaapp.jhctm.nettask;

import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.jhctm.JhHttpInformation;
import com.hemaapp.jhctm.JhNetTask;
import com.hemaapp.jhctm.model.Cart;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2017/2/7.
 * 购物车
 */
public class CartListTask extends JhNetTask {
    public CartListTask(JhHttpInformation information,
                        HashMap<String, String> params) {
        super(information, params);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new Result(jsonObject);
    }

    private class Result extends HemaArrayResult<Cart> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public Cart parse(JSONObject jsonObject) throws DataParseException {
            return new Cart(jsonObject);
        }

    }
}
