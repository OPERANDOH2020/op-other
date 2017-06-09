#!/bin/bash

source eu.operando.config.sh
if [[ $? != 0 ]]; then
	echo "Couldn't locate config! Exiting...";
	exit -1;
fi

source eu.operando.utils.sh
if [[ $? != 0 ]]; then
	echo "Couldn't locate utils! Exiting...";
	exit -1;
fi

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

#######################################
# Waits for a docker service to be online
# Arguments:
#    $1: docker-name
#    $2: port to check
#    $3: path to check
#    $4: expected status code in answer
#######################################
function wait_service_online() {
  EXPECTED=$4
  if [ -z $4 ]; then
    EXPECTED=200
  fi
  _info "Waiting for $1 to be online (expected code is $EXPECTED)..."
  ONLINE=false
  while [ $ONLINE == false ]; do
    HTTP_CODE=$(docker run --rm --link $1:$1 byrnedo/alpine-curl -sS --output /dev/null --silent --write-out "%{http_code}" "http://$1:$2/$3")
    RET=$?
    if [ "$RET" -eq "0" ]; then
      _info "GET http://$1:$2/$3\treturned $HTTP_CODE"
      if [ "$HTTP_CODE" -eq "$EXPECTED" ]; then
        _info "Connection to $1 ok";
      else
        _warn "Could enstablish connection with $1, but http code returned was $HTTP_CODE"
        _wait_enter
      fi
      ONLINE=true
    else
      #_warn "Connection to $1 failed";
      sleep 10;
    fi
  done
}

#######################################
# Waits for mysql to be online
# Arguments:
#    $1: docker-mysql-name
#    $2: username
#    $3: password
#    $4: db
#######################################
function wait_mysql_online() {
  _info "Waiting for $1 to be online..."
  MYSQL_ONLINE=false
  while [ $MYSQL_ONLINE == false ]; do
    docker run --rm --link $1:mysql.host mysql sh -c "exec mysql -h mysql.host -u $2 -p$3 $4 -e 'show tables;';"
    RET=$?
    if [ "$RET" -eq "0" ]; then
      _info "Connection to $1 ok";
      MYSQL_ONLINE=true
    else
      _warn "Connection to $1 failed";
      sleep 10;
    fi
  done
}

## script

_title "Checking system configuration"
#check ubuntu 16.04
if [ ! "$OS_TYPE" == "Ubuntu" ]; then
  _warn "This script was developed for Ubuntu 16.04, but your OS reports to be: $OS_TYPE $OS_VERSION"
elif [ ! "$OS_VERSION" == "16.04" ]; then
  _warn "This script was developed for Ubuntu 16.04, but your OS reports to be: $OS_TYPE $OS_VERSION"
else
  _info "Your system $OS_TYPE $OS_VERSION is fully supported"
fi

# get the local IP
LOCAL_IP=$(get_ip_address_for_network_interface "${NET_INTERFACE}")
_info "The following IP will be used as LOCAL_IP for all operando addresses: $LOCAL_IP"

if $PERSISTENCE; then
  _info "Preparing volume directory: $VOLUMES"
  mkdir -p $VOLUMES
fi

# first we start dnsmasq to fake dns
_title "DEPLOY: dnsmasq"
docker run -d --name dnsmasq --cap-add=NET_ADMIN -e "LOCAL_IP=$LOCAL_IP" registry.devops.operando.esilab.org:5000/operando/eu.operando.dnsmasq.server:ALPHA

# get docker IP of dnsmasq
DNS_IP=$(docker exec -i -t dnsmasq /bin/sh -c "$(get_command_string_to_get_ip_address_for_network_interface eth0)")
_info "The following IP will be used as DNS_IP for all operando components: $DNS_IP"

# AAPI
if $PERSISTENCE; then
  _title "DEPLOY: openldap WITH persistance"
  docker run -d -p 389:389 -p 636:636 --name openldap --dns $DNS_IP -v $VOLUMES/ldap/data:/var/lib/ldap -v $VOLUMES/ldap/config:/etc/ldap/slapd.d -e LDAP_DOMAIN=nodomain -e LDAP_ORGANISATION=nodomain -e HOSTNAME=integration.operando.dmz.lab.esilab.org -e LDAP_TLS_VERIFY_CLIENT=allow registry.devops.operando.esilab.org:5000/operando/eu.operando.core.as.openldap.server:ALPHA
else
  _title "DEPLOY: openldap NO persistance"
  docker run -d -p 389:389 -p 636:636 --name openldap --dns $DNS_IP -e LDAP_DOMAIN=nodomain -e LDAP_ORGANISATION=nodomain -e HOSTNAME=integration.operando.dmz.lab.esilab.org -e LDAP_TLS_VERIFY_CLIENT=allow registry.devops.operando.esilab.org:5000/operando/eu.operando.core.as.openldap.server:ALPHA
