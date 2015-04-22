# Pentaho BI Server Plugin Integration for PAT #

This is a new Eclipse project (derived from Aaron Phillip's [EchoPlugin Sample Project](http://wiki.pentaho.com/display/ServerDoc2x/Echo+Plugin+-+a+sample+plugin+for+the+BI+Platform)) that will build and deploy the Pentaho Anaysis Tool plugin to the Pentaho BI Server.


## Disclaimer ##

Setting up this plugin is not for the faint of heart. The requirements consist mainly of the latest code in several projects under development.

## Requirements ##

In order to build and deploy this plugin, you need to have a Pentaho BI Server deployed and running, as well as a deployed Pentaho Analysis Tool. I will give you the coordinates for the code lines that work with this plugin, but please refer to the deployment documentation for the server and for PAT to get those web applications set up.

These instructions are written for a configuration where the BI Server and Pentaho Anaysis Tool are deployed to the same Tomcat server. This is for convenience; you can have them deployed separately if you wish. Adjust the instructions as necessary for your deployment configuration.

The tools you need are as follows:

  1. Apache Ant 1.7.0 or greater
  1. Pentaho BI Server (Citrus), the latest build (as of 5/20/2009) exists on the continuous integration server - http://ci.pentaho.com/view/Platform/job/BISERVER-CE/
  1. Pentaho Analysis Tool, the latest build (as of 5/20/2009) is needed. Also exists on the continuous integration server - http://ci.pentaho.com/job/Pentaho_Analysis_Tool/org.pentaho$pat/
  1. The PAT plugin source code, downloadable from subversion repo: svn checkout http://pentahoanalysistool.googlecode.com/svn/bi-plugin/trunk

## Installation ##

  * Install and/or deploy both the Pentaho BI Server and the Pentaho Analysis Tool.
  * Download the PAT plugin project.
  * Open the build.properties file, found in the root of the plugin project.
  * Change the path for the pentaho.dir property to the location of your BI Server installation. Save and close the file when completed.

> EX. ` pentaho.dir=E:/workspaces/trunk_workspace/tomcat-pci-test/biserver-ce `

  * From a command prompt, in the root of the pentaho plugin project, run the following commands: **ant**, then **ant install**.

> EX. ` E:\workspaces\pat_workspace\PentahoAnalysisTool>ant `
> > ` E:\workspaces\pat_workspace\PentahoAnalysisTool>ant install `

These commands will build, then deploy the PAT plugin and solution files to your BI Server. You can verify by checking your pentaho-solutions\system folder for a new folder named pat-plugin. There will also be new solution files under pentaho-solutions\bi-developers\PatPlugin. In the next set of steps, you will modify the deployed solution to point to your PAT and XML/a resources.

  * Open the sample.xpav file in the pentaho-solutions\bi-developers\PatPlugin folder.
  * Change the `<app-connection>` node's text value to contain the server, port and context path to your PAT web application. The rest of the URL is the access point into PAT and must be left alone.


> EX. If your domain is **mydomain.com**, and PAT is deployed to **port 8888**, and the context path is **pat** then your `<app-connection>` text value should be ` http://mydomain.com:8888/pat/pentaho/simpleXmla.do `

  * Change the `<xmla-provider>` node's attribute values for url, username and password to point to your XML/a provider. The default values in the solution point to the XML/a servlet available in a Pentaho BI Server deployed to localhost.
  * Save and close sample.xpav.

Once you have built, deployed,and modified your solution, open a browser and login to the Pentaho BI Server. We will now refresh the plugins and the solution repository, then watch our PAT plugin at work.

  * Navigate to the admin page of your BI Server. On a default localhost installation, this page would reside at http://localhost:8080/pentaho/Admin.
  * Click the Update Solution Repository link. You should get confirmation that this succeeded.
  * Click the Plugin Adapter link. You should get confirmation that this succeeded, and if you peruse the return message, you should see that the Pat Plugin registered successfully.

Now, return to the home page of PUC, the user console for the BI Server. Navigate the solutions folders to the PatPlugin folder. You should see one piece of content there, "My First Analysis View". Click the content item and PAT should launch. Enjoy!

Please post any comments, feedback, problems or bugs to [the PAT forum](http://forums.pentaho.org/forumdisplay.php?f=258).

## Development ##

If you want to compile and work on the PAT plugin in Eclipse or any other IDE, you will need to create a classpath reference to the Pentaho platform jars in your Pentaho BI Server installation. I accomplished this by linking the classes directory of my development BI server into the PAT plugin project' s classpath. You may find another route suitable.

For help with understanding Pentaho BI Server plugins in general, or seeing how a simple plugin is setup, visit [this page](http://wiki.pentaho.com/display/ServerDoc2x/BI+Platform+Plugins+in+V2).