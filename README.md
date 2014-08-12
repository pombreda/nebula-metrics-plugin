nebula-metrics-plugin
==============

Gather metric and build info into ElasticSearch

## Usage

### Applying the Plugin

To include, add the following to your build.gradle

    buildscript {
      repositories { jcenter() }

      dependencies {
        classpath 'com.netflix.nebula:nebula-metrics-plugin:1.12.+'
      }
    }

    apply plugin: 'nebula-metrics'

### Tasks Provided

`<your tasks>`

### Extensions Provided

`<your extensions>`
