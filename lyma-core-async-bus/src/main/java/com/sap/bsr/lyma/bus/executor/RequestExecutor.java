package com.sap.bsr.lyma.bus.executor;

import com.sap.bsr.lyma.bus.com.sap.bsr.lyma.bus.event.AbstractBidirectionalDataEvent;

/**
 * Created by cclaudiu on 3/4/16.
 */
public interface RequestExecutor<RESP> {

    void execute();

    AbstractBidirectionalDataEvent<RESP> getExecutedEvent();
}
