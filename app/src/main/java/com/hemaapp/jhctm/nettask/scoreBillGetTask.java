package com.hemaapp.jhctm.nettask;

import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.jhctm.JhHttpInformation;
import com.hemaapp.jhctm.JhNetTask;
import com.hemaapp.jhctm.model.ScoreBill;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2017/2/23.
 * 积分交易详情
 */
public class scoreBillGetTask extends JhNetTask {
    public scoreBillGetTask(JhHttpInformation information,
                           HashMap<String, String> params) {
        super(information, params);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new Result(jsonObject);
    }

    private class Result extends HemaArrayResult<ScoreBill> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public ScoreBill parse(JSONObject jsonObject) throws DataParseException {
            return new ScoreBill(jsonObject);
        }
    }
}
