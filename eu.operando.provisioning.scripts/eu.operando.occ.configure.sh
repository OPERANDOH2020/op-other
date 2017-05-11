curl -X POST --header "Content-Type: application/json" --header "Accept: */*" -d @eu.operando.occ.configure.amiuser.json "localhost:8080/operando/interfaces/aapi/aapi/user/register"

curl -X POST --header "Content-Type: application/json" -- header "Accept: */*" -d @eu.operando.occ.configure.amipolicies.json "localhost:8096/operando/core/pdb/OSP"