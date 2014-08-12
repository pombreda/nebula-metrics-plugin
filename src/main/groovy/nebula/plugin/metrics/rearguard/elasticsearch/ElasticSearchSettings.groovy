package nebula.plugin.metrics.rearguard.elasticsearch

import groovy.transform.Canonical

/**
 * Assuming a eureka based search
 */
@Canonical
class ElasticSearchSettings {
    String clusterName
    int port
}
