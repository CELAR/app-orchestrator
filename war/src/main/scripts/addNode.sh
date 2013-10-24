#!/bin/bash 

LOG_OUTPUT="/tmp/orchestrator/addvm.log"

function addnode {
	echo "`date`: adding started ($1) ">> $LOG_OUTPUT
	ssh $1 'pgrep -f cassandra | xargs kill' >> $LOG_OUTPUT 
	ssh $1 '/local/apache-cassandra-1.2.6/bin/cassandra &' >> $LOG_OUTPUT 
	echo "`date`: adding done ($1) ">> $LOG_OUTPUT
}

nodes="`getNodes.sh cassandra`"
#nodes="83.212.116.239  83.212.117.11 83.212.117.124 83.212.117.108 83.212.117.125 83.212.117.126 83.212.117.31 83.212.117.123 83.212.106.9 83.212.122.156 83.212.111.233 83.212.125.226"
seednode="`getNodes.sh seednode`"
#seednode="83.212.116.239"
currentNodes=`/opt/apache-cassandra-1.2.6/bin/nodetool -host $seednode status | awk '$1=="UN" { print $2 }'`

removed="false"
for node in `echo $nodes`
do
  found=`echo $currentNodes | grep -w $node `
  if [ "$found" == "" ]
  then
	echo $node
	addnode $node &
	removed="true"
	exit 0
  fi 
done

echo "no nodes left" >&2
