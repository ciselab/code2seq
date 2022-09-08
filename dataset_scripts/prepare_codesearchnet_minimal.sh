#!/bin/bash

FILE=../datasets/codesearchnet-minimal.tar.gz
DATASETFOLDER=../datasets
if [ -f "$FILE" ]; 
then
    echo "$FILE exists - unpacking it"
    tar -xvf $FILE --directory $DATASETFOLDER
else 
    # Download the dataset
    bash prepare_codesearchnet.sh

    cd ../datasets/codesearchnet/raw

    # Trim the dataset
    echo "Trimming the dataset..."

    echo "$(head -n 50 test/test.jsonl)" > test/test.jsonl
    echo "$(head -n 50 train/train.jsonl)" > train/train.jsonl
    echo "$(head -n 50 valid/valid.jsonl)" > valid/valid.jsonl
fi

echo "Dataset prepared."