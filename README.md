# op-other
For miscellaneous items, e.g. code which is shared across multiple containers.

## op-other-common-java
Contains java code which is shared by modules across multiple other Git repositories. Dependent modules include:
* Gatekeeper
* Regulator API
* OSP API

## test-dependencies
A repository to keep track of the dependencies which are required for testing. Some Java test code for other modules is dependent on this project. Dependent modules include:
* Gatekeeper
* Regulator API
* OSP API

## docker-compose
Contains a docker-compose script. With Docker installed, this script will download Docker images of the necessary OPERANDO modules, install them, configure them to communicate with each other, and run the platform.
