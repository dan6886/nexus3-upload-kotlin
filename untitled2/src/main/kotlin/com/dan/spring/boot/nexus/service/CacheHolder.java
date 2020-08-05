package com.dan.spring.boot.nexus.service;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import java.util.concurrent.TimeUnit;

import static io.reactivex.Observable.interval;

public abstract class CacheHolder<E> {
    private Disposable timer = null;


    public CacheHolder() {
        this(20, TimeUnit.SECONDS);
    }

    public CacheHolder(long period, TimeUnit unit) {
        timer = interval(2, period, unit).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                e = refresh();
            }
        });
    }

    private E e = null;

    public E get() {
        if (e == null) {
            e = refresh();
        }
        return e;
    }

    protected abstract E refresh();

    public void fetch() {
        e = refresh();
    }

    public void destroy() {
        if (timer != null && !timer.isDisposed()) {
            timer.dispose();
        }
    }
}
