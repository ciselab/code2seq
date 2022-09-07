#!/bin/bash

# set -e makes the shell script exit if any command exists with non-zero exit code
set -e

if [ "$preprocess" = true ]; 
then bash preprocess.sh 
else echo "Not preprocessing."
fi

if [ "$train" = true -a "$trainFromScratch" = true ]; 
then bash train.sh
else echo "Not training a new model."
fi