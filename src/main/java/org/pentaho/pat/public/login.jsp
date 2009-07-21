<%@ taglib prefix='c' uri='http://java.sun.com/jstl/core_rt' %>
<%@ page import="org.springframework.security.ui.AbstractProcessingFilter" %>
<%@ page import="org.springframework.security.ui.webapp.AuthenticationProcessingFilter" %>
<%@ page import="org.springframework.security.AuthenticationException" %>

<html>
  <head>
    <title>Pentaho Analysis Tool - Login</title>
	<link rel="stylesheet" type="text/css" href="login.css" />
  </head>

  <body onload="document.f.j_username.focus();">
	<div id="main">
	<div id="content">
    <h1>Pentaho Analysis Tool - Login</h1>

	  <p>Valid users:<br/>
	  username <b>admin</b>, password <b>admin</b></p>

    <c:if test="${not empty param.login_error}">
      <font color="red">
        Your login attempt was not successful, try again.<br/><br/>
        Reason: <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>.
      </font>
    </c:if>

    <form name="f" action="<c:url value='/pat/loginProcess'/>" method="POST">
      <table id="formtable">
        <tr><td id="lable">User:</td><td id="datafield"><input type='text' name='j_username' value='<c:if test="${not empty param.login_error}"><c:out value="${SPRING_SECURITY_LAST_USERNAME}"/></c:if>'/></td></tr>
        <tr><td id="lable">Password:</td><td id="datafield"><input type='password' name='j_password'></td></tr>
        <!--tr><td><input type="checkbox" name="_spring_security_remember_me"></td><td>Don't ask for my password for two weeks</td></tr-->

        <tr><td><input name="submit" type="submit"></td><td><input name="reset" type="reset"></td></tr>
        
      </table>

    </form>
	</div>
	</div>
  </body>
</html>