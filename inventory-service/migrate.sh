#!/bin/bash

# Exit on any error
set -e

# Load environment variables from .env
if [ -f .env ]; then
  echo "Loading environment variables from .env..."
  export $(grep -v '^#' .env | xargs)
else
  echo "❌ .env file not found. Please create one with DB credentials."
  exit 1
fi

# Verify required env vars are set
REQUIRED_VARS=(DB_HOST DB_PORT DB_NAME DB_USER DB_PASSWORD)
for var in "${REQUIRED_VARS[@]}"; do
  if [[ -z "${!var}" ]]; then
    echo "❌ Environment variable '$var' is not set. Check your .env file."
    exit 1
  fi
done

# Build JDBC URL
JDBC_URL="jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}"

# Run Flyway migration
echo "🚀 Running Flyway migration..."
mvn flyway:migrate \
  -Dflyway.url=$JDBC_URL \
  -Dflyway.user=$DB_USER \
  -Dflyway.password=$DB_PASSWORD

echo "✅ Flyway migration completed successfully."
