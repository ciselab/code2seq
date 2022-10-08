#!/bin/bash

# set -e makes the shell script exit if any command exists with non-zero exit code
set -e

if [ "$preprocess" = true ]; 
then bash preprocess.sh --dataset="$dataset" --include_comments="$includeComments" --exclude_stopwords="$excludeStopwords" --include_tfidf="$useTfidf" --number_keywords="$numberOfTfidfKeywords"
else echo "Not preprocessing."
fi

if [ "$train" = true ]; 
then bash train.sh --dataset="$dataset" --continue_training_from_checkpoint="$continueTrainingFromCheckpoint"
else echo "Not training a new model."
fi

# How to keep the container open even if it errors:
# tail -f /dev/null