#!/bin/bash 
seednode="83.212.116.239"
currentNodes=`/opt/apache-cassandra-1.2.6/bin/nodetool -host $seednode status | awk '$1=="UN" { print $2 }'`
echo $currentNodes
removeNode=`echo $currentNodes | awk '{ print $1 }' `
echo "Removing: $removeNode"
if [ "$removeNode" == "$seednode" ]
then
   removeNode=`echo $currentNodes | awk '{ print $2 }' `
   echo "Removing: $removeNode"
fi

ssh $removeNode '/local/apache-cassandra-1.2.6/bin/nodetool decommission'
ssh $removeNode 'rm -rf /local/cassandra/data/data/usertable/data'
ssh $removeNode 'pgrep -f cassandra | xargs kill'

#/opt/apache-cassandra-1.2.6/bin/nodetool -host $seednode status
