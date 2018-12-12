FROM maven:3.5.2-jdk-8

RUN apt-get update && apt-get install ant
