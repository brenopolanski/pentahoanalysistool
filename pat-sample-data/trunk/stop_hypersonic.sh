#!/bin/sh
### ====================================================================== ###
##                                                                          ##
##  HSQLDB Start Script                                                     ##
##                                                                          ##
### ====================================================================== ###

DIR_REL=`dirname $0`
cd $DIR_REL
DIR=`pwd`
cd -

. "$DIR/set-java.sh"

  setJava

#---------------------------------#
# dynamically build the classpath #
#---------------------------------#
THE_CLASSPATH=
for i in `ls $DIR_REL/lib/hsqldb*.jar`
do
  THE_CLASSPATH=${THE_CLASSPATH}:${i}
done
echo "classpath is $THE_CLASSPATH"

"$_JAVA" -cp $THE_CLASSPATH org.hsqldb.util.ShutdownServer -url "jdbc:hsqldb:hsql://localhost/sampledata" -user "SA" -password "" 
"$_JAVA" -cp $THE_CLASSPATH org.hsqldb.util.ShutdownServer -url "jdbc:hsqldb:hsql://localhost/hibernate" -user "SA" -password ""

# -----------------------------
# FOODMART SHUTDOWN
# "$_JAVA" -cp $THE_CLASSPATH org.hsqldb.util.ShutdownServer -url "jdbc:hsqldb:hsql://localhost/foodmart" -user "sa" -password "" 
