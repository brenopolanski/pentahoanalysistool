# Introduction #

We need to keep our code clean. To do so we have defined some coding guidelines, please follow them!

# General Guidelines #

Our coding guidelines are based on [the Java standard conventions](http://java.sun.com/docs/codeconv/html/CodeConvTOC.doc.html), [GWT codestyle](http://code.google.com/intl/de-DE/webtoolkit/makinggwtbetter.html#codestyle) and [Mondrian coding guide](http://mondrian.pentaho.org/documentation/developers_guide.php#Coding_guide)

## Using Eclipse Preferences for Code Style ##
  * Formatter: In Eclipse: Window|Preferences|Java|Code Style|Formatter - Import Style .. - import [pat-style.xml](http://pentahoanalysistool.googlecode.com/files/pat-style-eclipse-1.0.xml). This formatter settings will set indentation to 4 spaces, line width 120 etc.

  * Code Templates Comments: In Window|Preferences|Java|Code Style|Code Templates|Comments
    * **Files**
```
/*
 * Copyright (C) 2009 ${user}
 *
 * This program is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the Free 
 * Software Foundation; either version 2 of the License, or (at your option) 
 * any later version.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along 
 * with this program; if not, write to the Free Software Foundation, Inc., 
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
 *
 */
```
    * **Types:**
```
/**
 * Does X and Y and provides an abstraction for Z.
 * @created Jan 1, 2009 
 * @since X.Y.Z
 * @author ${user}
 * 
 */
```

## Comments and Javadoc ##
Every file should have an GPLv2 license header at the top, prefaced with a copyright notice. A package statement and import statements should follow, each block separated by a blank line. Next is the class or interface declaration. In the Javadoc comments, describe what the class or interface does and include tags @since VERSION-NUMBER, @author and @created

```
/*
 * Copyright (C) 2009 Tom Barber
 *
 * This program is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the Free 
 * Software Foundation; either version 2 of the License, or (at your option) 
 * any later version.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along 
 * with this program; if not, write to the Free Software Foundation, Inc., 
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
 *
 */

package com.google.foo;

import com.google.bar.Blah;
import com.google.bar.Yada;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Does X and Y and provides an abstraction for Z.
 * @created Apr 23, 2009 
 * @since 0.5.0
 * @author Tom Barber
 * 
 */
public class Foo {
  ...
} 
```

## Class Structure and Member Sort Order ##

Java types should have the following member order:

  1. Nested Types (mixing inner and static classes is okay)
  1. Static Fields
  1. Static Initializers
  1. Static Methods
  1. Instance Fields
  1. Instance Initializers
  1. Constructors
  1. Instance Methods

Members that fall into the same category (e.g. static methods) should also be sorted in this order based on visibility:

  1. public
  1. protected
  1. default
  1. private

All methods should be sorted alphabetically. Sorting is optional but recommended for fields.

## Spacing and Indentation ##
  * Use spaces, not tabs.
  * Indentation 4. (line wrapping indentation 4)
  * Open braces on the same line as the preceding 'if', 'else', 'while' statement, or method or 'class' declaration.
  * Use braces even for single-line blocks.
  * Try to keep lines shorter than 80 characters.


## Variables ##

  * Try to use descriptive variables names if possible
  * Declare variables as near to their first use as possible.
  * Don't initialize variables with 'dummy' values just to shut up the compiler.
  * One declaration per line is recommended.

## Function length ##

Try to keep each function as short as one page in your IDE if possible


# PAT GWT Guidelines #

## General ##

  * Mark non-localized strings with the //$NON-NLS tag (non externalized strings)
```
log.warn(Messages.getString("Util.JdbcDriverFinder.NoDriversInPath")); //$NON-NLS-1$
```
  * If you set properties with strings, externalize them! These constants will be listed all together at the top of the class!
```
 /** Height of the panel. */
 private static final String HEIGHT = "280px"; //$NON-NLS-1$
 ...
 myPanel.setHeight(HEIGHT);
```

## New Widgets/Panels ##

  * Set up the widget's elements within the constructor (1..n class functions possible if it makes sense). This is necessary for a consistent code design and avoids rendering issues that could be caused if the widgets' are set up in the onLoad() method etc.
```
public class ConnectMondrianPanel extends LayoutComposite {
public ConnectMondrianPanel() {
  nameTextBox = new TextBox();
}
...
```
> OR
```
public class ConnectMondrianPanel extends LayoutComposite {
public ConnectMondrianPanel() {
  init();
}
...
private void init() {
 nameTextBox = new TextBox();
 urlTextBox = new TextBox();
}
```

  * Always extend your new widgets from LayoutComposite not LayoutPanel or any other existing panel
    * **CORRECT:**
```
public class ConnectMondrianPanel extends LayoutComposite {
public ConnectMondrianPanel() {
  init();
}
```
    * **WRONG:**
```
public class ConnectMondrianPanel extends LayoutPanel {
public ConnectMondrianPanel() {
  init();
}
```