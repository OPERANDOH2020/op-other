#!/bin/bash

## functions

#######################################
# Returns the command string that, when 
# executed, will return the IP address 
# of the given network interface.
# Arguments:
#   $1: network interface name
#######################################
function get_command_string_to_get_ip_address_for_network_interface () {
  echo "ifconfig $1 | grep \"inet addr\" | cut -d ':' -f 2 | cut -d ' ' -f 1"
}

#######################################
# Returns the IP address of the given 
# network interface.
# Arguments:
#   $1: network interface name
#######################################
function get_ip_address_for_network_interface () {
  echo $(/bin/bash -c "$(get_command_string_to_get_ip_address_for_network_interface $1)")
}

## script

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
LOCAL_IP=$(get_ip_address_for_network_interface "eth0")
echo "The following IP will be used as LOCAL_IP for all operando addresses: $LOCAL_IP"

# first we start dnsmasq to fake dns
docker run -d --name dnsmasq --cap-add=NET_ADMIN -e "LOCAL_IP=$LOCAL_IP" registry.devops.operando.esilab.org:5000/operando/eu.operando.dnsmasq.server:ALPHA

# get docker IP of dnsmasq
DNS_IP=$(docker exec -i -t dnsmasq /bin/sh -c "$(get_command_string_to_get_ip_address_for_network_interface eth0)")
echo "The following IP will be used as DNS_IP for all operando components: $DNS_IP"

# AAPI
docker run -d -p 389:389 -p 636:636 --name openldap --dns $DNS_IP -e LDAP_DOMAIN=nodomain -e LDAP_ORGANISATION=nodomain -e HOSTNAME=integration.operando.dmz.lab.esilab.org -e LDAP_TLS_VERIFY_CLIENT=allow registry.devops.operando.esilab.org:5000/operando/eu.operando.core.as.openldap.server:ALPHA
docker run -d -p 8101:8080 -p 8105:8443 --name cas --dns $DNS_IP registry.devops.operando.esilab.org:5000/operando/eu.operando.core.as.cas.server:ALPHA
docker run -d -p 8135:8080 --name aapi --dns $DNS_IP registry.devops.operando.esilab.org:5000/operando/eu.operando.interfaces.aapi.server:ALPHA

# MySQL and Mongo DBs
docker run -d -p 3306:3306 --name operando.mysql --dns $DNS_IP -e "MYSQL_ROOT_PASSWORD=root" registry.devops.operando.esilab.org:5000/operando/eu.operando.core.mysql.server:ALPHA
docker run -d -p 27017:27017 --name operando.mongo --dns $DNS_IP registry.devops.operando.esilab.org:5000/operando/eu.operando.core.mongo.server:ALPHA
# we have to sleep here while MySQL starts up and creates its databases. In the future I think that this should be replaced by mounting a pre-exisitng DB.
sleep 30

# LDB
docker run -d -p 8090:8080 --name ldb --dns $DNS_IP -e "MYSQL_DB_HOST=mysql.integration.operando.lan.esilab.org" -e "MYSQL_DB_NAME=operando_logdb" -e "MYSQL_DB_PASSWORD=root" -e "MYSQL_DB_USER=root" registry.devops.operando.esilab.org:5000/operando/eu.operando.core.ldb.server:ALPHA
docker run -d -p 8091:8080 --name ldb.search --dns $DNS_IP -e "MYSQL_DB_HOST=mysql.integration.operando.lan.esilab.org" -e "MYSQL_DB_NAME=operando_logdb" -e "MYSQL_DB_PASSWORD=root" -e "MYSQL_DB_USER=root" registry.devops.operando.esilab.org:5000/operando/eu.operando.core.ldb.search.server:ALPHA

# DAN
docker run -d -p 8111:8080 --name dan --dns $DNS_IP -e "MYSQL_DB_HOST=mysql.integration.operando.lan.esilab.org" -e "MYSQL_DB_NAME=operando_dan" -e "MYSQL_DB_PASSWORD=root" -e "MYSQL_DB_USER=root" registry.devops.operando.esilab.org:5000/operando/eu.operando.pdr.dan.server:ALPHA

# PDB
docker run -d -p 8096:8080 --name pdb --dns $DNS_IP -e "MONGO_HOST=mongo.integration.operando.lan.esilab.org" -e "MONGO_PORT=27017" -e "LDB_ENDPOINT=http://ldb.integration.operando.lan.esilab.org:8090/operando/core/ldb" -e "AAPI_ENDPOINT=http://aapi.integration.operando.lan.esilab.org:8135/operando/interfaces/aapi" registry.devops.operando.esilab.org:5000/operando/eu.operando.core.pdb.server:ALPHA
