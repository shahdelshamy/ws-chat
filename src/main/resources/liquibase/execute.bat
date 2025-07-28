@echo off

:: Change directory to the location of the .bat file
cd /d "E:\myWorks\STS(java)\ws-chat"

CALL mvn org.liquibase:liquibase-maven-plugin:4.27.0:update -Dliquibase.propertyFile=src/main/resources/liquibase/config/liquibase.properties

pause 