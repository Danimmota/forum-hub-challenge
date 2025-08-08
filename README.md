<h1 align="center">🧠 Fórum Hub API</h1>

![Java](https://img.shields.io/badge/Java-17+-red?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-2.7-green?style=for-the-badge&logo=springboot)
![Maven](https://img.shields.io/badge/Maven-3.8-blue?style=for-the-badge&logo=apachemaven)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=for-the-badge&logo=mysql)
![JWT](https://img.shields.io/badge/JWT-Security-orange?style=for-the-badge&logo=jsonwebtokens)
![Lombok](https://img.shields.io/badge/Lombok-enabled-yellow?style=for-the-badge&logo=lombok)

- API RESTful desenvolvida em Java com Spring Boot para gerenciamento de tópicos e respostas em um fórum de discussão.

[📚 Tecnologias Utilizadas](#-tecnologias-utilizadas) - [🛠️ Funcionalidades](#-funcionalidades) - [🔐 Segurança](#-segurança) - [🔎 Endpoints](#-endpoints) - [⚙️ Como rodar o projeto localmente](#-como-rodar-o-projeto-localmente) - [🧪 Testes](#-testes) - [👩‍💻 Autora](#-autora)

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
- Testes automatizados:
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
    - Tópicos SOLUCIONADOS não podem ser editados nem receber novas respostas
    - Filtragem de tópicos por data de criação (mais recentes primeiro)
    - Filtragem por termo no título (case-insensitive)
    - Listagem de topicos por nome do curso ou autor
    - Somente o autor e `ADMIN` podem deletar um tópico
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

---
## 🔎 Endpoints


---

## ⚙️ Como rodar o projeto localmente

---

## 🧪 Testes

- Testes unitários e de integração
---

# 🧾 Licença

Apache 2.0

---

## 👩‍💻 Autora

Desenvolvido por Daniela Medeiro Mota em realização do Challenge: ForumHub ONE - Oracle Next Education

📧 Email: danielamedeiromota@hotmail.com

[🔗 LinkedIn](https://www.linkedin.com/in/danielammota/)