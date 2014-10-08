#!/bin/bash

RANDOM_PASSWORD=$(/bin/cat /dev/urandom | tr -dc 0-9a-zA-Z | head -c 16)
CELAR_ORCHESTRATOR_HOME=/opt/celar/celar-orchestrator
KEYSTORE_PATH=$CELAR_ORCHESTRATOR_HOME/keystore.jks
CONF_FILE=$CELAR_ORCHESTRATOR_HOME/conf/orchestrator.properties


SLIPSTREAM_CONTEXT=/tmp/slipstream.context

create_keystore(){
/usr/bin/keytool -genkey \
-keyalg RSA \
-alias celar-orchestrator \
-keypass $RANDOM_PASSWORD \
-storepass $RANDOM_PASSWORD \
-keystore $KEYSTORE_PATH \
-validity 730 << EOF
Giannis Giannakopoulos
ATHENA
CELAR
Europe
Europe
EU
yes
EOF
}

configure_server(){
#/bin/sed -i 's|# server.ssl.keystore.path = |server.ssl.keystore.path = '$KEYSTORE_PATH'|' $CONF_FILE;
#/bin/sed -i "s/# server.ssl.keystore.password = /server.ssl.keystore.password = $RANDOM_PASSWORD/" $CONF_FILE
#/bin/sed -i "s/# server.ssl.port = 8443/server.ssl.port = 8443/" $CONF_FILE

DEPLOYMENT_ID=$(grep diid $SLIPSTREAM_CONTEXT | awk '{print $3}')
SS_SERVER_IP=$(grep serviceurl $SLIPSTREAM_CONTEXT  |  tr '/' '\t' | awk '{print $4}')
/bin/sed -i "s/# slipstream.deployment.id = /slipstream.deployment.id = $DEPLOYMENT_ID/" $CONF_FILE
/bin/sed -i "s/# slipstream.server.host = /slipstream.server.host = $SS_SERVER_IP/" $CONF_FILE
/bin/sed -i "s/# celar.server.host = /celar.server.host = $SS_SERVER_IP/" $CONF_FILE
}

create_service(){
        /bin/ln -sv $CELAR_ORCHESTRATOR_HOME/bin/celar-orchestrator /etc/init.d/
}
create_keystore;

configure_server;

create_service

/bin/rm $CELAR_ORCHESTRATOR_HOME/lib/slf4j-jdk14-1.4.2.jar

service celar-orchestrator start;

