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

#### ✅ Version 1.2 — Completed
- Album and music domain created
- Album and music database migrations added with Liquibase
- User album lists added
- Album reviews with rating and comments added
- Liked albums and favorite artists support added
- Main artist image and album cover selection added
- Unit tests added for ArtistController
- Unit tests added for artist image and album cover selection
- Security and role guard improvements

#### 🚧 Version 1.3 — In Progress


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

You must configure **Database credentials**.

```properties
spring.application.name=Raven

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

#### ✅ Versão 1.2 — Completa
- Domínio de álbuns e músicas criado
- Migrations de álbuns e músicas adicionadas com Liquibase
- Listas de álbuns do usuário adicionadas
- Reviews de álbuns com nota e comentário adicionadas
- Suporte a álbuns curtidos e artistas favoritos adicionado
- Seleção de imagem principal do artista e capa principal do álbum adicionada
- Testes unitários adicionados para ArtistController
- Testes unitários adicionados para seleção de imagem de artista e capa de álbum
- Melhorias na segurança e nas regras de roles

#### 🚧 Versão 1.3 — Em andamento

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

Você precisa configurar **credenciais do banco**.

```properties
spring.application.name=Raven

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
