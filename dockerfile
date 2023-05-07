FROM openjdk:11.0.10-jre-slim-buster

# Pinpoint Agent 설치
ARG PINPOINT_VERSION=2.5.1
ADD https://github.com/pinpoint-apm/pinpoint/releases/download/v${PINPOINT_VERSION}/pinpoint-agent-${PINPOINT_VERSION}.tar.gz /opt/pinpoint-agent.tar.gz
RUN tar -xf /opt/pinpoint-agent.tar.gz -C /opt && rm /opt/pinpoint-agent.tar.gz && mv /opt/pinpoint-agent-${PINPOINT_VERSION} /opt/pinpoint-agent

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
COPY json json/

# Pinpoint Agent 설정
ENV PINPOINT_AGENT_ID=smu-enip-prod-01
ENV PINPOINT_APPLICATION_NAME=smu-enip
ENTRYPOINT ["java", "-javaagent:/opt/pinpoint-agent/pinpoint-bootstrap-${PINPOINT_VERSION}.jar", "-Dpinpoint.agentId=${PINPOINT_AGENT_ID}", "-Dpinpoint.applicationName=${PINPOINT_APPLICATION_NAME}", "-jar", "/app.jar"]