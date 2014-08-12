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

import nebula.plugin.metrics.providers.ReportingProvider
import nebula.plugin.metrics.vanguard.BuildContextFactory
import nebula.plugin.metrics.vanguard.ReportingCollector
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.file.FileResolver
import org.gradle.messaging.actor.ActorFactory
import org.gradle.process.internal.DefaultJavaForkOptions
import org.gradle.process.internal.WorkerProcessBuilder

import javax.inject.Inject

class MetricsPlugin implements Plugin<Project> {

    private org.gradle.internal.Factory<WorkerProcessBuilder> processBuilderFactory
    private ActorFactory actorFactory
    private FileResolver fileResolver

    private MetricsExtension metricsExtension

    @Inject
    MetricsPlugin(org.gradle.internal.Factory<WorkerProcessBuilder> processBuilderFactory, ActorFactory actorFactory, FileResolver fileResolver) {
        this.fileResolver = fileResolver
        this.processBuilderFactory = processBuilderFactory
        this.actorFactory = actorFactory
    }

    @Override
    void apply(Project project) {
        // Has to be on root project
        if (project != project.rootProject) {
            throw new GradleException("Metrics plugin can only be applied to root project")
        }

        def globalOptions = new DefaultJavaForkOptions(fileResolver);
        metricsExtension = project.extensions.create('metrics', MetricsExtension, globalOptions)

        project.afterEvaluate {
            if (metricsExtension.providerConfiguration) {
                // Context Object
                def buildContext = BuildContextFactory.create(project)

                // Use extension to create a provider
                ReportingProviderFactory providerFactory = new DefaultReportingProviderFactory()
                ReportingProvider provider = providerFactory.create(metricsExtension.providerConfiguration, buildContext)

                // This has been around for a while now
                ReportingCollector collector = ReportingCollector.getInstance()

                // Open the flood gates
                collector.buffer.setSink(provider)
            }
        }
    }
}
