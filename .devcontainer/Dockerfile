# # Base image with Java 11
# FROM mcr.microsoft.com/devcontainers/java:1-11-bullseye

# ENV TOMCAT_VERSION=9.0.85
# ENV CATALINA_HOME=/opt/tomcat

# USER root

# # Install dependencies + Maven
# RUN apt-get update \
#  && apt-get install -y \
#     curl \
#     unzip \
#     maven \
#     python-is-python3 \
#     python3-requests \
#  && rm -rf /var/lib/apt/lists/*

# # Install Tomcat
# RUN curl -fsSL https://archive.apache.org/dist/tomcat/tomcat-9/v${TOMCAT_VERSION}/bin/apache-tomcat-${TOMCAT_VERSION}.tar.gz \
#  | tar xz -C /opt && \
#  mv /opt/apache-tomcat-${TOMCAT_VERSION} ${CATALINA_HOME}

# # Cleanup default webapps, set permissions
# RUN rm -rf ${CATALINA_HOME}/webapps/* && \
#     chmod +x ${CATALINA_HOME}/bin/*.sh

# # Make sure m2 and tomcat are writable by vscode
# RUN mkdir -p /home/vscode/.m2 && \
#     chown -R vscode:vscode /home/vscode/.m2 ${CATALINA_HOME}

# # Default workspace
# WORKDIR /workspace

# # Run as vscode by default
# USER vscode

# Base image: OpenJDK 17 for ARM
FROM mcr.microsoft.com/devcontainers/java:1-11-bullseye

# Install Tomcat 9
ENV TOMCAT_VERSION=9.0.85
ENV CATALINA_HOME=/opt/tomcat
ENV PATH=$CATALINA_HOME/bin:$PATH

RUN apt-get update \
 && apt-get install -y \
    net-tools \
    procps \
    curl \
    unzip \
    maven \
    python-is-python3 \
    python3-requests \
 && rm -rf /var/lib/apt/lists/*

# Install Tomcat
RUN curl -fsSL https://archive.apache.org/dist/tomcat/tomcat-9/v${TOMCAT_VERSION}/bin/apache-tomcat-${TOMCAT_VERSION}.tar.gz \
 | tar xz -C /opt && \
 mv /opt/apache-tomcat-${TOMCAT_VERSION} ${CATALINA_HOME}

# Cleanup default webapps, set permissions
RUN rm -rf ${CATALINA_HOME}/webapps/* && \
    chmod +x ${CATALINA_HOME}/bin/*.sh

# Make sure m2 and tomcat are writable by vscode
RUN mkdir -p /home/vscode/.m2 && \
    chown -R vscode:vscode /home/vscode/.m2 ${CATALINA_HOME}

COPY .devcontainer/cwbi-truststore.jks /opt/tomcat/conf/cwbi-truststore.jks

USER vscode
WORKDIR /workspace
