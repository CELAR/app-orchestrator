#!/bin/bash

service celar-orchestrator stop
kill -9 $(cat /tmp/celar-orchestrator.pid)
rm  -rf /etc/init.d/celar-orchestrator
rm  -rf /opt/celar/celar-orchestrator/keystore.jks
rm  -rf /tmp/celar-orchestrator.pid

