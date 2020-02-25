package thehole.black.process.demo.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.MethodSorters;
import thehole.black.process.demo.shoe.internal.OrderRepository;
import thehole.black.process.demo.trip.TripStatus;
import thehole.black.process.demo.trip.TripsJob;
import thehole.black.process.demo.trip.internal.TripOrderRepository;
import thehole.black.process.demo.test.support.io.service.SerializationService;
import thehole.black.process.histogram.HistogramStatus;
import thehole.black.process.impl.ProcessingFlags;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestSuiteTrips {

    private TripsJob job = new TripsJob();

    @Rule
    public TestRule watcher = new TestWatcher() {
        protected void starting(Description description) {
            System.out.println("-Running : " + description.getMethodName());
        }
    };

    @BeforeClass
    public static void setupLoggingEnv() {
        System.setProperty(ProcessingFlags.NEN_PROCESSING_LOG_DIR, System.getProperty("user.home") + "/.processing");
        System.setProperty(ProcessingFlags.NEN_PROCESSING_LOGGING_APP, "coolApp");
        System.setProperty(ProcessingFlags.NEN_PROCESSING_LOGGING_SEPARATE_FILE, "true");
    }

    @Before
    public void setup() {
        OrderRepository.getInstance().physicallyWipe();
        SerializationService.getInstance().setSilent(true);
    }

    @After
    public void clearTmp() {
        OrderRepository.getInstance().physicallyWipe();
    }

    private void reInitRepo() {

    }

    @Test
    public void a_trips_go_from_none_done_to_all_done() {

        HistogramStatus status = job.getHistogram(TripOrderRepository.getInstance().load().stream());
        assertEquals(status.getActuallyFinishedPercent(), Integer.valueOf(0));
        assertTrue(status.getActualProgressPercent() > 10);

        assertFalse(TripOrderRepository.getInstance().load().stream().filter(o -> o.getCurrentStatus().equals(TripStatus.Status.CAR_CONFIRMED)).findFirst().isPresent());
        job.doJob();
        assertEquals(100l, TripOrderRepository.getInstance().load().stream().filter(o -> o.getCurrentStatus().equals(TripStatus.Status.CAR_CONFIRMED)).count());
    }
}
