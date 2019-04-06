package com.github.davidmoten.websocket;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;
import org.apache.commons.io.input.TailerListenerAdapter;
import org.reactivestreams.Subscription;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;

public class FileTailer {

    private final File file;

    public FileTailer(File file) {
        this.file = file;
    }

    public Observable<String> getStream(final long pollIntervalMs) {
        return Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                TailerListener listener = createListener(emitter);
                final Tailer tailer = new Tailer(file, listener, pollIntervalMs);
                try {
                    tailer.run();
                } catch (Throwable e) {
                    emitter.onError(e);
                }
            }
        });
    }

    private TailerListenerAdapter createListener(final ObservableEmitter<String> emitter) {
        return new TailerListenerAdapter() {

            @Override
            public void fileRotated() {
                // ignore, just keep tailing
            }

            @Override
            public void handle(String line) {
                emitter.onNext(line);
            }

            @Override
            public void fileNotFound() {
                emitter.onError(new FileNotFoundException(file.toString()));
            }

            @Override
            public void handle(Exception ex) {
                emitter.onError(ex);
            }
        };
    }

}
