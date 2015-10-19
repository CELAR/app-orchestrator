#!/bin/bash


[ "$#" -lt 2 ] && echo "I need username and password to be set for building " && exit 1

git add . && git commit -m 'debugging' && git push && curl  -ik -X POST http://83.212.116.20/job/build_app-orchestrator/build --user $1:$2

for i in $(seq 1 20); do 
	curl -s http://83.212.116.20/job/build_app-orchestrator//lastBuild/api/json | grep "\"building\":true"  | wc -l; sleep 5; 
done
