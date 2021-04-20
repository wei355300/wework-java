#!/bin/bash

lib_path="./lib"
process_name="chatdata-0.0.1-SNAPSHOT.jar"

if [ `id -u` -ne "0" ]; then
    echo "ERROR: Permission denied." 1>&2
    exit 1
fi

start() {
    java -Djava.library.path=${lib_path} -jar ${process_name} &
}


stop() {
    sn_pid=`ps -ef|grep "${process_name}" | grep -v "grep" | awk '{print $2}'`
	if [ -n "${sn_pid}" ]; then
		kill -15 ${sn_pid}
	else
		echo "process not found"
	fi
}


status() {
	if [ -n "$(ps -ef | grep ${process_name} | grep -v grep)" ]; then
		echo "${process_name} is running"
	else
		echo "${process_name} stopped"
	fi
}


case "$1" in
    start)
        start
        sleep 1
        status
    ;;

    stop)
        stop
        status
    ;;

    restart)
        stop 1
        status
        echo "${process_name} restarting..."
        sleep 1
        start
        status
    ;;

    status)
        status
    ;;

    *)
        echo $"Usage: $0 {start|stop|restart|status}"
        exit 1
    ;;

esac
