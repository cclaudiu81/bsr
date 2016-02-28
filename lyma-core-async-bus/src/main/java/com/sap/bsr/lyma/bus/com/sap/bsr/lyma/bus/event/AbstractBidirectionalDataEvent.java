package com.sap.bsr.lyma.bus.com.sap.bsr.lyma.bus.event;

import com.google.common.base.Optional;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * EventBus exports a mechanism for uni-directional communication, adhering to fire-and-forget
 * mechanism, that actor framework implement.
 * However the clients expects an async-response from the computation.
 * Making EventBus bi-directional requires the use of java's concurrency framework.
 *
 * This LymaEvent abstraction should be inherited by all Events created. It exports the mechanism
 * for making the client's thread stop, while the execution of the computation is in progress, and once
 * the computation thread ends, the response is returned to the caller.
 *
 * How's working?
 * The Client issues a eventBus.post using an event that descends from this abstraction; and immediatelly
 * invokes the getResponse() operation to return a payload to its client. Internally the countDownLatch
 * attempts to STOP the current-thread(issuer of request) and gives the execution-control to the computation-thread
 * that is the handler for this event. the handler computes the calculations and issues a setResponse() when it's done;
 * the setResponse() operation sets the response-payload, and count-downs 1; -> the COMPUTATION_THREAD will be 0,
 * making this to RETURN to clients-thread, after setting the response;
 *
 * Created by cclaudiu on 2/27/16.
 */
public abstract class AbstractBidirectionalDataEvent<T> {

    private static final int COMPUTATION_THREAD = 1;
    private final CountDownLatch countDownLatch = new CountDownLatch(COMPUTATION_THREAD);
    private T response;

    public Optional<T> getResponse(long millis) {
        try {
            countDownLatch.await(millis, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            return Optional.absent();
        }

        /* response might be null if awayting-thread-time is less than computation-thread-time */
        return Optional.fromNullable(response);
    }

    public void setResponse(T responsePayload) {
        this.response = responsePayload;
        countDownLatch.countDown();
    }
}
