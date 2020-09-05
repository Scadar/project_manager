FROM bellsoft/liberica-openjdk-centos:11.0.7
ADD target/project_manager-0.0.1-SNAPSHOT.jar project_manager-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "project_manager-0.0.1-SNAPSHOT.jar"]
