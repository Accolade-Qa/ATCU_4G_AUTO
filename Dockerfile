# Use an official base image with Java
FROM openjdk:11-jdk-slim

# Set environment variables for Maven and Eclipse installation
ENV MAVEN_VERSION=3.8.8 \
    ECLIPSE_VERSION=2023-06

# Install required tools, dependencies, and clean up
RUN apt-get update && apt-get install -y --no-install-recommends \
    wget \ 
    curl \
    git \ 
    unzip \
    && rm -rf /var/lib/apt/lists/*

# Install Maven
RUN curl -fsSL https://downloads.apache.org/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz | tar -xz -C /opt \
    && ln -s /opt/apache-maven-${MAVEN_VERSION} /opt/maven

ENV MAVEN_HOME=/opt/maven
ENV PATH=$MAVEN_HOME/bin:$PATH

# Install Eclipse
RUN wget -O /tmp/eclipse.tar.gz https://www.eclipse.org/downloads/download.php?file=/technology/epp/downloads/release/${ECLIPSE_VERSION}/R/eclipse-java-${ECLIPSE_VERSION}-R-linux-gtk-x86_64.tar.gz \
    && tar -xzf /tmp/eclipse.tar.gz -C /opt \
    && ln -s /opt/eclipse /usr/local/bin/eclipse \
    && rm /tmp/eclipse.tar.gz

# Set up working directory
WORKDIR /app

# Copy project files into the container
COPY . /app

# Pre-download Maven dependencies
RUN mvn clean install -DskipTests

# Default command for the container
CMD ["bash"]
