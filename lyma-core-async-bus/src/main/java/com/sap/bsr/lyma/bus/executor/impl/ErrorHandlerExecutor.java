package com.sap.bsr.lyma.bus.executor.impl;

import com.sap.bsr.lyma.bus.com.sap.bsr.lyma.bus.event.AbstractBidirectionalDataEvent;
import com.sap.bsr.lyma.bus.com.sap.bsr.lyma.bus.event.ResponsePayload;
import com.sap.bsr.lyma.bus.executor.RequestExecutor;

/**
 * Created by cclaudiu on 3/4/16.
 */
public class ErrorHandlerExecutor<RESP> implements RequestExecutor<RESP> {
    private final RequestExecutor configuredRequestExecutor;

    public ErrorHandlerExecutor(RequestExecutor executor) {
        this.configuredRequestExecutor = executor;
    }

    @Override
    public void execute() {
        try {
            configuredRequestExecutor.execute();
        } catch(Exception generalException) {

            // set the response accordingly! an exception visitor might be implemented so that we set the error-code accordingly
            final AbstractBidirectionalDataEvent executedEvent = configuredRequestExecutor.getExecutedEvent();
            executedEvent.setResponse(new ResponsePayload<RESP>() {
                @Override public int responseErrorCode() {
                    return AbstractBidirectionalDataEvent.ResponseCode.INTERNAL_SERVER_ERROR.code();
                }

                @Override public RESP responsePayload() {
                    return null;
                }
            });
        }
    }

    @Override
    public AbstractBidirectionalDataEvent<RESP> getExecutedEvent() {
        return null;
    }
}
