#!/usr/bin/python3

import sys

sum_num = 0
for num in sys.argv[1].split(' '):
    sum_num += int(num)
sys.stdout.write(str(sum_num))