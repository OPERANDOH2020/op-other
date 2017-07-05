#!/bin/bash
echo | ./eu.operando.stop.sh
./eu.operando.start.sh
cd occ-amiaccountsetup
python amiAccountSetup.py amiPeople.csv
cd ../occ-reports
./eu.operando.create.report.information.sh
cd ..