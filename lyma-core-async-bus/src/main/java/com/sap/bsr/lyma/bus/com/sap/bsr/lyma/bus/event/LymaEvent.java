package com.sap.bsr.lyma.bus.com.sap.bsr.lyma.bus.event;

/**
 * Stands as a marker interface for any type of events
 * Created by cclaudiu on 2/27/16.
 */
public interface LymaEvent<EVENT_CONTEXT, EVENT_TYPE> {

    EVENT_CONTEXT getEventContext();
    EVENT_TYPE getEventType();
}
