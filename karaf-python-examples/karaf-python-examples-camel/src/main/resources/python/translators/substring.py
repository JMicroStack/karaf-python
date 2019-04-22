#!/usr/bin/python3

import sys, os
SCRIPT_HOME = os.environ.get('WORKHOME_PATH', None)

data = []
for line in sys.stdin:
    data.append(line)
data = ''.join(data).strip()


start = int(sys.argv[1].split(":")[0][1:])
stop = int(sys.argv[1].split(":")[1][0:-1])
sys.stdout.write(data[start:stop])