package nebula.plugin.metrics.providers.elasticsearch

import groovy.transform.Canonical
import nebula.plugin.metrics.providers.ReportingProviderConfiguration

/**
 * Applies to both In Process and Remote Configurations, and for Eureka/Direct
 */
@Canonical
class ElasticSearchProviderConfiguration implements ReportingProviderConfiguration {

    boolean remote = false
    String clusterName
    String hostName // If blank, we'll use eureka based on the clusterName
    int port
    // TODO index name too

    @Override
    boolean isValid() {
        return clusterName && port != 0
    }
}
