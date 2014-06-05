#!/bin/bash


ORCH_PATH=/opt/celar/celar-orchestrator/
WAR_PATH=$ORCH_PATH/celar-orchestrator.war
SCR_PATH=$ORCH_PATH/scripts/


ln -s $WAR_PATH $CATALINA_HOME/webapps/
ln -s $SCR_PATH/*.sh /usr/local/bin/

exit 0
