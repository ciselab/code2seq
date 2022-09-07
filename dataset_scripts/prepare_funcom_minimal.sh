#!/bin/bash

# Download the dataset
bash prepare_funcom.sh

cd ../datasets/funcom/raw

# Trim the dataset
echo "Trimming the dataset..."

echo "$(head -n 50 test/functions.test.jsonl)" > test/functions.test.jsonl
echo "$(head -n 50 train/functions.train.jsonl)" > train/functions.train.jsonl
echo "$(head -n 50 valid/functions.val.jsonl)" > valid/functions.val.jsonl

echo "Dataset prepared."