@echo off

:: Change directory to the location of the .bat file
cd /d %~dp0

CALL mvn liquibase:update -Dliquibase.propertyFile=src/main/resources/liquibase/config/liquibase.properties

pause 