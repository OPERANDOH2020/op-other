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

# first we start dnsmasq to fake dns
_title "UPDATE: dnsmasq"
docker pull registry.devops.operando.esilab.org:5000/operando/eu.operando.dnsmasq.server:ALPHA


# AAPI
_title "UPDATE: openldap"
docker pull registry.devops.operando.esilab.org:5000/operando/eu.operando.core.as.openldap.server:ALPHA

_title "UPDATE: cas"
docker pull registry.devops.operando.esilab.org:5000/operando/eu.operando.core.as.cas.server:ALPHA

_title "UPDATE: aapi"
docker pull registry.devops.operando.esilab.org:5000/operando/eu.operando.interfaces.aapi.server:ALPHA

# MySQL and Mongo DBs
_title "UPDATE: mysql"
docker pull registry.devops.operando.esilab.org:5000/operando/eu.operando.core.mysql.server:ALPHA

_title "UPDATE: mongodb"
docker pull registry.devops.operando.esilab.org:5000/operando/eu.operando.core.mongo.server:ALPHA

# LDB
_title "UPDATE: ldb"
docker pull registry.devops.operando.esilab.org:5000/operando/eu.operando.core.ldb.server:ALPHA

_title "UPDATE: ldb.search"
docker pull registry.devops.operando.esilab.org:5000/operando/eu.operando.core.ldb.search.server:ALPHA

# DAN
_title "UPDATE: dan using config/repositoryManagersRegistry.yml"
docker pull registry.devops.operando.esilab.org:5000/operando/eu.operando.pdr.dan.server:ALPHA

# PDB
_title "UPDATE: pdb"
docker pull registry.devops.operando.esilab.org:5000/operando/eu.operando.core.pdb.server:ALPHA

# PC
_title "UPDATE: pc"
docker pull registry.devops.operando.esilab.org:5000/operando/eu.operando.core.pc.server:ALPHA

# Web UI
_title "UPDATE: webui"
docker pull registry.devops.operando.esilab.org:5000/operando/eu.operando.webui.console.server:ALPHA

# RightManager RM
_title "UPDATE: rm"
docker pull registry.devops.operando.esilab.org:5000/operando/eu.operando.core.rm.server:ALPHA

# GateKeeper
_title "UPDATE: gatekeeper"
docker pull registry.devops.operando.esilab.org:5000/operando/eu.operando.pdr.gk.server:ALPHA

_title "SUCCESS"
_wait_enter
