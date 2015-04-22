# Development Sprint 0.5 #

The following describes the action plan and planned new features for this development sprint.

## General Action Plan ##
  * consistent pat license
  * make mockups of everything we need
  * make list of needed widgets
  * updated lifecycle (including needed backend methods)
  * check if we can/should use xul
  * follow rules for cleaning / re-using existing widgets
  * create JIRA cases for planned tasks
  * **follow [code guideline](CodeGuidelines.md)**

## Rules for GUI Code ##
  * move pat.client > pat.client.deprecated
  * create new i18n classes / properties [getResourceByKey("pat.client.mdx")]
  * choose widget/panels we still need
    * check for [code guideline](CodeGuidelines.md)
    * check if code looks clean
    * check comments
    * check if widget css tagged properly
    * check for warnings and remove them
    * switch to new i18n method
    * XUL'ify if thats the decision
    * make sure the correct exception message is shown

## Details for 0.5.0 ##

### GUI Design ###
  * like jakes mockup until page 10
  * no menubar
  * button bar: new connection, manage connections, new QM query, new mdx query, quick new query, help
  * filter drop target
  * member selection like on slide 18
  * execute query button for execution
  * dimension stack panel right/left as user preference
  * new mdx editor
  * improved olaptable

### Lifecycle ###
**create new connection**
  * wizard pops up
  * choose jdbc mondrian / xmla
  * enter details (paste schema or upload)
  * save & close / next
  * if next:
    * choose QM / mdx query
    * if QM query:
      * get cubes all connected connections (expand the one from the new connection)
    * create new query tab

**manage connections**
  * opens up connection list popup
    * delete connection
    * add connection > create new connection workflow > back to connection list
    * modify connection > opens up connection details (show schema / change and save it or upload new schema)
    * save connection
    * back to connection list popup


**new QM query**
  * opens up cubemenu (treelist with connections as parent nodes)
  * select cube
  * new query tab opens

**new mdx query**
  * opens up connection list
  * select connection
  * new mdx query tab opens

**quick new query**
  * dropdownlist with connections/cubes
  * if connection selected > button new mdx query is enabled
  * if cube selected > new QM query / new mdx query is enabled
  * on button click > new query tab


**new querying workflow**
  * **QM query:**
    * QM query tab opens up
    * its possible to move dimensions in the dimensionpanel from UNUSED to COLUMNS / ROWS / FILTER axis
    * other way to change selection is to drag the dimensions to the canvas to the COLUMNS / ROWS / FILTER drop targets
    * when dropped its possible to include members with the dimension browser
    * execute query

  * **change QM query:**
    * in the dim panel click on dimension and select "browse dimension for selection"
    * click on "Edit" mode > canvas transforms back to the state before the first query execution, click on dimension and choose "browse dimension for selection"
      * opens up dimension browser
      * change selection
      * close dimension browser
    * execute query

  * **new / change MDX query:**
    * mdx query tab opens up
    * on the top of the query panel mdx editor area
    * below table
    * execute

new integration lifecycle
  * TBD

## Post 0.5.0 ##
**0.5.1**
  * nice fancy css UI
  * allow formula for measures on the filter axis ([Measures](Measures.md).[Actual](Actual.md) > 10)
  * convert QM query to MDX query
  * swap axis

**0.5.2**
  * biserver integration
1. save/load queries with solution repository
2. new queries (show schema/cubes window just like Jpivot and analyzer) ... would result in a new PUC tab with a PAT without anything except the query tab within PAT

STEPS to take for that:
1. make hibernate save and reload just like biserver HSQLDB (maybe even create a new HSQLDB next to hibernate/quartz/sampledata as default db)
2. enable mdx query saving
3. strip out security so it works with the biserver
4. introduce some parameters like we had already.. for demo mode + last biserver integration
5. enable "connect on startup" for connections
6. the new query thing... not 100% sure how exactly we're going to do this.. if we need a separate plugin or whatever
7. change the pat biserver plugin so it adapts to the new things i just said

  * save queries in the biserver

**0.5.x**
  * drop targets in the table
  * drill support
  * advanced dim browser / member selection like slide 23