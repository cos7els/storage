FROM openjdk:11
RUN mkdir /app
WORKDIR /app
COPY target/storage-0.0.1-SNAPSHOT.jar /app
ENTRYPOINT java -jar /app/storage-0.0.1-SNAPSHOT.jar