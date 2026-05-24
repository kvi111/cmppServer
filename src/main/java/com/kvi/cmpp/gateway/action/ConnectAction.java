/**
 * Project Name cmpp-gateway
 * File Name ConnectAction.java
 * Package Name com.kvi.cmpp.gateway.action
 * Create Time 2018年8月30日
 * Create by name：kvi
 * Copyright © 2015, 2017, kvi. All rights reserved.
 */
package com.kvi.cmpp.gateway.action;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kvi.cmpp.gateway.exception.GateWayException;
import com.kvi.cmpp.gateway.stack.ClientInfo;
import com.kvi.cmpp.gateway.stack.MsgCommand;
import com.kvi.cmpp.gateway.stack.MsgConnect;
import com.kvi.cmpp.gateway.stack.MsgConnectResp;
import com.kvi.cmpp.gateway.utils.Constants;
import com.kvi.cmpp.gateway.utils.EhCache;
import com.kvi.cmpp.gateway.utils.MsgUtils;

/**
 * ClassName: ConnectAction.java <br>
 * Description: cmpp 认证操作<br>
 * Create by: name：kvi <br>
 * Create Time: 2018年8月30日<br>
 */
public class ConnectAction extends ActionFactoy {

	private final static Logger logger = LoggerFactory.getLogger(ConnectAction.class);

	/* (non-Javadoc)
	 * @see com.kvi.cmpp.gateway.action.ActionFactoy#exec()
	 */
	@Override
	protected void exec() throws Exception {
		logger.info("<接收 Connect 消息>");

		if (checkBody()) {
			MsgConnectResp connectResp = new MsgConnectResp();
			connectResp.setTotalLength(Constants.MessageTotalLength.CONNECT);
			connectResp.setCommandId(MsgCommand.CMPP_CONNECT_RESP);
			connectResp.setSequenceId(message.getSequenceId());
			connectResp.setStatus(0x0001);
			connectResp.setVersion(Constants.CMPP_VERSION);
			logger.info("<Connect 响应消息{}>", ToStringBuilder.reflectionToString(connectResp));
			session.write(connectResp.toByteArry());
			return;
		}

		MsgConnectResp connectResp = new MsgConnectResp();
		connectResp.setTotalLength(Constants.MessageTotalLength.CONNECT);
		connectResp.setCommandId(MsgCommand.CMPP_CONNECT_RESP);
		connectResp.setSequenceId(message.getSequenceId());
		int status = 0x0000;
		try {
			checkConnectRequest();
		} catch (GateWayException e) {
			status = e.getErrorCode();
			logger.error("checkConnectRequest message {} &&& status= {}", e.getMessage(), status);
		}
		connectResp.setStatus(status);
		String authenticatorSource = message.getBodys().getString("authenticatorSourceStr");
		String clientIp = message.getClientIp();
		ClientInfo clientInfo = (ClientInfo) EhCache.get(EhCache.CACHE_NAME, clientIp);
		String sharedSecret = clientInfo != null ? clientInfo.getSharedSecret() : "";

		connectResp.setAuthenticatorISMG(MsgUtils.getAuthenticatorISMG(status, authenticatorSource, sharedSecret));
		connectResp.setVersion(Constants.CMPP_VERSION);
		logger.info("<Connect 响应消息{}>", ToStringBuilder.reflectionToString(connectResp));
		session.write(connectResp.toByteArry());

	}

	/* (non-Javadoc)
	 * @see com.kvi.cmpp.gateway.action.ActionFactoy#readMessage()
	 */
	@SuppressWarnings({ "unchecked" })
	@Override
	protected <T> T readMessage() throws Exception {

		MsgConnect msgConnect = new MsgConnect();
		byte[] sourceAddr = new byte[6];
		ioBuffer.get(sourceAddr);
		msgConnect.setSourceAddr(new String(sourceAddr));
		byte[] aiByte = new byte[16];
		ioBuffer.get(aiByte);
		msgConnect.setAuthenticatorSource(aiByte);
		msgConnect.setVersion(ioBuffer.get());
		msgConnect.setTimestamp(ioBuffer.getInt());
		return (T) msgConnect;

	}

}
