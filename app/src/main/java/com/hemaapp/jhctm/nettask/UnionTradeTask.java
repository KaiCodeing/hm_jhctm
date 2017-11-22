package com.hemaapp.jhctm.nettask;

import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.jhctm.JhHttpInformation;
import com.hemaapp.jhctm.JhNetTask;
import com.hemaapp.jhctm.model.UnionTrade;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * ��ȡ��������ǩ����
 */
public class UnionTradeTask extends JhNetTask {

	public UnionTradeTask(JhHttpInformation information,
			HashMap<String, String> params) {
		super(information, params);
	}

	public UnionTradeTask(JhHttpInformation information,
			HashMap<String, String> params, HashMap<String, String> files) {
		super(information, params, files);
	}

	@Override
	public Object parse(JSONObject jsonObject) throws DataParseException {
		return new Result(jsonObject);
	}

	private class Result extends HemaArrayResult<UnionTrade> {

		public Result(JSONObject jsonObject) throws DataParseException {
			super(jsonObject);
		}

		@Override
		public UnionTrade parse(JSONObject jsonObject)
				throws DataParseException {
			return new UnionTrade(jsonObject);
		}

	}
}
