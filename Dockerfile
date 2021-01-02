FROM maven:3.6.3-adoptopenjdk-11

COPY . /project
RUN cd /project && mvn package -DskipTests

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-Dblabla", "-jar","/project/target/groceries-0.0.1-SNAPSHOT.jar"]
