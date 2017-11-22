package com.hemaapp.jhctm.nettask;

import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.jhctm.JhHttpInformation;
import com.hemaapp.jhctm.JhNetTask;
import com.hemaapp.jhctm.model.Card;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2017/2/5.
 * 我的卡包
 */
public class CardListTask  extends JhNetTask{
    public CardListTask(JhHttpInformation information,
                         HashMap<String, String> params) {
        super(information, params);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new Result(jsonObject);
    }

    private class Result extends HemaPageArrayResult<Card> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public Card parse(JSONObject jsonObject) throws DataParseException {
            return new Card(jsonObject);
        }

    }
}
