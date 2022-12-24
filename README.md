Jenkins Log File Filter Plugin
===============================

[![Jenkins Build Status](https://ci.jenkins.io/buildStatus/icon?job=Plugins%2Flog-file-filter-plugin%2Fmain)](https://ci.jenkins.io/job/Plugins/job/log-file-filter-plugin/job/main/)
[![Jenkins Plugins](https://img.shields.io/jenkins/plugin/v/log-file-filter)](https://plugins.jenkins.io/log-file-filter/)
[![Jenkins Plugin installs](https://img.shields.io/jenkins/plugin/i/log-file-filter)](https://plugins.jenkins.io/log-file-filter/)


This plugin allows filtering Jenkins' console output by means of regular expressions. Some expressions are included by default; to see which are the default expressions go to the Jenkins global settings and click the "Enable default regexp" help icon. They can be turned off by unchecking the "Enable default regexp" checkbox.

To define a custom filter, go to the Jenkins global settings and add a new entry in the "Custom regexp pairs" setting. The regexes follow the conventions explained in [the Java "Pattern" class](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html#sum).

To disable the filtering, just uncheck the "Enable globally" checkbox in the global settings. Note that not even the default regexp will work.

# Configuring the plugin

Note that the filtering is applied to all projects.

![Global configuration](readmeImages/globalConfig.png "Global configuration")

![Global configuration](readmeImages/projectConfigWindowsBatch.png "Sample logging")
 
![Global configuration](readmeImages/consoleOutput.png "Console output")


# Pipeline usage

To enable the log file filter inside a pipeline, simply surround the code with the logFileFilter call:

```groovy
...
logFileFilter {
    ...
  echo 'This is a filtered SECRET'
    ...
}
echo 'Log file filter is not active here SECRET'
...
```
