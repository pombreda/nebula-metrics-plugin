package nebula.plugin.metrics.vanguard

import org.gradle.api.Project

class BuildContextFactory {
    static BuildContext create(Project project) {
        // This has to be the root project
        if (project != project.rootProject) {
            throw new IllegalArgumentException("Build context has to be built off the root project, not ${project}")
        }

        String user = System.getProperty('user.name')
        String rootProjectName = project.rootProject.name
        return new BuildContext(user, rootProjectName)
    }
}
