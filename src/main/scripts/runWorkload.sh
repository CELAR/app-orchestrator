#!/bin/bash
nodes="83.212.121.127 83.212.115.199 83.212.125.101"
echo -n "Client Nodes: "
echo $nodes
function run_workload {
	for node in `echo $nodes`
	do
       	  echo "Refresh: $node"
       	  ssh $node '/opt/refresh.sh'
	done 

	for node in `echo $nodes`
	do
		echo "Starting: $node"
		ssh $node '/opt/ycsb-0.1.4/bin/ycsb run cassandra-10 -P /opt/ycsb-0.1.4/workloads/workloada  -threads 20 -p maxexecutiontime=1000000000 -p hostsFile=/opt/hosts -p operationcount=1000000000 -p recordcount=1000 -p target=1000 -p sinusoidal=true -p period=3600 -p offset=1000 -P /opt/ycsb-0.1.4/XProbe.properties -s > /opt/ycsb-0.1.4/logs 2> /opt/ycsb-0.1.4/logs &'
	done

	echo "Started"
	while [ true ]
	do
		for node in `echo $nodes`
		do
			echo "Refresh: $node"
			ssh $node '/opt/refresh.sh'
		done    
		sleep 60
	done
}

function kill_workload {
	for i in $nodes; do
		ssh $i "pkill java";
		echo "Workload from $i killed";
	done
	echo "Killed all"
}

function workload_status {
	for i in $nodes; do
		[ "`ssh $i "jps | grep -i client"`" = "" ] && echo "Not running (node $i)";
	done
}

LOGFILE="/tmp/WORKLOAD_LOGS.txt"
case $1 in
	kill)
		echo "Killing workload";
		kill_workload	;
		echo "`date`: killed workload" >> $LOGFILE
		;;
	status)
		echo "`date`: took workload status command" >> $LOGFILE;
		workload_status ;
		;;
	*)
		echo "Starting workload";
		echo "`date`: new workload" >> $LOGFILE;
		run_workload >> $LOGFILE &
		;;
esac
