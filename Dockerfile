FROM --platform=linux/amd64 eclipse-temurin:19-jdk
VOLUME /tmp
 COPY target/*.jar backend-1.0.jar
#COPY target/*.jar app.jar

# ENV PORT=8080
ENTRYPOINT ["java", "-jar", "backend-1.0.jar"]
#ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8080
#FROM bellsoft/liberica-openjdk-alpine-musl
#COPY target/*.jar backend-1.0.jar
#EXPOSE 8089
#ENTRYPOINT ["java", "-jar", "backend-1.0.jar"]

#FROM eclipse-temurin:17-jdk-alpine
#COPY target/*.jar backend-1.0.jar
#ENTRYPOINT ["java","-jar","backend-1.0.jar"]

