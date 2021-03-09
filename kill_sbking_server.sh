#!/bin/bash

# Linux specific function to kill a process given its id.
function linux_kill_server() {
	kill "$1"
}

# Windos specific function that kill a process given its id.
function windows_kill_server() {
	taskkill -f -pid "$1"
}

# Selects which killing process function to use depending on OS.
function kill_server_by_os(){
	echo "Killing SBKing server PID = $1"
	if [ "$(expr substr $(uname -s) 1 5)" == "Linux" ]; then
	    linux_kill_server "$1"
	elif [ "$(expr substr $(uname -s) 1 10)" == "MINGW32_NT" ]; then
	    windows_kill_server "$1"
	elif [ "$(expr substr $(uname -s) 1 10)" == "MINGW64_NT" ]; then
	    windows_kill_server "$1"
	fi
}

# This function kills the sbking-server.jar process regardless of OS.
function main() {
	PID=`jps -l | grep sbking-server.jar | cut -d ' ' -f1`

	if [ ! -z "$PID" ]; then
		kill_server_by_os "${PID}"
	fi
}

main
