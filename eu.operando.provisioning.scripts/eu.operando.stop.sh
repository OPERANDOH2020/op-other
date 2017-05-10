#!/bin/bash

source eu.operando.utils.sh
if [[ $? != 0 ]]; then
	echo "Couldn't locate utils! Exiting...";
	exit -1;
fi

source eu.operando.config.sh
if [[ $? != 0 ]]; then
	echo "Couldn't locate config! Exiting...";
	exit -1;
fi

## script

# remove webui
$SUDO docker rm -f webui
# remove pc container
$SUDO docker rm -f pc
# remove pdb container
$SUDO docker rm -f pdb
# remove DAN container
$SUDO docker rm -f dan
# remove LDB containers
$SUDO docker rm -f ldb.search
$SUDO docker rm -f ldb
# remove Mongo container
$SUDO docker rm -f operando.mongo
# remove MySQL container
$SUDO docker rm -f operando.mysql
# remove AAPI containers
$SUDO docker rm -f aapi
$SUDO docker rm -f cas
$SUDO docker rm -f openldap
# remove DNS container
$SUDO docker rm -f dnsmasq

_title "SUCCESS"
_wait_enter
