package com.hemaapp.jhctm.nettask;

import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.jhctm.JhHttpInformation;
import com.hemaapp.jhctm.JhNetTask;
import com.hemaapp.jhctm.model.Mall;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2017/1/9.
 * 超市列表
 */
public class MallListTask extends JhNetTask {
    public MallListTask(JhHttpInformation information,
                      HashMap<String, String> params) {
        super(information, params);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new Result(jsonObject);
    }

    private class Result extends HemaArrayResult<Mall> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public Mall parse(JSONObject jsonObject) throws DataParseException {
            return new Mall(jsonObject);
        }

    }
}
