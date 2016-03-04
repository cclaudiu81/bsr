package com.sap.bsr.lyma.bus.com.sap.bsr.lyma.bus.event;

/**
 * Created by cclaudiu on 3/4/16.
 */
public interface ResponsePayload<T> {

    int responseErrorCode();
    T responsePayload();
}
