# SeleniumLocalWebTest: Selenium Java Project Integration with GitHub Actions & Docker Hub

> **College Experiment 6 Reference Project**  
> Complete implementation of automated browser testing using Selenium WebDriver, TestNG, Maven, GitHub Actions CI/CD pipeline, and Docker containerization.

---

## 🛠️ Technology Stack

- **Language:** Java JDK 17 (Eclipse Temurin)
- **Automation Framework:** Selenium WebDriver 4.25.0
- **Testing Framework:** TestNG 7.10.2
- **Build Tool:** Apache Maven
- **Frontend:** Responsive HTML5 & CSS3 Login Application
- **Version Control:** Git & GitHub
- **CI/CD:** GitHub Actions (Ubuntu runner with Chrome Headless)
- **Containerization:** Docker & Docker Hub (Multi-stage build)

---

## 📁 Project Structure

```
SeleniumLocalWebTest/
├── pom.xml
├── testng.xml
├── Dockerfile
├── .gitignore
├── README.md
├── .github/
│   └── workflows/
│       └── selenium-tests.yml
└── src/
    ├── main/
    │   └── resources/
    │       └── webpage/
    │           └── login.html
    └── test/
        └── java/
            └── com/example/
                └── LocalLoginTest.java
```

---

## 🌐 Web Application Under Test (`login.html`)

A responsive DevOps Login Portal featuring:
- **Heading:** `Welcome to DevOps`
- **Username Field:** `id="username"`
- **Password Field:** `id="password"`
- **Submit Button:** `id="loginButton"`
- **Feedback Message Area:** `id="message"`
- **Card Container:** `id="loginBox"` styled with `background-color: lightblue`

### Test Credentials
| Role | Username | Password | Expected Output |
| :--- | :--- | :--- | :--- |
| **Valid User** | `student` | `12345` | `"Welcome to DevOps! Login Successful"` |
| **Invalid User** | *(any other)* | *(any other)* | `"Invalid Username or Password"` |

---

## 🧪 Test Cases Implemented (`LocalLoginTest.java`)

1. **`validLoginTest()`**
   - Inputs `student` and `12345`.
   - Clicks `#loginButton`.
   - Asserts that `#message` text equals `"Welcome to DevOps! Login Successful"`.

2. **`invalidLoginTest()`**
   - Inputs invalid credentials.
   - Clicks `#loginButton`.
   - Asserts that `#message` text equals `"Invalid Username or Password"`.

3. **`checkBackgroundColor()`**
   - Inspects the `#loginBox` CSS property `background-color`.
   - Asserts that the background color is `lightblue` (`rgb(173, 216, 230)`).

---

## 🚀 Local Execution

### Prerequisites
- Java JDK 17 installed and added to `PATH`
- Apache Maven installed and added to `PATH`
- Google Chrome browser installed

### Running via Terminal
From the project root directory:

```bash
mvn clean test
```

### Expected Output
```text
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running TestSuite
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: ... s -- in TestSuite
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

### Viewing TestNG Reports
After execution, open either of the following reports in any browser:
- Detailed Suite Report: `target/surefire-reports/index.html`
- Emailable Report: `target/surefire-reports/emailable-report.html`

---

## 🔄 GitHub Actions CI/CD Pipeline

The workflow defined in `.github/workflows/selenium-tests.yml` automatically triggers on every push and pull request to `main`:
1. Checks out repository code.
2. Configures Eclipse Temurin JDK 17 with Maven caching.
3. Sets up Google Chrome Stable.
4. Prints versions of Java, Maven, and Chrome.
5. Runs `mvn clean test` in headless mode.
6. Uploads the generated TestNG reports as downloadable pipeline artifacts.

---

## 🐳 Docker Integration

### Build Docker Image
```bash
docker build -t <your-dockerhub-username>/selenium-local-web-test:latest .
```

### Run Tests in Container
```bash
docker run --rm <your-dockerhub-username>/selenium-local-web-test:latest
```

### Push to Docker Hub
```bash
docker login
docker push <your-dockerhub-username>/selenium-local-web-test:latest
```
