## Java 17 JDK asosiy imaj
#FROM eclipse-temurin:17-jdk
#
## Spring Boot jar faylini konteynerga nusxalash
#COPY target/shopproject-0.0.1-SNAPSHOT.jar app.jar
#
## Default active profile docker bo‘ladi
#ENV SPRING_PROFILES_ACTIVE=docker
#
## Jar faylni ishga tushirish
#ENTRYPOINT ["java","-jar","/app.jar"]
