package com.hemaapp.jhctm.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

public class WeixinTrade extends XtomObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6013096570500672617L;
	private String appid;// �����˺�ID ΢�ŷ���Ĺ����˺�ID
	private String partnerid;// �̻��� ΢��֧��������̻���
	private String prepayid;// Ԥ֧�����׻ỰID ΢�ŷ��ص�֧�����׻ỰID
	private String packageValue;// ��չ�ֶ� ����д�̶�ֵSign=WXPay
	private String noncestr;// ����ַ��� ����ַ�����������32λ��
	private String timestamp;// ʱ��� ʱ���
	private String sign;// ǩ��

	public WeixinTrade(JSONObject jsonObject) throws DataParseException {
		if (jsonObject != null) {
			try {
				appid = get(jsonObject, "appid");
				partnerid = get(jsonObject, "partnerid");
				prepayid = get(jsonObject, "prepayid");
				packageValue = get(jsonObject, "package");
				noncestr = get(jsonObject, "noncestr");
				timestamp = get(jsonObject, "timestamp");
				sign = get(jsonObject, "sign");

				log_i(toString());
			} catch (JSONException e) {
				throw new DataParseException(e);
			}
		}
	}

	@Override
	public String toString() {
		return "WeixinTrade [appid=" + appid + ", partnerid=" + partnerid
				+ ", prepayid=" + prepayid + ", packageValue=" + packageValue
				+ ", noncestr=" + noncestr + ", timestamp=" + timestamp
				+ ", sign=" + sign + "]";
	}

	/**
	 * @return the appid
	 */
	public String getAppid() {
		return appid;
	}

	/**
	 * @return the partnerid
	 */
	public String getPartnerid() {
		return partnerid;
	}

	/**
	 * @return the prepayid
	 */
	public String getPrepayid() {
		return prepayid;
	}

	/**
	 * @return the packageValue
	 */
	public String getPackageValue() {
		return packageValue;
	}

	/**
	 * @return the noncestr
	 */
	public String getNoncestr() {
		return noncestr;
	}

	/**
	 * @return the timestamp
	 */
	public String getTimestamp() {
		return timestamp;
	}

	/**
	 * @return the sign
	 */
	public String getSign() {
		return sign;
	}

}
