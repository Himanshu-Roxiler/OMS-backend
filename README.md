## Prerequisites

Before you begin, ensure you have the following:

- **Git:** If you haven't already, [install Git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git) on your
  system.

- **Docker:** [Install Docker](https://docs.docker.com/get-docker/) on your system. This is required to containerize
  your application.

## Setup

Follow these steps to set up your environment:

1. **Clone the Repository:** Clone this repository to your local machine using the following command:
2. **Create an application.properties File:** Create an application.properties file and place it somewhere on your
   system. You can use any text editor to create this file. The content should be as follows, but make sure to replace
   the placeholders with your actual values:

```
spring.datasource.url=jdbc:postgresql://localhost:5432/oms
spring.datasource.username=postgres
spring.datasource.password=postgres
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type=TRACE
security.jwt.token.secret-key=SECRETKEY
spring.oauth.google.clientId=YOUR_GOOGLE_CLIENT_ID
spring.oauth.outlook.clientId=YOUR_OUTLOOK_CLIENT_ID
spring.mail.host=email-smtp.eu-central-1.amazonaws.com
spring.mail.username=YOUR_MAIL_USERNAME
spring.mail.password=YOUR_MAIL_PASSWORD
spring.mail.properties.mail.transport.protocol=smtp
spring.mail.properties.mail.smtp.port=587
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
spring.mail.properties.mail.smtp.from=no-reply@yourdomain.com
forget-password-url=http://localhost:4200/forget-password
```

3. **Create a Data Directory:** Create a directory on your system where you want to store data for the application. This
   directory will be mounted as a volume for the Docker container.

# Running the application

Use the following commands to start and stop the Docker container for your application:

1. **Start the container:** To start the container, use the following command:

```
sudo VOLUME_LOCATION=/path/to/store/data/directory PROPERTIES_FILE_LOCATION=/path/to/application.properties docker
compose up --build -d
```

Replace /path/to/store/data/directory with the path to the data directory you created earlier, and
/path/to/application.properties with the path to your application.properties file.

2. **Stop the container:** To stop the container, use the following command:

```
sudo VOLUME_LOCATION=/path/to/store/data/directory PROPERTIES_FILE_LOCATION=/path/to/application.properties docker
compose down -v
```

Again, replace /path/to/store/data/directory and /path/to/application.properties with the appropriate paths.

That's it! Your application should now be up and running in a Docker container with the specified configuration.