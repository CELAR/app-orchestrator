#!/bin/bash 
seednode="83.212.116.239"
currentNodes=`/opt/apache-cassandra-1.2.6/bin/nodetool -host $seednode status | awk '$1=="UN" { print $2 }'`
for i in $currentNodes; do
	echo $i;
done
