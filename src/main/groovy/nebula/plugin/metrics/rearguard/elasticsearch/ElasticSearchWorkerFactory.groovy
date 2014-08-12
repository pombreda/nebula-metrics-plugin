package nebula.plugin.metrics.rearguard.elasticsearch

import nebula.plugin.metrics.rearguard.MetricReportingWorkerFactory
import org.gradle.api.Action
import org.gradle.process.internal.WorkerProcessContext

class ElasticSearchWorkerFactory implements MetricReportingWorkerFactory {

    ElasticSearchWorkerFactory() {
    }

    @Override
    Action<? super WorkerProcessContext> create() {
        return new ElasticSearchWorker(extension.getUrl(), processorFactory, extension.getAllJvmArgs());
    }
}
