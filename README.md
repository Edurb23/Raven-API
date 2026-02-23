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

## EN — How to Run the Project

### 📋 Requirements

- **Java 17** (see `pom.xml` → `<java.version>17</java.version>`)
- **Maven 3.9+**
- **Docker & Docker Compose**
- **MySQL 8.x**

Project base:
- **Spring Boot 3.5.0** (`pom.xml` parent)

---

### 🛠 Configuration (`application.properties`)

This project uses **`src/main/resources/application.properties`**.

You must configure **Spotify credentials** + **Database credentials**.

```properties
spring.application.name=Raven

# =========================
# SPOTIFY API CONFIG
# =========================
spotify.client-id=YOUR_SPOTIFY_CLIENT_ID
spotify.client-secret=YOUR_SPOTIFY_CLIENT_SECRET
spotify.token-url=
spotify.base-url=

# =========================
# DATABASE (MySQL)
# =========================
spring.datasource.url=jdbc:mysql://localhost:3306/
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# =========================
# LIQUIBASE
# =========================
spring.liquibase.enabled=true
spring.liquibase.change-log=classpath:/db/changelog/master.yaml
```

---

### 🔑 What are `spotify.client-id` and `spotify.client-secret`?

They are credentials from a **Spotify Developer App**, used to generate an access token and call Spotify endpoints.

How to get yours:

1. Go to the **Spotify Developer Dashboard**
2. Create an app
3. Copy **Client ID** and **Client Secret**
4. Paste them into your `application.properties` (locally only)

✅ Tip: use environment variables or a `application-local.properties` ignored by Git to avoid leaking secrets.

---

### 🐳 Running with Docker (Recommended)

`docker-compose.yml` is included in the repository.

Run:

```bash
docker-compose up --build
```

This will start MySQL + the application (depending on your compose setup).

---

### 💻 Running Locally (Without Docker)

1️⃣ Create the database:

```sql
CREATE DATABASE raven_db;
```

2️⃣ Run the project:

```bash
mvn clean install
mvn spring-boot:run
```

---

### 🧩 Liquibase Notes

This project uses **Liquibase** to version database changes.

When the app starts:
- Liquibase applies migrations from `classpath:/db/changelog/master.yaml`
- It may also include **seed/insert scripts** to populate initial data (ex: default genres)

So the database can be created and filled automatically when running the application.

---

### 📄 Swagger (OpenAPI)

After starting the app, open:

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

```properties
spring.application.name=Raven

# =========================
# SPOTIFY API CONFIG
# =========================
spotify.client-id=SEU_SPOTIFY_CLIENT_ID
spotify.client-secret=SEU_SPOTIFY_CLIENT_SECRET
spotify.token-url=SUA_URL_TOKEN
spotify.base-url=SUA__URL

# =========================
# DATABASE (MySQL)
# =========================
spring.datasource.url=jdbc:mysql://localhost:3306/
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

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
