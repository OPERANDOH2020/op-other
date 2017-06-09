#!/bin/bash

## globals

NET_INTERFACE="docker0";
PERSISTENCE=false;
INTERACTIVE=true;
VOLUMES="/volumes/operando";
CONFIGURATION="./config/fcsr";
CULTURE="en";

## system

OS_TYPE=`cat /etc/issue | awk '{printf($1)}'`
OS_VERSION=`cat /etc/issue | awk '{printf($2)}' |  awk -F. '{printf($1"."$2)}'`

## check for sudo

SUDO=''
if (( $EUID != 0 )); then
    SUDO='sudo'
fi
