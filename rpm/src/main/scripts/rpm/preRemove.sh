#!/bin/bash

service tomcat stop		# stop tomcat to remove a webapp

WAR_PATH=$CATALINA_HOME/webapps/celar-orchestrator.war
DIR_PATH=$CATALINA_HOME/webapps/celar-orchestrator

rm -rf $WAR_PATH $DIR_PATH

exit 0
