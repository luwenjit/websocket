package com.bjsxt.config;

import java.util.Set;

import javax.websocket.Endpoint;
import javax.websocket.Session;
import javax.websocket.server.ServerApplicationConfig;
import javax.websocket.server.ServerEndpointConfig;

public class WebSocketConfig  implements  ServerApplicationConfig {

	/*
	 * 1.  getAnnotatedEndpointClasses
	 * 2.  getEndpointConfigs
	 *
	 * 上面的两个方法都是用来注册 webSocket的。   只不过注册的方式不同。  1方法是 注解的方式
	 * 2方法是 接口的方式
	 *
	 * 显然 注解的方式更加的 灵活简单。  接口的方式更加的传统，严谨。
	 *
	 * (non-Javadoc)
	 * @see javax.websocket.server.ServerApplicationConfig#getAnnotatedEndpointClasses(java.util.Set)
	 */

	public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> scan) {
		// TODO Auto-generated method stub
		System.out.println("正在扫描所有的webSocket服务！！！");
		// scan标识扫描到的websocket服务
		System.out.println("config:" + scan.size());
		return scan;
	}

	public Set<ServerEndpointConfig> getEndpointConfigs(
			Set<Class<? extends Endpoint>> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
