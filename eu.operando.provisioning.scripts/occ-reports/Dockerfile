FROM ubuntu
ADD reportExecutions.sql reportExecutions.sql

RUN apt-get update && apt-get install mysql-client -y && rm -rf /var/lib/apt/lists/*

CMD mysql -u root -proot -h mysql.host -D operando_report < reportExecutions.sql
