package com.sap.bsr.lyma.bus;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.sap.bsr.lyma.bus.com.sap.bsr.lyma.bus.event.LymaEvent;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Wrapper Object over default EventBus that encapsulates and expose the EventBus communication
 * through Channels, such that in there may be many EventBus-es dependending on the subscribed channels
 *
 * Created by cclaudiu on 2/27/16.
 */
public final class LymaEventBus {

    private static final Executor EXECUTOR_CACHED_THREADS_POOL = Executors.newCachedThreadPool();
    private static final ConcurrentHashMap<Channel, EventBus> EVENT_BUS_CACHE = new ConcurrentHashMap();

    public enum Channel {
        REQUEST,
        RESPONSE,
        DEFAULT
    }

    private static final void register(final Object eventHandler, Channel channel) {
        Preconditions.checkNotNull(channel, "Missing a valid channel for registering the Event Handler on!");

        EventBus asyncEventBus = EVENT_BUS_CACHE.get(channel);
        if(asyncEventBus == null) {
            asyncEventBus = new AsyncEventBus(channel.name(), EXECUTOR_CACHED_THREADS_POOL);
            EVENT_BUS_CACHE.put(channel, asyncEventBus);
        }

        asyncEventBus.register(eventHandler);
    }

    public static final <T, V> void broadcast(final LymaEvent<T, V> lymaEvent, final Object eventHandler) {
        broadcast(lymaEvent, Channel.DEFAULT, eventHandler);
    }

    public static final <T, V> void broadcast(final LymaEvent<T, V> lymaEvent, Channel channel, final Object eventHandler) {
        register(eventHandler, channel);
        final EventBus asyncEventBus = EVENT_BUS_CACHE.get(channel);

        asyncEventBus.post(lymaEvent);
    }
}
