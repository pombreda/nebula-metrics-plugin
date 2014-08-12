package nebula.plugin.metrics.vanguard

import groovy.transform.Canonical
import org.joda.time.DateTime
import org.joda.time.Duration

@Canonical
class Interval implements Metric {
    DateTime start
    DateTime end

    long getElapsed() {
        new Duration(start, end).millis
    }


    @Override
    public String toString() {
        return "Timer{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}
