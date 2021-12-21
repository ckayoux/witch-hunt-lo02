#!/bin/bash
find -type f -name *.java -print > sources.txt
if test -d build
then t="a"
else
    mkdir build
fi
javac -d build -verbose @sources.txt &> build.logs 
cat build.logs |head -20 |lolcat -ad 1 -F 0.4
cat build.logs |tail +20 |lolcat -iF 0.4
cd build
echo "" 
java fr.sos.witchhunt.controller.Application

