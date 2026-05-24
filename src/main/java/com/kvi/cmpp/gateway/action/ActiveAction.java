/**
 * Project Name cmpp-gateway
 * File Name ConnectAction.java
 * Package Name com.kvi.cmpp.gateway.action
 * Create Time 2018年8月30日
 * Create by name：kvi
 * Copyright © 2015, 2017, kvi. All rights reserved.
 */
package com.kvi.cmpp.gateway.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kvi.cmpp.gateway.stack.MsgActiveTestResp;
import com.kvi.cmpp.gateway.stack.MsgCommand;
import com.kvi.cmpp.gateway.utils.Constants;

/**
 * ClassName: ConnectAction.java <br>
 * Description: cmpp心跳监测<br>
 * Create by: name：kvi <br>
 * Create Time: 2018年8月30日<br>
 */
public class ActiveAction extends ActionFactoy {

	private final static Logger logger = LoggerFactory.getLogger(ActiveAction.class);

	/* (non-Javadoc)
	 * @see com.kvi.cmpp.gateway.action.ActionFactoy#exec()
	 */
	@Override
	protected void exec() throws Exception {
		logger.info("<客户端心跳信息, sessionId={}, sequenceId={}>", session.getId(), message.getSequenceId());
		MsgActiveTestResp active = new MsgActiveTestResp();
		active.setTotalLength(Constants.MessageTotalLength.ACTIVE_TEST);
		active.setCommandId(MsgCommand.CMPP_ACTIVE_TEST_RESP);
		active.setSequenceId(message.getSequenceId());
		active.setReserved((byte)0);
		session.write(active.toByteArry());

	}

	/* (non-Javadoc)
	 * @see com.kvi.cmpp.gateway.action.ActionFactoy#readMessage()
	 */
	@Override
	protected <T> T readMessage() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
