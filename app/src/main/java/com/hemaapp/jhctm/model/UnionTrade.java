package com.hemaapp.jhctm.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * ��������ǩ����
 */
public class UnionTrade extends XtomObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String respCode;// ��Ӧ�� �ɹ�ʱΪ��00��
	private String respMsg;// ���������Ϣ���� ��respCode<>��00��ʱ�Ż᷵�ش��ֶ�
	private String tn;// ����������ˮ�� �ǳ���Ҫ���ͻ�����Ҫ�õ�
	private String reqReserved;// �ҷ����������׵���

	public UnionTrade(JSONObject jsonObject) throws DataParseException {
		if (jsonObject != null) {
			try {
				respCode = get(jsonObject, "respCode");
				respMsg = get(jsonObject, "respMsg");
				tn = get(jsonObject, "tn");
				reqReserved = get(jsonObject, "reqReserved");
				log_i(toString());
			} catch (JSONException e) {
				throw new DataParseException(e);
			}
		}
	}

	@Override
	public String toString() {
		return "UnionTrade [respCode=" + respCode + ", respMsg=" + respMsg
				+ ", tn=" + tn + ", reqReserved=" + reqReserved + "]";
	}

	/**
	 * @return the respCode
	 */
	public String getRespCode() {
		return respCode;
	}

	/**
	 * @return the respMsg
	 */
	public String getRespMsg() {
		return respMsg;
	}

	/**
	 * @return the tn
	 */
	public String getTn() {
		return tn;
	}

	/**
	 * @return the reqReserved
	 */
	public String getReqReserved() {
		return reqReserved;
	}

}
