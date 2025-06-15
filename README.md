# Sistema de Gestão de Coletores de Resíduos Eletrônicos - Reciclogrid

Este projeto é a **API Backend** responsável por gerenciar os dados dos coletores e operadores. Ele fornece os endpoints REST consumidos pelo frontend React.

---

## 🛠️ Tecnologias Utilizadas

- **Java 21**
- **Spring Boot**
- **Spring Web (REST)**
- **Spring Data JPA**
- **Hibernate**
- **PostgreSQL**
- **Maven**
- **Flyway** 

---

## ⚙️ Configuração do Banco de Dados

No arquivo `src/main/resources/application-dev.properties`, configure as credenciais do PostgreSQL:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/nome_do_banco
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```
