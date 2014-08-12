package nebula.plugin.metrics.vanguard

import nebula.plugin.metrics.midguard.ReportingBuffer
import nebula.plugin.metrics.providers.ReportingProvider

class ReportingCollector {
    @groovy.transform.PackageScope
    ReportingBuffer buffer

    ReportingCollector(ReportingBuffer buffer) {
        this.buffer = buffer
    }

    private static ReportingCollector instance = null;

    public static ReportingCollector getInstance() {
        if(instance == null) {
            synchronized(Singleton.class) {
                if(instance == null) {
                    ReportingBuffer buffer = new ReportingBuffer()
                    instance = new ReportingCollector(buffer)
                }
            }
        }
        return instance;
    }

    public setSink(ReportingProvider provider) {
        buffer.setSink(provider)
    }

    // For testing
    public reset() {
        if (buffer) {
            buffer.mollusk.forceStop()
        }
        buffer = new ReportingBuffer()
    }

    public static record(Interval timer) {
        getInstance().buffer.queue(timer)
    }
}
