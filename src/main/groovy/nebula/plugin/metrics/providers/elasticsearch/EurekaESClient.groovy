package nebula.plugin.metrics.providers.elasticsearch

import com.google.common.collect.Sets
import com.netflix.appinfo.InstanceInfo
import com.netflix.discovery.DiscoveryClient
import com.netflix.discovery.DiscoveryManager
import com.netflix.discovery.shared.Application
import org.elasticsearch.action.admin.cluster.health.ClusterHealthStatus
import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

public class EurekaESClient {
    private Logger log = LoggerFactory.getLogger(EurekaESClient.class);

    private TransportClient client;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> checkerHandle;
    private final Properties props;
    private final String clusterName;
    private final int port;

    public EurekaESClient(String clusterName, int port, String hostname) {
        this(clusterName, port, new )
        this.clusterName = clusterName;
        this.port = port;                             // 7102
        initialize();
    }

    public EurekaESClient(String clusterName, int port, ESRegistry registry) {
        this.clusterName = clusterName;
        this.port = port;                             // 7102
        initialize(registry);
    }

    private void initialize(ESRegistry registry) {
        Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", clusterName).build();
        client = new TransportClient(settings);
        registry.updateESRegistry(client, clusterName);
        final Runnable checker = new Runnable() {
            public void run() {
                registry.updateESRegistry(client, clusterName);
            }
        };
        checkerHandle = scheduler.scheduleAtFixedRate(checker, 60, 60, TimeUnit.SECONDS);
    }

    public void shutdown() {
        client.close();
        checkerHandle.cancel(true);
    }

    public Client getClient() {
        return client;
    }
}