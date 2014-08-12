package nebula.plugin.metrics.providers

import nebula.plugin.metrics.vanguard.Metric

/**
 * Created by extension, and used by midguard
 */
interface ReportingProvider<C extends ReportingProviderConfiguration> {
    // Report API
    void report(Iterable<Metric> metrics)

    // Operations
    void flush()
}
