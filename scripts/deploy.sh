#!/bin/bash
BUILD_JAR=$(ls /home/ubuntu/action/server/build/libs/competnion-0.0.1-SNAPSHOT.jar)
JAR_NAME=$(basename $BUILD_JAR)

echo "> 현재 시간: $(date)" >> /home/ubuntu/action/server/deploy.log

echo "> build 파일명: $JAR_NAME" >> /home/ubuntu/action/server/deploy.log

echo "> build 파일 복사" >> /home/ubuntu/action/server/deploy.log
DEPLOY_PATH=/home/ubuntu/action/server/build/libs/
cp $BUILD_JAR $DEPLOY_PATH

echo "> 현재 실행중인 애플리케이션 pid 확인" >> /home/ubuntu/action/server/deploy.log
CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z $CURRENT_PID ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다." >> /home/ubuntu/action/server/deploy.log
else
  echo "> kill -9 $CURRENT_PID" >> /home/ubuntu/action/server/deploy.log
  sudo kill -9 $CURRENT_PID
  sleep 5
fi

echo "> Redis 서버 시작" >> /home/ubuntu/action/server/deploy.log
sudo service redis-server start

DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
echo "> DEPLOY_JAR 배포"    >> /home/ubuntu/action/server/deploy.log
sudo nohup java -jar $DEPLOY_JAR --spring.profiles.active=rds > /home/ubuntu/action/server/deploy.log 2>&1 &