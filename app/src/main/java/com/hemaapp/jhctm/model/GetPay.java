package com.hemaapp.jhctm.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

public class GetPay extends XtomObject implements Serializable{
	private String pubid;//订单id  
	private String total_fee;//需支付的金额 

public GetPay(JSONObject jsonObject) throws DataParseException {

	if (jsonObject != null) {
		try {
			pubid = get(jsonObject, "pubid");
			total_fee=get(jsonObject,"total_fee");
		} catch (JSONException e) {
			throw new DataParseException(e);
		}
	}

}

/**
 * @return the pubid
 */
public String getPubid() {
	return pubid;
}

/**
 * @param pubid the pubid to set
 */
public void setPubid(String pubid) {
	this.pubid = pubid;
}

/**
 * @return the total_fee
 */
public String getTotal_fee() {
	return total_fee;
}

/**
 * @param total_fee the total_fee to set
 */
public void setTotal_fee(String total_fee) {
	this.total_fee = total_fee;
}


}
