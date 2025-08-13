<h1 align="center">🧠 Fórum Hub API</h1>

![Java](https://img.shields.io/badge/Java-21-red?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5-green?style=for-the-badge&logo=springboot)
![Maven](https://img.shields.io/badge/Maven-3.8-blue?style=for-the-badge&logo=apachemaven)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=for-the-badge&logo=mysql)
![Flyway](https://img.shields.io/badge/Migrations-Flyway-red?style=for-the-badge&logo=flyway)
![JWT](https://img.shields.io/badge/JWT-Security-orange?style=for-the-badge&logo=jsonwebtokens)
![Lombok](https://img.shields.io/badge/Lombok-enabled-yellow?style=for-the-badge&logo=lombok)
![License](https://img.shields.io/badge/License-Apache2.0-gree?style=for-the-badge&logo=apache)

- O ForumHub é uma API RESTful desenvolvida em Java com Spring Boot para gerenciamento de cursos, tópicos, respostas e usuários em um fórum de discussão.

[📚 Tecnologias Utilizadas](#-tecnologias-utilizadas) - [🛠️ Funcionalidades](#-funcionalidades) - [🔐 Segurança](#-segurança) - [🔎 Endpoints](#-endpoints) - [⚙️ Como rodar o projeto localmente](#-como-rodar-o-projeto-localmente) - [📄 Documentação](#-documentação) - [🧪 Testes](#-testes) - [👩‍💻 Autora](#-autora)

---

## 📚 Tecnologias Utilizadas

- Java 21
- Spring Boot
- Spring Data JPA
- Spring Security 
- Token JWT
  - Auth0
- Bean Validation
- Flyway para criação do banco de dados
- MySQL
- Testes unitários:
  - JUnit 5
  - Mockito
- Lombok
- Maven
- Swagger/OpenAPI

---

## 🛠️ Funcionalidades

- ✅ Cadastro de usuários com validação de senha forte
- ✅ Autenticação com JWT
- ✅ Controle de acesso:

  - ✅ **Tópicos**:
    - Apenas `USER` autenticado podem criar novos tópicos
    - Apenas o autor pode editar um tópico
    - Apenas o autor pode atualizar o STATUS de um tópico como SOLUCIONADO
    - Tópicos SOLUCIONADOS não podem ser editados nem receber novas respostas - *Validado*
    - Filtragem de tópicos por data de criação (mais recentes primeiro)
    - Filtragem por termo no título (case-insensitive)
    - Listagem de topicos por nome do curso ou autor
    - Somente o autor e `ADMIN` podem deletar um tópico - *Validado*
    
  - ✅ **Respostas**: 
    - Apenas `USER` autenticado pode responder um tópico
    - Listagem de respostas por tópico ou autor
    - Somente o autor e `ADMIN` podem deletar uma resposta
- ✅ Paginação e ordenação nos endpoints de listagem


---

## 🔐 Segurança

- JWT Token para autenticação e autorização
- Controle de acesso com `ROLE_USER` e `ROLE_ADMIN`
- Filtros configurados para proteger endpoints
- Validação das senhas utilizando Bean Validation com expressão regular (regex) para garantir:
  - Mínimo de 8 caracteres 
  - Pelo menos 1 letra maiúscula 
  - Pelo menos 1 número 
  - Pelo menos 1 caractere especial

---
## 🔎 Endpoints

- Topicos
  - topics-controller
    - POST /topics
    - POST /topics/{topicId}/answers
    - PUT /topics//topic/{topicId}
    - PUT /topics/{id}
    - GET /topics
    - GET /topics/{id}
    - GET /topics/{authorId}
    - GET /topics/term
    - GET /topics/search
    - DELETE /topics/{id}
  
- Usuários
  - user-controller
    - POST /users
    - POST /users/admin
    - GET /users
      -  **Payload**
    ```
    Exemplo de entrada:
    {
    "page": 0,
    "size": 10,
    "sort":"name"
    }
    ```
    ```
    Exemplo de saída
    Response body:
    {
    "content": [
    {
    "id": 1,
    "name": "Daniela",
    "email": "daniela@email.com",
    "role": "ROLE_USER"
    },
    {
    "id": 4,
    "name": "Joana",
    "email": "joana@email.com",
    "role": "ROLE_USER"
    },
    {
    "id": 3,
    "name": "Maria",
    "email": "maria@email.com",
    "role": "ROLE_ADMIN"
    }
    ],
    "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
    "empty": false,
    "unsorted": false,
    "sorted": true
    }
    "offset": 0,
    "unpaged": false,
    "paged": true
    },
    "last": true,
    "totalPages": 1,
    "totalElements": 5,
    "size": 10,
    "number": 0,
    "sort": {
    "empty": false,
    "unsorted": false,
    "sorted": true
    }
    ```
    - GET /users/{id}
    - DELETE /users/{id}
    
- Autenticação
  - authentication-controller
    - POST /login
      - **Payload**
    ```
    Exemplo de entrada: 
    {
        "email": "maria@email.com",
        "password": "D1456&eT"
     }
    ```
    ```
    Exemplo de saída 
    Response body:
    {
        "JWTtoken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJGb3J1bUh1YiBBUEkiLCJzdWIiOiJqb2FuYUBlbWFpbC5jb20iLCJpZCI6NCwiaWF0IjoxNzU0OTU4NzQ1LCJleHAiOjE3NTQ5NjU5NDV9.ylmxPQgrI7W6_d2im_rNHrauDfb_cVeLtUT4IdmaQ5g"
    }
    ```
  
- Cursos
  - course-controller
    - POST /courses
    - GET /courses
    - GET /courses/search
    - DELETE/courses/{id}
  
- Respostas
  - answer-controller
    - GET /answers
    - GET /answers/{topicId}
    - GET /answers/answers/{authorId}

---

## ⚙️ Como rodar o projeto localmente

### 1. Clone o repositório
``` bash

git clone https://github.com/Danimmota/forum-hub-challenge.git
```
- Vá na pasta em que clonou e abra o Git Bash
- 
### 2. Configure o banco de dados
- Crie um banco de dados no MYSQL Workbench com o nome `forum_hub` com o scrip: 
```
CREATE DATABASE forum_hub
```
- Após atualize as configurações no `aplication.properties`:
```
server.port=8081

spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/forum_hub
spring.datasource.username=root
spring.datasource.password=${MYSQL_PASSWORD}
api.security.token.secret=${JWT_TOKEN}
```

---

## 🧪 Testes

- Testes unitários
  - TopicsControllerTest
    - Criação de tópico Cenario 01 ✅
    - Criação de tópico Cenario 02 ✅
    - Responder há um tópico ✅
---
## 📄 Documentação

 - Acesse o link após start da aplicação: http://localhost:8081/swagger-ui/index.html#/

---
# 🧾 Licença

[Apache 2.0](https://github.com/Danimmota/forum-hub-challenge/blob/main/LICENSE.txt)

---

## 👩‍💻 Autora

Desenvolvido por Daniela Medeiro Mota em realização do Challenge: ForumHub ONE - Oracle Next Education + ALURA

📧 Email: danielamedeiromota@hotmail.com

[🔗 LinkedIn](https://www.linkedin.com/in/danielammota/)