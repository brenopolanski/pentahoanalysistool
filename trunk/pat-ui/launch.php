<?php
//  Start the session
session_start();

//  Check if the username is set, if not logout
if (!isset($_SESSION['username'])) {
    header('Location: logout.php');
}
?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>PATui - Analysis View Demo</title>
        <link rel="stylesheet" href="css/blueprint/screen.css" type="text/css" media="screen, projection">
        <!--[if lt IE 8]>
        <link rel="stylesheet" href="css/blueprint/ie.css" type="text/css" media="screen, projection">
        <![endif]-->
        <link rel="stylesheet" href="css/styles.css" type="text/css" media="screen, projection">
        <script type="text/javascript" src="js/jquery.js"></script>
        <script type="text/javascript" src="js/jquery.ui.js"></script>
        <script type="text/javascript" src="js/jquery.layout.js"></script>
        <script type="text/javascript" src="js/jquery.blockui.js"></script>
        <script type="text/javascript" src="js/application.js"></script>
    </head>
    <body id="launch">
        <!-- North / Toolbar -->
        <div id="toolbar" class="ui-layout-north">
            <ul id="menu-list">
                <li><a class="home" href="launch.php" title="Home">&nbsp;</a></li>
                <li><a class="logout" href="logout.php" title="Logout">&nbsp;</a></li>
            </ul>
        </div>
        <!-- Eof North / Toolbar -->
        <!-- West / Sidebar -->
        <div id="sidebar" class="ui-layout-west">
            <!-- Data -->
            <h3 class="bottom first">Data</h3>
            <div class="sidebar-inner">
                <select id="data-list">
                    <option value="none">Select a cube</option>
                </select>
            </div>
            <!-- Eof data -->
            <!-- Dimensions -->
            <h3 class="bottom">Dimensions</h3>
            <div class="sidebar-inner">
                <ul id="dimension-list">
                    <li class="placeholder quiet"><em>No dimensions available</em></li>
                </ul>
            </div>
            <!-- Eof dimensions -->
            <!-- Measures -->
            <h3 class="bottom">Measures</h3>
            <div class="sidebar-inner">
                <ul id="measure-list">
                    <li class="placeholder quiet"><em>No measures available</em></li>
                </ul>
            </div>
            <!-- Eof measures -->
        </div>
        <!-- Eof West / Sidebar -->
        <!-- Content / Center -->
        <div class="ui-layout-center">
            <div id="content">
                <!-- Column drop area -->
                <div id="column-drop">
                    <div class="column-drop-title">
                        <strong>Columns</strong>
                    </div>
                    <div class="column-drop-area">
                        <ul id="column-axis" class="top bottom">
                            <li class="placeholder quiet"><em>Drop dimensions and measures for columns here</em></li>
                        </ul>
                    </div>
                    <div class="clear"></div>
                </div>
                <!-- Eof column drop area -->
                <!-- Row drop area -->
                <div id="row-drop">
                    <div class="row-drop-title">
                        <strong>Rows</strong>
                    </div>
                    <div class="row-drop-area">
                        <ul id="row-axis" class="top bottom">
                            <li class="placeholder quiet"><em>Drop dimensions and measures for rows here</em></li>
                        </ul>
                    </div>
                    <div class="clear"></div>
                    <hr class="space"/>
                    <hr class="space"/>
                    <div>
                        <strong>row-axis:</strong>
                        <div id="rows">
                        </div>
                        <br/>
                        <strong>column-axis:</strong>
                        <div id="columns">
                        </div>
                    </div>
                </div>
                <!-- Eof row drop area -->
            </div>
        </div>
        <!--Eof Content / Center-->
    </body>
</html>