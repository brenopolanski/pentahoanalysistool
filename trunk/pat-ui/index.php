<?php
    //  Start session
    session_start();
    //  Destroy session
    session_destroy();
?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>PATui - Login to PATui Demo</title>
        <link rel="stylesheet" href="css/blueprint/screen.css" type="text/css" media="screen, projection">
        <!--[if lt IE 8]>
        <link rel="stylesheet" href="css/blueprint/ie.css" type="text/css" media="screen, projection">
        <![endif]-->
        <link rel="stylesheet" href="css/styles.css" type="text/css" media="screen, projection">
        <script type="text/javascript" src="js/jquery.js"></script>
    </head>
    <!-- Login -->
    <body id="login">
        <!-- Container -->
        <div class="container">
            <div class="span-10 prepend-7 append-7">
                <!-- Header -->
                <h2><strong>Login to PAT<em>ui</em></strong></h2>
                <!-- Eof header -->
                <!-- Errors -->
                <div id="sys-error" class="error">
                    An error has occurred with PAT, please see below.<br/>
                    <span class="small">&nbsp;</span>
                </div>
                <!-- Eof errors -->
                <!-- Login-wrapper -->
                <div id="login-wrapper" class="span-10">
                    <div class="append-1 prepend-1 prepend-top append-bottom">
                        <form method="post" action="" name="login_form" id="login_form">
                            <label class="bottom" for="username">Login</label>
                            <input class="login" id="username" name="username" size="30" type="text">
                            <hr class="space">
                            <label class="bottom" for="password">Password</label>
                            <input class="password" id="password" name="password" size="30" type="password">
                            <hr class="space">
                            <input type="submit" id="submit" name="submit" value="Login" class="button">
                        </form>
                    </div>
                </div>
                <!-- Eof login-wrapper -->
                <!-- Footer -->
                <div id="footer" class="span-10 quiet prepend-top ralign small">
                    Powered by <a href="http://code.google.com/p/pentahoanalysistool/" target="_blank">PAT</a> version 8.0 &copy; 2010
                </div>
                <!-- Eof footer -->
            </div>
        </div>
        <!-- Eof container -->
        <script type="text/javascript">
            $(function() {
                $("#auth-error, #sys-error").hide();
                $('input#submit').click(function() {
                    var data_string = 'username='+$('#username').val()+'&password='+$('#password').val();
                    $(this).attr("disabled", true).val('Please wait...');
                    $.ajax({
                        type: "POST",
                        url: "inc/info.php",
                        data: data_string,
                        success: function(data) {
                            if(data != 0) {
                                $("#sys-error span").html(data);
                                $("#sys-error").fadeIn(400);
                                $('input#submit').attr("disabled", false).val('Login');
                            }else if(data == 0) {
                                window.location.replace("launch.php");
                            }
                        },
                        error: function(data) {
                            $("#sys-error").show();
                            $("#sys-error span").html(data);
                            $(".error").fadeIn(400);
                            $(this).attr("disabled", false).val('Login');
                        }
                    });
                    return false;
                });
            });
        </script>
    </body>
</html>