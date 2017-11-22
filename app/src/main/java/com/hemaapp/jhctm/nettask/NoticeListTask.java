package com.hemaapp.jhctm.nettask;


import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.jhctm.JhHttpInformation;
import com.hemaapp.jhctm.JhNetTask;
import com.hemaapp.jhctm.model.Notice;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;


/**
 * ��ȡ�û�֪ͨ�б�
 */
public class NoticeListTask extends JhNetTask {

	public NoticeListTask(JhHttpInformation information,
						  HashMap<String, String> params) {
		super(information, params);
	}

	public NoticeListTask(JhHttpInformation information,
						  HashMap<String, String> params, HashMap<String, String> files) {
		super(information, params, files);
	}

	@Override
	public Object parse(JSONObject jsonObject) throws DataParseException {
		return new Result(jsonObject);
	}

	private class Result extends HemaPageArrayResult<Notice> {

		public Result(JSONObject jsonObject) throws DataParseException {
			super(jsonObject);
		}

		@Override
		public Notice parse(JSONObject jsonObject) throws DataParseException {
			return new Notice(jsonObject);
		}

	}
}
