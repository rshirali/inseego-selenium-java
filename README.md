# Inseego – Selenium Java Automation Framework

This repository contains a simple **Selenium + TestNG + Maven** automation framework
for validating UI flows on the [Inseego website](https://inseego.com).

The goal is to demonstrate clean structure, maintainable code, and reproducible
test execution using Java-based tools.

---

## 🧰 Tech Stack

| Tool | Purpose |
|------|----------|
| **Java 17+** | Programming language |
| **Selenium WebDriver** | UI automation engine |
| **TestNG** | Test framework (annotations, reports, assertions) |
| **Maven** | Build tool and dependency management |
| **Log4j2** | Logging configuration |

---

## 📦 Project Structure

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
- **DriverFactory.java** → Manages browser setup and teardown.
- **HomePage.java** → Page Object class for the landing page.
- **HeaderFooterSmokeTest.java** → TestNG test verifying page sections.

---

## ⚙️ Prerequisites

Ensure the following are installed on your system:

| Tool | Version | Check Command |
|------|----------|----------------|
| Java | 17 or newer | `java -version` |
| Maven | 3.9+ | `mvn -v` |
| Git | Latest | `git --version` |
| Browser | Chrome or Edge | preinstalled |

> 💡 **Note:** ChromeDriver / EdgeDriver are automatically managed via `WebDriverManager`
if included in your dependencies.

---

## 🚀 Setup & Run

### 1️⃣ Clone the repository
```bash
git clone git@github.com:rshirali/inseego-selenium-java.git
cd inseego-selenium-java
```

### 2️⃣ Build the project
```bash
mvn clean compile
```

### 3️⃣ Run tests
```bash
mvn test
```

### 4️⃣ View Reports
After execution, open the TestNG HTML report:

```
target/surefire-reports/index.html
```

---

## 🧩 Test Configuration

TestNG suite file:  
`src/test/resources/testng.xml`

Example:
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

---

## 🧱 Common Maven Commands

| Command | Purpose |
|----------|----------|
| `mvn clean` | Remove old build files |
| `mvn compile` | Compile main source code |
| `mvn test` | Run all tests |
| `mvn surefire-report:report` | Generate HTML reports |
| `mvn dependency:tree` | View dependency graph |

---

## 🧭 Folder Summary

| Folder | Description |
|---------|--------------|
| `src/main/java` | Framework and page object source |
| `src/test/java` | TestNG tests |
| `src/test/resources` | Config and XML test suite |
| `target/` | Compiled classes, reports, logs (auto-generated) |

---

## 🧹 .gitignore (for reference)

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

---

## 📘 Notes

- Default browser: **Chrome**
- Extendable to Firefox/Edge via `DriverFactory`
- Modular structure for future parallel execution or CI/CD integration.

---

## 🧑‍💻 Author

**Rajeev Shirali**  
Automation Architect / SDET  
[GitHub Profile](https://github.com/rshirali)
