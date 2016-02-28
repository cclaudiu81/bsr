package com.sap.bsr.lyma.bus.handler;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;
import com.sap.bsr.lyma.bus.com.sap.bsr.lyma.bus.event.ContextCalculationEvent;

/**
 * Do as little tasks workload as you can in each handlers
 *
 * Created by cclaudiu on 2/27/16.
 */
public class ContextCalculationHandler {

    /* non-javadoc:: each handler should take exactly 1 argument that identifies the TYPE of the Event */
    @Subscribe
    @AllowConcurrentEvents
    public void onContextCalculation(ContextCalculationEvent calculationEvent) {
        System.out.println(calculationEvent.getRequestEventContext());

        calculationEvent.setResponse("CALCULATION-RESPONSE-PAYLOAD");
    }

    @Subscribe
    @AllowConcurrentEvents
    public void onCatchAllEvent(DeadEvent deadEventWrapperForNotHandledEvents) {
        System.out.println(deadEventWrapperForNotHandledEvents.getEvent());
    }
}
