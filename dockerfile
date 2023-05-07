FROM openjdk:11.0.10-jre-slim-buster
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
COPY json json/
ENTRYPOINT ["java","-jar",\
"-javaagent:./pinpoint-agent-2.5.1/pinpoint-bootstrap-2.5.1.jar",\
"-Dpinpoint.config=./pinpoint-agent-2.5.1/pinpoint-root.config",\
"-Dpinpoint.agentId=smu-enip-prod-01","-Dpinpoint.applicationName=smu-enip-prod",\
"/app.jar"]
