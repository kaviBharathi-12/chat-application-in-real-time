#!/bin/bash

echo "Compiling Real-time Chat Application..."

# Create build directory
mkdir -p build

# Compile all Java files
find src -name "*.java" -print0 | xargs -0 javac -d build -cp build

if [ $? -eq 0 ]; then
    echo "✅ Compilation successful!"
    echo "To run the application, execute:"
    echo "java -cp build com.chatapp.ChatApplication"
else
    echo "❌ Compilation failed!"
    exit 1
fi