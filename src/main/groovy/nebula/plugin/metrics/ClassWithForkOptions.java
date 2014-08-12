package nebula.plugin.metrics;

import org.gradle.api.file.FileCollection;
import org.gradle.process.JavaForkOptions;
import org.gradle.process.ProcessForkOptions;
import org.gradle.process.internal.DefaultJavaForkOptions;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * From TNG/remote-executor-gradle-plugin
 * @param <CURRENT_CLASS>
 */
public abstract class ClassWithForkOptions<CURRENT_CLASS extends ClassWithForkOptions> {
    private DefaultJavaForkOptions forkOptions;

    public ClassWithForkOptions(DefaultJavaForkOptions forkOptions) {
        this.forkOptions = forkOptions;
    }

    protected abstract CURRENT_CLASS getCorrectThis();

    /**
     * {@inheritDoc}
     */
    public File getWorkingDir() {
        return forkOptions.getWorkingDir();
    }

    /**
     * {@inheritDoc}
     */
    public void setWorkingDir(Object dir) {
        forkOptions.setWorkingDir(dir);
    }

    /**
     * {@inheritDoc}
     */
    public CURRENT_CLASS workingDir(Object dir) {
        forkOptions.workingDir(dir);
        return getCorrectThis();
    }

    /**
     * {@inheritDoc}
     */
    public String getExecutable() {
        return forkOptions.getExecutable();
    }

    /**
     * {@inheritDoc}
     */
    public CURRENT_CLASS executable(Object executable) {
        forkOptions.executable(executable);
        return getCorrectThis();
    }

    /**
     * {@inheritDoc}
     */
    public void setExecutable(Object executable) {
        forkOptions.setExecutable(executable);
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, Object> getSystemProperties() {
        return forkOptions.getSystemProperties();
    }

    /**
     * {@inheritDoc}
     */
    public void setSystemProperties(Map<String, ?> properties) {
        forkOptions.setSystemProperties(properties);
    }

    /**
     * {@inheritDoc}
     */
    public CURRENT_CLASS systemProperties(Map<String, ?> properties) {
        forkOptions.systemProperties(properties);
        return getCorrectThis();
    }

    /**
     * {@inheritDoc}
     */
    public CURRENT_CLASS systemProperty(String name, Object value) {
        forkOptions.systemProperty(name, value);
        return getCorrectThis();
    }

    /**
     * {@inheritDoc}
     */
    public FileCollection getBootstrapClasspath() {
        return forkOptions.getBootstrapClasspath();
    }

    /**
     * {@inheritDoc}
     */
    public void setBootstrapClasspath(FileCollection classpath) {
        forkOptions.setBootstrapClasspath(classpath);
    }

    /**
     * {@inheritDoc}
     */
    public CURRENT_CLASS bootstrapClasspath(Object... classpath) {
        forkOptions.bootstrapClasspath(classpath);
        return getCorrectThis();
    }

    /**
     * {@inheritDoc}
     */
    public String getMinHeapSize() {
        return forkOptions.getMinHeapSize();
    }

    /**
     * {@inheritDoc}
     */
    public String getDefaultCharacterEncoding() {
        return forkOptions.getDefaultCharacterEncoding();
    }

    /**
     * {@inheritDoc}
     */
    public void setDefaultCharacterEncoding(String defaultCharacterEncoding) {
        forkOptions.setDefaultCharacterEncoding(defaultCharacterEncoding);
    }

    /**
     * {@inheritDoc}
     */
    public void setMinHeapSize(String heapSize) {
        forkOptions.setMinHeapSize(heapSize);
    }

    /**
     * {@inheritDoc}
     */
    public String getMaxHeapSize() {
        return forkOptions.getMaxHeapSize();
    }

    /**
     * {@inheritDoc}
     */
    public void setMaxHeapSize(String heapSize) {
        forkOptions.setMaxHeapSize(heapSize);
    }

    /**
     * {@inheritDoc}
     */
    public List<String> getJvmArgs() {
        return forkOptions.getJvmArgs();
    }

    /**
     * {@inheritDoc}
     */
    public void setJvmArgs(Iterable<?> arguments) {
        forkOptions.setJvmArgs(arguments);
    }

    /**
     * {@inheritDoc}
     */
    public CURRENT_CLASS jvmArgs(Iterable<?> arguments) {
        forkOptions.jvmArgs(arguments);
        return getCorrectThis();
    }

    /**
     * {@inheritDoc}
     */
    public CURRENT_CLASS jvmArgs(Object... arguments) {
        forkOptions.jvmArgs(arguments);
        return getCorrectThis();
    }

    /**
     * {@inheritDoc}
     */
    public boolean getEnableAssertions() {
        return forkOptions.getEnableAssertions();
    }

    /**
     * {@inheritDoc}
     */
    public void setEnableAssertions(boolean enabled) {
        forkOptions.setEnableAssertions(enabled);
    }

    /**
     * {@inheritDoc}
     */
    public boolean getDebug() {
        return forkOptions.getDebug();
    }

    /**
     * {@inheritDoc}
     */
    public void setDebug(boolean enabled) {
        forkOptions.setDebug(enabled);
    }

    /**
     * {@inheritDoc}
     */
    public List<String> getAllJvmArgs() {
        return forkOptions.getAllJvmArgs();
    }

    /**
     * {@inheritDoc}
     */
    public void setAllJvmArgs(Iterable<?> arguments) {
        forkOptions.setAllJvmArgs(arguments);
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, Object> getEnvironment() {
        return forkOptions.getEnvironment();
    }

    /**
     * {@inheritDoc}
     */
    public CURRENT_CLASS environment(Map<String, ?> environmentVariables) {
        forkOptions.environment(environmentVariables);
        return getCorrectThis();
    }

    /**
     * {@inheritDoc}
     */
    public CURRENT_CLASS environment(String name, Object value) {
        forkOptions.environment(name, value);
        return getCorrectThis();
    }

    /**
     * {@inheritDoc}
     */
    public void setEnvironment(Map<String, ?> environmentVariables) {
        forkOptions.setEnvironment(environmentVariables);
    }

    /**
     * {@inheritDoc}
     */
    public CURRENT_CLASS copyTo(ProcessForkOptions target) {
        forkOptions.copyTo(target);
        return getCorrectThis();
    }

    /**
     * {@inheritDoc}
     */
    public CURRENT_CLASS copyTo(JavaForkOptions target) {
        forkOptions.copyTo(target);
        return getCorrectThis();
    }}
