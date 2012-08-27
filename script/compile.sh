#!/bin/bash
cd ~/sbking/src/core
javac *.java -d ~/sbking/src/bin -classpath ~/sbking/src
cd ~/sbking/src/gui/sample
javac *.java -d ~/sbking/src/bin -classpath ~/sbking/src
cd ~/sbking/src/gui
javac *.java -d ~/sbking/src/bin -classpath ~/sbking/src
cd ~/sbking
