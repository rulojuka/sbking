FROM maven:3.9.3-eclipse-temurin-17-focal AS MAVEN_TOOL_CHAIN
WORKDIR /tmp/

COPY pom-server.xml /tmp/pom.xml
COPY checkstyle /tmp/checkstyle/
RUN mvn dependency:resolve-plugins dependency:go-offline -B

COPY src /tmp/src/
RUN mvn package -B

FROM eclipse-temurin:17.0.7_7-jre-focal
RUN mkdir /opt/app
COPY --from=MAVEN_TOOL_CHAIN /tmp/target/sbking*.jar /opt/app/server.jar
EXPOSE 8080
CMD ["java", "-jar", "/opt/app/server.jar"]