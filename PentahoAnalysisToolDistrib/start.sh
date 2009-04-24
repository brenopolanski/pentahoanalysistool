#!/bin/sh
### ====================================================================== ###
##                                                                          ##
##  Pentaho Start Script                                                    ##
##                                                                          ##
### ====================================================================== ###
S1="x$JAVA"
S2="x"
if [ $S1=$S2 ]; then
  S1="x$JAVA_HOME"
  if [ $S1=$S2 ]; then
    JAVA="java"
  else
    JAVA="$JAVA_HOME/bin/java"
  fi
fi


if [ "$?" = 0 ]; then
  cd data
  sh start_hypersonic.sh &
  cd ../jetty/
$JAVA -jar -DSTOP.KEY=secret -DSTOP.PORT=8079 -Xmx512M -XX:PermSize=64M -XX:MaxPermSize=128M  -DCONSOLE_HOME=. start.jar
fi


