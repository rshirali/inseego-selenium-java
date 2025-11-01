# Inseego – Selenium Java Automation Framework

This repository contains a Selenium + TestNG + Maven automation framework for validating UI flows on the Inseego website.

The framework demonstrates a clean, maintainable structure and reproducible test execution using standard Java-based tools.

## Tech Stack

| Tool | Purpose |
|------|----------|
| Java 17+ | Programming language |
| Selenium WebDriver | UI automation engine |
| TestNG | Test framework (annotations, reports, assertions) |
| Maven | Build tool and dependency management |
| Log4j2 | Logging configuration |

## Project Structure
```
inseego-selenium-java/
├── pom.xml
├── src
│   ├── main/java/com/demo/core/DriverFactory.java
│   ├── main/java/com/demo/pages/HomePage.java
│   └── test/java/com/demo/tests/HeaderFooterSmokeTest.java
└── target/
    └── surefire-reports/
```

### Key Components
- DriverFactory.java – Manages browser setup and teardown  
- HomePage.java – Page Object for the landing page  
- HeaderFooterSmokeTest.java – Test verifying page sections  

## Prerequisites

Ensure the following are installed:

| Tool | Version | Check Command |
|------|----------|---------------|
| Java | 17+ | `java -version` |
| Maven | 3.9+ | `mvn -v` |
| Git | Latest | `git --version` |
| Browser | Chrome or Edge | Preinstalled |

ChromeDriver and EdgeDriver are automatically managed via WebDriverManager.

## Setup and Run

### Clone the repository
```bash
git clone git@github.com:rshirali/inseego-selenium-java.git
cd inseego-selenium-java
```

### Build the project
```bash
mvn clean compile
```

### Run tests
```bash
mvn test
```

Or specify runtime parameters:
```bash
mvn -DbaseUrl=https://inseego.com -Dbrowser=chrome -Dheadless=false test
```

### View reports
After execution:
```
target/surefire-reports/index.html
```

## Test Configuration

TestNG suite file: `src/test/resources/testng.xml`

```xml
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">
<suite name="Inseego Suite">
  <test name="Smoke">
    <classes>
      <class name="com.demo.tests.HeaderFooterSmokeTest"/>
    </classes>
  </test>
</suite>
```

## Common Maven Commands

| Command | Purpose |
|----------|----------|
| `mvn clean` | Remove old build files |
| `mvn compile` | Compile main source |
| `mvn test` | Run all tests |
| `mvn surefire-report:report` | Generate HTML reports |
| `mvn dependency:tree` | Show dependency graph |

## Folder Summary

| Folder | Description |
|---------|--------------|
| `src/main/java` | Framework and page object source |
| `src/test/java` | TestNG tests |
| `src/test/resources` | Config and XML suite |
| `target/` | Compiled classes, reports, logs |

## .gitignore Reference
```
target/
*.class
*.log
*.iml
.idea/
.vscode/
.DS_Store
test-output/
surefire-reports/
```

## Author
Rajeev Shirali  
Automation Architect / SDET  
https://github.com/rshirali
