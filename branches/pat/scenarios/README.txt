Contents of this README:

1. About
2. Building
3. Licence
4. Contact
--------------------------------------------------------------------------------

 

1. About

PAT is a GWT based project that aims to replace JPivot in the Pentaho BI Server.
PAT allows users to utilize new AJAX technology
when querying their mondrian cubes, which in return gives a nicer end user 
experience and easier to understand interface.

--------------------------------------------------------------------------------

2. Building
OOOOOOOOLD
PAT is a mavenized project, which allows us to use central repositories for 
dependency management. You can get maven from http://maven.apache.org/.

Maven Goals

The following goals can be used in eclipse runtime configuration > maven build
or in command line by running mvn followed by the goal (in project directory). 
The first time you do this it will download a lot of dependencies so will take a 
lot longer. However this will not happen next time.

GWT HOSTED:
	com.totsp.gwt:maven-googlewebtoolkit2-plugin:gwt
	Runs the application GWT hosted mode

GWT HOSTED DEBUG:
	com.totsp.gwt:maven-googlewebtoolkit2-plugin:debug
	Runs the application in GWT hosted debug mode, you can then connect with 
	a new debugging runtime configuration (New Remote Java Application).

COMPILE:
	compile
	I think thats clear ;-)

PACKAGE:
	clean package
	Cleans and then packages the application into a WAR file, which you can 
	deploy easily by dropping it into tomcat/webapps e.g

JAVADOC:
	javadoc:javadoc
	Creates the PAT Javadoc files.

TESTS:
	test
	Runs the PAT Test Suite

JETTY:
	jetty:run-exploded
	Runs PAT in a hosted jetty webserver

ECLIPSE:
	eclipse:clean eclipse:eclipse
	which cleans the eclipse project (drops eclipse files and target folder...) 
	and recreates an eclipse project based on pom.xml. This helps when you get 
	strange eclipse errors and want to clean it somehow 
--------------------------------------------------------------------------------


3. Licence

This program is free software; you can redistribute it and/or modify it
under the terms of the GNU General Public License as published by the
Free Software Foundation; either version 2 of the License, or (at your
option) any later version. The program source code is also freely
available as per Section 4 of this README.

This program is distributed in the hope that it will be useful, but WITHOUT
ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public
License for more details.

You should have received a copy of the GNU General Public License
along with this program (in a file called LICENSE.txt); if not, go
to http://www.gnu.org/copyleft/gpl.html or write to

  Free Software Foundation, Inc.
  59 Temple Place - Suite 330
  Boston, MA 02111-1307 USA
--------------------------------------------------------------------------------


4. Contact

You can find more information in the following places.

Website
http://code.google.com/p/pentahoanalysistool

Developer Mailing List
http://groups.google.com/group/pentahoanalysistool-dev

User Mailing List
http://groups.google.com/group/pentahoanalysistool-users