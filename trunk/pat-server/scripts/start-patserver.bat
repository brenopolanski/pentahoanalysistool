@echo off
setlocal

call "%~dp0\data\set-java.bat"

cd data
start start_hypersonic.bat
cd ..\tomcat\bin
set CATALINA_HOME=%~dp0tomcat
set CATALINA_OPTS=-Xms256m -Xmx768m -XX:MaxPermSize=256m -Dfile.encoding=UTF-8
set JAVA_HOME=%_JAVA_HOME%
call startup
:quit
endlocal