package nebula.plugin.metrics.providers.elasticsearch

import com.google.common.collect.Sets
import groovyx.net.http.RESTClient
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class EurekaRestRegistry implements ESRegistry {
    private Logger log = LoggerFactory.getLogger(EurekaRestRegistry.class);

    String eurekaName
    RESTClient eureka
    private Set<String> esServerSet = new HashSet<String>();

    EurekaRestRegistry(String eurekaName) {
        this.eurekaName = eurekaName
        eureka = new RESTClient( "http://${eurekaName}/discovery/v2/" )
    }

    @Override
    void updateESRegistry(TransportClient client, String clusterName, int port) {
        def resp = eureka.get(path: 'apps/${clusterName}')
        def json = resp.getData()
        Set<String> newServerSet = json.instance.findAll { it.status == 'UP' }.collect { it.hostName } as Set

        for (String removed : Sets.difference(esServerSet, newServerSet)) {
            client.removeTransportAddress(new InetSocketTransportAddress(removed, port));
            esServerSet.remove(removed);
            log.info(removed + " was removed");
        }

        for (String added : Sets.difference(newServerSet, esServerSet)) {
            if (EurekaClientRegistry.healthy(added, clusterName, port)) {
                client.addTransportAddress(new InetSocketTransportAddress(added, port));
                esServerSet.add(added);
                log.info(added + " was added");
            }
        }
    }
}
