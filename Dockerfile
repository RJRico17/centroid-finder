# java part
FROM maven:3.9.3-eclipse-temurin-17 AS build

ENV VIDEO_DIRECTORY=/videos
ENV RESULT_DIRECTORY=/results

WORKDIR /app
COPY processor/pom.xml .
COPY processor/src ./src
# run to make jar
RUN mvn clean package -DskipTests

# node time node time node tiem
FROM node:20-alpine AS node-build
WORKDIR /server
COPY server/package*.json ./
# install node_modules i recognize that
RUN npm install
COPY server ./

# witht he pwoer of friendship bring it all together
FROM eclipse-temurin:23-jdk-alpine
WORKDIR /app

#jar
COPY --from=build /app/target/*.jar app.jar
#node
COPY --from=node-build /server /server

EXPOSE 8080 3000

#start that damn thing up
CMD sh -c "java -jar app.jar & cd /server && node server.js"