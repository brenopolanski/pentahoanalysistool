#!/bin/bash

export CLASSPATH="/root/.m2/repository/com/google/gwt/gwt-user/1.5.3/gwt-user-1.5.3.jar":"/root/.m2/repository/com/google/gwt/gwt-dev/1.5.3/gwt-dev-1.5.3-linux.jar":"/home/bugg/workspace/PAT/src/main/java":"/home/bugg/workspace/PAT/src/main/resources":"/home/bugg/workspace/PAT/classes":"/home/bugg/workspace/PAT/target/classes":"/root/.m2/repository/com/google/code/gwt-dnd/gwt-dnd/2.5.6/gwt-dnd-2.5.6.jar":"/root/.m2/repository/log4j/log4j/1.2.9/log4j-1.2.9.jar":"/root/.m2/repository/org/olap4j/olap4j/0.9.7/olap4j-0.9.7.jar":"/root/.m2/repository/eigenbase/eigenbase-properties/1.1.0.10924/eigenbase-properties-1.1.0.10924.jar":"/root/.m2/repository/commons-collections/commons-collections/2.1/commons-collections-2.1.jar":"/root/.m2/repository/com/gwtext/gwtext/2.0.6/gwtext-2.0.6.jar":"/root/.m2/repository/eigenbase/eigenbase-xom/1.3.0.11999/eigenbase-xom-1.3.0.11999.jar":"/root/.m2/repository/javacup/javacup/0.10k/javacup-0.10k.jar":"/root/.m2/repository/com/pentaho/mondrian/3.0.4.11371/mondrian-3.0.4.11371.jar":"/root/.m2/repository/mysql/mysql-connector-java/5.1.6/mysql-connector-java-5.1.6.jar":"/root/.m2/repository/jfree/jcommon/1.0.12/jcommon-1.0.12.jar":"/root/.m2/repository/eigenbase/eigenbase-resgen/1.3.0.11873/eigenbase-resgen-1.3.0.11873.jar":"/root/.m2/repository/commons-dbcp/commons-dbcp/1.2.2/commons-dbcp-1.2.2.jar":"/root/.m2/repository/commons-pool/commons-pool/1.4/commons-pool-1.4.jar":"/root/.m2/repository/commons-math/commons-math/1.2/commons-math-1.2.jar":"/root/.m2/repository/commons-logging/commons-logging/1.1.1/commons-logging-1.1.1.jar":"/root/.m2/repository/commons-vfs/commons-vfs/1.0/commons-vfs-1.0.jar":"/root/.m2/repository/net/sf/gwt-widget/gwt-widgets/0.2.0/gwt-widgets-0.2.0.jar"

"/usr/lib/jvm/java-6-sun-1.6.0.07/jre/bin/java" -Xmx512m -cp $CLASSPATH -Dcatalina.base="/home/bugg/workspace/PAT/target/tomcat"  com.google.gwt.dev.GWTShell -gen "/home/bugg/workspace/PAT/target/.generated" -logLevel INFO -style DETAILED -out "/home/bugg/workspace/PAT/target/project-0.0.1-SNAPSHOT" -port 8888 org.pentaho.pat.PAT/PAT.html
