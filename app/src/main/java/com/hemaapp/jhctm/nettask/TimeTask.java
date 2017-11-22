package com.hemaapp.jhctm.nettask;

import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.jhctm.JhHttpInformation;
import com.hemaapp.jhctm.JhNetTask;
import com.hemaapp.jhctm.model.ScoreTime;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

public class TimeTask extends JhNetTask {

    public TimeTask(JhHttpInformation information,
                      HashMap<String, String> params) {
        super(information, params);
    }
    public TimeTask(JhHttpInformation information,
                      HashMap<String, String> params, HashMap<String, String> files) {

        super(information, params, files);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new Result(jsonObject);
    }

    private class Result extends HemaPageArrayResult<ScoreTime> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public ScoreTime parse(JSONObject jsonObject) throws DataParseException {
            return new ScoreTime(jsonObject);
        }

    }
}