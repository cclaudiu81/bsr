package com.sap.bsr.lyma.bus.executor.impl;

import com.sap.bsr.lyma.bus.com.sap.bsr.lyma.bus.event.AbstractBidirectionalDataEvent;
import com.sap.bsr.lyma.bus.executor.RequestExecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by cclaudiu on 3/4/16.
 */
public abstract class AbstractRequestExecutor<RESP> implements RequestExecutor<RESP> {

    private final AbstractBidirectionalDataEvent<RESP> requestEvent;

    public AbstractRequestExecutor(AbstractBidirectionalDataEvent<RESP> requestEvent) {
        this.requestEvent = requestEvent;
    }

    @Override
    public AbstractBidirectionalDataEvent<RESP> getExecutedEvent() {
        return requestEvent;
    }

    /** every client should provide the task to execute */
    public abstract void execute();

    /** convenient operation for doing heavy computation that don't return a response */
    @SuppressWarnings("unused")
    public void fireAndForget() {
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        final Future<?> submittedTask = executorService.submit(new Runnable() {
            @Override public void run() {
                execute();
            }
        });
    }
}
