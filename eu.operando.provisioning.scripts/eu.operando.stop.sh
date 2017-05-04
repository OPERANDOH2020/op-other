# stop LDB containers
docker stop ldb.search
docker stop ldb
docker stop operando.mysql
# stop AAPI containers
docker stop aapi
docker stop cas
docker stop openldap
# stop DNS container
docker stop dnsmasq

# remove LDB containers
docker rm ldb.search
docker rm ldb
docker rm operando.mysql
# remove AAPI containers
docker rm aapi
docker rm cas
docker rm openldap
# remove DNS container
docker rm dnsmasq
