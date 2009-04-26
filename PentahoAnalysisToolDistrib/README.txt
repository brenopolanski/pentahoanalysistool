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

2. Usage

Unzip and run start.sh/.bat, this launches the hsql database and the jetty web 
app.
When Jetty says its listening on :8080, in your favourite web browser navigate to
http://localhost:8080/
It should now ask for a username and password which for the time being is
admin/admin.
You should now be presented with the PAT interface.

To connect to your database, go to file, connect. If you are using the mondrian
panel, then the url for the demo hosted mode hsql database is 
jdbc:hsqldb:hsql://localhost/sampledata, and the username is sa, and the password
is blank. You need to upload a mondrian schema, so press browse and upload
Sampledata.mondrian.xml. If you press connect you should see a connection
successful message box.

The list of available cubes should now be populated, expand the tree and select
Quadrant Analysis. The display will now change to show the dimensions available
in the cube.
You can now drag the dimensions from the dimension panel on the left, to the 
axis widgets on the main panel. Once you have populated the widgets you can then
expand the dimension's tree and right click on the members to create the query.

Lastly, hit execute query, and the mdx query should be executed and data returned.
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
