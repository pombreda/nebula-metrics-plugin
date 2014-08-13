package nebula.plugin.metrics.providers.elasticsearch

import com.google.common.collect.Sets
import com.netflix.appinfo.InstanceInfo
import com.netflix.discovery.DiscoveryClient
import com.netflix.discovery.DiscoveryManager
import com.netflix.discovery.shared.Application
import org.elasticsearch.action.admin.cluster.health.ClusterHealthStatus
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class EurekaClientRegistry implements ESRegistry {
    private Logger log = LoggerFactory.getLogger(EurekaClientRegistry.class);

    private Set<InstanceInfo> esServerSet = new HashSet<InstanceInfo>();

    @Override
    public void updateESRegistry(TransportClient client, String clusterName, int port) {
        log.info("updateESRegistry...");
        try {
            Application  app = getApplication(clusterName);
            List<InstanceInfo>  instances = getInstances(clusterName, app);
            Set<InstanceInfo> newServerSet = new HashSet<InstanceInfo>();

            for (InstanceInfo instance : instances) {
                if (instance.getStatus() == InstanceInfo.InstanceStatus.UP) {
                    newServerSet.add(instance);
                }
            }

            for (InstanceInfo removed : Sets.difference(esServerSet, newServerSet)) {
                client.removeTransportAddress(new InetSocketTransportAddress(removed.getHostName(), port));
                esServerSet.remove(removed);
                log.info(removed.getId() + " was removed");
            }

            for (InstanceInfo added : Sets.difference(newServerSet, esServerSet)) {
                if (healthy(added.getHostName(), clusterName, port)) {
                    client.addTransportAddress(new InetSocketTransportAddress(added.getHostName(), port));
                    esServerSet.add(added);
                    log.info(added.getId() + " was added");
                }
            }
        } catch (Exception e) {
            log.error("Exception while updateESRegistry: " + e.getMessage());
        }
    }
    private List<InstanceInfo> getInstances(String clusterName, Application app)
    {
        List<InstanceInfo> ins = app.getInstances();
        if ( ins == null )
        {
            throw new RuntimeException(
                    "Failed to get instances for discovery application \'" +
                            clusterName.toUpperCase() + "\'");
        }
        return ins;
    }
    private Application getApplication(String clusterName)
    {
        DiscoveryClient discoveryClient = DiscoveryManager.getInstance().getDiscoveryClient();
        if (discoveryClient == null) {
            throw new RuntimeException("Failed to get discovery client");
        }

        Application app = discoveryClient.getApplication(clusterName.toUpperCase());
        if (app == null) {
            throw new RuntimeException("Failed to get application \'"
                    + clusterName.toUpperCase() + "\' from discovery");
        }

        return app;
    }

    public static boolean healthy(String host,String clusterName, int port) {
        TransportClient localClient = null;
        try {
            localClient = new TransportClient(ImmutableSettings.settingsBuilder().put("cluster.name", clusterName).build())
                    .addTransportAddress(new InetSocketTransportAddress(host, port));
            ClusterHealthStatus healthStatus = localClient.admin().cluster().prepareHealth().setTimeout("1000").execute().get().getStatus();
            if (healthStatus == ClusterHealthStatus.GREEN || healthStatus == ClusterHealthStatus.YELLOW) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        } finally {
            if (localClient != null) {
                localClient.close();
            }
        }
    }

}
