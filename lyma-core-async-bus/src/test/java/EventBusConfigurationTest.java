import com.sap.bsr.lyma.bus.LymaEventBus;
import com.sap.bsr.lyma.bus.com.sap.bsr.lyma.bus.event.CalculateContextEvent;
import com.sap.bsr.lyma.bus.handler.ContextCalculationHandler;
import org.junit.Test;

/**
 * Created by cclaudiu on 2/27/16.
 */
public class EventBusConfigurationTest {

    @Test
    public void create_instance_of_event_bus_successfully() {
        LymaEventBus.broadcast(configureMockEvent("Channel:: REQUEST, Event:: ContextCalculation", "CalculationEventContext"),
                               LymaEventBus.Channel.REQUEST, new ContextCalculationHandler());
    }

    @Test
    public void register_event_bus_and_broadcasting_should_use_default_channel_if_none_given() {
        LymaEventBus.broadcast(configureMockEvent("Channel:: DEFAULT, Event:: ContextCalculation", "CalculationEventContext"),
                                new ContextCalculationHandler());
    }

    @Test
    public void benchmarking() {
        for(int idx = 0; idx < 300; idx += 1) {
            LymaEventBus.broadcast(configureMockEvent("Channel:: DEFAULT, Event:: ContextCalculation", "CalculationEventContext"),
                    new ContextCalculationHandler());
        }
    }

    private CalculateContextEvent configureMockEvent(final String eventType, final String eventContext) {
        return new CalculateContextEvent(eventType, eventContext);
    }
}
