/**
 * Project Name pushServer
 * File Name ConnectionFilter.java
 * Package Name com.kvi.pushServer.filter
 * Create Time 2018年3月15日
 * Create by name：kvi
 * Copyright © 2015, 2017, kvi. All rights reserved.
 */
package com.kvi.cmpp.gateway.filter;

import java.net.InetSocketAddress;

import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * ClassName: ConnectionFilter.java <br>
 * Description: <br>
 * @author name：kvi <br>
 * @date: 2018年3月15日<br>
 */
public class ConnectionFilter extends IoFilterAdapter{
    
    private static Logger log = LoggerFactory.getLogger(ConnectionFilter.class);

    @Override
    public void messageReceived(NextFilter nextFilter, IoSession session, Object message) throws Exception {
        InetSocketAddress socketAddress = (InetSocketAddress) session.getRemoteAddress();
        log.debug("远程服务器地址{}" , socketAddress.getAddress().getHostAddress());
        nextFilter.messageReceived(session, message);
    }
}
