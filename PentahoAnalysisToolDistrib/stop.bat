@echo off

REM ***************************************
REM   BATCH SCRIPT TO STOP PAT
REM ***************************************

goto :startjava

:startjava
cd data
start stop_hypersonic.bat
cd ..\jetty 
java -DSTOP.PORT=8079 -DSTOP.KEY=secret -jar start.jar --stop
