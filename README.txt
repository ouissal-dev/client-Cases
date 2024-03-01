REST API Application
This application provides a REST API for managing client cases and messages. It allows clients to send messages, create cases, and interact with agents.

*** Installation ***

==>Prerequisites
    Java 11 or 17
    Maven

==>Steps
    Clone the repository: git clone <repository-url>
    Navigate to the project directory: cd <project-directory>
    Build the project using Maven: mvn clean package

==> Running the Application
    After building the project, you can run the application using the generated JAR file. Make sure to configure the server port in the application.properties file if needed.

    java -jar target/<jar-file-name>.jar

==>Access the API at:
    http://localhost:8088/

==>API Documentation
   The API documentation is available using Swagger UI. You can access it at:

   http://localhost:8088/swagger-ui/index.html


*** Usage ***
==> Create a Client Message
To create a new client message and generate a unique ID, use the following endpoint:

    URL: /open-issue
    Method: POST
    Request Body:     {
                         "name": "string",
                         "message": "string"
                       }
    Response: The ID of the created message
    
==> Create a Client Case
To create a new client case using the ID generated from the previous API, use the following endpoint:

    URL: /create-case
    Method: POST
    Request Body:
                    {
                      "messageId": "string"
                    }
    Response: The ID of the created case

==>Create an Agent Message
To create a new agent message for a given case, use the following endpoint:

    URL: /answer-agent/{caseId}
    Method: POST
    Request Body:
                    {
                      "name": "string",
                      "message": "string"
                    }
    Response: The ID of the created message

==>Update a Client Case
To update a client case with the last generated agent message ID, use the following endpoint:

    URL: /update-case
    Method: PUT
    Request Body:
                {
                  "messageId": "string"
                }
    Response: A success message
   
==> Retrieve all Client Cases
To retrieve all client cases, use the following endpoint:

    URL: /client-cases
    Method: GET
    Response: A list of all client cases