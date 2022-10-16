###########################################################
# Change the following values to train a new model.
# type: the name of the new model, only affects the saved file name.
# dataset: the name of the dataset, as was preprocessed using preprocess.sh
# test_data: by default, points to the validation set, since this is the set that
#   will be evaluated after each training iteration. If you wish to test
#   on the final (held-out) test set, change 'val' to 'test'.

dataset_name=default
variant=default
continue_training_from_checkpoint=true

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
    dataset )    dataset_name="$OPTARG" ;;
    continue_training_from_checkpoint )    continue_training_from_checkpoint="$OPTARG" ;;
    variant ) variant="$OPTARG" ;;
    ??* )          die "Illegal option --$OPT" ;;  # bad long option
    ? )            exit 2 ;;  # bad short option (error reported via getopts)
  esac
done
shift $((OPTIND-1)) # remove parsed options and args from $@ list

echo "Dataset: $dataset_name" 
echo "Training from a previous checkpoint: $continue_training_from_checkpoint"

type=exp_${dataset_name}_${variant}
data_dir=datasets/${dataset_name}/preprocessed/exp_${variant}
data=${data_dir}/${dataset_name}
test_data=${data_dir}/${dataset_name}.val.c2s
model_dir=models/${type}

mkdir -p ${model_dir}
set -e
python3 -u code2seq.py --data ${data} --test ${test_data} --save_path ${model_dir} --model_path ${model_dir} --continue_training_from_checkpoint ${continue_training_from_checkpoint}
