package nebula.plugin.metrics.providers.elasticsearch

import nebula.plugin.metrics.providers.ReportingProvider
import nebula.plugin.metrics.vanguard.BuildContext

class ElasticSearchInProcessProvider implements ReportingProvider {
    ElasticSearchInProcessProvider(ElasticSearchProviderConfiguration esConfiguration, BuildContext buildContext) {
    }

    @Override
    void report(Iterable iterable) {

    }
}
