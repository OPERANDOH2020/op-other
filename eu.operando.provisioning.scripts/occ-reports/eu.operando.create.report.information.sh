docker build --tag occ.report_config .
docker run --rm --link operando.mysql:mysql.host occ.report_config
