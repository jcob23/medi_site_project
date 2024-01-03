FROM openjdk:21

WORKDIR /app
COPY build/libs/medi-site.jar .

ENTRYPOINT ["java", "-jar", "medi-site.jar"]