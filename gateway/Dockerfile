FROM amazoncorretto:11
# debug
ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8081
COPY target/*.jar gateway.jar
ENTRYPOINT ["java", "-jar", "/gateway.jar"]