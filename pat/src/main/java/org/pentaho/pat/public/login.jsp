<%@ taglib prefix='c' uri='http://java.sun.com/jstl/core_rt' %>
<%@ page import="org.springframework.security.ui.AbstractProcessingFilter" %>
<%@ page import="org.springframework.security.ui.webapp.AuthenticationProcessingFilter" %>
<%@ page import="org.springframework.security.AuthenticationException" %>

<html>
  <head>
  <meta http-equiv="content-type" content="text/html; charset=windows-1250">
  <meta name="generator" content="Pentaho Analysis Tool">
  <title>Pentaho Analysis Tool - Login - Version 0.5</title>
  <link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/2.8.0r4/build/reset-fonts-grids/reset-fonts-grids.css">
  <style type="text/css">
  
  /* Globals */
  html, body {
    color:  #333333;
    height: 100%; 
  }
  
  .clr {
    clear:  both;
    height: 0;
  }
  
  h1 {
    color:      #d5d5d5;
    font-size:  153.9%;
  }
  
  h2 {
    font-size: 100%;
  }
  
  /* Header */
  
  #hd {
    background:     #F5F5F5;
    border-bottom:  1px solid #D5D5D5;
    height:         54px;
    line-height:    54px;
    padding:        0 10px;
    text-align:     right;
  }
  
  /* Container */
  
  #container {
    height:       214px;
    left:         50%;
    margin-left:  -200px; 
    margin-top:   -135px;
    position:     absolute;
    text-align:   center;
    top:          50%;
    width:        400px; 
  }
  
  /* Module Wrapper */
  
  .md-wrap {
    border:                 4px solid #F5F5F5;
    text-align:             left;
    -moz-border-radius:     4px; 
    -webkit-border-radius:  4px;
  }

    /* Module Header */
    
    .md-hd {
      background:     #EFEFEF url('images/md-hd-bg.png') repeat center center;
      border-bottom:  1px solid #D5D5D5;
      height:         26px;
      line-height:    26px;
      padding-left:   5px;
    }
    
    .md-hd h2 {
      background:   transparent url('images/arrow_right_16.png') no-repeat left center;
      font-weight:  700;
      padding-left: 20px;
    }
  
    /* Module Body */
  
    .md-bd {
      background: #FFFFFF;
      padding:    0 10px;
    }
    
    .md-bd .left {
      float:          left;
      padding-left:   15px;
      padding-top:    55px;
      padding-right:  25px;
    }
    
    .md-bd .right {
      float:  left;
    }
    
    .md-bd .right input[type='text'],
    .md-bd .right input[type='password'] {
      border:   1px solid #D5D5D5;
      padding:  4px;
      width:    280px;
    }
    
    .md-bd .right ul li label {
      color:          #777777;
      display:        block;
      font-weight:    700;
      margin-top:     15px;
      margin-bottom:  5px;
    }
    
    /* Module Footer */
    
    .md-ft {
      padding:    20px 20px;
      text-align: right;
    }
    
    .md-ft .button {
      background:     #EFEFEF url('images/md-hd-bg.png') repeat center center;
      border:         1px solid #D5D5D5;
      cursor:         pointer;
      padding:        2px 0;
      text-align:     center;
      width:          88px;
    }
  
  </style>
  </head>

  <body onload="document.f.j_username.focus();">
  <!-- Header -->
  <div id="hd"><h1>Pentaho Analysis Tool 0.5</h1></div>
  <!-- Container -->
  <div id="container">
    <!-- Form -->
    <form name="f" action="<c:url value='/pat/loginProcess'/>" method="POST">
      <!-- Module Wrapper -->

      <div class="md-wrap">
        <!-- Module Header -->
        <div class="md-hd">
        <h2>Login to Pentaho Analysis Tool</h2>
    <c:if test="${not empty param.login_error}">
      <font color="red">
        Your login attempt was not successful, try again.<br/><br/>
        Reason: <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>.
      </font>
    </c:if>

        </div>
        <!-- Module Body -->
        <div class="md-bd">
          <div class="left">

            <img src="images/home_go_32.png" alt="Login Icon">
          </div>
          <div class="right">
            <ul>
              <li><label>Username:</label></li>
              <li><input type="text" name="j_username" value='<c:if test="${not empty param.login_error}"><c:out value="${SPRING_SECURITY_LAST_USERNAME}"/></c:if>' /></li>
              <li><label>Password:</label></li>
              <li><input type="password" name="j_password" value="" /></li>
 
        
            </ul>
          </div>
          <div class="clr"></div>
        </div>
        <!-- Module Footer -->
        <div class="md-ft">
          <input type="submit" value="Login" class="button" /> or <input type="reset" value="Reset" class="button" />
        </div>

      </div>
    </form>
  </div>
  </body>
</html>
