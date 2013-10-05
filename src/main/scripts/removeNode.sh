#!/bin/bash 

LOG_OUTPUT="/tmp/REMOVEVM`date +"%Y%m%d%H%M%S"`.log"

function remove {
	ssh $1 '/local/apache-cassandra-1.2.6/bin/nodetool decommission' > $LOG_OUTPUT
	ssh $1 'rm -rf /local/cassandra/data/data/usertable/data' >> $LOG_OUTPUT
	ssh $1 'pgrep -f cassandra | xargs kill' >> $LOG_OUTPUT
}

seednode="83.212.116.239"
currentNodes=`/opt/apache-cassandra-1.2.6/bin/nodetool -host $seednode status | awk '$1=="UN" { print $2 }'`
removeNode=`echo $currentNodes | awk '{ print $1 }' `
if [ "$removeNode" == "$seednode" ]
then
   removeNode=`echo $currentNodes | awk '{ print $2 }' `
fi
echo $removeNode

remove $removeNode &

#/opt/apache-cassandra-1.2.6/bin/nodetool -host $seednode status
