#!/bin/bash
cd ~/sbking/src
appletviewer -J-Djava.security.policy=bin/policy bin/$1.html
cd ~/sbking
