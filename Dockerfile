FROM openjdk:17
RUN mkdir /app
WORKDIR /app
COPY target/storage-0.0.1-SNAPSHOT.jar /app/storage.jar
ENTRYPOINT java -jar /app/storage.jar