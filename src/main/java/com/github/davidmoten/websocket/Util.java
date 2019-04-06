package com.github.davidmoten.websocket;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;

class Util {

	static Observable<String> createDummyStream() {
		return Observable.interval(100, TimeUnit.MILLISECONDS).map(toMessage());
	}

	static Func1<Long, String> toMessage() {
		return new Func1<Long, String>() {

			@Override
			public String call(Long i) {
				return i
						+ " this a longish log message similar in length to many typical log lines";
			}
		};
	}

}
