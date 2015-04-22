#A rough idea of the UI features we want to implement

# Introduction #

Here's some of my ideas


# Details #

![http://wamonline.org.uk/RoughIdeas.png](http://wamonline.org.uk/RoughIdeas.png)


## Toolbar Panel ##
The toolbar panel should probably only be visible in Standalone mode not Pentaho hosted, as it should contain drop down menus, with things like logout/connect/save/help/connection manager etc. Things that a relevant when running as a standalone application but not relevant when triggered as an xaction.

## Control Bar Panel ##
This should contain menu items relevant to whats currently being displayed in the data display. Ie: cube selection, properties, calculated measure wizard, chart setting, export to x format etc. It should also dynamically change to be relevant, ie: if the chart is currently on display show charting options, if the data table is on display show options relevant to that, hopefully this will simplify things a little and reduce screen clutter.

## MDX/Filter Panel ##
MDX Panel, allows MDX code entry(needs to be worked out how) and there will be an execute button. I'd like to do syntax highlighting etc, if possible?? Filter Panel, don't know, haven't thought about it yet.

## Dimension Panel ##
Dimension Panel, something like Paul's accordion panel seems like a likely choice when it comes to making things simple, but ideas and suggestions need to be worked on to come up with a clean interface that makes it obvious. The Rows and Columns trees will also disappear in 0.2 where dropping to a grid will be utilized, cleaning up that dialog and freeing up real estate.

## Drill Down Panel ##
Shows the contents of the drill, nothing too special

## Data Display ##
I think this should possibly be a tab panel, with tab 1 being the data table, and tab 2 being a default jfreechart. This would allow quick and easy visual representation of the data being shown on the table, and give plenty of space to charting.