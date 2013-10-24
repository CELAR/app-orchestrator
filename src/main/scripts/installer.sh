#!/bin/bash
# file used to automatically deploy the resizing scripts and the 
# orchestrator war.

[ -z "$CATALINA_BASE"] && echo "No CATALINA_BASE found, exiting.." && exit 1;

function deploy_war {
	WAR=$(find . -name "*.war");
	cp $WAR $CATALINA_BASE/webapps/;
} 

function deploy_scripts {
	cp scripts/* /usr/local/bin/
}

deploy_scripts
deploy_war

exit 0
