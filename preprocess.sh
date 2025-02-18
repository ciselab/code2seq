#!/usr/bin/env bash
###########################################################
# Change the following values to preprocess a new dataset.
# TRAIN_DIR, VAL_DIR and TEST_DIR should be paths to      
#   directories containing sub-directories with .java files
# DATASET_NAME is just a name for the currently extracted 
#   dataset.                                              
# MAX_DATA_CONTEXTS is the number of contexts to keep in the dataset for each 
#   method (by default 1000). At training time, these contexts
#   will be downsampled dynamically to MAX_CONTEXTS.
# MAX_CONTEXTS - the number of actual contexts (by default 200) 
# that are taken into consideration (out of MAX_DATA_CONTEXTS)
# every training iteration. To avoid randomness at test time, 
# for the test and validation sets only MAX_CONTEXTS contexts are kept 
# (while for training, MAX_DATA_CONTEXTS are kept and MAX_CONTEXTS are
# selected dynamically during training).
# SUBTOKEN_VOCAB_SIZE, TARGET_VOCAB_SIZE -   
#   - the number of subtokens and target words to keep 
#   in the vocabulary (the top occurring words and paths will be kept). 
# NUM_THREADS - the number of parallel threads to use. It is 
#   recommended to use a multi-core machine for the preprocessing 
#   step and set this value to the number of cores.
# PYTHON - python3 interpreter alias.

# set -e makes the shell script exit if any command exists with non-zero exit code
set -e

# Default preprocessing values
DATASET_NAME=default
VARIANT=default
INCLUDE_COMMENTS=true
EXCLUDE_STOPWORDS=false
USE_TFIDF=false
NUMBER_OF_TFIDF_KEYWORDS=45

# This code block is used to get long two-dash arguments from the command line.
die() { echo "$*" >&2; exit 2; }  # complain to STDERR and exit with error
needs_arg() { if [ -z "$OPTARG" ]; then die "No arg for --$OPT option"; fi; }

while getopts ab:c:-: OPT; do
  # support long options: https://stackoverflow.com/a/28466267/519360
  if [ "$OPT" = "-" ]; then   # long option: reformulate OPT and OPTARG
    OPT="${OPTARG%%=*}"       # extract long option name
    OPTARG="${OPTARG#$OPT}"   # extract long option argument (may be empty)
    OPTARG="${OPTARG#=}"      # if long option argument, remove assigning `=`
  fi
  case "$OPT" in
    dataset )    DATASET_NAME="$OPTARG" ;;
    include_comments )     INCLUDE_COMMENTS="$OPTARG" ;;
    exclude_stopwords ) EXCLUDE_STOPWORDS="$OPTARG" ;;
    include_tfidf )  USE_TFIDF="$OPTARG" ;;
    number_keywords )  NUMBER_OF_TFIDF_KEYWORDS="$OPTARG" ;;
    variant ) VARIANT="$OPTARG" ;;
    ??* )          die "Illegal option --$OPT" ;;  # bad long option
    ? )            exit 2 ;;  # bad short option (error reported via getopts)
  esac
done
shift $((OPTIND-1)) # remove parsed options and args from $@ list

echo "Dataset: $DATASET_NAME" 
echo "Variant: $VARIANT"
echo "Including comments: $INCLUDE_COMMENTS" 
echo "Excluding stopwords: $EXCLUDE_STOPWORDS" 
echo "Using TFIDF: $USE_TFIDF" 
echo "TFIDF keywords: $NUMBER_OF_TFIDF_KEYWORDS" 


INPUT_DIR=datasets
TRAIN_DIR=${INPUT_DIR}/${DATASET_NAME}/raw/train
VAL_DIR=${INPUT_DIR}/${DATASET_NAME}/raw/valid
TEST_DIR=${INPUT_DIR}/${DATASET_NAME}/raw/test

# Preprocessing configs
MAX_DATA_CONTEXTS=1000
MAX_CONTEXTS=200
SUBTOKEN_VOCAB_SIZE=186277
TARGET_VOCAB_SIZE=26347
NUM_THREADS=64
PYTHON=python3
###########################################################

OUTPUT_DIR=${INPUT_DIR}/${DATASET_NAME}/preprocessed/exp_${VARIANT}

mkdir -p ${INPUT_DIR}/${DATASET_NAME}/preprocessed/exp_${VARIANT}

