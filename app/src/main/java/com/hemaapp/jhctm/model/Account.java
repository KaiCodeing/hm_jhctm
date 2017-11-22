package com.hemaapp.jhctm.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2017/2/5.
 * 账户余额各种信息
 */
public class Account extends XtomObject implements Serializable {
    private String feeaccount;//余额
    private String score;//积分
    private String share_score;//兑换币
    private String gold_score;//兑换金积分
    private String freeze_gold_score;//冻结金积分
    private String score2gold;//积分、金积分兑换比例
    private String share2gold;//兑换币、金积分兑换比例
    private String gold_multi_param;//兑换券积分倍增系数
    private String gold_multi_adcount;//兑换券积分倍增广告推广次数
    private String gold_multi_freeze_days;//兑换券积分倍增冻结返还时间
    private String gold_price;//兑换券积分平台售价（人民币）
    private String scorecard_price;//平台积分卡售价（人民币）
    private String mixcard_price;//合成卡片贴现数量（人民币）
    public Account(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                feeaccount = get(jsonObject, "feeaccount");
                score = get(jsonObject, "score");
                share_score = get(jsonObject, "share_score");
                gold_score = get(jsonObject, "gold_score");
                freeze_gold_score = get(jsonObject, "freeze_gold_score");
                score2gold = get(jsonObject, "score2gold");
                share2gold = get(jsonObject, "share2gold");
                gold_multi_param = get(jsonObject, "gold_multi_param");
                gold_multi_adcount = get(jsonObject, "gold_multi_adcount");
                gold_multi_freeze_days = get(jsonObject, "gold_multi_freeze_days");
                gold_price = get(jsonObject,"gold_price");
                scorecard_price = get(jsonObject,"scorecard_price");
                mixcard_price = get(jsonObject,"mixcard_price");
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "Account{" +
                "feeaccount='" + feeaccount + '\'' +
                ", score='" + score + '\'' +
                ", share_score='" + share_score + '\'' +
                ", gold_score='" + gold_score + '\'' +
                ", freeze_gold_score='" + freeze_gold_score + '\'' +
                ", score2gold='" + score2gold + '\'' +
                ", share2gold='" + share2gold + '\'' +
                ", gold_multi_param='" + gold_multi_param + '\'' +
                ", gold_multi_adcount='" + gold_multi_adcount + '\'' +
                ", gold_multi_freeze_days='" + gold_multi_freeze_days + '\'' +
                ", gold_price='" + gold_price + '\'' +
                ", scorecard_price='" + scorecard_price + '\'' +
                ", mixcard_price='" + mixcard_price + '\'' +
                '}';
    }

    public String getFeeaccount() {
        return feeaccount;
    }

    public String getFreeze_gold_score() {
        return freeze_gold_score;
    }

    public String getGold_multi_adcount() {
        return gold_multi_adcount;
    }

    public String getGold_multi_freeze_days() {
        return gold_multi_freeze_days;
    }

    public String getGold_multi_param() {
        return gold_multi_param;
    }

    public String getGold_price() {
        return gold_price;
    }

    public String getGold_score() {
        return gold_score;
    }

    public String getMixcard_price() {
        return mixcard_price;
    }

    public String getScore2gold() {
        return score2gold;
    }

    public String getScore() {
        return score;
    }

    public String getScorecard_price() {
        return scorecard_price;
    }

    public String getShare2gold() {
        return share2gold;
    }

    public String getShare_score() {
        return share_score;
    }
}
