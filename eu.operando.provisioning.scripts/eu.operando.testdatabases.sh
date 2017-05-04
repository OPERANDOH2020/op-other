#!/bin/bash

#check ubuntu 16.04
OS_TYPE=`cat /etc/issue | awk -F. '{printf($1)}'`
OS_VERSION=`cat /etc/issue | awk -F. '{printf($2)}'`
if [ [ "$OS_TYPE" -eq "Ubuntu" ] && [ "$OS_VERSION" -eq "16.04.2" ] ]
then
  echo "Same OS and Version"
else
  echo "Warning: This script was developed for Ubuntu 16.04"
fi

# get docker IP of dnsmasq
DNS_IP=`docker exec -i -t dnsmasq hostname -i | awk -F' ' '{printf($1)}'`
echo "The following IP will be used as DNS_IP for all operando components: $DNS_IP"

docker run -i -t --name eu.operando.core.ldb.server.test.00 --dns $DNS_IP -e "MYSQL_DB_HOST=mysql.integration.operando.lan.esilab.org" -e "MYSQL_DB_NAME=operando_logdb" -e "MYSQL_DB_PASSWORD=root" -e "MYSQL_DB_USER=root" registry.devops.operando.esilab.org:5000/operando/eu.operando.core.ldb.server.test.00:ALPHA

docker stop eu.operando.core.ldb.server.test.00
docker rm eu.operando.core.ldb.server.test.00
