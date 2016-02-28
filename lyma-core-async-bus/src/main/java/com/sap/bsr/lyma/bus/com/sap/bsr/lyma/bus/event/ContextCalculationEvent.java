package com.sap.bsr.lyma.bus.com.sap.bsr.lyma.bus.event;

/**
 * Created by cclaudiu on 2/27/16.
 */
public class ContextCalculationEvent extends AbstractBidirectionalDataEvent<String> {
    private final String requestEventContext;

    public ContextCalculationEvent(final String requestEventContext) {
        this.requestEventContext = requestEventContext;
    }

    public String getRequestEventContext() {
        return requestEventContext;
    }
}
