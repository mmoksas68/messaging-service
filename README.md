# Messaging Service REST API microservice

This is a messaging microservice created by using Spring Boot, MySQL, Docker.

### Requirements
- Docker

### How to Start the Application

- Clone the repository. 
- Type "docker-compose up" to the commend line.

# Endpoints are documented using Swagger-UI

After running the project you can reach the endpoint documentations from the link below also you can try the endpoints from this link.

**http://localhost:8080/swagger-ui.html#/**


Screenshots of documentations can be found below:

![alt text](https://github.com/mmoksas68/messaging-service/blob/master/screenshots/authentication-controller.PNG?raw=true)
<br>
![alt text](https://github.com/mmoksas68/messaging-service/blob/master/screenshots/user-controller.PNG?raw=true)
<br>
![alt text](https://github.com/mmoksas68/messaging-service/blob/master/screenshots/friend-controller.PNG?raw=true)
<br>
![alt text](https://github.com/mmoksas68/messaging-service/blob/master/screenshots/message-controller.PNG?raw=true)
<br>
![alt text](https://github.com/mmoksas68/messaging-service/blob/master/screenshots/action-controller.PNG?raw=true)
<br>
![alt text](https://github.com/mmoksas68/messaging-service/blob/master/screenshots/errors-controller.PNG?raw=true)
<br>

# A messaging process is explained below:
	
	1) Create a user called 'user1'.

	2) Create a second user called 'user2'.
	
	3) Login with user1 credentials.

	4) Send friend request from user1 to user2.

	5) Login with user2 credentials.

	6) Accept friend request of user1.

	7) Send a message to user1.

	8) Login with user1 credentials.

	9) Send a message to user2.

	10) You can keep messaging or access to the former messages as wanted.


# Test results are shown below:

![alt text](https://github.com/mmoksas68/messaging-service/blob/master/screenshots/general-coverage.PNG?raw=true)
<br>

![alt text](https://github.com/mmoksas68/messaging-service/blob/master/screenshots/services-coverage.PNG?raw=true)
<br>

![alt text](https://github.com/mmoksas68/messaging-service/blob/master/screenshots/test-results.PNG?raw=true)
<br>