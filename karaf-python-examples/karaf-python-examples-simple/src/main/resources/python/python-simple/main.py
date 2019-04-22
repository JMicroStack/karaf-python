#!/usr/bin/python3

import sys

SCRIPT_HOME = os.environ.get('WORKHOME_PATH', None)
print(f"Work home for python script: {SCRIPT_HOME}")

data = []
for line in sys.stdin:
    data.append(line)

print(f"Input data: {data}")