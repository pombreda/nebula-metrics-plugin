package nebula.plugin.metrics.providers.text

import groovy.transform.Canonical
import nebula.plugin.metrics.providers.ReportingProviderConfiguration

@Canonical
class TextFileProviderConfiguration implements ReportingProviderConfiguration {

    File destination

    @Override
    boolean isValid() {
        return destination != null
    }
}
