package nebula.plugin.metrics.providers.elasticsearch

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthStatus;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.collect.Sets;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.DiscoveryManager;
import com.netflix.discovery.shared.Application;

public class ESClient {
    private Logger log = LoggerFactory.getLogger(ESClient.class);

    private TransportClient client;
    private Set<InstanceInfo> esServerSet = new HashSet<InstanceInfo>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> checkerHandle;
    private final Properties props;
    private final String clusterName;
    private final int port;

    public ESClient(String clusterName, int port) {
        this.clusterName = clusterName;
        this.port = port;                             // 7102
        initialize();
    }

    private void initialize() {
        Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", clusterName).build();
        client = new TransportClient(settings);updateESRegistry();
        final Runnable checker = new Runnable() {
            public void run() {
                updateESRegistry();
            }
        };
        checkerHandle = scheduler.scheduleAtFixedRate(checker, 60, 60, TimeUnit.SECONDS);
    }

    private void updateESRegistry() {
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

    public void shutdown() {
        client.close();
        checkerHandle.cancel(true);
    }

    public Client getClient() {
        return client;
    }
}