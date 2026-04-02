# GRSMU Application

A web application built with **Spring Boot** for Grodno State Medical University (ГРГМУ). The application provides a server-rendered web interface built with Java on the backend and HTML/CSS on the frontend.

---

## Tech Stack

| Layer      | Technology                   |
|------------|------------------------------|
| Backend    | Java, Spring Boot            |
| Frontend   | HTML, CSS (Thymeleaf/JSP)    |
| Build tool | Maven (Maven Wrapper)        |

---

## Prerequisites

- **Java 17+** (or the version specified in `pom.xml`)
- **Maven** — or use the included Maven Wrapper (`mvnw`)

---

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/roklimovich/grsmu-application.git
cd grsmu-application
```

### 2. Build the project

Using the Maven Wrapper (no local Maven installation required):

```bash
# Linux / macOS
./mvnw clean install

# Windows
mvnw.cmd clean install
```

### 3. Run the application

```bash
# Linux / macOS
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

The application will start on **http://localhost:8080** by default.

---

## Project Structure

```
grsmu-application/
├── data/                        # Data files / seed data
├── src/
│   ├── main/
│   │   ├── java/                # Java source code (controllers, services, models)
│   │   └── resources/
│   │       ├── templates/       # HTML templates
│   │       ├── static/          # Static assets (CSS, JS, images)
│   │       └── application.properties
│   └── test/
│       └── java/                # Unit and integration tests
├── .mvn/wrapper/                # Maven Wrapper configuration
├── pom.xml                      # Maven project descriptor
├── mvnw                         # Maven Wrapper script (Unix)
└── mvnw.cmd                     # Maven Wrapper script (Windows)
```

---

## Configuration

Application settings can be adjusted in `src/main/resources/application.properties`. Common properties to configure:

```properties
# Server port (default: 8080)
server.port=8080

# Database connection (if applicable)
spring.datasource.url=jdbc:...
spring.datasource.username=...
spring.datasource.password=...
```

---

## Building a JAR

To produce a standalone executable JAR:

```bash
./mvnw clean package
java -jar target/grsmu_application-*.jar
```

---

