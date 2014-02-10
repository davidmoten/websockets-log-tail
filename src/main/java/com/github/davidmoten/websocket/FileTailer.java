package com.github.davidmoten.websocket;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;
import org.apache.commons.io.input.TailerListenerAdapter;

import rx.Observable;
import rx.Observable.OnSubscribeFunc;
import rx.Observer;
import rx.Subscription;

public class FileTailer {

	private final File file;

	public FileTailer(File file) {
		this.file = file;
	}

	public Observable<String> getStream(int numBytes, final long pollIntervalMs) {
		return Observable.create(new OnSubscribeFunc<String>() {

			@Override
			public Subscription onSubscribe(
					final Observer<? super String> observer) {
				TailerListener listener = createListener(observer);
				final Tailer tailer = new Tailer(file, listener, pollIntervalMs);
				Thread t = new Thread(createRunnable(observer, tailer));
				t.start();
				return createSubscription(tailer);
			}
		});
	}

	private TailerListenerAdapter createListener(
			final Observer<? super String> observer) {
		return new TailerListenerAdapter() {
			@Override
			public void handle(String line) {
				observer.onNext(line);
			}

			@Override
			public void fileNotFound() {
				observer.onError(new FileNotFoundException(file.toString()));
			}

			@Override
			public void handle(Exception ex) {
				observer.onError(ex);
			}
		};
	}

	private Runnable createRunnable(final Observer<? super String> observer,
			final Tailer tailer) {
		return new Runnable() {
			@Override
			public void run() {
				try {
					tailer.run();
				} catch (Exception e) {
					observer.onError(e);
				}
			}
		};
	}

	private Subscription createSubscription(final Tailer tailer) {
		return new Subscription() {
			@Override
			public void unsubscribe() {
				tailer.stop();
			}
		};
	}
}
