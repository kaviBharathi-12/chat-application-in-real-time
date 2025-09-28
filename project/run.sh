#!/bin/bash

echo "Starting Real-time Chat Application..."
echo "====================================="

# Check if build directory exists
if [ ! -d "build" ]; then
    echo "Build directory not found. Compiling first..."
    ./compile.sh
    if [ $? -ne 0 ]; then
        echo "❌ Compilation failed!"
        exit 1
    fi
fi

# Run the application
java -cp build com.chatapp.ChatApplication