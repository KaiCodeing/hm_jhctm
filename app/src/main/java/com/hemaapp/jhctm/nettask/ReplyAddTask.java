package com.hemaapp.jhctm.nettask;

import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.jhctm.JhHttpInformation;
import com.hemaapp.jhctm.JhNetTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * Created by WangYuxia on 2017/2/14.
 */

public class ReplyAddTask extends JhNetTask {

    public ReplyAddTask(JhHttpInformation information,
                         HashMap<String, String> params) {
        super(information, params);
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
                return get(jsonObject, "reply_id");
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }

    }
}

