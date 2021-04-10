FROM maven:3.6.3-adoptopenjdk-11 AS MAVEN_TOOL_CHAIN
WORKDIR /tmp/

COPY pom.xml /tmp/pom.xml
COPY checkstyle /tmp/checkstyle/
RUN mvn dependency:resolve-plugins dependency:go-offline -B

COPY src /tmp/src/
RUN mvn package -B

FROM adoptopenjdk:11-jre-openj9
RUN mkdir /opt/app
COPY --from=MAVEN_TOOL_CHAIN /tmp/target/sbking*with-dependencies.jar /opt/app/server.jar
EXPOSE 60000
CMD ["java", "-jar", "/opt/app/server.jar"]