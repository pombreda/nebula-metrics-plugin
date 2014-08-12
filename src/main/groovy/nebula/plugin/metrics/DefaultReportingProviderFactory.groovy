package nebula.plugin.metrics

import nebula.plugin.metrics.providers.ReportingProvider
import nebula.plugin.metrics.providers.ReportingProviderConfiguration
import nebula.plugin.metrics.providers.elasticsearch.ElasticSearchInProcessProvider
import nebula.plugin.metrics.providers.elasticsearch.ElasticSearchProviderConfiguration
import nebula.plugin.metrics.providers.text.TextFileProvider
import nebula.plugin.metrics.providers.text.TextFileProviderConfiguration
import nebula.plugin.metrics.vanguard.BuildContext

class DefaultReportingProviderFactory implements ReportingProviderFactory {
    @Override
    public <C extends ReportingProviderConfiguration> ReportingProvider<C> create(C configuration, BuildContext buildContext) {
        if (!configuration.isValid()) {
            throw new IllegalArgumentException("Configuration (${configuration}) is not in a valid state")
        }
        if (configuration instanceof ElasticSearchProviderConfiguration) {
            ElasticSearchProviderConfiguration esConfiguration
            if (esConfiguration.remote) {

            } else {
                return new ElasticSearchInProcessProvider(esConfiguration, buildContext)
            }
        }
        if (configuration instanceof TextFileProviderConfiguration) {
            return new TextFileProvider( (TextFileProviderConfiguration) configuration, buildContext)
        }
        throw new IllegalArgumentException("Unable to find provider for ${configuration.class.name}")
    }
}
