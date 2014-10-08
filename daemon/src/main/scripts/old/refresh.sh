#!/bin/bash 
echo "Refreshing" > /opt/hosts_t
sleep 10
seednode="`getNodes.sh seednode`"
/opt/apache-cassandra-1.2.6/bin/nodetool -host $seednode status | awk '$1=="UN" { print $2 }' > /opt/hosts
rm -f /opt/hosts_t

