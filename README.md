# Recruitment System Project Documentation

## Table of Contents
- [IDE Setup](#ide-setup)
  - [Front-end Development](#front-end-development)
  - [Back-end Development](#back-end-development)
- [Required Environment](#required-environment)
  - [Front-end Environment](#front-end-environment)
  - [Back-end Environment](#back-end-environment)
- [Running the Project](#running-the-project)
  - [Running the Front-end](#running-the-front-end)
  - [Running the Back-end](#running-the-back-end)
- [Deployment with Docker](#deployment-with-docker)

## IDE Setup

For this project, we recommend using two different Integrated Development Environments (IDEs) to maximize efficiency based on the type of project component.

### Front-end Development
For front-end development, we will use:
- **Visual Studio Code**

### Back-end Development
For back-end development, we will use:
- **JetBrains IntelliJ IDEA**

## Required Environment

To ensure smooth development and deployment, please make sure the following environment configurations are met.

### Front-end Environment
- **Angular** v16.2.12
- **PrimeNG** v16.4.4

### Back-end Environment
- **JDK** v11.0.22, v17.0.9
- **Maven** v3.9.6

## Running the Project

### Running the Front-end

To run the front-end of the project, follow these steps:

1. **Clone the Front-end Repository**  
   Use one of the following commands to clone the repository:
   - SSH:  
     `git clone git@github.com:tranminhnhut9999/recruitment-system-frontend.git`
   - HTTP:  
     `https://github.com/tranminhnhut9999/recruitment-system-frontend.git`

2. **Install Dependencies**  
   Navigate to the project directory and run:  
   `npm install --force`

3. **Start the Front-end Server**  
   Run:  
   `npm run start`  
   Then, access the application in your browser at:  
   `http://localhost:4200`

### Running the Back-end

To run the back-end services, follow these steps:

1. **Clone the Back-end Repository**  
   Use one of the following commands to clone the repository:
   - SSH:  
     `git clone git@github.com:tranminhnhut9999/recruitment-system-backend.git`
   - HTTP:  
     `https://github.com/tranminhnhut9999/recruitment-system-backend.git`

2. **Install Dependencies for Each Service**  
   Navigate to each service directory and run:  
   `mvn clean install`

3. **Run the Back-end Services**  
   Start each service by running its main class in the following order:
   - **Service Discovery**
   - **API Gateway**
   - **Authentication Service**
   - **Email Service**
   - **Configuration Service**
   - **Tracking Application Service**
   - **Job Service**

This ensures that the services are up and running in the correct sequence.

## Deployment with Docker

To deploy the project using Docker, ensure that all services are built and ready for containerization.

1. **Build JAR Files for Microservices**  
   For each service, build the JAR file using the following command:  
   `mvn clean package -P prod`  
   Make sure each service has a JAR file in its `target/` directory.

2. **Deploy Using Docker Compose**  
   Once all JAR files are built, navigate to the root of the back-end project (`recruitment-system-backend/`) and run:  
   `docker compose up --build`

This command will build the Docker images and start the containers.

---

Follow these instructions to ensure a smooth setup and deployment process for the Recruitment System project.
