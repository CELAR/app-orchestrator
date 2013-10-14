#!/bin/bash 

LOG_OUTPUT="/tmp/orchestrator/addvm.log"

function addnode {
	echo "`date`: adding started ($1) ">> $LOG_OUTPUT
	ssh $1 'pgrep -f cassandra | xargs kill' >> $LOG_OUTPUT 
	ssh $1 '/local/apache-cassandra-1.2.6/bin/cassandra &' >> $LOG_OUTPUT 
	echo "`date`: adding done ($1) ">> $LOG_OUTPUT
}


# not working nodes right now: 83.212.117.119 83.212.116.154 
nodes="83.212.116.239  83.212.117.11 83.212.117.124 83.212.117.108 83.212.117.125 83.212.117.126 83.212.117.31 83.212.117.123"
seednode="83.212.116.239"
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
