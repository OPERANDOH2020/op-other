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

sudo docker run -i -t --name eu.operando.core.ldb.server.test.00 --dns $DNS_IP -e "MYSQL_DB_HOST=mysql.integration.operando.lan.esilab.org" -e "MYSQL_DB_NAME=operando_logdb" -e "MYSQL_DB_PASSWORD=root" -e "MYSQL_DB_USER=root" registry.devops.operando.esilab.org:5000/operando/eu.operando.core.ldb.server.test.00:ALPHA
sudo docker run -i -t --name eu.operando.core.pdb.server.test.00 --dns $DNS_IP -e "PDB_ENDPOINT=http://pdb.integration.operando.lan.esilab.org:8096/operando/core/pdb" registry.devops.operando.esilab.org:5000/operando/eu.operando.core.pdb.server.test.00:ALPHA
sudo docker run -i -t --name eu.operando.core.ae.server.test.00 --dns $DNS_IP -e "MYSQL_DB_HOST=mysql.integration.operando.lan.esilab.org" -e "MYSQL_DB_NAME=operando_personaldatadb" -e "MYSQL_DB_PASSWORD=root" -e "MYSQL_DB_USER=root" registry.devops.operando.esilab.org:5000/operando/eu.operando.core.ae.server.test.00:ALPHA
sudo docker run -i -t --name eu.operando.core.pfb.server.test.00 --dns $DNS_IP -e "MYSQL_DB_HOST=mysql.integration.operando.lan.esilab.org" -e "MYSQL_DB_NAME=operando_privacyforbenefitdb" -e "MYSQL_DB_PASSWORD=root" -e "MYSQL_DB_USER=root" registry.devops.operando.esilab.org:5000/operando/eu.operando.core.pfb.server.test.00:ALPHA
sudo docker run -i -t --name eu.operando.core.bda.server.test.00 --dns $DNS_IP -e "MYSQL_DB_HOST=mysql.integration.operando.lan.esilab.org" -e "MYSQL_DB_NAME=operando_bigdataanalyticsdb" -e "MYSQL_DB_PASSWORD=root" -e "MYSQL_DB_USER=root" registry.devops.operando.esilab.org:5000/operando/eu.operando.core.bda.server.test.00:ALPHA
sudo docker run -i -t --name eu.operando.pdr.dan.server.test.00 --dns $DNS_IP -e "MYSQL_DB_HOST=mysql.integration.operando.lan.esilab.org" -e "MYSQL_DB_NAME=operando_dan" -e "MYSQL_DB_PASSWORD=root" -e "MYSQL_DB_USER=root" registry.devops.operando.esilab.org:5000/operando/eu.operando.pdr.dan.server.test.00:ALPHA
sudo docker run -i -t --name eu.operando.webui.rg.server.test.00 --dns $DNS_IP -e "MYSQL_DB_HOST=mysql.integration.operando.lan.esilab.org" -e "MYSQL_DB_NAME_TEST1=operando_data" -e "MYSQL_DB_NAME_TEST2=operando_report" -e "MYSQL_DB_PASSWORD=root" -e "MYSQL_DB_USER=root" registry.devops.operando.esilab.org:5000/operando/eu.operando.webui.rg.server.test.00:ALPHA

docker rm eu.operando.core.ldb.server.test.00 -f
docker rm eu.operando.core.pdb.server.test.00 -f
docker rm eu.operando.core.ae.server.test.00 -f
docker rm eu.operando.core.pfb.server.test.00 -f
docker rm eu.operando.core.bda.server.test.00 -f
docker rm eu.operando.pdr.dan.server.test.00 -f
docker rm eu.operando.webui.rg.server.test.00 -f
