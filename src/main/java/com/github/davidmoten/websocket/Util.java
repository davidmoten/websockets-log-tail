package com.github.davidmoten.websocket;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

class Util {

	static Observable<String> createDummyStream() {
		return Observable.interval(100, TimeUnit.MILLISECONDS).map(toMessage());
	}

	static Function<Long, String> toMessage() {
		return new Function<Long, String>() {

			@Override
			public String apply(Long i) {
				return i
						+ " this a longish log message similar in length to many typical log lines";
			}
		};
	}

}
