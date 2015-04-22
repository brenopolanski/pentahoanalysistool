# Introduction #

We (pentahoanalysistool project team) work on creating a lightweight Query Model for OLAP cubes, since we need to connect our GWT application to the java (olap4j) backend. All of the following data/methods are serializable and used in RPC calls in GWT.
The method calls are session-aware and can be executed asynchronously



# Data #
### org.pentaho.pat.rpc.beans ###

The data we get from the backend is being structured in:
  * Rowheaders `[][]`
  * Columnheaders `[][]`
  * CellData `[][]`


# Discovery service #
### org.pentaho.pat.rpc.discovery ###
It allows users to:
  * get List of available Cubes
  * get List of available Dimensions in a Cube on a specific Axis
  * get Tree List of available Members of a specific Dimension


# Query Service #
### org.pentaho.pat.rpc.query ###

In order to build the query we support following methods right now (in chronological order of usage):
  * Create new Query
  * Move a dimension to a different axis (Parameter: Axis, Dimension )
  * Create Selection (Parameter: Dimension, List of Members)
  * Execute Query (returns data in the structure described above)
  * Clear Selection (clear query, ready for new one)


# Future Plans #

At the moment we only support RPC as interface, but we're planning on supporting JSON/WebService at some point.