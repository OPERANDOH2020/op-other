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

#check ubuntu 16.04

_title "Checking system configuration"
#check ubuntu 16.04
if [ ! "$OS_TYPE" == "Ubuntu" ]; then
  _warn "This script was developed for Ubuntu 16.04, but your OS reports to be: $OS_TYPE $OS_VERSION"
elif [ ! "$OS_VERSION" == "16.04" ]; then
  _warn "This script was developed for Ubuntu 16.04, but your OS reports to be: $OS_TYPE $OS_VERSION"
else
  _info "Your system $OS_TYPE $OS_VERSION is fully supported"
fi

$SUDO apt-get update
# $SUDO apt-get -y upgrade

# add Docker repo
$SUDO apt-get install -y apt-transport-https ca-certificates
$SUDO apt-key adv --keyserver hkp://p80.pool.sks-keyservers.net:80 --recv 58118E89F3A912897C070ADBF76221572C52609D
echo "deb https://apt.dockerproject.org/repo ubuntu-xenial main" | $SUDO tee /etc/apt/sources.list.d/docker.list
$SUDO apt-get update

# for uafs storage driver (not sure if really needed but in case of)
$SUDO apt-get install -y linux-image-extra-$(uname -r) linux-image-extra-virtual

#install docker
$SUDO apt-get install -y docker-engine

# not really needed start on default
# $SUDO service docker start

$SUDO usermod -aG docker $(whoami)

_info "Please log out and back in, in order for your user to be able to use Docker"
_wait_enter
