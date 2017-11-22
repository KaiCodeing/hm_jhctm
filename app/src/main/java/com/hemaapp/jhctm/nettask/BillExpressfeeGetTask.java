package com.hemaapp.jhctm.nettask;

import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.jhctm.JhHttpInformation;
import com.hemaapp.jhctm.JhNetTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2017/2/8.
 * 获取确认订单运费
 */
public class BillExpressfeeGetTask extends JhNetTask {

    public BillExpressfeeGetTask(JhHttpInformation information,
                       HashMap<String, String> params) {
        super(information, params);
    }

    public BillExpressfeeGetTask(JhHttpInformation information,
                       HashMap<String, String> params, HashMap<String, String> files) {
        super(information, params, files);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new Result(jsonObject);
    }
    private class Result extends HemaArrayResult<String> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public String parse(JSONObject jsonObject) throws DataParseException {
            try {
                return get(jsonObject, "shipping_fee");
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }

    }
}
