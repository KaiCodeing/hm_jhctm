package com.hemaapp.jhctm.nettask;

import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.jhctm.JhHttpInformation;
import com.hemaapp.jhctm.JhNetTask;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2016/10/18.
 * 收藏操作
 */
public class GoodsOperateTask extends JhNetTask {
    public GoodsOperateTask(JhHttpInformation information,
                            HashMap<String, String> params) {
        super(information, params);
    }

    public GoodsOperateTask(JhHttpInformation information,
                            HashMap<String, String> params, HashMap<String, String> files) {
        super(information, params, files);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new HemaBaseResult(jsonObject);
    }
}
