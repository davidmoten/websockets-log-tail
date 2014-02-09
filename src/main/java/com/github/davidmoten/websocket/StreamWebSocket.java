package com.github.davidmoten.websocket;

import java.io.IOException;

import org.eclipse.jetty.websocket.WebSocket;

import rx.Observable;
import rx.Subscription;
import rx.util.functions.Action1;

public class StreamWebSocket implements WebSocket {

	private final Observable<?> stream;
	private Connection connection;

	private Subscription subscription;

	public StreamWebSocket(Observable<?> stream) {
		this.stream = stream;
	}

	public void onMessage(String message) {
		try {
			connection.sendMessage(message);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void onClose(int closeCode, String message) {
		subscription.unsubscribe();
	}

	@Override
	public void onOpen(Connection connection) {
		this.connection = connection;
		this.subscription = stream.subscribe(new Action1<Object>() {
			@Override
			public void call(Object obj) {
				onMessage(obj.toString());
			}
		});
	}

}
