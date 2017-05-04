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
docker run -d --name dnsmasq --cap-add=NET_ADMIN -e "LOCAL_IP=$LOCAL_IP" registry.devops.operando.esilab.org:5000/operando/eu.operando.dnsmasq.server:ALPHA

# get docker IP of dnsmasq
DNS_IP=`docker exec -i -t dnsmasq hostname -i | awk -F' ' '{printf($1)}'`
echo "The following IP will be used as DNS_IP for all operando components: $DNS_IP"

# aapi modules
docker run -d -p 389:389 -p 636:636 --name openldap --dns $DNS_IP -e LDAP_DOMAIN=nodomain -e LDAP_ORGANISATION=nodomain -e HOSTNAME=integration.operando.dmz.lab.esilab.org -e LDAP_TLS_VERIFY_CLIENT=allow registry.devops.operando.esilab.org:5000/operando/eu.operando.core.as.openldap.server:ALPHA
docker run -d -p 8101:8080 -p 8105:8443 --name cas --dns $DNS_IP registry.devops.operando.esilab.org:5000/operando/eu.operando.core.as.cas.server:ALPHA
docker run -d -p 8135:8080 --name aapi --dns $DNS_IP registry.devops.operando.esilab.org:5000/operando/eu.operando.interfaces.aapi.server:ALPHA

# ldb modules
docker run -d -p 3306:3306 --name operando.mysql --dns $DNS_IP -e "MYSQL_ROOT_PASSWORD=root" registry.devops.operando.esilab.org:5000/operando/eu.operando.core.mysql.server:ALPHA
# we have to sleep here while MySQL starts up and creates its databases. In the future I think that this should be replaced by mounting a pre-exisitng DB.
sleep 30
docker run -d -p 8090:8080 --name ldb --dns $DNS_IP -e "MYSQL_DB_HOST=mysql.integration.operando.lan.esilab.org" -e "MYSQL_DB_NAME=operando_logdb" -e "MYSQL_DB_PASSWORD=root" -e "MYSQL_DB_USER=root" registry.devops.operando.esilab.org:5000/operando/eu.operando.core.ldb.server:ALPHA
docker run -d -p 8091:8080 --name ldb.search --dns $DNS_IP -e "MYSQL_DB_HOST=mysql.integration.operando.lan.esilab.org" -e "MYSQL_DB_NAME=operando_logdb" -e "MYSQL_DB_PASSWORD=root" -e "MYSQL_DB_USER=root" registry.devops.operando.esilab.org:5000/operando/eu.operando.core.ldb.search.server:ALPHA
