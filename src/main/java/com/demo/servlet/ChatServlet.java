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
@WebServlet(urlPatterns = { "/ChatServlet" }, asyncSupported = true)
public class ChatServlet extends HttpServlet {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -260157400324419618L;

    /**
     * 
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        System.out.println("--->join servlet");
        res.setContentType("text/html;charset=UTF-8");
        res.setHeader("Cache-Control", "private");
        res.setHeader("Pragma", "no-cache");
        req.setCharacterEncoding("UTF-8");

        String word = req.getParameter("word");
        System.out.println("--->chat:" + word);
        System.out.println(AsyncContextQueueWriter.MESSAGE_QUEUE);

        AsyncContextQueueWriter.MESSAGE_QUEUE.add(word);

        PrintWriter writer = res.getWriter();
        // for IE
        writer.println("<!doctype html public \"-//w3c//dtd html 4.0 transitional//en\">");
        writer.println("<script type=\"text/javascript\">var comet = window.parent.comet;</script>");
        writer.flush();

        final AsyncContext ac = req.startAsync();
        ac.setTimeout(1 * 6 * 100000);
        ac.addListener(new AsyncListener() {
            public void onComplete(AsyncEvent event) throws IOException {
                System.out.println("onComplete");
                AsyncContextQueueWriter.ASYNC_CONTEXT_QUEUE.remove(ac);
            }

            public void onTimeout(AsyncEvent event) throws IOException {
                System.out.println("onTimeout");
                AsyncContextQueueWriter.ASYNC_CONTEXT_QUEUE.remove(ac);
            }

            public void onError(AsyncEvent event) throws IOException {
                System.out.println("onError");
                AsyncContextQueueWriter.ASYNC_CONTEXT_QUEUE.remove(ac);
            }

            public void onStartAsync(AsyncEvent event) throws IOException {
                System.out.println("onStartAsync");
            }
        });
        AsyncContextQueueWriter.ASYNC_CONTEXT_QUEUE.add(ac);
    }
}
