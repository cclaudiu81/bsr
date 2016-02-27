package com.sap.bsr.lyma.bus.handler;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.sap.bsr.lyma.bus.com.sap.bsr.lyma.bus.event.LymaEvent;

/**
 * Do as little tasks workload as you can in each handlers
 *
 * Created by cclaudiu on 2/27/16.
 */
public class ContextCalculationHandler {

    /* non-javadoc:: each handler should take exactly 1 argument that identifies the TYPE of the Event */
    @Subscribe
    @AllowConcurrentEvents
    public void onContextCalculation(LymaEvent<Object, Object> lymaEvent) {
        System.out.println(lymaEvent.getEventType());
        System.out.println(lymaEvent.getEventContext());
    }
}