fi

_title "DEPLOY: cas"
docker run -d -p 8101:8080 -p 8105:8443 --name cas --dns $DNS_IP registry.devops.operando.esilab.org:5000/operando/eu.operando.core.as.cas.server:ALPHA
wait_service_online cas 8080 / 400

_title "DEPLOY: aapi"
docker run -d -p 8135:8080 --name aapi --dns $DNS_IP registry.devops.operando.esilab.org:5000/operando/eu.operando.interfaces.aapi.server:ALPHA
wait_service_online aapi 8080 operando/interfaces/aapi/aapi/user/getOspList 201

# MySQL and Mongo DBs
if $PERSISTENCE; then
  _title "DEPLOY: mysql WITH persistance"
  docker run -d -p 3306:3306 --name operando.mysql --dns $DNS_IP -v $VOLUMES/mysql:/var/lib/mysql  -e "MYSQL_ROOT_PASSWORD=root" registry.devops.operando.esilab.org:5000/operando/eu.operando.core.mysql.server:ALPHA
  wait_mysql_online operando.mysql root root operando_logdb

  _title "DEPLOY: mongodb WITH persistance"
  docker run -d -p 27017:27017 --name operando.mongo --dns $DNS_IP -v $VOLUMES/mongodb:/data/db registry.devops.operando.esilab.org:5000/operando/eu.operando.core.mongo.server:ALPHA

else
  _title "DEPLOY: mysql NO persistance"
  docker run -d -p 3306:3306 --name operando.mysql --dns $DNS_IP -e "MYSQL_ROOT_PASSWORD=root" registry.devops.operando.esilab.org:5000/operando/eu.operando.core.mysql.server:ALPHA
  wait_mysql_online operando.mysql root root operando_logdb

  _title "DEPLOY: mongodb NO persistance"
  docker run -d -p 27017:27017 --name operando.mongo --dns $DNS_IP registry.devops.operando.esilab.org:5000/operando/eu.operando.core.mongo.server:ALPHA
fi
# phpmyadmin
_title "DEPLOY: phpmyadmin"
docker run -d -t --name phpmyadmin -p 8104:80 --dns $DNS_IP -e "MYSQL_ROOT_PASSWORD=root" -e "PMA_HOST=mysql.integration.operando.lan.esilab.org" -e "PMA_USER=root" -e "PMA_PASSWORD=root" registry.devops.operando.esilab.org:5000/operando/eu.operando.phpmyadmin.server:ALPHA

# LDB
_title "DEPLOY: ldb"
docker run -d -p 8090:8080 --name ldb --dns $DNS_IP -e "MYSQL_DB_HOST=mysql.integration.operando.lan.esilab.org" -e "MYSQL_DB_NAME=operando_logdb" -e "MYSQL_DB_PASSWORD=root" -e "MYSQL_DB_USER=root" registry.devops.operando.esilab.org:5000/operando/eu.operando.core.ldb.server:ALPHA
wait_service_online ldb 8080 / 200

_title "DEPLOY: ldb.search"
docker run -d -p 8091:8080 --name ldb.search --dns $DNS_IP -e "MYSQL_DB_HOST=mysql.integration.operando.lan.esilab.org" -e "MYSQL_DB_NAME=operando_logdb" -e "MYSQL_DB_PASSWORD=root" -e "MYSQL_DB_USER=root" registry.devops.operando.esilab.org:5000/operando/eu.operando.core.ldb.search.server:ALPHA
wait_service_online ldb.search 8080 / 200 "operando/core/ldbsearch/log/search?logType=notification&affectedUserId=stringoperando/core/ldbsearch/log/search?logType=notification&affectedUserId=string"

# DAN
_title "DEPLOY: dan using config/repositoryManagersRegistry.yml"
docker run -d -p 8111:8080 --name dan --dns $DNS_IP -e "MYSQL_DB_HOST=mysql.integration.operando.lan.esilab.org" -e "MYSQL_DB_NAME=operando_dan" -e "MYSQL_DB_PASSWORD=root" -e "MYSQL_DB_USER=root" registry.devops.operando.esilab.org:5000/operando/eu.operando.pdr.dan.server:ALPHA

wait_service_online dan 8080 /operando/pdr/dan/ 400
DANCONFIG=$(cat $CONFIGURATION/repositoryManagersRegistry.yml)
docker exec -t dan /bin/sh -c "echo \"$DANCONFIG\" > /usr/local/tomcat/webapps/operando#pdr#dan/WEB-INF/classes/repositoryManagersRegistry.yml"
docker stop dan
docker start dan
wait_service_online dan 8080 /operando/pdr/dan/ 400

