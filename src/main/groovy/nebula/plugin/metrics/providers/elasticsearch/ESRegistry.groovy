package nebula.plugin.metrics.providers.elasticsearch

import org.elasticsearch.client.transport.TransportClient

public interface ESRegistry {
    void updateESRegistry(TransportClient client, String clusterName, int port)
}