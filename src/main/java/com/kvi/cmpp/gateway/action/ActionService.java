/**
 * Project Name cmpp-gateway
 * File Name package-info.java
 * Package Name com.kvi.cmpp.gateway.action
 * Create Time 2018年3月19日
 * Create by name：kvi
 * Copyright © 2015, 2017, kvi. All rights reserved.
 */
package com.kvi.cmpp.gateway.action;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

import com.kvi.cmpp.gateway.stack.BaseMessage;

/** 
 * ClassName: ActionService.java <br>
 * Description: <br>
 * Create by: name：kvi <br>
 * Create Time: 2017年6月5日<br>
 */
public interface ActionService {

    public void doProcess(IoSession session, BaseMessage command) throws Exception;
    
    public <T> T readMessage(IoBuffer ioBuffer) throws Exception;
}
