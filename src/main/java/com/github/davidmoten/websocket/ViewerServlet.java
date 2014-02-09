package com.github.davidmoten.websocket;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;

import rx.Observable;
import rx.util.functions.Func1;

public class ViewerServlet extends WebSocketServlet {
	private static final long serialVersionUID = -1151103043909699131L;

	@Override
	public WebSocket doWebSocketConnect(HttpServletRequest req, String protocol) {
		Observable<?> stream = createObservable();
		return new StreamWebSocket(stream);
	}

	private Observable<String> createObservable() {
		return Observable.interval(100, TimeUnit.MILLISECONDS).map(toMessage());
	}

	private Func1<Long, String> toMessage() {
		return new Func1<Long, String>() {

			@Override
			public String call(Long i) {
				return i
						+ " this a longish log message similar in length to many typical log lines";
			}
		};
	}
}