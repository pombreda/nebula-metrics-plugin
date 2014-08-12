package nebula.plugin.metrics.midguard

import nebula.plugin.metrics.providers.ReportingProvider
import nebula.plugin.metrics.vanguard.Metric

import java.util.concurrent.ConcurrentLinkedQueue

/**
 * We won't always have access to a sink, and we have to stick around a little while to finish sending metrics.
 * We'll buffer results and use a non-daemon thread to send the results.
 */
class ReportingBuffer {
    ConcurrentLinkedQueue<Metric> metrics = new ConcurrentLinkedQueue<>();
    MolluskThread mollusk

    void setSink(ReporingProvider provider) {
        MolluskThread mollusk = new MolluskThread(metrics, provider)
        mollusk = mollusk.start()
    }

    void queue(Metric metric) {
        metrics.add(metric)
    }
}
