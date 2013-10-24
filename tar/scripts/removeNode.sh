#!/bin/bash 

LOG_OUTPUT="/tmp/orchestrator/removevm.log"

function remove {
	echo "`date`: removing started ($1) ">> $LOG_OUTPUT
	ssh $1 '/local/apache-cassandra-1.2.6/bin/nodetool decommission' >> $LOG_OUTPUT
	ssh $1 'pgrep -f cassandra | xargs kill' >> $LOG_OUTPUT
	ssh $1 'rm -rf /local/cassandra/data' >> $LOG_OUTPUT
	echo "`date`: removing done ($1) ">> $LOG_OUTPUT
}

#seednode="83.212.116.239"
seednode="`getNodes.sh seednode`"
currentNodes=`/opt/apache-cassandra-1.2.6/bin/nodetool -host $seednode status | awk '$1=="UN" { print $2 }'`
removeNode=`echo $currentNodes | awk '{ print $1 }' `
if [ "$removeNode" == "$seednode" ]
then
   removeNode=`echo $currentNodes | awk '{ print $2 }' `
fi
[ "$removeNode" == "" ] && echo "no nodes left" >&2 && exit 0

echo $removeNode
remove $removeNode &

#/opt/apache-cassandra-1.2.6/bin/nodetool -host $seednode status
