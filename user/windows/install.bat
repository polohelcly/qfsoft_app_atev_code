
set MVN_HOME=d:\3rd_lib\apache-maven-3.3.9
set path_main=d:\data\publish_application
set project_name=qfsoft_app_atev_code

cd %path_main%\%project_name%
%MVN_HOME%\bin\mvn clean install -Dmaven.test.skip=true
