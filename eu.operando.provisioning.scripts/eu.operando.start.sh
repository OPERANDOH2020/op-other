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

# get the local IP
LOCAL_IP=`hostname -I | awk -F' ' '{printf($1)}'`
echo "The following IP will be used as LOCAL_IP for all operando addresses: $LOCAL_IP"

# first we start dnsmasq to fake dns
sudo docker run -d --name eu.operando.dnsmasq.server --cap-add=NET_ADMIN -e "LOCAL_IP=$LOCAL_IP" registry.devops.operando.esilab.org:5000/operando/eu.operando.dnsmasq.server:ALPHA

# get docker IP of dnsmasq
DNS_IP=`sudo docker exec -i -t eu.operando.dnsmasq.server hostname -i | awk -F' ' '{printf($1)}'`
echo "The following IP will be used as DNS_IP for all operando components: $DNS_IP"

sudo docker run -d -p 3306:3306 --name eu.operando.core.mysql.server --dns $DNS_IP -e "MYSQL_ROOT_PASSWORD=root" registry.devops.operando.esilab.org:5000/operando/eu.operando.core.mysql.server:ALPHA

sudo docker run -d -p 27017:27017 --name eu.operando.core.mongo.server --dns $DNS_IP registry.devops.operando.esilab.org:5000/operando/eu.operando.core.mongo.server:ALPHA

sudo docker run -d -p 389:389 -p 636:636 --name eu.operando.core.as.openldap.server --dns $DNS_IP -e LDAP_DOMAIN=nodomain -e LDAP_ORGANISATION=nodomain -e HOSTNAME=integration.operando.dmz.lab.esilab.org -e LDAP_TLS_VERIFY_CLIENT=allow registry.devops.operando.esilab.org:5000/operando/eu.operando.core.as.openldap.server:ALPHA

sudo docker run -d -p 8101:8080 -p 8105:8443 --name eu.operando.core.as.cas.server --dns $DNS_IP registry.devops.operando.esilab.org:5000/operando/eu.operando.core.as.cas.server:ALPHA

sleep 15  

sudo docker run -d -p 8135:8080 --name eu.operando.interfaces.aapi.server --dns $DNS_IP registry.devops.operando.esilab.org:5000/operando/eu.operando.interfaces.aapi.server:ALPHA

sudo docker run -d -p 8090:8080 --name eu.operando.core.ldb.server --dns $DNS_IP -e "MYSQL_DB_HOST=mysql.integration.operando.lan.esilab.org" -e "MYSQL_DB_NAME=operando_logdb" -e "MYSQL_DB_PASSWORD=root" -e "MYSQL_DB_USER=root" registry.devops.operando.esilab.org:5000/operando/eu.operando.core.ldb.server:ALPHA

sudo docker run -d -p 8111:8080 --name eu.operando.pdr.dan.server --dns $DNS_IP -e "MYSQL_DB_HOST=mysql.integration.operando.lan.esilab.org" -e "MYSQL_DB_NAME=operando_dan" -e "MYSQL_DB_PASSWORD=root" -e "MYSQL_DB_USER=root" registry.devops.operando.esilab.org:5000/operando/eu.operando.pdr.dan.server:ALPHA

sudo docker run -d -p 8110:8080 --name eu.operando.pdr.gk.server --dns $DNS_IP registry.devops.operando.esilab.org:5000/operando/eu.operando.pdr.gk.server:ALPHA

sudo docker run -d -p 8112:8080 --name eu.operando.pdr.rpm.server --dns $DNS_IP registry.devops.operando.esilab.org:5000/operando/eu.operando.pdr.rpm.server:ALPHA

sudo docker run -d -p 8133:8080 --name eu.operando.interfaces.rapi.server --dns $DNS_IP registry.devops.operando.esilab.org:5000/operando/eu.operando.interfaces.rapi.server:ALPHA

sudo docker run -d -p 8131:8080 --name eu.operando.interfaces.oapi.server --dns $DNS_IP registry.devops.operando.esilab.org:5000/operando/eu.operando.interfaces.oapi.server:ALPHA

sudo docker run -d -p 8096:8080 --name eu.operando.core.pdb.server --dns $DNS_IP -e "MONGO_HOST=mongo.integration.operando.lan.esilab.org" -e "MONGO_PORT=27017" -e "LDB_ENDPOINT=http://ldb.integration.operando.lan.esilab.org:8090/operando/core/ldb" -e "AAPI_ENDPOINT=http://aapi.integration.operando.lan.esilab.org:8135/operando/interfaces/aapi" registry.devops.operando.esilab.org:5000/operando/eu.operando.core.pdb.server:ALPHA

