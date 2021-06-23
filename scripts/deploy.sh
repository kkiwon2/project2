#!/bin/bash

REPOSITORY=/home/ec2-user/app/step2
PROJECT_NAME=project2

echo "> Build 파일 복사"

cp $REPOSITORY/zip/*.jar $REPOSITORY/

echo "> 현재 구동중인 애플리케이션 pid 확인"

# 현재 수행중인 스프링 부트 애플리케이션의 프로세스 ID를 찾습니다. -> 실행 중이면 종료하기 위해서입니다.
CURRENT_PID=$(lsof -i :8081)

echo "현재 구동중인 어플리케이션 pid: $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
    echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
    echo "> kill -15 $CURRENT_PID"
    kill -15 $CURRENT_PID
    sleep 5
fi

echo "> 새 어플리케이션 배포"

JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> JAR Name: $JAR_NAME"

echo "> $JAR_NAME 에 실행권한 추가"

# Jar 파일은 실행 권한이 없는 상태입니다. -> nohup으로 실행할 수 있게 실행 권한을 부여합니다.
chmod +x $JAR_NAME

echo "> $JAR_NAME 실행"

nohup java -jar \
    -Dspring.config.location=classpath:/application.properties,/home/ec2-user/app/application-oauth.properties \
    $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &

