import com.google.common.base.Optional;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;
import com.sap.bsr.lyma.bus.LymaEventBus;
import com.sap.bsr.lyma.bus.com.sap.bsr.lyma.bus.event.AbstractBidirectionalDataEvent;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Unit testing Testing the Lyma Event-Bus Wrapper
 * <p>
 * Created by cclaudiu on 2/27/16.
 */
public class EventBusConfigurationTest {

    private final LymaEventBus asyncEventBus = LymaEventBus.instance();

    @Test
    public void create_instance_of_event_bus_successfully_and_post_event_HOW_TO_USE() {
        asyncEventBus
                .subscribe(new ContextCalculationHandler())
                .postEvent(new ContextCalculationEvent("CalculationEventContext"));
    }

    @Test
    public void unsubscribing_a_handler_is_permitted_only_using_the_same_handler_instance_and_should_not_trigger_any_action() {
        final ContextCalculationHandler calculationHandler = new ContextCalculationHandler();
        asyncEventBus.subscribe(calculationHandler);

        asyncEventBus.unsubscribe(calculationHandler);
        asyncEventBus.postEvent(new ContextCalculationEvent("CalculationEventContext"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void attempting_to_unsubscribe_a_handler_using_a_new_instance_should_fail_with_exception() {
        asyncEventBus.subscribe(new ContextCalculationHandler());

        asyncEventBus.unsubscribe(new ContextCalculationHandler());
        asyncEventBus.postEvent(new ContextCalculationEvent("CalculationEventContext"));
    }

    @Test
    public void catch_all_handler_for_any_unhandled_events_is_invoked_wrapping_the_not_handler_event_in_a_core_dead_event() {
        // TODO: impl today
    }

    @Test
    public void client_thread_should_await_computation_thread_that_returns_a_response_after_completition() {
        final long awaitingTimeoutHigherThanComputationTimeout = 1200L;
        final AwaitCalculationEvent awaitCalculationEvent = new AwaitCalculationEvent("CALCULATE_CONTEXT_REQ");

        asyncEventBus.subscribe(new LongCalculationHandler())
                .postEvent(awaitCalculationEvent);

        final Optional<String> calculationEventResponse = awaitCalculationEvent.getResponse(awaitingTimeoutHigherThanComputationTimeout);

        assertThat(calculationEventResponse.get(), is("AWAIT:: LONG_CALCULATION_RESPONSE_PAYLOAD"));
    }

    @Test
    public void computation_thread_should_return_absent_for_computations_that_exceed_awaiting_timeout() {
        final long awatingTimeoutLessThanComputationTimeout = 500L;
        final CalculationExceedsTimeoutEvent exceedsTimeoutEvent = new CalculationExceedsTimeoutEvent("CALCULATE_CONTEXT_REQ");

        asyncEventBus.subscribe(new LongCalculationHandler())
                     .postEvent(exceedsTimeoutEvent);

        final Optional<String> calculationEventResponse = exceedsTimeoutEvent.getResponse(awatingTimeoutLessThanComputationTimeout);

        assertThat(calculationEventResponse.isPresent(), is(false));
    }

    @Test
    public void client_thread_should_be_able_to_retrieve_the_computation_response() {
        final long awaitingTimeout = 2000L;
        final ContextCalculationEvent calculationEvent = new ContextCalculationEvent("CALCULATE_CONTEXT_REQ");

        asyncEventBus.subscribe(new ContextCalculationHandler())
                     .postEvent(calculationEvent);

        final Optional<String> calculationEventResponse = calculationEvent.getResponse(awaitingTimeout);

        assertThat(calculationEventResponse.get(), is("CALCULATION_RESPONSE_PAYLOAD"));
    }

    @Test
    public void subscribing_2_handlers_for_same_event_should_trigger_actions_for_both_handlers() {
        final MultiCalculationEvent multiCalculationEvent = new MultiCalculationEvent("PAYLOAD_MULTICALCULATION");
        asyncEventBus.subscribe(new MultiCalculationHandler())
                .postEvent(multiCalculationEvent);
    }

    static class MultiCalculationHandler {
        @Subscribe
        @AllowConcurrentEvents
        public void firstHandler(MultiCalculationEvent multiCalculationEvent) {
            System.out.println("first-handler:: " + multiCalculationEvent.eventPayload);
        }

        @Subscribe
        @AllowConcurrentEvents
        public void secondHandler(MultiCalculationEvent multiCalculationEvent) {
            System.out.println("second-handler:: " + multiCalculationEvent.eventPayload);
        }
    }

    static class MultiCalculationEvent extends AbstractBidirectionalDataEvent<String> {
        public String eventPayload;
        MultiCalculationEvent(String payload) { this.eventPayload = payload; }
    }


    /* Demo Handler that mirror the real implementation definitions */
    static class ContextCalculationHandler {

        /* non-javadoc:: each handler should take exactly 1 argument that identifies the TYPE of the Event */
        @Subscribe
        @AllowConcurrentEvents
        public void onContextCalculation(ContextCalculationEvent calculationEvent) {
            calculationEvent.setResponse("CALCULATION_RESPONSE_PAYLOAD");
        }

        @Subscribe
        @AllowConcurrentEvents
        public void onCatchAllEvent(DeadEvent deadEventWrapperForNotHandledEvents) {
            // TODO: do something
            System.out.println(deadEventWrapperForNotHandledEvents.getEvent());
        }
    }

    /* Demo Handler that mirror the real implementation definitions */
    static class LongCalculationHandler {
        @Subscribe
        @AllowConcurrentEvents
        public void onCalculationExceeds(final CalculationExceedsTimeoutEvent calculationEvent) throws InterruptedException {
            Thread.sleep(1000l);
            calculationEvent.setResponse("EXPIRES:: LONG_CALCULATION_RESPONSE_PAYLOAD");
        }

        @Subscribe
        @AllowConcurrentEvents
        public void onCalculationExceeds(final AwaitCalculationEvent awaitCalculationEvent) throws InterruptedException {
            Thread.sleep(1000l);
            awaitCalculationEvent.setResponse("AWAIT:: LONG_CALCULATION_RESPONSE_PAYLOAD");
        }

    }


    /* Demo Events that mirror the real implementation definition */
    static class ContextCalculationEvent extends AbstractBidirectionalDataEvent<String> {
        final String requestEventContext;

        public ContextCalculationEvent(final String requestEventContext) {
            this.requestEventContext = requestEventContext;
        }
    }

    static class CalculationExceedsTimeoutEvent extends AbstractBidirectionalDataEvent<String> {
        final String requestEventContext;

        public CalculationExceedsTimeoutEvent(final String requestEventContext) {
            this.requestEventContext = requestEventContext;
        }
    }

    static class AwaitCalculationEvent extends AbstractBidirectionalDataEvent<String> {
        final String requestEventContext;

        public AwaitCalculationEvent(final String requestEventContext) {
            this.requestEventContext = requestEventContext;
        }
    }
}