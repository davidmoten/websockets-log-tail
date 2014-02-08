package com.github.davidmoten.websocket;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;

import rx.Observable;
import rx.Observable.OnSubscribeFunc;
import rx.Observer;
import rx.Subscription;
import rx.util.Range;
import rx.util.functions.Action1;
import rx.util.functions.Func1;

public class ViewerServlet extends WebSocketServlet {
	private static final long serialVersionUID = -1151103043909699131L;

	@Override
	public WebSocket doWebSocketConnect(HttpServletRequest req, String protocol) {
		Observable<?> stream = createObservable();
		return new StreamWebSocket(stream);
	}

	private Observable<String> createObservable() {
		return Observable.create(new OnSubscribeFunc<Integer>() {

			@Override
			public Subscription onSubscribe(Observer<? super Integer> o) {
				final RangeRunnable runnable = new RangeRunnable(Range.create(
						1, 100000), o);
				Thread t = new Thread(runnable);
				t.start();
				return new Subscription() {

					@Override
					public void unsubscribe() {
						runnable.cancel();
					}
				};
			}
		}).map(new Func1<Integer, String>() {

			@Override
			public String call(Integer i) {
				return i
						+ " this a longish log message similar in length to many typical log lines";
			}
		});
	}

	public static void main(String[] args) {
		Observable.interval(500, TimeUnit.MILLISECONDS).subscribe(
				new Action1<Long>() {

					@Override
					public void call(Long t1) {
						System.out.println("boo");
					}
				});
	}
}
