/**
 * Project Name cmpp-gateway
 * File Name package-info.java
 * Package Name com.kvi.cmpp.gateway.action
 * Create Time 2018年3月19日
 * Create by name：kvi
 * Copyright © 2015, 2017, kvi. All rights reserved.
 */
package com.kvi.cmpp.gateway.action;

import org.apache.commons.lang3.StringUtils;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kvi.cmpp.gateway.exception.GateWayException;
import com.kvi.cmpp.gateway.stack.BaseMessage;
import com.kvi.cmpp.gateway.stack.ClientInfo;
import com.kvi.cmpp.gateway.utils.EhCache;
import com.kvi.cmpp.gateway.utils.MsgUtils;
import com.kvi.cmpp.gateway.utils.Utils;

/**
 * ClassName: ActionFactoy.java <br>
 * Description: <br>
 * Create by: name：kvi <br>
 * Create Time: 2017年6月5日<br>
 */
public abstract class ActionFactoy implements ActionService {
	private static Logger logger = LoggerFactory.getLogger(ActionFactoy.class);

	protected IoSession session;
	protected BaseMessage message;
	protected IoBuffer ioBuffer;
	protected Object rawMessage; // 存储原始读取的消息对象

	/**
	 * Description：校验body是否有数据
	 *
	 * @return boolean
	 * @author name：kvi
	 **/
	protected boolean checkBody() {
		return message.getBodys() == null || StringUtils.isBlank(message.getBodys().toJSONString());
	}

	protected void checkConnectRequest() {
		// 获取客户端 IP，根据客户端 IP 来获取其配置
		String clientIp = message.getClientIp();
		ClientInfo clientInfo = (ClientInfo) EhCache.get(EhCache.CACHE_NAME, clientIp);
		GateWayException.checkCondition(clientInfo == null, 0x0002, "非源地址");
		// 企业编码 也就是 SP_Id
		String sourceAddr = message.getBodys().getString("sourceAddr");
		// 客户端签名（hex 字符串）
		String authenticatorSource = message.getBodys().getString("authenticatorSourceStr");
		// CMPP 规范要求 timestamp 为 10 位 MMDDHHMMSS 格式（带前导零）
		int ts = message.getBodys().getIntValue("timestamp");
		String timestamp = String.format("%010d", ts);
		logger.debug("<request sourceAddr ={}, AuthenticatorSource={}>", sourceAddr, authenticatorSource);
		byte[] serverAuth = MsgUtils.getAuthenticatorSource(sourceAddr, clientInfo.getSharedSecret(), timestamp);
		String sign = Utils.bytes2Hex(serverAuth);
		logger.debug("<serverSign={},clientSign={}>", sign, authenticatorSource);
		boolean flag = sign.equals(authenticatorSource);
		GateWayException.checkCondition(!flag, 0x0003, "认证失败");

		String clientVersion = message.getBodys().getString("version");
		logger.debug("<clientVersion={},serverVersion={}>", clientVersion, clientInfo.getVersion());
		//GateWayException.checkCondition(!clientVersion.equals(clientInfo.getVersion()), 0x0004, "版本不一致");
	}

	protected abstract void exec() throws Exception;

	protected abstract <T> T readMessage() throws Exception;

	public void doProcess(IoSession session, BaseMessage message) throws Exception {
		try {
			this.session = session;
			this.message = message;
			exec();
		} catch (Exception e) {
			throw new GateWayException("doProcess msg connect error " + e);
		}
	}

	public <T> T readMessage(IoBuffer ioBuffer) throws Exception {
		try {
			this.ioBuffer = ioBuffer;
			return readMessage();
		} catch (Exception e) {
			throw new GateWayException("read msg connect error " + e);
		}
	}
}
