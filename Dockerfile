# java part
FROM maven:3.9.2-eclipse-temurin-17 AS build

ENV VIDEO_DIRECTORY=/videos
ENV OUTPUT_DIRECTORY=/results
ENV JAR_PATH=/app/processor/target/app.jar

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
RUN npm install ffmpeg
COPY server ./

# witht he pwoer of friendship bring it all together
FROM eclipse-temurin:17-jdk AS runtime

RUN apt-get update && apt-get install -y \
    ffmpeg \
    nodejs \
    npm \
    && rm -rf /var/lib/apt/lists/*


WORKDIR /app
RUN mkdir -p /app/processor/target

#jar
COPY --from=build /app/target/centroid-finder-1.0-SNAPSHOT.jar /app/processor/target/app.jar
#node
COPY --from=node-build /server /server

EXPOSE 8080 3000

#start that damn thing up
CMD sh -c "java -jar /app/processor/target/app.jar & cd /server && node server.js"