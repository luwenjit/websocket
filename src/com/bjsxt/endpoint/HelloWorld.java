
package com.bjsxt.endpoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.bjsxt.vo.Login;
import com.bjsxt.vo.Msg;
import com.google.gson.Gson;



@ServerEndpoint("/hello")
public class HelloWorld {


	// 每次打开一个 webSocket通道， 都会生成一个 helloWorld实例，   而一个 endPoint实例， 用来应对一个通道的信息传递。
	// 所以  我们 helloWorld  线程安全哦。。。

	//存储每个用户的session信息，这里可以用list<session> 或者 map<userid,session>这种结构
	//在onopen时存入，在onclose时清掉
	private static  Set<HelloWorld>  ss= new HashSet<HelloWorld>();
	//存储用户列表
	private static  List<String>  nickNames=new ArrayList<String>();
	private Session  session ;
	private Gson gson=new Gson();
	private String nickName;

	/**
	 * websocket 多线程对象
	 */
	public HelloWorld() {
		System.out.println("创建了一个 helloWorld实例！@！！！");

	}
	@OnOpen
	public void  open(Session  session ){

		/**
		 * serlvet中的session和jsp中的session 与 websocket中的session不是一个session
		 */

		System.out.println("建立了一个 webSocket通道！！！  sid:"+session.getId());

		String queryString= session.getQueryString();
		this.nickName = queryString.substring(queryString.indexOf("=")+1);
		ss.add(this);
		nickNames.add(nickName);
		this.session = session;

		Login  login=new Login();
		login.setWelcome(nickName+"进入聊天室！！");
		login.setNickNames(nickNames);

		//login对象转为  json格式
		String msg = gson.toJson(login);
		broadcast(ss,msg);
	}

	@OnClose
	public void close(){

		ss.remove(this);
		nickNames.remove(nickName);

		Login  login=new Login();
		login.setWelcome(nickName+"退出聊天室！！");
		login.setNickNames(nickNames);

		String msg = gson.toJson(login);
		broadcast(ss,msg);

	}



	@OnMessage
	public   void  receiveMsg(Session session,String msg ){
		System.out.println("收到信息啦 ， 来自sid:"+session.getId());
		System.out.println("信息："+msg);

		Msg  temp= gson.fromJson(msg, Msg.class);
		temp.setDate(new Date().toLocaleString());

		broadcast(ss, gson.toJson(temp));

	}

	// 广播 实现群聊
	// 私聊 需要定位到某个人的session

	/**
	 * 私聊
	 * 	1、客户端jsp 需要指定发送聊天的是哪个 如username 可通过消息结构化传回to字段
	 * 	2、服务器需要根据username判断这个人的session  构建map<username,session>存储关系
	 * 	3、客户端发送的消息 需要 指定当前是单聊还是群聊，这样服务器可以判断调用不同的方法  客服端获取是否有to对象
	 *
	 */
	private void  broadcast(Set<HelloWorld>  ss, String msg){
		//for循环其实就是群聊，私聊就针对某个session.getBasicRemote.sendText(消息)
		for (Iterator iterator = ss.iterator(); iterator.hasNext();) {
			HelloWorld helloWorld = (HelloWorld) iterator.next();

			try {
				helloWorld.session.getBasicRemote().sendText(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}






}
