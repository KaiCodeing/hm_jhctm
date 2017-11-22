package com.hemaapp.jhctm.nettask;

import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.jhctm.JhHttpInformation;
import com.hemaapp.jhctm.JhNetTask;
import com.hemaapp.jhctm.model.City;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2017/1/12.
 * 品牌商列表
 */
public class MerchantListTask extends JhNetTask {
    public MerchantListTask(JhHttpInformation information,
                         HashMap<String, String> params) {
        super(information, params);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new Result(jsonObject);
    }

    private class Result extends HemaPageArrayResult<City> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public City parse(JSONObject jsonObject) throws DataParseException {
            return new City(jsonObject);
        }
    }
}
