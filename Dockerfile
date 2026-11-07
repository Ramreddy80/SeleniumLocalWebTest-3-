# ==============================================================================
# Multi-Stage Dockerfile for Selenium Java Automation with TestNG & Chrome
# Primary Reference: College Experiment 6
# ==============================================================================

# ------------------------------------------------------------------------------
# Stage 1: Build Stage (Maven with Eclipse Temurin JDK 17)
# ------------------------------------------------------------------------------
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy Maven POM and TestNG configuration
COPY pom.xml .
COPY testng.xml .

# Download dependencies (cached layer)
RUN mvn dependency:go-offline -B

# Copy project source code (including login.html in resources)
COPY src ./src

# Compile main and test code
RUN mvn clean test-compile

# ------------------------------------------------------------------------------
# Stage 2: Runtime Stage (Eclipse Temurin JRE 17 with Google Chrome Stable)
# ------------------------------------------------------------------------------
FROM eclipse-temurin:17-jre AS runtime
WORKDIR /app

# Avoid prompts during apt installation
ENV DEBIAN_FRONTEND=noninteractive

# Install Google Chrome Stable, Maven, and required system libraries for Chrome
RUN apt-get update && apt-get install -y --no-install-recommends \
    wget \
    gnupg \
    ca-certificates \
    curl \
    maven \
    fonts-liberation \
    libasound2t64 \
    libatk-bridge2.0-0 \
    libatk1.0-0 \
    libatspi2.0-0 \
    libc6 \
    libcairo2 \
    libcups2 \
    libdbus-1-3 \
    libdrm2 \
    libgbm1 \
    libglib2.0-0 \
    libnspr4 \
    libnss3 \
    libpango-1.0-0 \
    libx11-6 \
    libx11-xcb1 \
    libxcb1 \
    libxcomposite1 \
    libxdamage1 \
    libxext6 \
    libxfixes3 \
    libxrandr2 \
    libxrender1 \
    libxss1 \
    libxtst6 \
    xdg-utils \
    && wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | gpg --dearmor -o /usr/share/keyrings/google-chrome.gpg \
    && echo "deb [arch=amd64 signed-by=/usr/share/keyrings/google-chrome.gpg] http://dl.google.com/linux/chrome/deb/ stable main" > /etc/apt/sources.list.d/google-chrome.list \
    && apt-get update \
    && apt-get install -y --no-install-recommends google-chrome-stable \
    && rm -rf /var/lib/apt/lists/*

# Copy cached Maven dependencies from build stage
COPY --from=build /root/.m2 /root/.m2

# IMPORTANT: Ensure full Maven config, TestNG XML, source files (login.html), and target are available
COPY --from=build /app/pom.xml ./pom.xml
COPY --from=build /app/testng.xml ./testng.xml
COPY --from=build /app/src ./src
COPY --from=build /app/target ./target

# Verify Chrome and Java in runtime
RUN java -version && google-chrome --version

# Set default execution command
ENTRYPOINT ["mvn"]
CMD ["test"]
