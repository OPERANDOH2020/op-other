#!/bin/bash

sudo sh -c 'echo "DOCKER_OPTS=\"--insecure-registry registry.devops.operando.esilab.org:5000\"" >> /etc/default/docker'
sudo sed -i s/.Service./[Service]\\\nEnvironmentFile=-\\\/etc\\\/default\\\/docker/ /lib/systemd/system/docker.service
sudo sed -i s/fd:\\\/\\\//fd:\\\/\\\/\ \$DOCKER_OPTS/ /lib/systemd/system/docker.service 
sudo systemctl daemon-reload
sudo service docker restart
sudo docker info

