# 🔗 Flip URL Shortener

A lightweight Spring Boot service that transforms long URLs into compact short codes and seamlessly expands them back to
their original destinations. Built with REST endpoints and database persistence for reliable URL management.

---

## 🚀 Quick Start

### Prerequisites

- **Java 25** (configured via Gradle toolchains)
- Compatible JDK installed on your system

### Running Locally

Launch the application with local configuration:

```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```

**Need a database?** Start external dependencies using Docker Compose:

```bash
docker-compose up
```

---

## 🧪 Testing

Run the complete test suite:

```bash
./gradlew test
```

### Test Coverage

Jacoco generates comprehensive code coverage reports automatically with each test run.

📊 **View the HTML report:**  
`build/reports/jacoco/test/html`

---

## 🎨 Code Formatting

This project uses **Spotless** with **Google Java Format** to maintain consistent code style.

**Format your code:**

```bash
./gradlew spotlessApply
```

**Check formatting:**

```bash
./gradlew spotlessCheck
```

---

## 📚 API Documentation

Interactive API documentation powered by **Swagger UI** is available when the application is running.

**Access it at:** `/swagger-ui`

### API Endpoints

#### 🔗 Shorten a URL

Creates a short URL from a long original URL.

```bash
curl -X POST http://localhost:8080/api/v1/urls/shorten \
  -H "Content-Type: application/json" \
  -d '{
    "originalUrl": "https://www.example.com/very/long/url/that/needs/shortening"
  }'
```

**Response:**

```json
{
  "shortUrl": "abc123"
}
```

#### 🔍 Expand a Short URL

Retrieves the original URL from a short URL code.

```bash
curl -X GET "http://localhost:8080/api/v1/urls/expand?shortUrl=abc123"
```

**Response:**

```json
{
  "originalUrl": "https://www.example.com/very/long/url/that/needs/shortening"
}
```

---

## ⚙️ Configuration

### Supported Spring Profiles

| Profile | Description                   |
|---------|-------------------------------|
| `local` | Local development environment |
| `dev`   | Development server            |
| `test`  | Testing environment           |
| `prod`  | Production deployment         |

---

## 📦 Tech Stack

- **Spring Boot** - Application framework
- **Java 25** - Programming language
- **Gradle** - Build tool
- **Jacoco** - Code coverage
- **Spotless** - Code formatting
- **OpenAPI/Swagger** - API documentation