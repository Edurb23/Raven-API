# Usar uma imagem base do OpenJDK
FROM openjdk:17-jdk-slim

# Definir o diretório de trabalho
WORKDIR /app

# Copiar o arquivo .jar do seu projeto para o contêiner
COPY target/Raven-0.0.1-SNAPSHOT.jar app.jar

# Expor a porta em que a aplicação Spring Boot vai rodar
EXPOSE 8081

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
