/*
 * Copyright 2014 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nebula.plugin.metrics

import nebula.plugin.metrics.vanguard.Interval
import nebula.plugin.metrics.vanguard.ReportingCollector
import nebula.test.ProjectSpec
import org.joda.time.DateTime

class MetricsPluginSpec extends ProjectSpec {

    def setup() {
        ReportingCollector.getInstance().reset()
    }

    @Override
    void getPluginName() {
        'nebula-metrics'
    }

    def 'configure plugin'() {
        when:
        def interval = new Interval(new DateTime().minusMinutes(1), new DateTime())
        ReportingCollector.record(interval)

        File destMetrics = new File(project.buildDir, 'metric.txt')
        project.plugins.apply(MetricsPlugin)
        project.metrics {
            file {
                destination = destMetrics
            }
        }

        then:
        !destMetrics.exists()
        ReportingCollector.getInstance().buffer.metrics.size() == 1

        when:
        project.evaluate()

        then:
        Thread.sleep(150)
        // Should have drained by now
        ReportingCollector.getInstance().buffer.metrics.size() == 0

        destMetrics.exists()
        destMetrics.text == ''
    }
}
