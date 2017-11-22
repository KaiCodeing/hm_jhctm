package com.hemaapp.jhctm.nettask;

import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.jhctm.JhHttpInformation;
import com.hemaapp.jhctm.JhNetTask;
import com.hemaapp.jhctm.model.Rebat;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2017/4/12.
 * 冻结积分
 */
public class GoldRebatListTask  extends JhNetTask{
    public GoldRebatListTask(JhHttpInformation information,
                            HashMap<String, String> params) {
        super(information, params);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new Result(jsonObject);
    }

    private class Result extends HemaPageArrayResult<Rebat> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public Rebat parse(JSONObject jsonObject) throws DataParseException {
            return new Rebat(jsonObject);
        }
    }
}
