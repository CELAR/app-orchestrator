#!/bin/bash

# the first argument must be the prefix
BUFFER=""; 
for i in `cat /etc/hosts | grep $1`; do 
	BUFFER=$BUFFER"`echo $i | grep \"83.212\"` "; 
done; 
echo $BUFFER

exit 0
