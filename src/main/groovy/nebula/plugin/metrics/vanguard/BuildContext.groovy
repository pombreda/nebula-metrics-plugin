package nebula.plugin.metrics.vanguard

import groovy.transform.ToString
import groovy.transform.TupleConstructor

@TupleConstructor
@ToString
@groovy.transform.Immutable
class BuildContext {
    String user
    String rootProjectName


}
