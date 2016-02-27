package com.sap.bsr.lyma.bus.com.sap.bsr.lyma.bus.event;

/**
 * Created by cclaudiu on 2/27/16.
 */
public class CalculateContextEvent implements LymaEvent<String, String> {
    private final String eventType;
    private final String eventContext;

    public CalculateContextEvent(final String eventType, final String eventContext) {
        this.eventContext = eventType;
        this.eventType = eventContext;
    }

    @Override
    public String getEventContext() {
        return eventContext;
    }

    public String getEventType() {
        return eventType;
    }
}
