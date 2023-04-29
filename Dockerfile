FROM openjdk:11
WORKDIR /app
COPY ./build/libs/fileprovider-1.0.0-RELEASE.jar ./app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]