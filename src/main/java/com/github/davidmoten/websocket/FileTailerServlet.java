package com.github.davidmoten.websocket;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;

public class FileTailerServlet extends WebSocketServlet {

	private static final long serialVersionUID = -7026451362362726993L;

	@Override
	public WebSocket doWebSocketConnect(HttpServletRequest request,
			String protocol) {
		FileTailer tailer = new FileTailer(new File("/var/log/syslog"));
		return new StreamWebSocket(tailer.getStream(1000));
	}

}