TRAIN_DATA_FILE=${OUTPUT_DIR}/${DATASET_NAME}.train.raw.txt
VAL_DATA_FILE=${OUTPUT_DIR}/${DATASET_NAME}.val.raw.txt
TEST_DATA_FILE=${OUTPUT_DIR}/${DATASET_NAME}.test.raw.txt
EXTRACTOR_JAR=JavaExtractor/JPredict/target/JavaExtractor-0.0.1-SNAPSHOT.jar

echo "Extracting paths from validation set..."
${PYTHON} JavaExtractor/extract.py --dir ${VAL_DIR} --max_path_length 8 --max_path_width 2 --num_threads ${NUM_THREADS} -d ${DATASET_NAME} --jar ${EXTRACTOR_JAR} --include_comments ${INCLUDE_COMMENTS} --exclude_stopwords ${EXCLUDE_STOPWORDS} --include_tfidf ${USE_TFIDF} --number_keywords ${NUMBER_OF_TFIDF_KEYWORDS} > ${VAL_DATA_FILE} 2>> error_log.txt
echo "Finished extracting paths from validation set"
echo "Extracting paths from test set..."
${PYTHON} JavaExtractor/extract.py --dir ${TEST_DIR} --max_path_length 8 --max_path_width 2 --num_threads ${NUM_THREADS} -d ${DATASET_NAME} --jar ${EXTRACTOR_JAR} --include_comments ${INCLUDE_COMMENTS} --exclude_stopwords ${EXCLUDE_STOPWORDS} --include_tfidf ${USE_TFIDF} --number_keywords ${NUMBER_OF_TFIDF_KEYWORDS} > ${TEST_DATA_FILE} 2>> error_log.txt
echo "Finished extracting paths from test set"
echo "Extracting paths from training set..."
${PYTHON} JavaExtractor/extract.py --dir ${TRAIN_DIR} --max_path_length 8 --max_path_width 2 --num_threads ${NUM_THREADS} -d ${DATASET_NAME} --include_comments ${INCLUDE_COMMENTS} --exclude_stopwords ${EXCLUDE_STOPWORDS} --include_tfidf ${USE_TFIDF} --number_keywords ${NUMBER_OF_TFIDF_KEYWORDS} --jar ${EXTRACTOR_JAR} | shuf > ${TRAIN_DATA_FILE} 2>> error_log.txt
echo "Finished extracting paths from training set"

TARGET_HISTOGRAM_FILE=${OUTPUT_DIR}/${DATASET_NAME}.histo.tgt.c2s
SOURCE_SUBTOKEN_HISTOGRAM=${OUTPUT_DIR}/${DATASET_NAME}.histo.ori.c2s
NODE_HISTOGRAM_FILE=${OUTPUT_DIR}/${DATASET_NAME}.histo.node.c2s

echo "Creating histograms from the training data"
cat ${TRAIN_DATA_FILE} | cut -d' ' -f1 | tr '|' '\n' | awk '{n[$0]++} END {for (i in n) print i,n[i]}' > ${TARGET_HISTOGRAM_FILE}
cat ${TRAIN_DATA_FILE} | cut -d' ' -f2- | tr ' ' '\n' | cut -d',' -f1,3 | tr ',|' '\n' | awk '{n[$0]++} END {for (i in n) print i,n[i]}' > ${SOURCE_SUBTOKEN_HISTOGRAM}
cat ${TRAIN_DATA_FILE} | cut -d' ' -f2- | tr ' ' '\n' | cut -d',' -f2 | tr '|' '\n' | awk '{n[$0]++} END {for (i in n) print i,n[i]}' > ${NODE_HISTOGRAM_FILE}

${PYTHON} preprocess.py --train_data ${TRAIN_DATA_FILE} --test_data ${TEST_DATA_FILE} --val_data ${VAL_DATA_FILE} \
  --max_contexts ${MAX_CONTEXTS} --max_data_contexts ${MAX_DATA_CONTEXTS} --subtoken_vocab_size ${SUBTOKEN_VOCAB_SIZE} \
  --target_vocab_size ${TARGET_VOCAB_SIZE} --subtoken_histogram ${SOURCE_SUBTOKEN_HISTOGRAM} \
  --node_histogram ${NODE_HISTOGRAM_FILE} --target_histogram ${TARGET_HISTOGRAM_FILE} --output_name ${OUTPUT_DIR}/${DATASET_NAME}
    
# If all went well, the raw data files can be deleted, because preprocess.py creates new files 
# with truncated and padded number of paths for each example.
rm ${TRAIN_DATA_FILE} ${VAL_DATA_FILE} ${TEST_DATA_FILE} ${TARGET_HISTOGRAM_FILE} ${SOURCE_SUBTOKEN_HISTOGRAM} \
  ${NODE_HISTOGRAM_FILE}

