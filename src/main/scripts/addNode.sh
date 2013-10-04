#!/bin/bash 
# removed:83.212.116.154, 83.212.117.31 
nodes=" 83.212.116.239 83.212.117.119 83.212.117.123 83.212.117.11 83.212.117.124 83.212.117.108 83.212.117.125 83.212.117.126 "
seednode="83.212.116.239"
currentNodes=`/opt/apache-cassandra-1.2.6/bin/nodetool -host $seednode status | awk '$1=="UN" { print $2 }'`
echo -n "Current Nodes: "
echo $currentNodes
removed="false"
for node in `echo $nodes`
do
  echo $node
  found=`echo $currentNodes | grep -w $node `
  echo $found
  if [ "$found" == "" ]
  then
	echo "Adding: $node"
	ssh $node 'pgrep -f cassandra | xargs kill'
	ssh $node '/local/apache-cassandra-1.2.6/bin/cassandra &'
	removed="true"
	break
  fi 
done

#if [ "$removed" == "true" ]
#then
#   for node in `echo $currentNodes`
#   do
#	echo "Cleaning $node"
#	ssh $node '/local/apache-cassandra-1.2.9/bin/nodetool cleanup'	
#   done
#else
#   echo "No more nodes available"
#fi

#./apache-cassandra-1.2.9/bin/nodetool -host $seednode status
