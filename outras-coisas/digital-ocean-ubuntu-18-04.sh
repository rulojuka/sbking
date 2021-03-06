#!/bin/bash
# Update everything and
# Installs Java 10 openjdk version "10.0.2" 2018-07-17
# and Maven - it takes about 3 minutes
apt update && apt install -y default-jdk maven

mkdir workspace
cd workspace
git clone https://github.com/rulojuka/sbking.git
cd sbking
mvn dependency:resolve

mvn package
java -jar target/sbking-1.0.0-alpha-jar-with-dependencies.jar

