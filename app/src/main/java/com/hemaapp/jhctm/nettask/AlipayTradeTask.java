package com.hemaapp.jhctm.nettask;

import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.jhctm.JhHttpInformation;
import com.hemaapp.jhctm.JhNetTask;
import com.hemaapp.jhctm.model.AlipayTrade;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * ��ȡ֧��������ǩ��
 */
public class AlipayTradeTask extends JhNetTask {

	public AlipayTradeTask(JhHttpInformation information,
			HashMap<String, String> params) {
		super(information, params);
	}

	public AlipayTradeTask(JhHttpInformation information,
			HashMap<String, String> params, HashMap<String, String> files) {
		super(information, params, files);
	}

	@Override
	public Object parse(JSONObject jsonObject) throws DataParseException {
		return new Result(jsonObject);
	}

	private class Result extends HemaArrayResult<AlipayTrade> {

		public Result(JSONObject jsonObject) throws DataParseException {
			super(jsonObject);
		}

		@Override
		public AlipayTrade parse(JSONObject jsonObject)
				throws DataParseException {
			return new AlipayTrade(jsonObject);
		}

	}
}
