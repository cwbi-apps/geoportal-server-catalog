SHELL := bash
.DEFAULT_GOAL := help

TOMCAT_WEBAPPS := /opt/tomcat/webapps
GEO_WAR_DIR := $(TOMCAT_WEBAPPS)/catalog
AUTH_DIR := /opt/tomcat/conf/authentication

help:
	@echo "Geoportal Server Catalog 3.0.2"
	@echo ""
	@echo "make build            Build Geoportal WAR"
	@echo "make deploy           Deploy exploded WAR (hot reload)"
	@echo "make clean            Clean Tomcat deployment"
	@echo "make auth-simple      Switch to simple authentication"
	@echo "make auth-keycloak    Switch to Keycloak authentication"
	@echo "make reindex          Reindex OpenSearch"
	@echo "make logs             Tail Tomcat logs"

build:
	mvn -f geoportal/pom.xml clean package -DskipTests

deploy: build
	rm -rf $(GEO_WAR_DIR)
	mkdir -p $(GEO_WAR_DIR)
	unzip -q geoportal/target/*.war -d $(GEO_WAR_DIR)
	@echo "✔ Geoportal deployed (exploded WAR)"

clean:
	rm -rf $(GEO_WAR_DIR)
	rm -rf /opt/tomcat/logs/*
	@echo "✔ Tomcat geoportal cleaned"

auth-simple:
	@echo "authentication-simple.xml" > /tmp/gpt_auth
	export GPT_AUTHENTICATION=authentication-simple.xml
	@echo "✔ Switched to SIMPLE auth (restart Tomcat)"

auth-keycloak:
	@echo "authentication-keycloak.xml" > /tmp/gpt_auth
	export GPT_AUTHENTICATION=authentication-keycloak.xml
	@echo "✔ Switched to KEYCLOAK auth (restart Tomcat)"

reindex:
	curl -X POST http://localhost:8080/geoportal/rest/management/reindex

logs:
	tail -f /opt/tomcat/logs/catalina.out
