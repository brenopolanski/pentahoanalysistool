# Introduction #

This section tries to describe the basic structure of a query object. A query object represents the current selections of the user and exposes methods to manipulate it's structure.

This is a 'work in progress' and serves as a placeholder for our discussion's notes.


# Details #

## Top level : Query ##

A query has the following properties

  * axis (implemented via QueryAxis)

### axis ###

A query has many QueryAxis objects as it's children. One of them is an "unused" axis, which contains dimensions that are not actually included in the query yet. The axis supported will be the following :

  * columns
  * rows
  * slicer
  * unused

One of the dimensions is the Measures dimension. It is treated the same way as the other dimensions, thus allowing us to either crossjoin it with others, or simply drop it in the slicer axis.

## QueryAxis ##

A QueryAxis has the following properties.

  * dimensions (implemented via QueryDimension)

### dimensions ###

An axis is composed of a **ordered** list of dimensions that are currently "selected" for the given axis.

When parsing an axis dimensions into MDX, we use a Crossjoin function.

## QueryDimension ##

A QueryDimension specifies what selections are performed on a given cube dimension. It also defines what members are to be excluded and what sort rules we must apply to those selections. It has the following properties.

  * selections
  * exclusions
  * sort rule

### selections ###

This property defines what members of a given dimensions are to be included in the final MDX statement.

### exclusions ###

This property defines what needs to be excluded from the selection.

### sort rule ###

This defines wether we sort the selection in ascending or descending or natural order.