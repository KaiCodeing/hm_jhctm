package com.hemaapp.jhctm.nettask;

import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.jhctm.JhHttpInformation;
import com.hemaapp.jhctm.JhNetTask;
import com.hemaapp.jhctm.model.Bank;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2017/1/19.
 * 提现银行
 */
public class ClientBankListTask extends JhNetTask {
    public ClientBankListTask(JhHttpInformation information,
                            HashMap<String, String> params) {
        super(information, params);
        // TODO Auto-generated constructor stub
    }
    public ClientBankListTask(JhHttpInformation information,
                            HashMap<String, String> params, HashMap<String, String> files) {
        super(information, params, files);
    }
    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        // TODO Auto-generated method stub
        return new Result(jsonObject);
    }
    private class Result extends HemaArrayResult<Bank> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public Bank parse(JSONObject jsonObject)
                throws DataParseException {
            return new Bank(jsonObject);
        }

    }
}
