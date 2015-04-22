**Introduction**

The project is maven based. Either the maven plug in for eclipse or the stand alone can be used to build it with the following goals.

# Maven Goals #

The following goals can be used in eclipse runtime configuration > maven build or in command line by running mvn followed by the goal (in project directory).
The first time you do this it will download a lot of dependencies so will take a lot longer. However this will not happen next time.


**RUN**

```
gwt:run
```
Runs the application in GWT Hosted Mode.


**DEBUG**

```
gwt:debug
```

Runs the application in GWT Hosted debug mode, you can then connect with a new
debugging runtime configuration (New Remote Java Application)


**PACKAGE**

```
clean package
```

Cleans and then packages the application into a WAR file, which you
can deploy easily by dropping it into tomcat/webapps e.g

**JAVADOC**

```
javadoc:javadoc
```

Creates the PAT Javadoc files.

**TEST**

```
test
```

Runs the PAT Test Suite

**RUN JETTY**

```
jetty:run-exploded [-Djetty.port=9999]
```

Runs PAT in a hosted jetty webserver

**ECLIPSE**

When you open a new command line and change into project directory you
can run:

```
mvn eclipse:clean eclipse:eclipse
```

which cleans the eclipse project (drops eclipse files and target
folder...) and recreates an eclipse project based on pom.xml. This
helps when you get strange eclipse errors and want to clean it somehow