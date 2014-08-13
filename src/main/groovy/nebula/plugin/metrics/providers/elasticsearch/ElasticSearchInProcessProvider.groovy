package nebula.plugin.metrics.providers.elasticsearch

import nebula.plugin.metrics.providers.ReportingProvider
import nebula.plugin.metrics.vanguard.BuildContext
import nebula.plugin.metrics.vanguard.Metric

class ElasticSearchInProcessProvider implements ReportingProvider {
    BuildContext buildContext
    EurekaESClient client
    ElasticSearchInProcessProvider(ElasticSearchProviderConfiguration esConfiguration, BuildContext buildContext) {
        this.buildContext = buildContext
        client = new EurekaESClient(esConfiguration.clusterName, esConfiguration.port)
    }

    @Override
    void report(Iterable<Metric> iterable) {

    }

    @Override
    void flush() {

    }
}
