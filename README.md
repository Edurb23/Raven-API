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

Configure your `application.properties`:

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

- **Java 17** (ver `pom.xml` → `<java.version>17</java.version>`)
- **Maven 3.9+**
- **Docker & Docker Compose**
- **MySQL 8.x**

Base do projeto:
- **Spring Boot 3.5.0** (`pom.xml` parent)

---

### 🛠 Configuração (`application.properties`)

O projeto usa **`src/main/resources/application.properties`**.

Você precisa configurar **credenciais do Spotify** + **credenciais do banco**.

Exemplo (NÃO coloque secrets reais no GitHub):

```properties
spring.application.name=Raven

# =========================
# SPOTIFY API CONFIG
# =========================
spotify.client-id=SEU_SPOTIFY_CLIENT_ID
spotify.client-secret=SEU_SPOTIFY_CLIENT_SECRET
spotify.token-url=https://accounts.spotify.com/api/token
spotify.base-url=https://api.spotify.com/v1

# =========================
# LOGGING
# =========================
logging.level.org.springframework.web=DEBUG
logging.level.com.raven.project.Raven.services=DEBUG

# =========================
# DATABASE (MySQL)
# =========================
spring.datasource.url=jdbc:mysql://localhost:3306/raven_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# =========================
# JPA / HIBERNATE
# =========================
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# =========================
# LIQUIBASE
# =========================
spring.liquibase.enabled=true
spring.liquibase.change-log=classpath:/db/changelog/master.yaml
```

---

### 🔑 O que é `spotify.client-id` e `spotify.client-secret`?

São as credenciais do seu **App no Spotify Developer**, usadas para gerar o token de acesso e consumir os endpoints da API.

Como pegar as suas:

1. Acesse o **Spotify Developer Dashboard**
2. Crie um app
3. Copie **Client ID** e **Client Secret**
4. Cole no seu `application.properties` (somente local)

✅ Dica: use variáveis de ambiente ou um `application-local.properties` (ignorado pelo Git) para não vazar credenciais.

---

### 🐳 Rodando com Docker (Recomendado)

O `docker-compose.yml` já está no repositório.

Execute:

```bash
docker-compose up --build
```

---

### 💻 Rodando Localmente (Sem Docker)

1️⃣ Criar banco:

```sql
CREATE DATABASE raven_db;
```

2️⃣ Rodar aplicação:

```bash
mvn clean install
mvn spring-boot:run
```

---

### 🧩 Observação sobre Liquibase

O projeto usa **Liquibase** para versionar o banco.

Quando a aplicação inicia:
- Liquibase aplica migrations em `classpath:/db/changelog/master.yaml`
- Pode conter também **scripts de insert/seed** (ex: gêneros padrão)

Ou seja, o banco pode ser criado e populado automaticamente ao subir a aplicação.

---

### 📄 Swagger (OpenAPI)

Após iniciar a aplicação:

```
http://localhost:8080/swagger-ui/index.html






---

## ✨ Author / Autor
**Eduardo Braga**
