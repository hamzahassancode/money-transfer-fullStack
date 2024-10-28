#!/bin/bash

# Function to check if a command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Step 1: Check if Node.js is installed
if command_exists node; then
    echo "Node.js is already installed."
else
    echo "Node.js is not installed. Installing Node.js..."
    # Install Node.js (this example uses curl to fetch and install Node.js via nvm)
    curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.5/install.sh | bash
    # Load nvm to use it in the current shell session
    source ~/.nvm/nvm.sh
    nvm install node
fi

# Step 2: Check if npm is installed
if command_exists npm; then
    echo "npm is already installed."
else
    echo "npm is not installed. Please install npm manually."
    exit 1
fi

# Step 3: Create a new React app if the "node_modules" folder does not exist
if [ ! -d "node_modules" ]; then
    echo "Creating a new React app..."
    npx create-react-app my-app
    cd my-app || exit
else
    echo "React project already exists."
fi

# Step 4: Install Tailwind CSS, PostCSS, and Autoprefixer
echo "Installing Tailwind CSS, PostCSS, and Autoprefixer..."
npm install -D tailwindcss postcss autoprefixer
npx tailwindcss init

# Step 5: Install SweetAlert2
echo "Installing SweetAlert2..."
npm install sweetalert2

# Step 6: Install Axios
echo "Installing Axios..."
npm install axios

# Step 7: Install React Router DOM
echo "Installing React Router DOM..."
npm install react-router-dom

# Step 8: Print success message
echo "React project setup complete with all required dependencies."

