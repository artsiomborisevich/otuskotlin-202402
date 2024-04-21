# Base image to build a JRE
FROM eclipse-temurin:17 AS jre-build

# Custom Java runtime
RUN $JAVA_HOME/bin/jlink \
         --add-modules java.base \
         --strip-debug \
         --no-man-pages \
         --no-header-files \
         --compress=2 \
         --output /javaruntime

# Base image
FROM debian:buster-slim
ENV JAVA_HOME=/opt/java/openjdk
ENV PATH "${JAVA_HOME}/bin:${PATH}"

# Copy JRE from the base image
COPY --from=jre-build /javaruntime $JAVA_HOME

# Add app user
ARG APPLICATION_USER=appuser
RUN adduser --no-create-home -u 1000 --disabled-login ${APPLICATION_USER}

# Configure working directory
RUN mkdir /app && \
    chown -R $APPLICATION_USER /app

USER 1000

ARG JAR_FILE=kotlin-wiz/wiz-service/build/libs/wiz-service-0.0.1.jar
COPY --chown=1000:1000 ${JAR_FILE} /app/kotlinwiz.jar
WORKDIR /app

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/kotlinwiz.jar"]