#!/bin/bash

# This file downloads and prepares the FunCom dataset specifically.

mkdir -p ../datasets/funcom/raw

cd ../datasets/funcom/raw

# ------------ 
# Download and unzip tokenized and filtered datasets
wget https://s3.us-east-2.amazonaws.com/leclair.tech/data/funcom/funcom_filtered.tar.gz
wget https://s3.us-east-2.amazonaws.com/leclair.tech/data/funcom/funcom_tokenized.tar.gz

# Or: copy an existing dataset
# cp ../../../data/funcom_filtered.tar.gz funcom_filtered.tar.gz
# cp ../../../data/funcom_tokenized.tar.gz funcom_tokenized.tar.gz
# ------------ 

cp ../../../dataset_scripts/funcom_shuffle.py funcom_shuffle.py

# Unzip the downloaded archives
tar xvf funcom_filtered.tar.gz
tar xvf funcom_tokenized.tar.gz

# Copy required files from both datasets
cp ./funcom_processed/functions.json functions.json
cp ./funcom_tokenized/comments comments
cp ./funcom_tokenized/fid_pid fid_pid

# Create training directories
mkdir test
mkdir train
mkdir valid

# Run the shuffle file which creates training data distributions
python funcom_shuffle.py

# Remove the now redundant files
rm *.tar.gz
rm -rf funcom*
rm comments
rm fid_pid
rm functions.json

echo "Funcom dataset prepared."
exit