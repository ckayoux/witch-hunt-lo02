#!/bin/bash
find . -type f -name *.java -exec cat {} \; |wc -l