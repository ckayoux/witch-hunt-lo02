#!/bin/bash
find ./WitchHunt/src/java -type f -name *.java -print > sources.txt
if test -d build
then t="a"
else
    mkdir build
fi
javac -d build -verbose -Xlint @sources.txt &> build.logs 
cat build.logs |head -20 |lolcat -ad 1 -F 0.4
cat build.logs |tail +20 |lolcat -iF 0.4
cd build
cp  -r ../WitchHunt/src/resources/* ./ -v |lolcat -ad 2
echo "" 
java fr.sos.witchhunt.controller.core.Application

