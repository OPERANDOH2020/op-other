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
DNS_IP=`sudo docker exec -i -t eu.operando.dnsmasq.server hostname -i | awk -F' ' '{printf($1)}'`
echo "The following IP will be used as DNS_IP for all operando components: $DNS_IP"

sudo docker run -i -t --name eu.operando.core.ldb.server.test.01 --dns $DNS_IP registry.devops.operando.esilab.org:5000/operando/eu.operando.core.ldb.server.test.01:ALPHA
sudo docker run -i -t --name eu.operando.core.rm.server.test.01 --dns $DNS_IP -e "LDB_ENDPOINT=http://ldb.integration.operando.lan.esilab.org:8090/operando/core/ldb" -e "AAPI_ENDPOINT=http://aapi.integration.operando.lan.esilab.org:8135/operando/interfaces/aapi" -e "DAN_ENDPOINT=http://dan.integration.operando.lan.esilab.org:8111/operando/pdr/dan" -e "PC_ENDPOINT=http://pc.integration.operando.lan.esilab.org:8095/operando/core/pc" -e "RM_URLPATH=operando/core/rm" registry.devops.operando.esilab.org:5000/operando/eu.operando.core.rm.server.test.01:ALPHA
sudo docker run -i -t --name eu.operando.core.ae.server.test.01 --dns $DNS_IP registry.devops.operando.esilab.org:5000/operando/eu.operando.core.ae.server.test.00:ALPHA
sudo docker run -i -t --name eu.operando.core.pdb.server.test.01 --dns $DNS_IP registry.devops.operando.esilab.org:5000/operando/eu.operando.core.pfb.server.test.00:ALPHA
sudo docker run -i -t --name eu.operando.core.yellowpages.server.test.01 --dns $DNS_IP -e "MYSQL_DB_HOST=mysql.integration.operando.lan.esilab.org" -e "MYSQL_DB_NAME=operando_dan" -e "MYSQL_DB_PASSWORD=root" -e "MYSQL_DB_USER=root" registry.devops.operando.esilab.org:5000/operando/eu.operando.core.bda.server.test.00:ALPHA

docker rm eu.operando.core.ldb.server.test.01 -f
docker rm eu.operando.core.rm.server.test.01 -f
docker rm eu.operando.core.ae.server.test.01 -f
docker rm eu.operando.core.pdb.server.test.01 -f
docker rm eu.operando.core.yellowpages.server.test.01 -f


