FROM ubuntu
RUN apt-get update && apt-get install openjdk-17-jdk vim curl -y
RUN curl -L -O https://artifacts.elastic.co/downloads/beats/filebeat/filebeat-7.17.0-linux-x86_64.tar.gz
RUN tar xzvf filebeat-7.17.0-linux-x86_64.tar.gz
ADD filebeat.yml /filebeat-7.17.0-linux-x86_64/filebeat.yml
RUN chmod go-w /filebeat-7.17.0-linux-x86_64/filebeat.yml
ADD elastic-apm-agent-1.46.0.jar /apm.jar
WORKDIR /opt
ADD target/pro-epargne-*.jar pro-epargne.jar
ADD script.sh /script.sh
RUN chmod 777 /script.sh
EXPOSE 8080
ENTRYPOINT ["/script.sh"]
