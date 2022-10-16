import json
import os
from pathlib import Path

# Will only work for codesearchnet for now.
# For funcom json parsing is not neededself.
# For default dataset this approach is not suitable as the data is just pure java files.
DATASETS = [
        "codesearchnet", 
        # "default", 
        # "funcom"
        ]

for ds in DATASETS:

    path = Path("../datasets/" + ds + "/raw/")
    file_paths = [os.path.join(dirpath,f) for (dirpath, dirnames, filenames) in os.walk(path) for f in filenames]

    examples = 0
    inline_comment_count = 0

    # go over each file and collect its json
    for path in file_paths:
        with open(path, "r") as file:
            lines = file.readlines()

            for line in lines:
                examples += 1
                data = json.loads(line)
                code = str(data["original_string"])
                comment = str(data["docstring"])

                # Count inline comments
                if "//" in code or "/*" in code:
                    inline_comment_count += 1

    print("=========" + ds + "=========")
    print("TOTAL NUMBER OF EXAMPLES       : " + str(examples))
    print("TOTAL NUMBER OF INLINE COMMENTS: " + str(inline_comment_count))
    print("========================================")
