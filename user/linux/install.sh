#!/bin/bash

export MVN_HOME=/data/3rd_lib/apache-maven-3.3.9
export path_main=/data/publish_application
export project_name=qfsoft_app_atev_code

cd $path_main/$project_name
$MVN_HOME/bin/mvn clean install -Dmaven.test.skip=true
