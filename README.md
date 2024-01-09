# EWA Backend Team 3 systeemdocumentatie
https://gitlab.fdmci.hva.nl/se-ewa/2023-2024-1/peek-3/-/wikis/Systeemdocumentatie

## Deployment Manual
Certainly! Here's a basic guide for deploying a Java Spring Boot application using Render:

---

# Deploying Java Spring Boot Application on Render

This guide will walk you through deploying your Java Spring Boot application on Render, a powerful cloud platform for hosting various applications.

## Prerequisites

- Render Account: Sign up for a Render account at [Render.com](https://render.com) if you haven't already.
- Render CLI: Install the Render CLI by following the instructions provided [here](https://render.com/docs/cli/installation).

## Deployment Steps

### 1. Configure Your Spring Boot Application

Ensure your Spring Boot application is properly configured for deployment. Set the appropriate environment variables, update any necessary configurations, and verify that your application runs without issues locally.

### 2. Create a Render Web Service

1. Log in to your Render Dashboard.
2. Click on **New** and select **Web Service**.
3. Configure the settings:
    - **Service Name**: Choose a unique name for your service.
    - **Environment**: Select Java.
    - **Build Command**: Specify the build command for your Spring Boot app (e.g., `./gradlew build` or `./mvnw package`).
    - **Start Command**: Enter the command to start your application (e.g., `java -jar target/myapp.jar`).
    - **Environment Variables**: Add any necessary environment variables required by your application.

### 3. Add Required Resources

If your Spring Boot app requires a database or additional services:

1. Navigate to the Render Dashboard.
2. Click on **New** and select the required resource (e.g., PostgreSQL, MySQL, MongoDB).
3. Configure the resource and attach it to your service.

### 4. Deployment

1. Ensure your changes are committed and pushed to your repository.
2. Use the Render CLI to link your repository to the Render service:

   ```bash
   render up -n your-service-name
   ```

   Replace `your-service-name` with the name of your Render service.

3. Monitor the deployment process in your Render dashboard or CLI.
4. Once deployed, access your application via the provided Render URL.

### 5. Testing

Test your deployed Spring Boot application thoroughly to ensure it functions correctly on the Render platform.

## Conclusion

Congratulations! Your Java Spring Boot application is now successfully deployed on Render. You can further manage, scale, and monitor your application from the Render dashboard.

For additional details or troubleshooting, refer to Render's documentation or community forums.

---

Feel free to expand upon this guide with specific commands, configurations, or additional steps tailored to your application's requirements and Render's capabilities.

