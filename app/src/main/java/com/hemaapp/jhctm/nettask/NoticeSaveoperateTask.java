package com.hemaapp.jhctm.nettask;

import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.jhctm.JhHttpInformation;
import com.hemaapp.jhctm.JhNetTask;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

public class NoticeSaveoperateTask extends JhNetTask {

	public NoticeSaveoperateTask(JhHttpInformation information,
			HashMap<String, String> params) {
		super(information, params);
	}

	public NoticeSaveoperateTask(JhHttpInformation information,
								 HashMap<String, String> params, HashMap<String, String> files) {
		super(information, params, files);
	}

	@Override
	public Object parse(JSONObject jsonObject) throws DataParseException {
		return new HemaBaseResult(jsonObject);
	}

}
