# Deploying with Tomcat 6.X #

Below are guides on how to deploy PAT with Tomcat 6.X on Linux and Windows.

## Linux ##

### Before You Start ###

If you don't currently have Tomcat installed you will need to get the latest Java JDK, you can do this from the terminal prompt by running a command similar to below depending on your Linux distribution:

```
  user@term:~# sudo apt-get install sun-java6-jdk sun-java6-jre
```

Tomcat will need two enviroment variables setup before it can start succesfully. You can either run these from the terminal prompt (once off) or add them to your Linux's startup script.

**JAVA\_HOME**

```
  user@term:~# export JAVA_HOME=your_java_installation_directory
```

**JAVA\_OPTS**

```
  user@term:~# export JAVA_OPTS=-Xms512m -Xmx1024m -XX:PermSize=256m -XX:MaxPermSize=512m
```

### Installing Tomcat 6.X ###

If you already have Tomcat running you can skip this step.

Pick a place to extract Tomcat 6.X, for this tutorial I will be extracting it to my opt/ directory. Navigate to your opts directory from the terminal prompt:

```
  user@term:~# cd /opt/
  user@term:/opt#
```

Once in the opt/ directory run the following command at your terminal prompt to get the Tomcat 6.X .tar.gz file (latest tomcat version as of this wiki entry is 6.0.18):

```
  user@term:/opt# sudo wget http://apache.mirror.aussiehq.net.au/tomcat/tomcat-6/v6.0.18/bin/apache-tomcat-6.0.18.tar.gz
```

Once that has downloaded, at your terminal prompt extract it by running the following command:

```
  user@term:/opt# sudo tar -xvzf apache-tomcat-6.0.18.tar.gz
```

This should extract succesfully and an apache-tomcat-6.0.18/ directory will be created. To make things simplier rename the directory to tomcat:

```
  user@term:/opt# mv apache-tomcat-6.0.18 tomcat
```

### Deploying The Latest PAT WAR ###

Navigate to your webapps directory within your tomcat installation directory:

```
  user@term:/opt# cd tomcat/webapps/
  user@term:/opt/tomcat/webapps#
```

Once in the webapps directory you will need to download the latest PAT WAR file (latest version 0.2). You can do this either by downloading the file directly from the [downloads](http://code.google.com/p/pentahoanalysistool/downloads/list)] section or typing the following command while still in the webapps directory:

```
  user@term:/opt/tomcat/webapps# sudo wget http://pentahoanalysistool.googlecode.com/files/pat-0.2.0-GA.war
```

You are required to rename the war file to _pat.war_ before deploying it. The PAT application can only work properly in the _/pat_ context.


```
  user@term:/opt/tomcat/webapps# mv pat-0.2.0-GA.war pat.war
```

You will now need to start Tomcat, navigate to the bin directory within your Tomcat directory:

```
  user@term:/opt/tomcat/webapps# cd /opt/tomcat/bin
```

Then run the startup.sh file:

```
  user@term:/opt/tomcat/bin# sh ./startup.sh
```

After running this command Tomcat will create a pat-0.2.0-GA/ directory under your webapps/ directory.

Open up your browser and navigate to the following address:

```
  http://localhost:8080/pat-0.2.0-GA/
```
or
```
  http://ip_address:8080/pat-0.2.0-GA
```

When prompted to login use the username **admin** and the password **admin**.

## Windows ##

To be done.