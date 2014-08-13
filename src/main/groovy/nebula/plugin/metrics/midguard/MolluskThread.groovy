package nebula.plugin.metrics.midguard

import nebula.plugin.metrics.providers.ReportingProvider
import nebula.plugin.metrics.vanguard.Metric
import org.joda.time.DateTime
import org.joda.time.Duration

import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Thread to hang around
 */
class MolluskThread extends Thread {
    ConcurrentLinkedQueue<Metric> backlog
    ReportingProvider sink
    DateTime lastAccessed
    Duration settleTime = Duration.millis(5 * 1000)
    AtomicBoolean prematurelyStop = new AtomicBoolean(false)
    boolean running = false;

    MolluskThread(Queue<Metric> backlog, ReportingProvider sink) {
        super("MolluskProducer")
        this.backlog = backlog
        this.sink = sink
        lastAccessed = new DateTime()
        this.daemon = false // Start as a user thread
    }

    DateTime ping() {
        // Reset the settle time
        synchronized (lastAccessed) {
            lastAccessed = new DateTime()
        }
    }

    def forceStop() {
        prematurelyStop.set(true)
    }

    @Override
    void run() {
        running = true
        while(true) {
            List<Metric> batch = new LinkedList<Metric>()
            Metric metric
            while ((metric = backlog.poll()) != null) {
                batch.add(metric)
            }

            if (!batch.isEmpty()) {
                sink.report(batch)
                ping()
            }

            if (tooLong()) {
                sink.flush()
                setDaemon(true)
            }
            try {
                Thread.sleep(100)
            } catch (InterruptedException e) {
            }
        }
        running = false
    }

    private tooLong() {
        new Duration(lastAccessed, new DateTime()).isLongerThan(settleTime) || prematurelyStop.get()
    }
}
