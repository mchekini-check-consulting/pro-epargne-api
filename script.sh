#!/bin/bash

cd /filebeat-7.17.0-linux-x86_64/ && ./filebeat &
java -javaagent:/apm.jar \
          -Delastic.apm.service_name=pro-epargne \
          -Delastic.apm.server_urls=http://192.168.1.13:8200 \
          -Delastic.apm.secret_token= \
          -Delastic.apm.application_packages=com.checkconsulting \
          -jar /opt/pro-epargne.jar