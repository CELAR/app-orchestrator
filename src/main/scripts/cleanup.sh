#!/bin/bash

LOG_OUTPUT="/tmp/CLEANUP`date +"%Y%m%d%H%M%S"`.log"

TMP_FILE="/tmp/cleanup_process.tmp"

function cleanup {
	touch $TMP_FILE
	seednode="83.212.116.239"
	currentNodes=`/opt/apache-cassandra-1.2.6/bin/nodetool -host $seednode status | awk '$1=="UN" { print $2 }'`
	for node in `echo $currentNodes`
	do
	   echo "Cleaning $node" >> $LOG_OUTPUT
	   ssh $node '/local/apache-cassandra-1.2.6/bin/nodetool cleanup'  >> $LOG_OUTPUT
	done
	rm $TMP_FILE
}

function cleanup_terminated {
	[ -e "$TMP_FILE" ] && echo false && exit 0;		# one liner if -- reference gia to mellon npapa
	echo true
}

case $1 in
	finished)
		cleanup_terminated
		;;
	*)
		cleanup &
		;;
esac
