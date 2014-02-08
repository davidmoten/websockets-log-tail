package com.github.davidmoten.websocket;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

import rx.Observer;
import rx.util.Range;

public class RangeRunnable implements Runnable {

	private final Range range;
	private final AtomicBoolean keepGoing = new AtomicBoolean(true);
	private final Observer<? super Integer> observer;

	public RangeRunnable(Range range, Observer<? super Integer> o) {
		this.range = range;
		this.observer = o;
	}

	@Override
	public void run() {
		Iterator<Integer> it = range.iterator();
		while (keepGoing.get() && it.hasNext()) {
			observer.onNext(it.next());
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}
		observer.onCompleted();
	}

	public void cancel() {
		keepGoing.set(false);
	}

}
