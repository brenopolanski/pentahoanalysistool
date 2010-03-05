@echo off
setlocal

call "%~dp0\data\set-java.bat"

cd data
start stop_hypersonic.bat
cd ..\tomcat\bin
set CATALINA_HOME=%~dp0tomcat
set JAVA_HOME=%_JAVA_HOME%
shutdown.bat
endlocal
exit
