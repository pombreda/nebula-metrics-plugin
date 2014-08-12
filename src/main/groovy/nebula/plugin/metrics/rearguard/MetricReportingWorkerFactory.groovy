package nebula.plugin.metrics.rearguard

import org.gradle.api.Action
import org.gradle.api.internal.tasks.testing.WorkerTestClassProcessorFactory
import org.gradle.process.internal.WorkerProcessContext

public interface MetricReportingWorkerFactory {
    public Action<? super WorkerProcessContext> create();
}