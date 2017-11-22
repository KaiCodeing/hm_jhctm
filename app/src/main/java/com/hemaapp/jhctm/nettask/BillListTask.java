package com.hemaapp.jhctm.nettask;

import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.jhctm.JhHttpInformation;
import com.hemaapp.jhctm.JhNetTask;
import com.hemaapp.jhctm.model.OrderListInfor;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * Created by WangYuxia on 2017/2/10.
 */

public class BillListTask extends JhNetTask {

    public BillListTask(JhHttpInformation information,
                         HashMap<String, String> params) {
        super(information, params);
    }

    public BillListTask(JhHttpInformation information,
                         HashMap<String, String> params, HashMap<String, String> files) {
        super(information, params, files);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new Result(jsonObject);
    }

    private class Result extends HemaPageArrayResult<OrderListInfor> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public OrderListInfor parse(JSONObject jsonObject) throws DataParseException {
            return new OrderListInfor(jsonObject);
        }
    }
}

