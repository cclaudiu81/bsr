package com.sap.bsr.lyma.bus;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.sap.bsr.lyma.bus.com.sap.bsr.lyma.bus.event.AbstractBidirectionalDataEvent;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Singleton Wrapper over core EventBus that exposed a unique instance of EventBus
 * and allows for chaining-invocations.
 * It wraps the async-event-bus version, allowing for non-blocking operations through the
 * use of cached-thread-pool framework;
 * <p>
 * Created by cclaudiu on 2/27/16.
 */
public final class LymaEventBus {

    private static final Executor EXECUTOR_CACHED_THREADS_POOL = Executors.newCachedThreadPool();

    /* singleton across JVM runtime:: delegate responsibility of single instance creation to JVM/class-loader */
    private static final LymaEventBus LYMA_EVENT_BUS_INSTANCE = new LymaEventBus();
    private final EventBus asyncEventBus;

    private LymaEventBus() {
        this.asyncEventBus = new AsyncEventBus(EXECUTOR_CACHED_THREADS_POOL);
    }

    public static final LymaEventBus instance() {
        return LYMA_EVENT_BUS_INSTANCE;
    }

    public final LymaEventBus subscribe(final Object handler) {
        asyncEventBus.register(handler);
        return instance();
    }

    public final LymaEventBus unsubscribe(final Object handler) {
        asyncEventBus.unregister(handler);
        return instance();
    }

    public final <T> LymaEventBus postEvent(final AbstractBidirectionalDataEvent<T> bidirectionalEvent) {
        asyncEventBus.post(bidirectionalEvent);
        return instance();
    }
}
