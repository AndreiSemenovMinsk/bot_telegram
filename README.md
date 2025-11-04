# bot_telegram



docker-compose build --no-cache

docker-compose up --no-cache --build

docker save -o bot_telegram.tar bot_telegram-microservice1

docker-compose down --remove-orphans -v



При загрузке на raspberry
просто копирую все файлы в ~/telegram_bot

mvn clean install -DskipTests

docker-compose build --no-cache

docker-compose up



docker-compose exec jpacachespring-microservice1 bash

liquibase --changeLogFile=db/changelog/db.changelog-master.yaml --url='jdbc:postgresql://postgres:5432/mail_service_db' --username=mail_service --password=mail_service generate

netstat -ao |find /i "listening"  

docker status

docker stop $(docker ps -q)

mvn clean install


На Ubuntu
etc/nginx/sites-available