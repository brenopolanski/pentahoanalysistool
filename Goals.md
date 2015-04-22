# Roadmap #
This is the Roadmap (so far) of the project and where we aim to be heading.

A detailed list of tasks can be found [here](http://jira.pentaho.com/browse/PAT?report=com.atlassian.jira.plugin.system.project:roadmap-panel)

# Future Releases #


Details about this development sprint can be found [here](DevelopmentSprint050.md)
## PAT 1.0 (December 2009) ##
  * Theme
  * Drag and Drop
  * (Live Table) Writeback
  * Save/Load
  * Basic Users
  * Export to XLS
  * Drilling
  * Visual Totals
  * Chart drilling
  * MDX Editor (because of lack of calc'd member wizard support)
  * Measures filter wizard (Measures.Actual > 1000)
  * biserver integration

## Post PAT 1.0 Backlog ##
  * Enable mixed DnD and MDX querying
  * Saving filter sets
  * New Calculated Measures wizard
  * Formatting options
  * Be very user friendly with most possible functionality
  * Provide JSON/WebService connectivity to backend

# Released Versions #

## [PAT 0.5.0](DevelopmentSprint050.md) ##
  * Heavy UI redesign
  * Save connections in the persistance layer
  * New query workflow
  * New connection workflow
  * New dimension browser


## PAT 0.4 ##
  * Support of plain MDX Queries
  * Improve support for different integration modes
  * Connection Manager (only in integration demo mode STANDALONE / USER)
  * Refactoring / pruning of GUI classes
  * Removal of Toolbar + Title Widget to gain more space
  * Switch to Mosaic's Live Table + rewrite all Table related code
  * New Welcome Panel (Startup Dashboard)
  * Bug Fixes

## PAT 0.3 ##
  * Improve DnD Selection and user interaction in general
  * Replace Table with the new Scrolltable
  * Upgrade to GWT 1.6 / Mosaic 0.2
  * Multiple Tabs / Queries
  * Translation (NL)
  * Bug Fixes


## PAT 0.2 ##
  * Design new backend/frontend architecture
  * Choose GWT Widget Set (GWT Mosaic it is)
  * Connectivity (XMLA and Mondrian Connection)
  * Get/Set Cubes/Dims
  * Do basic Drag 'n Drop selection
  * Localization (French, German, English, Spanish)
  * Integrate Spring Security
  * Prepare Hibernate Persistance Layer

A complete changelog can be found [here](http://jira.pentaho.com/ReleaseNote.jspa?version=10615&styleName=Html&projectId=10165&Create=Create)

## PAT 0.1 ##
  * Fork and Mavenize Halogen
  * Port it to GWT-EXT
  * Get some ideas where PAT should head to


A complete changelog can be found [here](http://jira.pentaho.com/ReleaseNote.jspa?version=10613&styleName=Html&projectId=10165&Create=Create)