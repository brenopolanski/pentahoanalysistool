<?php
    //  Start session
    session_start();
    //  Destroy session
    session_destroy();
    //  Redirect to index
    header('Location: index.php');
?>
