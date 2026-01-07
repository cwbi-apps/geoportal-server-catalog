#!/usr/bin/env bash
set -euo pipefail

HOST="dev2.crrel.mil"
PORT="443"
OUT="cwbi-cert.pem"

echo "🔐 Fetching Keycloak leaf certificate from ${HOST}:${PORT} ..."

# Grab the first certificate presented by the server
openssl s_client -connect "${HOST}:${PORT}" -servername "${HOST}" -showcerts </dev/null 2>/dev/null \
  | sed -n '/-----BEGIN CERTIFICATE-----/,/-----END CERTIFICATE-----/p' > "${OUT}"

if [[ ! -s "${OUT}" ]]; then
  echo "❌ Failed to fetch certificate"
  exit 1
fi

echo "✅ Certificate saved to ${OUT}"

# Import into Java truststore
# echo "🔑 Importing into Java cacerts truststore..."
# keytool -importcert -noprompt \
#   -alias cwbi-cert \
#   -file "${OUT}" \
#   -keystore "$JAVA_HOME/lib/security/cacerts" \
#   -storepass changeit

echo "✅ Certificate imported successfully!"
echo "📌 You may need to restart Tomcat or your Docker container for changes to take effect."
