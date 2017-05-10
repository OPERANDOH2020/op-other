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

_title "REMOVING $VOLUMES"
$SUDO rm -Rf $VOLUMES

_title "SUCCESS"
_wait_enter
