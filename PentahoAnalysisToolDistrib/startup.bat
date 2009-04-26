@echo off
REM ***************************************
REM   BATCH SCRIPT TO START PAT
REM ***************************************
cd data
start start_hypersonic.bat
cd ..\jetty\

goto :startjava

:startjava
call java -jar -DSTOP.KEY=secret -DSTOP.PORT=8079 -Xmx512M -XX:PermSize=64M -XX:MaxPermSize=128M -DCONSOLE_HOME=. start.jar
