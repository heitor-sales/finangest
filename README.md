## 🔧 Configuração do Banco de Dados

1.  Crie um novo banco de dados no seu servidor PostgreSQL. Com nome, `finangest`.
2.  Atualize as configurações de conexão com o banco de dados no arquivo `src/main/resources/application.properties`.

**Exemplo para `application.properties`:
#Configurações de persistência e JDBC
spring.datasource.url= jdbc:postgresql://localhost:5432/finangest
spring.datasource.driver-class-name= org.postgresql.Driver
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.datasource.username=postgres
spring.datasource.password=ifpb
spring.jpa.hibernate.ddl-auto=update
