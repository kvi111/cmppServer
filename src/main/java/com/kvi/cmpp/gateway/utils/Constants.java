/**
 * Project Name cmpp-gateway
 * File Name Constants.java
 * Package Name com.kvi.cmpp.gateway.utils
 * Create Time 2018年8月30日
 * Create by name：kvi
 * Copyright © 2015, 2017, kvi. All rights reserved.
 */
package com.kvi.cmpp.gateway.utils;

/** 
 * ClassName: Constants.java <br>
 * Description: 常量类<br>
 * Create by: name：kvi <br>
 * Create Time: 2018年8月30日<br>
 */
public class Constants {
	
	public final static byte CMPP_VERSION = 0x30;
	public final static byte RESP_SUCCESS = 0x0000;

	/**
	 * action 常量
	 * ClassName: Constants.java <br>
	 * Description: <br>
	 * Create by: name：kvi <br>
	 * Create Time: 2018年8月30日<br>
	 */
	public static class ActionConstants{
		/** 心跳链接 */
		public final static String ACTIVE = "Active";
		/** 短信链接*/
		public final static String CONNECT = "Connect";
		/** 终止链接*/
		public final static String TERMINATE = "Terminate";
		/** 提交短信--下行*/
		public final static String SUBMIT = "Submit";
		/** 短信下发*/
		public final static String DELIVER = "Deliver";
	}
	
	/**
	 * 消息体长度 常量
	 * ClassName: Constants.java <br>
	 * Description: <br>
	 * Create by: name：kvi <br>
	 * Create Time: 2018年8月30日<br>
	 */
	public static class MessageTotalLength{
		public final static Integer CONNECT = 12 + 4 + 16 + 1;
		public final static Integer ACTIVE_TEST = 12 + 1;
		public final static Integer SUBMIT_REST = 12 + 8 + 4;
		public final static Integer DELIVER = 12 + 8 + 7 + 10 + 10 + 32 + 1;
		public final static Integer DELIVER_RESP = 12 + 8 + 4;
	}
}
