#!/bin/bash
# Quick Start Script for AI Ski Trip Advisor

echo "╔════════════════════════════════════════════╗"
echo "║   AI SKI TRIP ADVISOR - QUICK START        ║"
echo "╚════════════════════════════════════════════╝"
echo ""

# Check if PostgreSQL is running
echo "Checking prerequisites..."

# Check Java version
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed. Please install Java 11 or higher."
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 11 ]; then
    echo "❌ Java 11 or higher is required. Current version: $JAVA_VERSION"
    exit 1
fi
echo "✓ Java version OK"

# Check Maven
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven is not installed. Please install Maven 3.6 or higher."
    exit 1
fi
echo "✓ Maven found"

echo ""
echo "Building the project..."
mvn clean package

if [ $? -eq 0 ]; then
    echo ""
    echo "✓ Build successful!"
    echo ""
    echo "Starting AI Ski Trip Advisor..."
    echo ""
    mvn exec:java
else
    echo ""
    echo "❌ Build failed. Please check the error messages above."
    echo ""
    echo "Common issues:"
    echo "1. Update database.properties with your database credentials"
    echo "2. Update OpenAI API key in database.properties"
    echo "3. Ensure PostgreSQL database is running"
    exit 1
fi
