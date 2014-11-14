#!/bin/bash

service celar-orchestrator stop
rm  -rf /etc/init.d/celar-orchestrator
rm  -rf /opt/celar/celar-orchestrator/keystore.jks
rm  -rf /tmp/celar-orchestrator.pid

