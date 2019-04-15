#!/usr/bin/python3

import sys
print("CPython version:")
print(sys.version)


def imul(v):
    return v * v

simple_json = {}
for x in range(10):
    simple_json[f"result_{str(x)}x{str(x)}"] = imul(x)

print(simple_json)