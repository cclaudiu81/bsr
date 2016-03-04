package com.sap.bsr.lyma.bus.executor;

import com.sap.bsr.lyma.bus.com.sap.bsr.lyma.bus.event.AbstractBidirectionalDataEvent;
import com.sap.bsr.lyma.bus.com.sap.bsr.lyma.bus.event.ResponsePayload;

/**
 * Created by cclaudiu on 3/4/16.
 */
public interface RequestExecutor<RESP> {

    ResponsePayload<RESP> execute();

    AbstractBidirectionalDataEvent<ResponsePayload<RESP>> getExecutedEvent();
}
