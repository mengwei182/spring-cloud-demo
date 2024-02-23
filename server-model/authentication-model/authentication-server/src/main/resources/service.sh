ulimit -SHn 65535
appName=authentication-server
jpsCommand=authentication-server
port=9081

JAVA_OPTS="-Xms512m -Xmx512m -Xmn256m"

if [ -f /etc/profile ]; then
  . /etc/profile
fi

java=$JAVA_HOME/bin/java
jps=$JAVA_HOME/bin/jps

function stop() {
  pids=$(ps aux | grep -E "$jpsCommand" | grep -v grep | awk '{print $2}')
  if [ -n "$pids" ]; then
    for pid in $pids; do
      kill -9 $pid
    done
  fi
  printf "Waiting for stop $appName ..."
  while true; do
    lsof -i:$port | grep LISTEN >/dev/null 2>&1
    if [[ $? != 0 ]]; then break; fi
    printf "."
    sleep 1
  done
  echo
  printf "Stopped $appName."
  echo
}

function status() {
  pids=$(ps aux | grep -E "$jpsCommand" | grep -v grep | awk '{print $2}')
  if [ -n "$pids" ]; then
    echo "Started $appName."
    echo "PID   NAME"
    $jps -l | grep -E "$jpsCommand"
  else
    echo "Stopped $appName."
  fi
}

function start() {
  pids=$(ps aux | grep -E "$jpsCommand" | grep -v grep | awk '{print $2}')
  if [ -n "$pids" ]; then
    echo "$appName is already running."
    exit
  fi
  export LANG=zh_CN.UTF-8
  nohup java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar -Dspring.config.location=application.yml $jpsCommand.jar >/dev/null 2>&1 &
  #nohup java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=5005,suspend=n $JAVA_OPTS -Djava.ext.dirs=./lib/:$JAVA_HOME/jre/lib/ext -Djava.security.egd=file:/dev/./urandom -jar -Dspring.config.location=application.yml $jpsCommand.jar > /dev/null 2>&1 &
  printf "Waiting for start $appName ..."
  while true; do
    lsof -i:$port | grep LISTEN >/dev/null 2>&1
    if [[ $? == 0 ]]; then break; fi
    printf "."
    sleep 1
  done
  echo
  printf "Started $appName,"
  pid=$(ps aux | grep -E "$jpsCommand" | grep -v grep | awk '{print $2}')
  echo "running: PID:$pid"
}

case "$1" in
start)
  start
  sleep 1
  ;;
stop)
  stop
  sleep 1
  ;;
restart)
  $0 stop
  $0 start
  ;;
status)
  status
  ;;
*)
  echo "Usage: $0 {start|stop|restart|status}"
  exit $?
  ;;
esac