#!/bin/bash

CELAR_HOME=/opt/celar
CELAR_ORCHESTRATOR_HOME=$CELAR_HOME/celar-orchestrator
UPDATE_SCRIPT="update-orchestrator"

RANDOM_PASSWORD=$(/bin/cat /dev/urandom | tr -dc 0-9a-zA-Z | head -c 16)
KEYSTORE_PATH=$CELAR_ORCHESTRATOR_HOME/keystore.jks
CONF_FILE=$CELAR_ORCHESTRATOR_HOME/conf/orchestrator.properties
SLIPSTREAM_CONTEXT="/opt/slipstream/client/bin/slipstream.context"




install_service(){
	mkdir $CELAR_ORCHESTRATOR_HOME
	mv celar-orchestrator $CELAR_HOME
	mv $UPDATE_SCRIPT /usr/local/bin/

}

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
	/bin/sed -i 's|# server.ssl.keystore.path = |server.ssl.keystore.path = '$KEYSTORE_PATH'|' $CONF_FILE;
	/bin/sed -i "s/# server.ssl.keystore.password = /server.ssl.keystore.password = $RANDOM_PASSWORD/" $CONF_FILE
	/bin/sed -i "s/# server.ssl.port = 443/server.ssl.port = 443/" $CONF_FILE

	# The configuration takes place into the init script
}

create_service(){
	/bin/ln -svf $CELAR_ORCHESTRATOR_HOME/scripts/celar-orchestrator /etc/init.d/
	/bin/chmod +x $CELAR_ORCHESTRATOR_HOME/scripts/celar-orchestrator
}

install_pip_dependencies() {
	# this installs the SS client -- please use the pip repo when stable
	pip install https://pypi.python.org/packages/source/s/slipstream-client/slipstream-client-2.3.6.tar.gz
	pip install httplib2
}

install_service
create_keystore;
configure_server;
create_service;
install_pip_dependencies

service celar-orchestrator start;

