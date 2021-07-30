package com.bjsxt.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 登录servlet
 * 访问：http://localhost:8080/websocket_01/login.jsp
 * 被post请求拦截，这里存储user，页面通过sessionScope.user 获取这里的user值
 */
public class LoginServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String pwd = request.getParameter("pwd");
        System.out.println(username + "--" + pwd);

        //这里是测试代码     pwd 无效~
        HttpSession session = request.getSession();
        session.setAttribute("user", username);

        response.sendRedirect("chat.jsp");

    }

}
