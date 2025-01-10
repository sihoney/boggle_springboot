# 1. 기본 이미지 설정
FROM openjdk:17-jdk-slim

# 2. 애플리케이션 JAR 파일 복사
ARG WAR_FILE=target/boggle-0.0.1-SNAPSHOT.war
COPY ${WAR_FILE} /app.war

# 3. 애플리케이션 실행 명령본 실행 명령인 java -jar app.jar 
CMD ["java", "-jar", "/app.war"]