#!/bin/bash

# Download the dataset
bash prepare_codesearchnet.sh

cd ../datasets/codesearchnet/raw

# Trim the dataset
echo "Trimming the dataset..."

echo "$(head -n 1000 test/test.jsonl)" > test/test.jsonl
echo "$(head -n 1000 train/train.jsonl)" > train/train.jsonl
echo "$(head -n 1000 valid/valid.jsonl)" > valid/valid.jsonl

echo "Dataset prepared."