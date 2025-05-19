# 🏥 Sistema de Agendamento de Consultas Médicas

## 📌 Descrição do Projeto

O **Sistema de Agendamento de Consultas Médicas** tem como objetivo digitalizar e facilitar o processo de marcação, gestão e acompanhamento de consultas médicas em clínicas ou consultórios. Ele oferece controle completo sobre médicos, pacientes, especialidades e consultas, atendendo às necessidades de diferentes perfis de usuários: Administrador, Atendente e Médico.

---

## 🎯 Funcionalidades Principais

### 👨‍⚕️ Administrador
- Cadastro, edição, exclusão e listagem de médicos.
- Cadastro, edição, exclusão e listagem de atendentes.
- Gerenciamento de especialidades médicas.

### 🧑‍💼 Atendente
- Cadastro, edição, exclusão e listagem de pacientes.
- Agendamento, edição, cancelamento e visualização de consultas médicas.

### 🩺 Médico
- Visualização da própria agenda de consultas.
- Acesso aos dados básicos dos pacientes agendados.

---

## 🛠️ Tecnologias Utilizadas

### Backend:
- **Java 17+**
- **Jakarta EE / Java EE**
- **JPA (Hibernate ou EclipseLink)**
- **Maven** (para gerenciamento de dependências)

### Frontend:
- **JavaServer Faces (JSF)**
- **PrimeFaces** (componentes ricos para interface)

### Banco de Dados:
- **PostgreSQL** (ou alternativa: MySQL, H2 para testes locais)

### Outras Ferramentas:
- **Git** (controle de versão)
- **Apache Tomcat / Payara / WildFly** (servidores de aplicação compatíveis com Jakarta EE)
- **Lombok** (para reduzir boilerplate no código Java)
- **JUnit / Mockito** (para testes unitários e de integração)

---

## 💡 Possíveis Tecnologias Futuras

- **Spring Boot**: como alternativa para acelerar o desenvolvimento em versões futuras.
- **RESTful APIs**: para expor funcionalidades e integrar com outros sistemas ou mobile apps.
- **JWT / OAuth2**: para segurança e autenticação moderna.
- **Docker**: para empacotamento e deploy em ambientes conteinerizados.
- **Swagger/OpenAPI**: para documentação de API (caso REST seja implementado).
- **React ou Angular**: possível front-end moderno se houver interesse em desacoplar a UI.

---

## 📁 Estrutura Inicial do Projeto

```plaintext
src/
├── main/
│   ├── java/
│   │   └── br/com/seuprojeto/
│   │       ├── controller/
│   │       ├── model/
│   │       ├── repository/
│   │       ├── service/
│   │       └── util/
│   └── webapp/
│       ├── pages/
│       │   ├── medico/
│       │   ├── atendente/
│       │   ├── admin/
│       │   └── shared/
│       └── WEB-INF/
│           └── web.xml
└── test/
    └── java/
