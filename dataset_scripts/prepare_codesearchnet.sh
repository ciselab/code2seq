#!/bin/bash

# This file downloads and prepares the CodeSearchNet dataset specifically.
# Install the unzip package beforehand if your distribution does not have it

mkdir -p ../datasets/codesearchnet

# Copy and unzip files required for preparing the dataset
cp codesearchnet.zip ../datasets/codesearchnet
cd ../datasets/codesearchnet
unzip codesearchnet.zip
rm codesearchnet.zip

rm -rf raw && mv ./dataset ./raw 
cd raw

# ------------ 
# Download and unzip dataset
wget https://s3.amazonaws.com/code-search-net/CodeSearchNet/v2/java.zip

# Or: copy an existing dataset
# cp ../../../data/java.zip ./java.zip
# ------------ 

unzip java.zip 

# # Remove the now redundant archive file
rm *.zip

# # Run script to finalize the dataset unzipping
python preprocess.py
rm -rf */final
rm -rf java*
rm preprocess.py

echo "Codesearchnet dataset prepared."
exit