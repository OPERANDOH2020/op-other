#!/bin/bash
#Utils for bash scripts
GREEN='\e[1m\e[42m\e[1;30m'
YELLOW='\e[93m'
RED='\e[31m'
CYAN='\e[34m'
NC='\033[0m' # No Color
CURDIR=$(pwd);

function _exitOnError {
	printf "${RED}Script exit on ERROR ${YELLOW} $1 ${NC}\n";
	printf "${RED}Press [ENTER] to exit${NC}\n";
	read -p "" empty
	exit -1;
}

function _warn {
	printf "${YELLOW}Non critical error: $1 ${NC}\n";
}

function _info {
	printf "${CYAN}$1 ${NC}\n";
}

function _title {
	printf "${GREEN}$1 ${NC}\n";
}
function _wait_enter {
  if [ INTERACTIVE ]; then
  	printf "${YELLOW}Press [ENTER] to continue${NC}\n";
  	read -p "" empty
  fi
}

function _goToCWD {
	cd ${CURDIR};
}
