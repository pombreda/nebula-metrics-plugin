package nebula.plugin.metrics.providers.elasticsearch

import com.google.common.annotations.VisibleForTesting
import com.netflix.odin.eureka.Eureka
import org.elasticsearch.action.index.IndexRequestBuilder
import org.elasticsearch.action.index.IndexResponse
import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.InetSocketTransportAddress

/**
 * From https://stash.corp.netflix.com/projects/CPIE/repos/family/browse/buildSrc/src/main/groovy/com/netflix/platform/elasticsearch/ElasticSearch.groovy
 */
class Route53ESClient {
    static final String ROUTE53_NAME = 'es_share.dyntest.netflix.net'

    private final Closure clientProvider
    private final Closure indexRequestBuilderDecorator

    private Client client

    @VisibleForTesting
    protected ElasticSearch() {
        this.clientProvider = null
        this.indexRequestBuilderDecorator = null
    }

    ElasticSearch(Eureka eureka) {
        this(
                { ->
                    final hostnames = getHostnames(eureka)
                    final Settings settings = ImmutableSettings.settingsBuilder()
                            .put("cluster.name", "es_share")
                            .build()
                    final client = new TransportClient(settings)
                    hostnames.each { String hostname ->
                        client.addTransportAddress(new InetSocketTransportAddress(hostname, 7102))
                    }

                    client
                },
                { IndexRequestBuilder indexRequestBuilder ->
                    indexRequestBuilder
                })
    }

    @VisibleForTesting
    ElasticSearch(Closure clientProvider) {
        this(
                clientProvider,
                { IndexRequestBuilder indexRequestBuilder ->
                    // gain determinism at the cost of performance
                    indexRequestBuilder.setRefresh(true)
                })
    }

    @VisibleForTesting
    ElasticSearch(Closure clientProvider, Closure indexRequestBuilderDecorator) {
        this.clientProvider = clientProvider
        this.indexRequestBuilderDecorator = indexRequestBuilderDecorator

        this.client = clientProvider()
    }

    static String[] getHostnames(Eureka eureka) {
        String base = "discovery/v2/apps/${appId}"
        try {
            final instances = eureka.instancesForApp('elasticsearch').findAll { instance ->
                instance.vipAddress == 'elasticsearch-share.cloud.netflix.com'
            }
            instances.collect { instance ->
                instance.hostName
            }
        } catch (Exception e) {
            [ROUTE53_NAME] as String[]
        }
    }

    IndexResponse index(Closure indexRequestBuilderProvider) {
        indexRequestBuilderDecorator(indexRequestBuilderProvider(client))
                .execute()
                .actionGet()
    }
}
