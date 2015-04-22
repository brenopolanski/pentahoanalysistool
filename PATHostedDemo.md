# Deploying the Hosted Demo #

You can run the PAT hosted demo for both Linux and Windows, below are the relative guides for each operating system.

At the moment the hosted demo comes pre-configured with hsqldb.

## Linux ##

First create a working directory to extract the hosted demo:

```
  user@term:~# sudo mkdir tmp/
  user@term:~# cd tmp/
  user@term:/tmp#
```

Then download the PentahoAnalysisTool-0.2-Linux.tar.gz file from the [downloads](http://code.google.com/p/pentahoanalysistool/downloads/list) area or download it directly into your working directory:

```
  user@term:/tmp# sudo wget http://pentahoanalysistool.googlecode.com/files/PentahoAnalysisTool-0.2-Linux.tar.gz
```

After you have downloaded the file, extract the file:

```
  user@term:/tmp# sudo tar -xvzf http://pentahoanalysistool.googlecode.com/files/PentahoAnalysisTool-0.2-Linux.tar.gz
```

Once that has extracted succesfully enter the PentahoAnalysisTool/ directory and run the start.sh command:

```
  user@term:/tmp# cd PentahoAnalysisTool
  user@term:/tmp/PentahoAnalysisTool# sh ./start.sh
```

Navigate the following URL in your browser to view the demo:

```
  http://localhost:9999/
```
or
```
  http://your_ip_address:9999/
```

When prompted to login use the username **admin** and the password **admin**.

## Windows ##

To be done.