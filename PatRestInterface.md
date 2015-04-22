# Introduction #

Here you can find all the documentation about the restful interface PAT provides. If you feel that it should be altered or extended in anyway please file a change request, or ask nicely ;)


# Details #

PAT has generated a lot of interest from people wanting to integrate OLAP analysis into their applications but do not want to have to learn OLAP4J or want to use a different programming language. PAT hopes to bridge that gap by allowing users to most of the PAT functionality through a rest interface.

## Session ##

Sadly currently PAT is not stateless, hopefully we can do something to rectify this in 2.0 but for now you need a session object to create a serverside session, this will be active by default for 30 minutes from the last moment of activity on the server.

URL: http://demo.analytical-labs.com/rest/admin/session

Params: none

Headers: Accept, json or xml

Type: POST

Returns: SessionId, Connections, Cubes, Schema

Example:

`{"@sessionid":"66834b32-cb7d-4f31-b51d-684381962024","connections":{"connection":[{"@connectionid":"1603c5ef-990d-486f-86c9-53596165bc68","@connectionname":"Steelwheels","schemas":{"schema":{"@schemaname":"SteelWheels","cubes":{"cube":{"@cubename":"SteelWheelsSales"}}}}},{"@connectionid":"d7523714-f287-41a4-acbc-4c8d1c119fa1","@connectionname":"SampleData","schemas":{"schema":{"@schemaname":"SampleData","cubes":{"cube":{"@cubename":"Quadrant Analysis"}}}}}]}}`

## Create Query ##

URL: http://demo.analytical-labs.com/rest/admin/query/SteelWheels/SteelWheelsSales/tomsquery

Params: sessionId=uuid, connectionId=uuid

Headers: Accept, json or xml

Type: POST

Result:Query Id, Dimensions, Members

Example:

> http://pastebin.com/4PSs4McQ
## Delete Query ##

URL: http://demo.analytical-labs.com/rest/admin/query/SteelWheels/SteelWheelsSales/tomsquery

Params: sessionId=uuid

Type: DELETE

Result: HTTP 200

## Save Query ##

URL: http://demo.analytical-labs.com/rest/admin/query/SteelWheels/SteelWheelsSales/tomsquery

Params: sessionId=uuid

Type: PUT

Result: HTTP 200

## Run Query ##

URL: http://demo.analytical-labs.com/rest/admin/query/SteelWheels/SteelWheelsSales/tomsquery/run?sessionid=uuid

Params: queryobject = queryobject

Type: PUT

Result: Resultset

Example:
```
curl -XPUT --basic -u admin:admin -HContent-type:application/json --data-binary '{ "@queryid" : "e66399c7-6a2a-43c9-bce4-d6b2dd2351b2","axis" : { "@location" : "COLUMNS","@nonempty" : "true","dimensions" : [ { "@dimensionname" : "Measures","levels" : { "@levelcaption" : "MeasuresLevel","@levelname" : "[Measures].[MeasuresLevel]","members" : [ { "@membercaption" : "Fact Count", "@membername" : "[Measures].[Fact Count]","@status" : "NONE"},{ "@membercaption" : "Quantity","@membername" : "[Measures].[Quantity]","@status" : "NONE"},{ "@membercaption" : "Sales","@membername" : "[Measures].[Sales]","@status" : "NONE"}]}}]}, "axis":{"@nonempty":"true","@location":"ROWS","dimensions":[{"@dimensionname":"Order Status","levels":[{"@levelcaption":"Type","@levelname":"[Order Status].[Type]","members":[{"@status":"NONE","@membercaption":"Cancelled","@membername":"[Order Status].[Cancelled]"},{"@status":"NONE","@membercaption":"Disputed","@membername":"[Order Status].[Disputed]"},{"@status":"NONE","@membercaption":"In Process","@membername":"[Order Status].[In Process]"},{"@status":"NONE","@membercaption":"On Hold","@membername":"[Order Status].[On Hold]"},{"@status":"NONE","@membercaption":"Resolved","@membername":"[Order Status].[Resolved]"},{"@status":"NONE","@membercaption":"Shipped","@membername":"[Order Status].[Shipped]"}]},{"@levelcaption":"Type","@levelname":"[Order Status].[Type]","members":[{"@status":"NONE","@membercaption":"Cancelled","@membername":"[Order Status].[Cancelled]"},{"@status":"NONE","@membercaption":"Disputed","@membername":"[Order Status].[Disputed]"},{"@status":"NONE","@membercaption":"In Process","@membername":"[Order Status].[In Process]"},{"@status":"NONE","@membercaption":"On Hold","@membername":"[Order Status].[On Hold]"},{"@status":"NONE","@membercaption":"Resolved","@membername":"[Order Status].[Resolved]"},{"@status":"NONE","@membercaption":"Shipped","@membername":"[Order Status].[Shipped]"}]}]}]}}'  http://localhost:8080/rest/admin/query/SteelWheels/SteelWheelsSales/newquery/run?sessionid=56ccab1a-22a3-435f-8dcb-4cc5d4ee30ed
```
## Resultset ##