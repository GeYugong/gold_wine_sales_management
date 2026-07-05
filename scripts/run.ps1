$ErrorActionPreference = "Stop"
$env:JAVA_HOME = "D:\2software\temurin-jdk-17"
$env:Path = "$env:JAVA_HOME\bin;D:\2software\apache-maven-3.9.16\bin;$env:Path"
mvn exec:java
