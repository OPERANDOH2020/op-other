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

## script

# remove webui
docker rm -f webui
# remove pc container
docker rm -f pc
# remove pdb container
docker rm -f pdb
# remove DAN container
docker rm -f dan
# remove LDB containers
docker rm -f ldb.search
docker rm -f ldb
# remove Mongo container
docker rm -f operando.mongo
# remove MySQL container
docker rm -f operando.mysql
# remove AAPI containers
docker rm -f aapi
docker rm -f cas
docker rm -f openldap
# remove DNS container
docker rm -f dnsmasq

_title "SUCCESS"
_wait_enter
