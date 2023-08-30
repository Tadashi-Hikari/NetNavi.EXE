FROM openjdk:8-alpine

COPY target/uberjar/netnavi.jar /netnavi/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/netnavi/app.jar"]
