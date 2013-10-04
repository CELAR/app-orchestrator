#!/bin/bash 

seednode="83.212.110.122"
node=$1
echo "Adding : $node"
ssh $node '/local/apache-cassandra-1.2.9/bin/cassandra &'

./apache-cassandra-1.2.9/bin/nodetool -host $seednode status							
