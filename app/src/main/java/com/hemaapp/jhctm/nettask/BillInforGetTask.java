package com.hemaapp.jhctm.nettask;

import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.jhctm.JhHttpInformation;
import com.hemaapp.jhctm.JhNetTask;
import com.hemaapp.jhctm.model.OrderDetailInfor;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * Created by WangYuxia on 2017/2/13.
 */

public class BillInforGetTask extends JhNetTask {
    public BillInforGetTask(JhHttpInformation information,
                       HashMap<String, String> params) {
        super(information, params);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new Result(jsonObject);
    }

    private class Result extends HemaArrayResult<OrderDetailInfor> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public OrderDetailInfor parse(JSONObject jsonObject) throws DataParseException {
            return new OrderDetailInfor(jsonObject);
        }
    }
}

