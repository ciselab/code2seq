#!/bin/bash

# set -e makes the shell script exit if any command exists with non-zero exit code
set -e

if [ "$preprocess" = true ]; 
then bash preprocess.sh -d "$dataset" 
else echo "Not preprocessing."
fi

if [ "$train" = true -a "$trainFromScratch" = true ]; 
then bash train.sh  "$dataset" 
else echo "Not training a new model."
fi

# How to keep the container open even if it errors:
# tail -f /dev/null