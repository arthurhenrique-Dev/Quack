# 🦆 Quack — Game Review Social Network

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?style=for-the-badge&logo=springboot)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6.x-6DB33F?style=for-the-badge&logo=springsecurity)
![JWT](https://img.shields.io/badge/JWT-Tokens-black?style=for-the-badge&logo=jsonwebtokens)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-SQL-336791?style=for-the-badge&logo=postgresql)
![MongoDB](https://img.shields.io/badge/MongoDB-NoSQL-47A248?style=for-the-badge&logo=mongodb)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker)
![Swagger](https://img.shields.io/badge/Swagger-OpenAPI%203-85EA2D?style=for-the-badge&logo=swagger)
![2FA](https://img.shields.io/badge/2FA-Google%20Authenticator-4285F4?style=for-the-badge&logo=google)
![Mailtrap](https://img.shields.io/badge/Mailtrap-Email%20Service-00B2FF?style=for-the-badge&logo=mail.ru)

**Quack** é uma API RESTful de rede social para reviews de jogos — uma inspiração óbvia no Letterboxd, mas para games. Os usuários podem criar perfis, seguir uns aos outros, escrever e avaliar jogos, enquanto moderadores gerenciam o conteúdo da plataforma. O projeto foi construído com foco em segurança, qualidade de código e princípios sólidos de arquitetura de software.

---

## 🏗️ Arquitetura e Design

O projeto segue **Clean Architecture** combinada com **Arquitetura Hexagonal (Ports & Adapters)** e princípios de **DDD (Domain-Driven Design)**, garantindo que a lógica de negócio seja completamente isolada de frameworks e infraestrutura.

```
com.quack.quack_app/
├── Application/          # Casos de uso e Ports (interfaces)
│   ├── DTOs/
│   ├── Mappers/
│   ├── Ports/
│   └── UseCases/
├── Domain/               # Entidades, Value Objects, regras de negócio puras
│   ├── Exceptions/
│   ├── Games/
│   ├── Reviews/
│   ├── Users/            # BaseUser, Role, Status
│   └── ValueObjects/     # Email, Password, TokenUpdater...
└── Infra/                # Adapters de entrada e saída
    ├── Adapters/         # SQL (PostgreSQL), NoSQL (MongoDB), EmailGateway, TwoFAGateway
    ├── Configuration/
    ├── Documentation/    # Swagger Config
    └── Security/         # Spring Security, JWT, Filtros
```

**Princípios aplicados:**
- **Isolamento de Domínio:** Nenhuma dependência de framework nas entidades e regras de negócio.
- **Value Objects ricos:** `Email`, `Password`, `Rating` encapsulam validação e comportamento.
- **Casos de uso coesos:** Cada `UseCase` orquestra exatamente uma operação da aplicação.
- **Inversão de dependência:** O domínio define interfaces (`Ports`); a infraestrutura as implementa (`Adapters`).

---

## 🔐 Segurança

A autenticação e autorização são implementadas com **Spring Security + JWT**, com camadas adicionais de proteção:

- **JWT Stateless:** Tokens gerados com `HMAC256` via `Auth0 JWT`, sem sessões no servidor.
- **Autenticação de 2 Fatores (2FA):** Integração com **Google Authenticator** via `GoogleAuth`. O 2FA é obrigatório para ações sensíveis como troca de senha.
- **Roles:** Dois perfis de acesso — `USER` e `MODERATOR` — com permissões distintas por endpoint.
- **Criptografia de dados sensíveis:** Emails armazenados criptografados no banco via `AesEncryptor` (JPA `AttributeConverter`).
- **Autorização baseada no banco:** A role do usuário é sempre validada contra o banco de dados, garantindo integridade no controle de acesso.
- **Bcrypt:** Senhas armazenadas com hash `BCryptPasswordEncoder`.

---

## 🛠️ Tecnologias

| Categoria | Tecnologia |
|---|---|
| Linguagem | Java 21 |
| Framework | Spring Boot 3.x |
| Segurança | Spring Security 6, JWT (Auth0) |
| Banco Relacional | PostgreSQL + JPA/Hibernate |
| Banco Não Relacional | MongoDB |
| 2FA | Google Authenticator (`GoogleAuth`) |
| E-mail | JavaMailSender + Mailtrap |
| Documentação | Swagger / OpenAPI 3 |
| Testes | JUnit 5 + Mockito |
| Conteinerização | Docker + Docker Compose |

---

## 🚀 Como Executar

O projeto é totalmente containerizado. Você precisa apenas do **Docker** instalado.

**1. Clone o repositório:**
```bash
git clone https://github.com/arthurhenrique-Dev/Quack.git
cd Quack
```


**2. Suba a aplicação:**
```bash
docker-compose up -d
```

A API estará disponível em `http://localhost:8080`.
A documentação Swagger estará em `http://localhost:8080/swagger-ui.html`.

---

## 📡 Endpoints

### 🔑 Autenticação (`/quack/auth`)

| Método | Endpoint | Descrição | Acesso |
|---|---|---|---|
| `POST` | `/login` | Login e geração de token JWT | Público |
| `POST` | `/signUp` | Cadastro de usuário | Público |
| `POST` | `/moderator/signUp` | Cadastro de moderador | Público |
| `POST` | `/check` | Validação 2FA de usuário | Público |
| `POST` | `/management/check` | Validação 2FA de moderador | Público |

### 👤 Usuários (`/quack/user`)

| Método | Endpoint | Descrição | Acesso |
|---|---|---|---|
| `GET` | `/{id}` | Buscar perfil de usuário | Público |
| `GET` | `/` | Buscar usuários por filtro | Público |
| `GET` | `/{id}/reviews` | Listar reviews de um usuário | Público |
| `PUT` | `/customize/photo` | Alterar foto de perfil | USER |
| `PUT` | `/customize/description` | Alterar descrição | USER |
| `PUT` | `/customize/username` | Alterar username | USER |
| `PUT` | `/customize/email/send` | Solicitar troca de email | USER |
| `PUT` | `/customize/email/confirm/{token}` | Confirmar troca de email | USER |
| `PUT` | `/customize/password/send` | Solicitar troca de senha | USER |
| `PUT` | `/customize/password/confirm/{token}` | Confirmar troca de senha | USER |
| `POST` | `/follow/{id}` | Seguir usuário | USER |
| `DELETE` | `/unfollow/{id}` | Deixar de seguir usuário | USER |
| `DELETE` | `/customize/delete` | Deletar própria conta | USER |

### 📝 Reviews (`/quack/review`)

| Método | Endpoint | Descrição | Acesso |
|---|---|---|---|
| `POST` | `/` | Criar review | USER |
| `PUT` | `/{id}/review_update` | Atualizar texto da review | USER |
| `PUT` | `/{id}/rating_update` | Atualizar nota da review | USER |
| `DELETE` | `/{id}` | Deletar review | USER, MODERATOR |

### 🎮 Jogos (`/quack/games`)

| Método | Endpoint | Descrição | Acesso |
|---|---|---|---|
| `GET` | `/{id}` | Buscar jogo por id | Público |
| `GET` | `/` | Buscar jogos por filtro | Público |
| `POST` | `/` | Cadastrar novo jogo | MODERATOR |

### 🛡️ Moderação (`/quack/management`)

| Método | Endpoint | Descrição | Acesso |
|---|---|---|---|
| `POST` | `/users/{id}/activate` | Ativar usuário | MODERATOR |
| `DELETE` | `/users/{id}/ban` | Banir usuário | MODERATOR |
| `POST` | `/moderators/{id}/activate` | Ativar moderador | MODERATOR |
| `DELETE` | `/moderators/{id}/ban` | Banir moderador | MODERATOR |
| `DELETE` | `/moderators/review/{idReview}/userid/{idUser}` | Deletar review de usuário | MODERATOR |
| `PUT` | `/customize/email/send` | Solicitar troca de email | MODERATOR |
| `PUT` | `/customize/email/confirm/{token}` | Confirmar troca de email | MODERATOR |
| `PUT` | `/customize/password/send` | Solicitar troca de senha | MODERATOR |
| `PUT` | `/customize/password/confirm/{token}` | Confirmar troca de senha | MODERATOR |

---

## 🧪 Testes

Testes unitários implementados com **JUnit 5** e **Mockito**, com foco na camada de Application (casos de uso), onde a lógica de negócio é mais crítica e os adapters de infraestrutura são mockados.


---

## 🗄️ Infraestrutura

O `docker-compose.yaml` sobe três serviços automaticamente:

- **`quack_app`** — A aplicação Spring Boot
- **`quack_db`** — PostgreSQL para dados relacionais (usuários, moderadores, follows)
- **`quack_mongo`** — MongoDB para catálogo de jogos e reviews

O app aguarda o PostgreSQL estar saudável (`healthcheck`) antes de iniciar, evitando falhas de conexão na inicialização.
