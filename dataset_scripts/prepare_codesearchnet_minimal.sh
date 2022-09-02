#!/bin/bash

# Download the dataset
bash prepare_codesearchnet.sh

cd ../datasets/codesearchnet/raw

# Trim the dataset
echo "Trimming the dataset..."

sed -i '1001,$ d' test/test.jsonl
sed -i '1001,$ d' train/train.jsonl
sed -i '1001,$ d' valid/valid.jsonl

echo "Dataset prepared."