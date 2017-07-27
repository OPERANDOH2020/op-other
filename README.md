# op-other
For miscellaneous items, e.g. code which is shared across multiple containers.

You can find a description of OPERANDO's architecture on the [project website](https://www.operando.eu). You can find detailed specifications and descriptions of each of the modules in this repository in D5.5 (to be relased in October 2017) of [OPERANDO's public deliverables](https://www.operando.eu/servizi/moduli/moduli_fase01.aspx?mp=1&fn=6&Campo_78=&Campo_126=68&AggiornaDB=search&moduli1379178994=&__VIEWSTATEGENERATOR=D6660DC7&__EVENTVALIDATION=/wEWCAKInYjvBwK46/eoCgLW6PifAQLM6NSfAQLP6LicAQLM6NifAQLPm7uVCQKtvouLDQGIwuPU0XcXVk7W8FmpEwz15iKL).

## Reporting an issue
To report an issue, please use GitHub's built-in issue-tracking system for this repository, or send an email to bug-report@operando.eu.

## Contributing code
If you'd like to contribute code, we'd be happy to have your support, so please contact us at os-contributions@operando.eu! You can find some examples of how you might help us on the [contributions page](https://www.operando.eu) of our website.

N.B. The copyright for any code will be owned by the OPERANDOH2020 organisation, and can therefore be used by any of the partner organisations that make up the consortium in other applications.

## Overview
### eu.operando.provisioning.scripts
Contains scripts to launch the entire OPERANDO G2C platform, using Docker.

### op-other-common-java
Contains Java code which is shared by modules across multiple other Git repositories. Dependent modules include:
* Gatekeeper
* Regulator API
* OSP API

### test-dependencies
A folder to keep track of the dependencies which are required for testing. Some Java test code for other modules is dependent on this project. Dependent modules include:
* Gatekeeper
* Regulator API
* OSP API

## Note
The common code in op-other-common-java and test-dependencies does not have a function by itself, but forms part of the functions of the dependent modules. See the relevant repositories for detailed descriptions of functionality and installation/usage instructions.