# PDB
_title "DEPLOY: pdb"
docker run -d -p 8096:8080 --name pdb --dns $DNS_IP -e "MONGO_HOST=mongo.integration.operando.lan.esilab.org" -e "MONGO_PORT=27017" -e "LDB_ENDPOINT=http://ldb.integration.operando.lan.esilab.org:8090/operando/core/ldb" -e "AAPI_ENDPOINT=http://aapi.integration.operando.lan.esilab.org:8135/operando/interfaces/aapi" registry.devops.operando.esilab.org:5000/operando/eu.operando.core.pdb.server:ALPHA
wait_service_online pdb 8080 / 200

# RAPI
_title "DEPLOY: rapi"
docker run -d -p 8133:8080 --name rapi --dns $DNS_IP registry.devops.operando.esilab.org:5000/operando/eu.operando.interfaces.rapi.server:ALPHA
wait_service_online rapi 8080 operando/interfaces/rapi/regulator/osps/anything/compliance-report 401

# PC
_title "DEPLOY: pc"
docker run -d -p 8095:8080 --name pc --dns $DNS_IP registry.devops.operando.esilab.org:5000/operando/eu.operando.core.pc.server:ALPHA

# Web UI
_title "DEPLOY: webui"
docker run -d -p 8121:8084 --name webui --dns $DNS_IP registry.devops.operando.esilab.org:5000/operando/eu.operando.webui.console.server:ALPHA

docker exec -t webui /bin/sh -c "sed -i -e 's/uiCulture=\"it\"/uiCulture=\"$CULTURE\"/g' _PublishedWebsites/Operando-AdministrationConsole/Web.config"
docker stop webui
docker start webui

# RightManager RM
_title "DEPLOY: rm"
docker run -d -p 8102:8102 --name rm --dns $DNS_IP -e "LDB_ENDPOINT=http://ldb.integration.operando.lan.esilab.org:8090/operando/core/ldb" -e "AAPI_ENDPOINT=http://aapi.integration.operando.lan.esilab.org:8135/operando/interfaces/aapi" -e "DAN_ENDPOINT=http://dan.integration.operando.lan.esilab.org:8111/operando/pdr/dan" -e "PC_ENDPOINT=http://pc.integration.operando.lan.esilab.org:8095/operando/core/pc" -e "RM_URLPATH=operando/core/rm" registry.devops.operando.esilab.org:5000/operando/eu.operando.core.rm.server:ALPHA
wait_service_online rm 8102 / 301

# GateKeeper
_title "DEPLOY: gatekeeper"
docker run -d -p 8110:8080 --name gk --dns $DNS_IP registry.devops.operando.esilab.org:5000/operando/eu.operando.pdr.gk.server:ALPHA
wait_service_online gk 8080 / 200

# RG
_title "DEPLOY: rg"
docker run -d -p 8120:8080 --name rg.birt --dns $DNS_IP -e "MYSQL_DB_HOST=mysql.integration.operando.lan.esilab.org" -e "MYSQL_DB_NAME=operando_data" -e "MYSQL_DB_PASSWORD=root" -e "MYSQL_DB_USER=root" registry.devops.operando.esilab.org:5000/operando/eu.operando.webui.rg.birt.server:ALPHA
docker run -d -p 8122:8084 --name rg --dns $DNS_IP registry.devops.operando.esilab.org:5000/operando/eu.operando.webui.rg.server:ALPHA
wait_service_online rg 8084 /Report/Report 401

# CONFIGURATION
_title "===> DEPLOY COMPLETED, CONFIGURE OPERANDO"
_title "Register OSP USER"
HTTP_CODE=$(curl -X POST -sS --output /dev/null --silent --write-out "%{http_code}" --header "Content-Type: application/json" --header "Accept: */*" -d @$CONFIGURATION/osp-user.json "http://$LOCAL_IP:8135/operando/interfaces/aapi/aapi/user/register")
RET=$?
if [ "$RET" -eq "0" ]; then
  if [ "$HTTP_CODE" -eq "201" ]; then
    _info "OSP USER has been successfully registered";
  else
    _warn "Couldn't create OSP USER, http code returned was $HTTP_CODE"
    _wait_enter
  fi
else
  _warn "Couldn't create OSP USER, connection failed"
  _wait_enter
fi

_title "Register OSP POLICIES"
HTTP_CODE=$(curl -X POST -sS --output /dev/null --silent --write-out "%{http_code}" --header "Content-Type: application/json" --header "Accept: */*" -d @$CONFIGURATION/policies.json "http://$LOCAL_IP:8096/operando/core/pdb/OSP")
RET=$?
if [ "$RET" -eq "0" ]; then
  if [ "$HTTP_CODE" -eq "201" ]; then
    _info "OSP POLICIES has been successfully registered";
  else
    _warn "Couldn't create OSP POLICIES, http code returned was $HTTP_CODE"
    _wait_enter
  fi
else
  _warn "Couldn't create OSP POLICIES, connection failed"
  _wait_enter
fi

_title "SUCCESS"
_wait_enter
