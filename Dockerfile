# (1) use Alpine Linux for build stage
FROM alpine:3.10.1 as build
# (2) install build dependencies
RUN apk --no-cache add openjdk11
RUN apk --no-cache add maven

# build JDK with less modules
RUN /usr/lib/jvm/default-jvm/bin/jlink \
    --compress=2 \
    --module-path /usr/lib/jvm/default-jvm/jmods \
    --add-modules java.base,java.logging,java.xml,jdk.unsupported,java.sql,java.naming,java.desktop,java.management,java.security.jgss,java.instrument \
    --output /jdk-minimal

# fetch maven dependencies
WORKDIR /build
COPY pom.xml pom.xml
RUN mvn dependency:go-offline
# build
COPY src src
RUN mvn clean package

# prepare a fresh Alpine Linux with JDK
FROM alpine:3.10.1
# get result from build stage
COPY --from=build /build/target/*.jar /app.jar
COPY --from=build /jdk-minimal /opt/jdk/
VOLUME /tmp
EXPOSE 8080
CMD /opt/jdk/bin/java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar