# Spring Boot Backend Setup

If you were unable to generate the backend using the automated script, follow these steps:

1. Go to https://start.spring.io/
2. Fill in the following details:
   - Project: Maven
   - Language: Java
   - Spring Boot: 3.2.0 (or latest stable)
   - Group: com.jobportal
   - Artifact: jobportal-backend
   - Name: JobPortalBackend
   - Description: Job Portal Backend
   - Package name: com.jobportal.backend
   - Packaging: Jar
   - Java: 17 or 21
   - Dependencies: Spring Web, Spring Security, Spring Data JPA, Validation, Mail, Spring Boot DevTools
3. Click "Generate" to download the zip file.
4. Extract the contents into the `backend` folder in your project directory.
5. Open the backend folder in your IDE and run `mvn clean install` to verify the setup.

Once the backend is set up, I can proceed to implement the required features and connect it to the frontend.
