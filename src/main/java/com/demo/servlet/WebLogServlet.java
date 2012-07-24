package com.demo.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class WebLogServlet
 */
@WebServlet(urlPatterns = { "/WebLogServlet" }, asyncSupported = true)
public class WebLogServlet extends HttpServlet {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -260157400324419618L;

	/**
	 * ���ͻ���ע�ᵽ����Logger����Ϣ������
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html;charset=UTF-8");
		res.setHeader("Cache-Control", "private");
		res.setHeader("Pragma", "no-cache");
		req.setCharacterEncoding("UTF-8");
		PrintWriter writer = res.getWriter();
		// for IE
		writer.println("<!-- Comet is a programming technique that enables web servers to send data to the client without having any need for the client to request it. -->\n");
		writer.flush();

		final AsyncContext ac = req.startAsync();
		ac.setTimeout(10 * 60 * 1000);
		ac.addListener(new AsyncListener() {
			public void onComplete(AsyncEvent event) throws IOException {
//				WebLogAppender.ASYNC_CONTEXT_QUEUE.remove(ac);
			}

			public void onTimeout(AsyncEvent event) throws IOException {
//				WebLogAppender.ASYNC_CONTEXT_QUEUE.remove(ac);
			}

			public void onError(AsyncEvent event) throws IOException {
//				WebLogAppender.ASYNC_CONTEXT_QUEUE.remove(ac);
			}

			public void onStartAsync(AsyncEvent event) throws IOException {
			}
		});
        //		WebLogAppender.ASYNC_CONTEXT_QUEUE.add(ac);
	}
}
