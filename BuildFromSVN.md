# Introduction #
You can easily build PAT from source by following two steps.


**1. SVN Checkout**

Checkout the project with your favourite SVN client from:
```
http://pentahoanalysistool.googlecode.com/svn/trunk/pat/
```

**2. Maven Build**

Run Maven or Maven Eclipse Plugin (check HOWTO at http://smartgwt.rorschach.de/index.php5/Setting_up_Eclipse_%26_Maven2) with following goals

**RUN**

```
gwt:run
```
Runs the application


**PACKAGE**

```
clean package
```

Cleans and then packages the application into a WAR file, which you
can deploy easily by dropping it into tomcat/webapps e.g

**JETTY RUN**

```
jetty:run-exploded [-Djetty.port=9999]
```

Runs PAT and runs it in a Jetty web server

Check out Maven Goals for more goals that can be used

# Known Problems with Eclipse building #

There are reports that GWT-DEV files are extracted in a new directory (e.g. PROJECT\_HOME/{local.Repository}/com/google/gwt/gwt-dev/1.5.3) instead of the local maven repository (default: ~/.m2/repository/com/google/gwt/gwt-dev/1.5.3) during the build process.

This is (probably) caused by the maven-eclipse plugin. This can be solved due the use of an external maven installation (Change that in Eclipse Preferences : Maven : Installation - Add)