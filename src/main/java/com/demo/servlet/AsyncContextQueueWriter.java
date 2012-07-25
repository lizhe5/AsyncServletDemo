package com.demo.servlet;

import java.io.PrintWriter;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.servlet.AsyncContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * 
 * @author zzm
 */
@WebListener
public class AsyncContextQueueWriter implements ServletContextListener {

    public AsyncContextQueueWriter() {

    }

    /**
     */
    public static final BlockingQueue<String> MESSAGE_QUEUE       = new LinkedBlockingQueue<String>();

    /**
     */
    public static final Queue<AsyncContext>   ASYNC_CONTEXT_QUEUE = new ConcurrentLinkedQueue<AsyncContext>();

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        // TODO Auto-generated method stub

    }

    /**
     * 
     * @param cbuf
     * @param off
     * @param len
     * @throws IOException
     */

    /**
     */
    private Runnable notifierRunnable = new Runnable() {
                                          public void run() {
                                              boolean done = false;
                                              String message = null;
                                              System.out.println("--->"+MESSAGE_QUEUE);
                                              System.out.println(MESSAGE_QUEUE.size());
                                              System.out.println("aaa");
                                              while (!done) { 
                                                  System.out.println("--->take message...");
                                                  try {
                                                      message = MESSAGE_QUEUE.take();
                                                      System.out.println("--->msg=" + message);
                                                      for (AsyncContext context : ASYNC_CONTEXT_QUEUE) {
                                                          if (context == null) {
                                                              continue;
                                                          }
                                                          PrintWriter out = context.getResponse().getWriter();
                                                          if (out == null) {
                                                              continue;
                                                          }
                                                          String html = htmlEscape(message);
                                                          System.out.println("--->start to push..." + context
                                                                                  + "; "
                                                                                  + html);
                                                          out.println(htmlEscape(message));
                                                          out.flush();
                                                      }
                                                  } catch (Exception e) {
                                                      e.printStackTrace();
                                                      done = true;
                                                  }
                                              }
                                          }
                                      };

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        System.out.println("contextInitialized...");
        // TODO Auto-generated method stub
        new Thread(notifierRunnable).start();
    }

    /**
     * @param message
     * @return
     */
    private String htmlEscape(String message) {
        return "<script type='text/javascript'>\nwindow.parent.update(\"" + message.replaceAll("\n", "").replaceAll("\r", "")
                                + "\");</script>\n";
    }

}
