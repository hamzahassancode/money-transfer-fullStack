# Cliq Spring Boot Project 🍃

This project is a **Spring Boot** application developed for Cliq, providing RESTful web services for managing accounts and transactions. It integrates with a frontend (React) to provide a seamless user experience.

## Features

- **Account Management**: view accounts, view all transactions.
- **Transaction Management**: Perform money transfers between accounts, view transaction history.
- **Logging**: Capture and store application logs.
- **Error Handling**: Graceful handling of exceptions with meaningful error messages.

## Tech Stack

- **Backend**: Spring Boot, Spring Data JPA
- **Frontend**: React, Tailwind CSS, SweetAlert2
- **Database**: mySQL, h2
- **Testing**: JUnit
- **Build Tool**: Maven
- **Version Control**: Gitlab

## Prerequisites

To build and run this project locally, ensure you have the following:

- Java 21+
- Maven 3+
- mySQL

## Liquibase
Liquibase is integrated into the project for managing database schema changes. It allows for versioning of database changes, making it easy to track and apply updates across different environments.

## API Controllers
#### Account Controller

Method	| Path	                         | Description	                                                    
------------- |-------------------------------|-----------------------------------------------------------------
GET	| v1/accounts	                  | Get all accounts 	                                       
GET	| v1/accounts/{account_number}	 | Get account by account number  	                                                              


#### transfer Controller

Method	| Path	                    | Description	                                                    
------------- |--------------------------|-----------------------------------------------------------------
GET	| api/transfers	           | Get all transfers list 	                                       
POST	| v1/transfers/	           | transfer money to beneficiary account	            
POST	| v1/transfers/upload-csv	 | transfer bulk of transfer money             

#### Notes
- mySQL is used as a primary database.
- h2 is used as a test database.


## Setup Instructions

1. **Clone the repository 💾**
   ```bash
      https://gitlab.progressoft.io/ps.hamza.hassan/cliq-full-stack1
      
2. **Run the code manually or use Docker Compose:**
    1. **Manual Setup**
        1. Database Setup 📊

           Before running the backend, ensure that you have a MySQL server running and create a database called `cliq`. Follow these steps to set it up:

        - Open a terminal and log in to MySQL:
           ```bash
           mysql -u root -p root
            CREATE DATABASE cliq;      

        2. Backend ⚙
            - **Navigate to the Backend Directory**
                - cd Cliq-transfers
                - mvn clean install
                - mvn spring-boot:run
        4. Frontend 💻
           - **Run the React Application**
            - Install [Node.js](https://nodejs.org/) if you haven't already.
            - make sure You are in ```/my-react-app``` directory
            - run this command in terminal ```./setup_react_project.sh``` to install all requirement
            - run npm start to run react server ```npm start```

    2. **Using Docker Compose**

       If you have Docker installed, use the following command to start the application:

       ```bash 
          docker compose up

    3. **Using helm **

       If you have Kubernetes installed, use the following command to start the application:

       ```bash 
             cd fullHelmApp
             helm install myfullapp .    