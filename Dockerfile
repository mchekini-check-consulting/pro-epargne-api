FROM ubuntu
RUN apt-get update && apt-get install openjdk-17-jdk vim curl -y
WORKDIR /opt
ADD target/pro-epargne-*.jar pro-epargne.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "pro-epargne.jar"]