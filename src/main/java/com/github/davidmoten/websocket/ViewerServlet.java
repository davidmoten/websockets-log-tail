package com.github.davidmoten.websocket;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;

public class ViewerServlet extends WebSocketServlet {
	private static final long serialVersionUID = -1151103043909699131L;

	@Override
	public WebSocket doWebSocketConnect(HttpServletRequest req, String protocol) {
		return new StreamWebSocket(Util.createDummyStream());
	}
}