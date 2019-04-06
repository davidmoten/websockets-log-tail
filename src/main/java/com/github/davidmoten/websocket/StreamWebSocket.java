package com.github.davidmoten.websocket;

import java.io.IOException;

import org.eclipse.jetty.websocket.WebSocket;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class StreamWebSocket implements WebSocket, Observer<Object> {

    private final Observable<?> stream;
    private Connection connection;

    private Disposable disposable;

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
        System.out.println("disposing");
        disposable.dispose();
    }

    @Override
    public void onOpen(Connection connection) {
        this.connection = connection;
        stream.subscribeOn(Schedulers.io()).subscribe(this);
    }

    @Override
    public void onComplete() {
        System.out.println("complete");
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onNext(Object obj) {
        onMessage(obj.toString());
    }

    @Override
    public void onSubscribe(Disposable disposable) {
        this.disposable = disposable;
    }

}
