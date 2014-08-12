package nebula.plugin.metrics

import nebula.plugin.metrics.providers.ReportingProvider
import nebula.plugin.metrics.providers.ReportingProviderConfiguration
import nebula.plugin.metrics.vanguard.BuildContext

interface ReportingProviderFactory {
    public <C extends ReportingProviderConfiguration> ReportingProvider<C> create(C configuration, BuildContext buildContext);
}
