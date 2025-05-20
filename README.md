# WorkRight

Aplicação web para organização de tarefas com suporte a Pomodoro, construída com Spring Boot, PostgreSQL, JPA, Flyway, Thymeleaf, Tailwind CSS e HTMX.

## 🧱 Tecnologias utilizadas

- Java 17  
- Spring Boot 3  
- PostgreSQL  
- Flyway  
- JPA (Hibernate)  
- Thymeleaf  
- Tailwind CSS  
- HTMX  
- Docker / Docker Compose  

---

## 🚀 Como rodar o projeto

### 1. Pré-requisitos

- Docker e Docker Compose instalados  
- Java 17 (caso queira rodar localmente fora do Docker)  
- Maven (caso queira rodar localmente fora do Docker)  

---

### 2. Build do projeto

Se ainda não tiver o `.jar` compilado, você pode rodar o Maven:

```bash
./mvnw clean package -DskipTests
```

Isso vai gerar o arquivo:

```
target/workright-0.0.1-SNAPSHOT.jar
```

---

### 3. Subindo com Docker Compose

Use o seguinte comando para rodar o backend e o banco de dados:

```bash
docker-compose up --build
```

Esse comando irá:

- Subir o PostgreSQL com usuário e senha definidos  
- Executar o backend do Spring Boot usando o `.jar` gerado  
- Aplicar as migrations do Flyway automaticamente  

---

### 4. Acessando a aplicação

Abra seu navegador em:

```
http://localhost:8080
```

> ⚠️ Por padrão, o Spring Security está ativado. Será exibida uma tela de login com usuário e senha gerados automaticamente no terminal (`Using generated security password: ...`).  
> Você pode desativar ou configurar isso conforme seu sistema.

---

### 5. Estrutura do Docker

O `docker-compose.yml` contém:

- **db**: container com PostgreSQL, persistência de dados no volume `pgdata`  
- **app**: container que copia o `.jar` gerado e executa com `java -jar`  

Certifique-se de que o `target/workright-0.0.1-SNAPSHOT.jar` exista antes de subir os containers.

---

## 🛠 Variáveis de ambiente (via `application.properties`)

As configurações de banco de dados estão no `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://db:5432/workright
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.hibernate.ddl-auto=none
spring.flyway.enabled=true
```

---

## 🗃 Banco de dados

- O Flyway será executado automaticamente ao iniciar a aplicação.  
- A versão inicial é migrada com o arquivo `V1__init.sql` localizado em `src/main/resources/db/migration`.

---

## 🧪 Testes

Você pode rodar os testes (caso existam) com:

```bash
./mvnw test
```
