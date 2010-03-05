#!/bin/sh

DIR_REL=`dirname $0`
cd $DIR_REL
DIR=`pwd`
cd -

. "$DIR/data/set-java.sh"
  setJava

cd "$DIR/data" 
sh stop_hypersonic.sh &
cd "$DIR/tomcat/bin"
JAVA_HOME=$_JAVA_HOME
sh shutdown.sh
