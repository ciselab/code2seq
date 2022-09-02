#!/bin/bash

# Download the dataset
bash prepare_funcom.sh

cd ../datasets/funcom/raw

# Trim the dataset
echo "Trimming the dataset..."

sed -i '1001,$ d' test/functions.test.jsonl
sed -i '1001,$ d' train/functions.train.jsonl
sed -i '1001,$ d' valid/functions.val.jsonl

echo "Dataset prepared."