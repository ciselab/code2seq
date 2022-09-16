#!/bin/bash

# This file downloads and prepares the default dataset specifically.

mkdir -p ../datasets/default/raw

cd ../datasets/default/raw

# ------------ 
# Download and unzip tokenized and filtered datasets
# wget https://s3.amazonaws.com/code2seq/datasets/java-small.tar.gz

# Or: copy an existing dataset
cp ../../../data/java-small.tar.gz java-small.tar.gz
# ------------ 

# Unzip the downloaded archive
tar xvf java-small.tar.gz

# Move and rename folders
mv java-small/test ./test
mv java-small/training ./train
mv java-small/validation ./valid


# # Remove the now redundant files
rm *.tar.gz
rm -rf java*

# echo "Dataset downloaded and prepared."
exit