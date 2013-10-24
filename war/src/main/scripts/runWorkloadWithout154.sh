#!/bin/bash
nodes="83.212.121.127 83.212.115.199 83.212.125.101 83.212.117.118 83.212.117.119 83.212.117.127"
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
		ssh $node '/opt/ycsb-0.1.4/bin/ycsb run cassandra-10 -P /opt/ycsb-0.1.4/workloads/workloada  -threads 20 -p maxexecutiontime=1000000000 -p hostsFile=/opt/hosts -p operationcount=1000000000 -p recordcount=10000 -p target=750 -p sinusoidal=true -p period=7200 -p offset=700 -P /opt/ycsb-0.1.4/XProbe.properties -s > /opt/ycsb-0.1.4/logs 2> /opt/ycsb-0.1.4/logs &'
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
	for y in $nodes; do
		for i in `ssh  $y "ps aux | grep Client" | awk '{print $2}'`; do ssh $y "kill -9 $i" 2>/dev/null; done
		echo "Workload from $y killed";
	done
	echo "Killed all"
}

function workload_status {
	let x=3
	for i in $nodes; do
		[ "`ssh $i "jps | grep -i client"`" = "" ] && echo "Not running (node $i)" && let x=x-1;
	done
	[ $x -eq 3 ] && echo "Everyting works";
}

LOGFILE="/tmp/WORKLOAD_LOGS.txt"
case $1 in
	kill)
		echo "Killing workload";
		kill_workload	;
		echo "`date`: killed workload" >> $LOGFILE
		pkill runWorkload.sh; 
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
