FROM openjdk:23-jdk-slim

RUN apt-get update && apt-get install -y maven

WORKDIR /app