#!/usr/bin/env bash

keytool -importcert -noprompt \
  -alias cwbi-cert \
  -file "cwbi-cert.pem" \
  -keystore "$JAVA_HOME/lib/security/cacerts" \
  -storepass changeit