# op-other
For miscellaneous items, e.g. code which is shared across multiple containers.

## eu.operando.provisioning.scripts
Contains scripts to launch the entire OPERANDO G2C platform, using Docker.

## op-other-common-java
Contains Java code which is shared by modules across multiple other Git repositories. Dependent modules include:
* Gatekeeper
* Regulator API
* OSP API

## test-dependencies
A folder to keep track of the dependencies which are required for testing. Some Java test code for other modules is dependent on this project. Dependent modules include:
* Gatekeeper
* Regulator API
* OSP API