package com.isgala.xishuashua.bean;

import com.isgala.xishuashua.bean_.Base;

import java.io.Serializable;

/**
 * 预支付订单
 * 
 *
 */
public class UnifiedOrder extends Base {
	public DataBean data;

	public static class DataBean implements Serializable{
		public String appid;
		public String mch_id;
		public String noncestr;
		// public String package;
		public String partnerid;
		public String prepayid;
		public String result_code;
		public String return_code;
		public String return_msg;
		public String sign;
		public String trade_type;
		public String timestamp;
		public String out_trade_no;
	}
}
