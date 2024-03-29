<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">
    <title>My JSP 'index.jsp' starting page</title>

    <script type="text/javascript">
        var user = '${sessionScope.user }';
        var users = [];

        var ws;  // 管理登陆，退出，用户列表的 socket
        var ws2;  // 管理聊天 的 socket

        // window.onload 该方法自动加载，访问hello方法
        window.onload = ws_init;

        function ws_init() {
            var target = "ws://localhost:8080/websocket_01/hello?nickName=" + user;
            if ('WebSocket' in window) {
                ws = new WebSocket(target);
            } else if ('MozWebSocket' in window) {
                ws = new MozWebSocket(target);
            } else {
                alert('WebSocket is not supported by this browser.');
                return;
            }

            /**
             * 客户端的onopen onclose 可以不必实现
             */

            // ws.onopen = function () {
            //     showMsg("webSocket通道建立成功！！！");
            // };
            // 浏览器接收服务端消息
            ws.onmessage = function (event) {
                // 将event.data json格式数据转为string
                eval("var msg=" + event.data + ";");
                console.info(msg);

                if (msg.welcome != undefined)
                    showMsg(msg.welcome);
                if (msg.nickNames != undefined)
                    // 用户列表
                    showUser(msg.nickNames);
                if (msg.from != undefined) {
                    showContent(msg);
                }

            }
        }

        function showMsg(msg) {
            var content = document.getElementById("content");
            content.innerHTML += msg + "<br/>";
        }

        function showUser(users) {
            if (users.length == 0) return;
            var userList = document.getElementById("userList");
            userList.innerHTML = "";
            for (var i = 0; i < users.length; i++) {
                userList.innerHTML += users[i] + "<br/>";
            }
        }

        function showContent(msg) {

            var content = document.getElementById("content");
            content.innerHTML += msg.from + "--" + msg.date + "<br/>" + msg.content + "<br/>";

        }

        //浏览器发送消息到服务器
        function ws_send() {
            var msg = document.getElementById("msg");
            var temp = "{from:'" + user + "',content:'" + msg.value + "'}";

            ws.send(temp);
            msg.value = "";
        }


    </script>
</head>
<body>
<h1>这里是聊天室 ， 欢迎 ${sessionScope.user }进入聊天</h1>

<div id="content" style="background-color: aqua; width: 500px; height:400px;
			float:left;
		">
    <!--
        这个div显示所有的  聊天信息的内容
     -->
</div>
<div id="userList" style="background-color: olive; width: 200px; height:400px;
			   float: left;
		">
    <!--
        这个div 显示所有的在线用户
        这里要 显示两部分内容 是不是以为这要  两个 webSocket通道才能完成呢？？
        一个通道能否完成！！
        回答：  两个webSocket通道 显然更加简单一些啊。。。
     -->
</div>

<div style="margin-top: 10px;  clear: both;  ">
    <input id="msg"/>
    <button onclick="ws_send();">send</button>
</div>

</body>
</html>
