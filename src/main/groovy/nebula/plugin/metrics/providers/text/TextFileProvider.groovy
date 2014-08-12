package nebula.plugin.metrics.providers.text

import nebula.plugin.metrics.providers.ReportingProvider
import nebula.plugin.metrics.vanguard.BuildContext
import nebula.plugin.metrics.vanguard.Metric

/**
 * Tab-separated output
 * TODO JSON output
 */
class TextFileProvider implements ReportingProvider {
    BuildContext buildContext
    Writer writer

    TextFileProvider(TextFileProviderConfiguration textConfiguration, BuildContext buildContext) {
        this.buildContext = buildContext
        File dest = textConfiguration.destination
        if (!dest.parentFile.exists()) {
            dest.parentFile.mkdirs()
        }
        writer = new BufferedWriter(new PrintWriter(dest))
    }

    @Override
    void report(Iterable iterable) {
        for(Metric metric: iterable) {
            try {
                // Naive printing
                writer.println("${buildContext}\t${metric}")
            } catch(IOException ioe) {
                // TODO Log this
            }
        }
    }

    @Override
    void flush() {
        writer.close()
    }
}
