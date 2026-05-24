/**
 * Project Name cmpp-gateway
 * File Name package-info.java
 * Package Name com.kvi.cmpp.gateway.utils
 * Create Time 2018年3月19日
 * Create by name：kvi
 * Copyright © 2015, 2017, kvi. All rights reserved.
 */
package com.kvi.cmpp.gateway.utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.kvi.cmpp.gateway.stack.ClientInfo;

/**
 * ClassName: ReadYamlUtils.java <br>
 * Description: 读取配置文件<br>
 *
 * @author name：kvi <br>
 *         Create Time: 2018年3月19日<br>
 */
@Component
@SuppressWarnings("rawtypes")
public class ReadYamlUtils {

    static Map configMap = new HashMap();
    static {
        try {
            Yaml yaml = new Yaml();
            InputStream is = ReadYamlUtils.class.getClassLoader().getResourceAsStream("application.yaml");
            if (is == null) {
                LoggerFactory.getLogger(ReadYamlUtils.class).error("无法找到配置文件 application.yaml");
                System.exit(1);
            }
            Map applyFather = yaml.loadAs(is, HashMap.class);
            is.close();
            for (Object key : applyFather.keySet()) {
                if ("gatewar".equals(key)) {
                    configMap = (Map) applyFather.get(key);
                }
            }
        } catch (Exception e) {
            LoggerFactory.getLogger(ReadYamlUtils.class).error("读取配置文件异常", e);
            System.exit(1);
        }
    }

    /**
     * Description：实例化客户端信息
     *
     * @return String
     * @author name：kvi
     **/
    public static List<ClientInfo> getClientConfig() {
        List<ClientInfo> list = new ArrayList<ClientInfo>();
        List configList = (ArrayList) configMap.get("clientConfig");
        if (configList == null) {
            return list;
        }
        for (int i = 0; i < configList.size(); i++) {
            String jsonString = new Gson().toJson(configList.get(i));
            list.add(JSONObject.parseObject(jsonString, ClientInfo.class));
        }
        return list;
    }

    /**
     * Description：获取互联网短信网关端口号
     *
     * @return Integer
     * @author name：kvi <br>
     *         Create Time: 2018年3月19日<br>
     **/
    public static Integer getGatewayPort() {
        return (Integer) configMap.get("port");
    }

    /**
     * Description：获取短信网关版本
     *
     * @return Integer
     * @author name：kvi <br>
     *         Create Time: 2018年3月19日<br>
     **/
    public static Integer getGatewayVersion() {
        return (Integer) configMap.get("version");
    }

    public static void main(String[] args) throws Exception {
        List<ClientInfo> clientConfig = getClientConfig();
        System.out.println(ToStringBuilder.reflectionToString(clientConfig.get(0)));
    }

}
