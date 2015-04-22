Features that Pentaho wants in PAT (Needs updating)
# Introduction #
**This is a very old list, it needs to be updated**

This is a starting point for capturing Pentaho requirements for a thin analysis tool.

# Details #

## Grid ##
Dimension drop targets

Drill Interaction
**Support for all drill modes - drill member, drill position, drill replace** Double-click on member to drill down, shift-dk (?) to drill up

Support for right-click menu on:

Members
**keep only/remove only** sort ascending/descending
**show/hide parent** show/hide member properties
**pivot - maybe an arrow out for selecting nested location upon pivot** top/bottom filters

Data cells
**View Drill-through records** Annotate cells (comments) - future
**Attach file - future** Add hyperlink - future - should include ability to pass context

Sort Icon on all column headers - sort modes are default (outline order), ascending, descending

Hide Spans (suppress repeating headers for nested dimensions)

Show/Hide member properties

Pivot - pivots entire table

Show/Hide banding&nbsp;

Drill through indicators&nbsp;

Support for inline images in data cells (i.e. KPI indicators, trend indicators, other images)

Support for background color control based on conditional formatting definitions in the Mondrian Schema (header and data cells)&nbsp;

Resizing Columns - custom column sizes stored with reports, should be attached to a tuple (not positional)

Report level formatting - Ability to set font, font color, background color, border style at the cell, row or column (definitions should follow metadata - not be positional)&nbsp;

Sub-totals&nbsp;

row/column paging&nbsp;

## Cube and Dimension Browsing ##

Tree control for browsing cubes and dimensions
**drag and drop members/dimensions from tree onto grid** right-click
 Add to columns - expand to column position
 Add to rows - expand to row position&nbsp;

Selection Modes:
**Individual member selection** Also select Children
**Also select Level** Also select descendants
**Also select ancestors** Also select bottom level


Define Calculated member queries - provide a nice GUI for defining a calculation

Ability to define conditional formatting