package com.demo.servlet;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.servlet.AsyncContext;

/**
 * ��һ��Queue<AsyncContext>��ÿ��Context��Writer�������
 * @author zzm
 */
public class AsyncContextQueueWriter extends Writer {

	/**
	 * AsyncContext����
	 */
	private Queue<AsyncContext> queue;

	/**
	 * ��Ϣ����
	 */
	private static final BlockingQueue<String> MESSAGE_QUEUE = new LinkedBlockingQueue<String>();

	/**
	 * ������Ϣ���첽�̣߳����������http response��
	 * @param cbuf
	 * @param off
	 * @param len
	 * @throws IOException
	 */
	private void sendMessage(char[] cbuf, int off, int len) throws IOException {
		try {
			MESSAGE_QUEUE.put(new String(cbuf, off, len));
		} catch (Exception ex) {
			IOException t = new IOException();
			t.initCause(ex);
			throw t;
		}
	}

	/**
	 * �첽�̣߳�����Ϣ�����б��������ݣ����ͷ�take�����������������ݷ��͵�http response����
	 */
	private Runnable notifierRunnable = new Runnable() {
		public void run() {
			boolean done = false;
			while (!done) {
				String message = null;
				try {
					message = MESSAGE_QUEUE.take();
					for (AsyncContext ac : queue) {
						try {
							PrintWriter acWriter = ac.getResponse().getWriter();
							acWriter.println(htmlEscape(message));
							acWriter.flush();
						} catch (IOException ex) {
							System.out.println(ex);
							queue.remove(ac);
						}
					}
				} catch (InterruptedException iex) {
					done = true;
					System.out.println(iex);
				}
			}
		}
	};

	/**
	 * @param message
	 * @return
	 */
	private String htmlEscape(String message) {
		return "<script type='text/javascript'>\nwindow.parent.update(\""
				+ message.replaceAll("\n", "").replaceAll("\r", "") + "\");</script>\n";
	}

	/**
	 * ����һ��Ĭ�ϵ�writer�����������̨�����writer��ͬ����������������response����writer���첽���
	 */
	private static final Writer DEFAULT_WRITER = new OutputStreamWriter(System.out);

	/**
	 * ����AsyncContextQueueWriter
	 * @param queue
	 */
	AsyncContextQueueWriter(Queue<AsyncContext> queue) {
		this.queue = queue;
		Thread notifierThread = new Thread(notifierRunnable);
		notifierThread.start();
	}

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		DEFAULT_WRITER.write(cbuf, off, len);
		sendMessage(cbuf, off, len);
	}

	@Override
	public void flush() throws IOException {
		DEFAULT_WRITER.flush();
	}

	@Override
	public void close() throws IOException {
		DEFAULT_WRITER.close();
		for (AsyncContext ac : queue) {
			ac.getResponse().getWriter().close();
		}
	}

}