sudo docker run -d -p 8092:8080 --name eu.operando.core.ae.server --dns $DNS_IP -e "MYSQL_DB_HOST=mysql.integration.operando.lan.esilab.org" -e "MYSQL_DB_NAME=operando_personaldatadb" -e "MYSQL_DB_PASSWORD=root" -e "MYSQL_DB_USER=root" registry.devops.operando.esilab.org:5000/operando/eu.operando.core.ae.server:ALPHA

sudo docker run -d -p 8093:8080 --name eu.operando.core.pfb.server --dns $DNS_IP -e "MYSQL_DB_HOST=mysql.integration.operando.lan.esilab.org" -e "MYSQL_DB_NAME=operando_privacyforbenefitdb" -e "MYSQL_DB_PASSWORD=root" -e "MYSQL_DB_USER=root" registry.devops.operando.esilab.org:5000/operando/eu.operando.core.pfb.server:ALPHA

sudo docker run -d -p 8091:8080 --name eu.operando.core.ldb.search.server --dns $DNS_IP -e "MYSQL_DB_HOST=mysql.integration.operando.lan.esilab.org" -e "MYSQL_DB_NAME=operando_logdb" -e "MYSQL_DB_PASSWORD=root" -e "MYSQL_DB_USER=root" registry.devops.operando.esilab.org:5000/operando/eu.operando.core.ldb.search.server:ALPHA

sudo docker run -d -p 8098:8080 --name eu.operando.core.bda.server --dns $DNS_IP -e "MYSQL_DB_HOST=mysql.integration.operando.lan.esilab.org" -e "MYSQL_DB_NAME=operando_bigdataanalyticsdb" -e "MYSQL_DB_PASSWORD=root" -e "MYSQL_DB_USER=root" registry.devops.operando.esilab.org:5000/operando/eu.operando.core.bda.server:ALPHA

sudo docker run -d -p 8094:8080 --name eu.operando.core.ose.server --dns $DNS_IP registry.devops.operando.esilab.org:5000/operando/eu.operando.core.ose.server:ALPHA

sudo docker run -d -p 8095:8080 --name eu.operando.core.pc.server --dns $DNS_IP registry.devops.operando.esilab.org:5000/operando/eu.operando.core.pc.server:ALPHA

sudo docker run -d -p 8106:80 --name eu.operando.core.sos.nagios.server --dns $DNS_IP registry.devops.operando.esilab.org:5000/operando/eu.operando.core.sos.nagios.server:ALPHA

sudo docker run -d -p 8120:8080 --name eu.operando.webui.rg.birt.server --dns $DNS_IP -e "MYSQL_DB_HOST=mysql.integration.operando.lan.esilab.org" -e "MYSQL_DB_NAME=operando_data" -e "MYSQL_DB_PASSWORD=root" -e "MYSQL_DB_USER=root" registry.devops.operando.esilab.org:5000/operando/eu.operando.webui.rg.birt.server:ALPHA

sudo docker run -d -p 8122:8084 --name eu.operando.webui.rg.server --dns $DNS_IP registry.devops.operando.esilab.org:5000/operando/eu.operando.webui.rg.server:ALPHA

sudo docker run -d -p 8121:8084 --name eu.operando.webui.console.server --dns $DNS_IP registry.devops.operando.esilab.org:5000/operando/eu.operando.webui.console.server:ALPHA

sudo docker run -d -p 8140:8084 --name eu.operando.demo.yellowpages.server --dns $DNS_IP registry.devops.operando.esilab.org:5000/operando/eu.operando.demo.yellowpages.server:ALPHA

sudo docker run -d -p 8097:8080 --name eu.operando.core.pc.pq.server --dns $DNS_IP registry.devops.operando.esilab.org:5000/operando/eu.operando.core.pc.pq.server:ALPHA

sudo docker run -d -p 8102:8102 --name eu.operando.core.rm.server --dns $DNS_IP -e "LDB_ENDPOINT=http://ldb.integration.operando.lan.esilab.org:8090/operando/core/ldb" -e "AAPI_ENDPOINT=http://aapi.integration.operando.lan.esilab.org:8135/operando/interfaces/aapi" -e "DAN_ENDPOINT=http://dan.integration.operando.lan.esilab.org:8111/operando/pdr/dan" -e "PC_ENDPOINT=http://pc.integration.operando.lan.esilab.org:8095/operando/core/pc" -e "RM_URLPATH=operando/core/rm" registry.devops.operando.esilab.org:5000/operando/eu.operando.core.rm.server:ALPHA

