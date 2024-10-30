# BuggySoft User Service 

This is a user storage service for the BuggySoft Job Tracker. 

## Building and Running a Local Instance
In order to build and use our service you must install the following:

1. Maven 3.9.5: https://maven.apache.org/download.cgi Download and follow the installation instructions. Be sure to set the bin as described in Maven's README as a new path variable by editing the system variables if you are on Windows, or by following the instructions for MacOS.
2. JDK 17: This project used JDK 17 for development: https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
3. IntelliJ IDE: We recommend using IntelliJ but you are free to use any other IDE that you are comfortable with: https://www.jetbrains.com/idea/download/?section=windows
4. Clone the project from GitHub and open it in IntelliJ (or whatever IDE).
5. In order to run the project, either use the IntelliJ built in run function, or go to user/target and execute *java -jar user-0.0.1-SNAPSHOT.jar*.

## Endpoints
This section describes the endpoints that our service provides, as well as their inputs and outputs.

#### GET /, /index, /home
* Redirects to the homepage.
*

---

#### POST /register
* Registers a user in the system. If successful, user information will be stored in the database.
*
* @param loginRequest -- A 'LoginRequest' representing an entity with an email, password, first name, last name, and gender.
*
* @return A 'ResponseEntity' object containing either a success message or a failure and an error code.

---

#### POST /login
* Attempts to log in a user and checks if provided credentials are correct.
*
* @param loginRequest -- A 'LoginRequest' representing an entity with an email, password. Other parameters can be blank.
*
* @return A 'ResponseEntity' object containing either a success message or a failure and an error code.

---

#### POST /saveUser
* Saves a new user in the system with the provided details.
*
* @param loginRequest -- A 'LoginRequest' representing an entity with an email, password, first name, last name, and gender.
*
* @return A 'ResponseEntity' object containing either the created 'User' object or an error code.

---

#### PATCH /delete
* Deletes a user based on their email.
*
* @param loginRequest -- A 'LoginRequest' representing an entity with the email of the user to delete.
*
* @return A 'ResponseEntity' object containing either a success message or an error code.

---

#### GET /getAllUsers
* Retrieves all users asynchronously with pagination.
*
* @param page -- The page number for pagination.
*
* @param size -- The number of users per page.
*
* @return A 'ResponseEntity' object with a JSON object containing requestId and callbackUrl to check the status.

---

#### GET /getAllUserSync
* Retrieves all users synchronously with pagination.
*
* @param page -- The page number for pagination.
*
* @param size -- The number of users per page.
*
* @return A 'ResponseEntity' object with a JSON object containing user data and pagination links.

---

#### GET /listUsersStatus/{requestId}
* Retrieves the status of an asynchronous user retrieval request.
*
* @param requestId -- The unique identifier of the request to check its status.
*
* @return A 'ResponseEntity' object with either user data and pagination links, a status message if still processing, or an error message if the request ID is not found.

---

#### GET /getUser
* Retrieves information about a specific user by email.
*
* @param email -- The email address of the user to retrieve.
*
* @return A 'ResponseEntity' object containing either the 'User' object or an error message.

## Local Variables

Certain local variables need to be configured before the service can execute properly. 

1. DB_URL -- the url of the cloud database used to store the codes.
2. DB_USERNAME -- the username of the user account used to access the cloud database.
3. DB_PASSWORD -- the password of the user account used to access the cloud database.