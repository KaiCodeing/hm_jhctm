package com.hemaapp.jhctm.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

public class City extends XtomObject implements Serializable{
	private String id;
	private String name;
	private String parentid;
	private String nodepath;
	private String namepath;
	private String charindex;
	private String level;
	private String orderby;
	private String nickname;
	private String avatar;
	private String bail;
	public City(JSONObject jsonObject) throws DataParseException {
		if (jsonObject != null) {
			try {
				id = get(jsonObject,"id");
				name = get(jsonObject, "name");
				parentid = get(jsonObject, "parentid");
				nodepath = get(jsonObject, "nodepath");
				namepath = get(jsonObject, "namepath");
				charindex = get(jsonObject, "charindex");
				level = get(jsonObject, "level");
				orderby = get(jsonObject, "orderby");
				nickname = get(jsonObject,"nickname");
				avatar = get(jsonObject,"avatar");
				bail = get(jsonObject,"bail");
				//trade_branch_name= get(jsonObject, "trade_branch_name");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public String getNickname() {
		return nickname;
	}

	public String getAvatar() {
		return avatar;
	}

	public String getBail() {
		return bail;
	}

	@Override
	public String toString() {
		return "City{" +
				"avatar='" + avatar + '\'' +
				", id='" + id + '\'' +
				", name='" + name + '\'' +
				", parentid='" + parentid + '\'' +
				", nodepath='" + nodepath + '\'' +
				", namepath='" + namepath + '\'' +
				", charindex='" + charindex + '\'' +
				", level='" + level + '\'' +
				", orderby='" + orderby + '\'' +
				", nickname='" + nickname + '\'' +
				", bail='" + bail + '\'' +
				'}';
	}

	public City(String id, String name, String parentid, String nodepath,
				String namepath, String charindex, String level, String orderby) {
		super();
		this.id = id;
		this.name = name;
		this.parentid = parentid;
		this.nodepath = nodepath;
		this.namepath = namepath;
		this.charindex = charindex;
		this.level = level;
		this.orderby = orderby;
	}
	/*
	 * 1.��дequals�������η�������public,��Ϊ����д��Object�ķ���. 2.�������ͱ�����Object.
	 */
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (!(other instanceof City))
			return false;

		final City cat = (City) other;

		if (!getName().equals(cat.getName()))
			return false;
		if (!getId().equals(cat.getId()))
			return false;
		return true;
	}

	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getParentid() {
		return parentid;
	}
	public String getNodepath() {
		return nodepath;
	}
	public String getNamepath() {
		return namepath;
	}
	public String getCharindex() {
		return charindex;
	}
	public String getLevel() {
		return level;
	}
	public String getOrderby() {
		return orderby;
	}
	
}
