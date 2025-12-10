# Tennis Game Scoring


---

##  Prerequisites

* **Java 21** or higher
* **Maven 3.6+**


## 1. Build & Test
First, clone the repository and run the full test suite (Unit & Integration tests).

```bash
git clone https://github.com/luigcapo/tennis-game-scoring.git
cd tennis-game-scoring
mvn clean test
```

## 2. Run the Application
You can run the application directly using the Maven Exec plugin.

Syntax:

```bash
mvn -q -DskipTests compile exec:java -Dexec.args="<Sequence>"
```

Example:

```bash
mvn -q -DskipTests compile exec:java -Dexec.args="ABABAA"
```
## 3. Design Choices & Architecture
### Separation of Concerns (SRP)
The application is split into distinct layers:

Service: Pure business logic.

Validator: Responsible for protecting the application.

Model: simple POJOs/Enums.

### Security & Validation (Fail Fast)
I prefer to reject invalid data immediately .

Strict Format: Only A and B characters are allowed. Any other character (spaces, lowercase, numbers) triggers an immediate IllegalArgumentException.

Input length is capped at 500 characters. Exceeding this limit throws a SecurityException to prevent memory overflow attacks.

### Game Logic
Idempotency after Win: Once a player reaches the WIN state, any subsequent points in the sequence are ignored. 

### Logs

Technical logs (SLF4J/Logback) are separated from the Standard Output and are stored in the `logs/` directory at the project root.

### Tech Stack
Java 21

Maven

JUnit 5 

AssertJ 

Lombok

Logback / SLF4J
