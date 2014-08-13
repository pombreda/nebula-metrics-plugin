package nebula.plugin.metrics

import groovy.transform.Canonical
import nebula.plugin.metrics.providers.ReportingProviderConfiguration
import nebula.plugin.metrics.providers.elasticsearch.ElasticSearchProviderConfiguration
import nebula.plugin.metrics.providers.text.TextFileProviderConfiguration
import org.gradle.api.GradleException
import org.gradle.process.internal.DefaultJavaForkOptions
import org.gradle.util.ConfigureUtil

@Canonical
class MetricsExtension extends ClassWithForkOptions<MetricsExtension> {

    ReportingProviderConfiguration providerConfiguration

    public MetricsExtension(DefaultJavaForkOptions forkOptions) {
        super(forkOptions)
    }

    @Override
    protected MetricsExtension getCorrectThis() {
        return this;
    }

    private void assertSingleProvider() {
        if (providerConfiguration) {
            throw new GradleException("Only one provider can be used by the metrics plugin")
        }
    }

    /**
     * Use a text file backend
     */
    void file(Closure closure) {
        assertSingleProvider()
        providerConfiguration = new TextFileProviderConfiguration()
        ConfigureUtil.configure(closure, providerConfiguration)
    }

    /**
     * Use a elastic search backend
     * @param closure
     */
    void elasticsearch(Closure closure) {
        assertSingleProvider()
        providerConfiguration = new ElasticSearchProviderConfiguration()
        ConfigureUtil.configure(closure, providerConfiguration)
    }
}
