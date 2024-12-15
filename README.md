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

This section describes the endpoints provided by our service, along with their inputs and outputs.

---

### GET `/`, `/index`, `/home`
- **Description:** Redirects to the homepage.
- **Returns:** A JSON object with a welcome message and links to register and login.

---

### POST `/register`
- **Description:** Registers a new user in the system.
- **Request Body:** A `LoginRequest` object containing:
  - `email` (String)
  - `password` (String)
  - `firstname` (String)
  - `lastname` (String)
  - `gender` (String)
- **Returns:** A `ResponseEntity` containing a success or error message.

---

### POST `/login`
- **Description:** Logs in a user by verifying their credentials.
- **Request Body:** A `LoginRequest` object containing:
  - `email` (String)
  - `password` (String)
- **Returns:** A `ResponseEntity` containing user details and a token, or an error message.

---

### POST `/saveUser`
- **Description:** Saves a new user in the system.
- **Request Body:** A `LoginRequest` object containing:
  - `email` (String)
  - `password` (String)
  - `firstname` (String)
  - `lastname` (String)
  - `gender` (String)
- **Returns:** A `ResponseEntity` containing the created user object or an error message.

---

### PATCH `/delete`
- **Description:** Deletes a user based on their email.
- **Request Body:** A `LoginRequest` object containing:
  - `email` (String)
- **Returns:** A `ResponseEntity` containing a success or error message.

---

### GET `/getAllUsers`
- **Description:** Retrieves all users asynchronously with pagination.
- **Query Parameters:**
  - `page` (Integer): The page number for pagination.
  - `size` (Integer): The number of users per page.
- **Returns:** A `ResponseEntity` containing a JSON object with the request ID and callback URL.

---

### GET `/getAllUserSync`
- **Description:** Retrieves all users synchronously with pagination.
- **Query Parameters:**
  - `page` (Integer): The page number for pagination.
  - `size` (Integer): The number of users per page.
- **Returns:** A `ResponseEntity` containing a JSON object with user data and pagination links.

---

### GET `/listUsersStatus/{requestId}`
- **Description:** Retrieves the status of an asynchronous user retrieval request.
- **Path Parameters:**
  - `requestId` (String): The unique identifier of the request.
- **Returns:** A `ResponseEntity` containing user data and pagination links if completed, a processing status message if still processing, or an error message if the request ID is not found.

---

### GET `/getUser`
- **Description:** Retrieves information about a specific user by email.
- **Query Parameters:**
  - `email` (String): The email address of the user to retrieve.
- **Returns:** A `ResponseEntity` containing the user object or an error message.
  

## Local Variables

Certain local variables need to be configured before the service can execute properly. 

1. DB_URL -- the url of the cloud database used to store the codes.
2. DB_USERNAME -- the username of the user account used to access the cloud database.
3. DB_PASSWORD -- the password of the user account used to access the cloud database.
