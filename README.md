# 🎵 Raven — README (EN / BR)

---

##  EN — English Version

The **Raven** project was created for music lovers who want a modern platform to explore artists, genres, and musical information.  
It combines internal data with real information consumed from the **Spotify Public API**, all running on a well‑structured environment using **Spring Boot, Docker, and Liquibase**.

The project also includes Swagger (OpenAPI) documentation, providing a clear and interactive interface to explore and test all available REST endpoints.

### 🚀 Technologies Used
- Java + Spring Boot  
- Docker  
- Liquibase  
- MySQL  
- Spotify API
- Swagger (OpenAPI)  

### 🎯 Project Purpose
- Organize and manage artists and musical genres  
- Synchronize real data using the Spotify API  
- Maintain a structured, version‑controlled database
- Provide clear API documentation using Swagger  
- Serve as the foundation for future features such as albums, playlists, and a complete music platform  

### 📌 Versions

#### ✅ Version 1.0 — Completed
- REST APIs created  
- Database diagram completed  
- Database structured with Liquibase
- Swagger API documentation available 

#### 🚧 Version 1.2 — In Progress


# 🔧 Build & Run

### 📋 Requirements

Make sure you have installed:

- **Java 17 (LTS recommended)**
- **Maven 3.9+**
- **Docker & Docker Compose**
- **MySQL 8.x**

---

### 🛠 Environment Configuration

Configure your `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/raven_db
    username: root
    password: root
  jpa:
    hibernate:
      ddl-auto: validate
```

Make sure database credentials match your Docker configuration if using containers.

---

### 🐳 Running with Docker (Recommended)

```bash
docker-compose up --build
```

This will:

- Start the MySQL container
- Build the Spring Boot application
- Run Liquibase migrations automatically

---

### 💻 Running Locally (Without Docker)

1️⃣ Create the database manually:

```sql
CREATE DATABASE raven_db;
```

2️⃣ Build and run the project:

```bash
mvn clean install
mvn spring-boot:run
```

---

### 📄 Swagger Documentation

After starting the application, access:

```
http://localhost:8080/swagger-ui/index.html
```

---


---

##  BR — Versão em Português

O **Raven** é um projeto criado para amantes de música que desejam uma plataforma moderna para explorar artistas, gêneros e informações musicais.  
O sistema combina dados internos com informações reais da **API Pública do Spotify**, rodando em um ambiente bem estruturado com **Spring Boot, Docker e Liquibase**.

O projeto também conta com documentação da API utilizando Swagger (OpenAPI), permitindo visualizar e testar todos os endpoints de forma clara e interativa.

### 🚀 Tecnologias Utilizadas
- Java + Spring Boot  
- Docker  
- Liquibase  
- MySQL  
- API do Spotify
- Swagger (OpenAPI) 


### 🎯 Objetivo do Projeto
- Organizar e gerenciar artistas e gêneros musicais  
- Sincronizar dados reais usando a API do Spotify  
- Manter um banco estruturado e versionado
- Disponibilizar documentação clara da API com Swagger
- Servir como base para futuras funcionalidades como álbuns, playlists e um sistema musical completo

### 📌 Versões

#### ✅ Versão 1.0 — Completa
- APIs REST criadas  
- Diagrama do banco finalizado  
- Banco estruturado com Liquibase
- Documentação da API com Swagger 

#### 🚧 Versão 1.2 — Em andamento

# Como Executar o Projeto

### 📋 Requisitos

Certifique-se de ter instalado:

- **Java 17 (LTS recomendado)**
- **Maven 3.9+**
- **Docker & Docker Compose**
- **MySQL 8.x**

---

### 🛠 Configuração

Configure o `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/raven_db
    username: root
    password: root
  jpa:
    hibernate:
      ddl-auto: validate
```

---

### 🐳 Executando com Docker (Recomendado)

```bash
docker-compose up --build
```

O Docker irá:

- Subir o container do MySQL
- Construir a aplicação Spring Boot
- Executar as migrations do Liquibase automaticamente

---

### 💻 Executando Localmente

1️⃣ Criar banco manualmente:

```sql
CREATE DATABASE raven_db;
```

2️⃣ Rodar aplicação:

```bash
mvn clean install
mvn spring-boot:run
```

---

### 📄 Acessando o Swagger

Após iniciar a aplicação:

```
http://localhost:8080/swagger-ui/index.html
```






---

## ✨ Author / Autor
**Eduardo Braga**
