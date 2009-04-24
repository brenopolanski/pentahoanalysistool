<%@ page session="true" contentType="text/html;" %>
<html>
<meta name="gwt:property" content="locale=<%=request.getLocale()%>">
        <head>

                <!--                                           -->
                <!-- Any title is fine                         -->
                <!--                                           -->
                <title>Pentaho Analysis Tool</title>

                <!--                                           -->
                <!-- The module reference below is the link    -->
                <!-- between html and your Web Toolkit module  -->
                <!--                                           -->
                <meta name='gwt:module' content='org.pentaho.pat.Pat'/>

                  <!-- <link rel="stylesheet" type="text/css" href="js/ext/resources/css/ext-all.css"/>-->
<!--    <link rel="stylesheet" type="text/css" href="js/ext/resources/css/xtheme-gray.css" />-->

        </head>

        <!--                                           -->
        <!-- The body can have arbitrary html, or      -->
        <!-- we leave the body empty because we want   -->
        <!-- to create a completely dynamic ui         -->
        <!--                                           -->
        <body oncontextmenu="return false">

                <!--                                            -->
                <!-- This script is required bootstrap stuff.   -->
                <!-- You can put it in the HEAD, but startup    -->
                <!-- is slightly faster if you include it here. -->
                <!--                                            -->
                <script language="javascript" src="org.pentaho.pat.Pat.nocache.js"></script>

                <!-- OPTIONAL: include this if you want history support -->
        <iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1'
        style="position: absolute; width: 0; height: 0; border: 0"></iframe>
<div id="splash" style="height:100%;width:100%;">
        <table width="100%" height="90%">
                <tr>
                        <td width="100%" height="100%" align="center" valign="middle">


      <div id="splash_loading" alttext="Error">Loading</div>
                        </td>
                </tr>
        </table>
</div>
        </body>
</html>

