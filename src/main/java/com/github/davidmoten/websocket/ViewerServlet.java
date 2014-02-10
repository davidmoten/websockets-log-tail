package com.github.davidmoten.websocket;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;

import rx.Observable;

public class ViewerServlet extends WebSocketServlet {
	private static final long serialVersionUID = -1151103043909699131L;

	@Override
	public WebSocket doWebSocketConnect(HttpServletRequest req, String protocol) {
		Observable<?> stream = Util.createDummyStream();
		return new StreamWebSocket(stream);
	}

}