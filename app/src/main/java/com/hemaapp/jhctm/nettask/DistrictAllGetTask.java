package com.hemaapp.jhctm.nettask;

import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.jhctm.JhHttpInformation;
import com.hemaapp.jhctm.JhNetTask;
import com.hemaapp.jhctm.model.CitySan;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2017/1/9.
 * 所有地区
 */
public class DistrictAllGetTask extends JhNetTask {

    public DistrictAllGetTask(JhHttpInformation information,
                    HashMap<String, String> params) {
        super(information, params);
        // TODO Auto-generated constructor stub
    }
    public DistrictAllGetTask(JhHttpInformation information,
                    HashMap<String, String> params, HashMap<String, String> files) {
        super(information, params, files);
    }
    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        // TODO Auto-generated method stub
        return new Result(jsonObject);
    }
    private class Result extends HemaArrayResult<CitySan> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public CitySan parse(JSONObject jsonObject)
                throws DataParseException {
            return new CitySan(jsonObject);
        }

    }
}
