package com.github.davidmoten.websocket;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import rx.Observable;
import rx.Observable.OnSubscribeFunc;
import rx.Observer;
import rx.Subscription;
import rx.util.functions.Action0;
import rx.util.functions.Action1;

public class RxUtil {

	public static <T> Observable<Observable<T>> doWhenAllComplete(
			final Observable<Observable<T>> original, Action0 action) {
		final Counter counter = new Counter(action);
		final Observable<Observable<T>> countedOriginal = count(original,
				new Action1<Long>() {

					@Override
					public void call(Long count) {
						counter.majorComplete(count);
					}
				});
		return Observable.create(new OnSubscribeFunc<Observable<T>>() {

			@Override
			public Subscription onSubscribe(
					final Observer<? super Observable<T>> o) {

				Subscription subscription = countedOriginal
						.subscribe(createObserver(counter, o));

				return subscription;
			}

		});
	}

	public static <T> Observable<T> count(final Observable<T> obs,
			final Action1<Long> action) {
		return Observable.create(new OnSubscribeFunc<T>() {

			@Override
			public Subscription onSubscribe(final Observer<? super T> o) {
				final AtomicLong count = new AtomicLong(0);
				final Subscription sub = obs.subscribe(new Observer<T>() {

					@Override
					public void onCompleted() {
						action.call(count.get());
						o.onCompleted();
					}

					@Override
					public void onError(Throwable e) {
						o.onError(e);
					}

					@Override
					public void onNext(T t) {
						count.incrementAndGet();
						o.onNext(t);
					}
				});
				return new Subscription() {

					@Override
					public void unsubscribe() {
						sub.unsubscribe();
						action.call(count.get());
					}
				};
			}
		});
	}

	private static class Counter {
		private final Action0 action;
		private final AtomicBoolean majorComplete = new AtomicBoolean(false);
		private final AtomicLong majorCount = new AtomicLong(0);
		private final AtomicLong minorCompleteCount = new AtomicLong(0);

		Counter(Action0 action) {
			this.action = action;
		}

		void majorComplete(long count) {
			majorCount.set(count);
			majorComplete.set(true);
		}

		void minorComplete() {
			long count = minorCompleteCount.incrementAndGet();
			if (majorComplete.get() && majorCount.get() == count) {
				action.call();
			}
		}
	}

	private static <T> Observer<Observable<T>> createObserver(
			final Counter onFinished, final Observer<? super Observable<T>> o) {
		return new Observer<Observable<T>>() {

			@Override
			public void onCompleted() {
				o.onCompleted();
			}

			@Override
			public void onError(Throwable e) {
				o.onError(e);
			}

			@Override
			public void onNext(Observable<T> ob) {
				final Observable<T> ob2 = count(ob, new Action1<Long>() {
					@Override
					public void call(Long count) {
						onFinished.minorComplete();
					}
				});
				o.onNext(ob2);
			}
		};
	}

	public static void main(String[] args) {
		Observable<Integer> c = Observable.range(1, 100);
		Observable<Integer> c2 = count(c, new Action1<Long>() {

			@Override
			public void call(Long count) {
				System.out.println("finished counting " + count);
			}
		});
		long count = c2.count().toBlockingObservable().single();
		System.out.println("count=" + count);

		Observable<Observable<Long>> o = Observable.from(
				Observable.interval(1, TimeUnit.SECONDS).take(3), Observable
						.interval(1, TimeUnit.SECONDS).take(1));
		final AtomicBoolean finishedAll = new AtomicBoolean(false);
		Observable<Observable<Long>> o2 = doWhenAllComplete(o, new Action0() {
			@Override
			public void call() {
				finishedAll.set(true);
				System.out.println("finished all");
			}
		});
		Observable.merge(o2).toBlockingObservable()
				.forEach(new Action1<Long>() {
					@Override
					public void call(Long value) {
						System.out.println(value);
					}
				});
	}
}
