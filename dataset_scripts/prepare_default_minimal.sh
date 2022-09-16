#!/bin/bash


FILE=../datasets/default-minimal.tar.gz
DATASETFOLDER=../datasets
if [ -f "$FILE" ]; 
then
    echo "$FILE exists - unpacking it"
    tar -xvf $FILE --directory $DATASETFOLDER
else
    # A way to do this would be to unzip the whole archive and then just keep some particular files. Not implemented yet.
    bash prepare_default.sh  
fi

echo "Dataset prepared."