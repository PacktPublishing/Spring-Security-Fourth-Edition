# chapter17.03-calendar #

Execute the below command using Gradle from the project directory:

```shell
./gradlew bootRun
```

Alternatively, if you're using Maven, execute the following command from the project directory:

```shell
./mvnw spring-boot:run
```

To test the application, open a web browser and navigate to:
[http://localhost:8080](http://localhost:8080)
![img.png](docs/img.png)

At this point, we can start the `chapter17.00-authorization-server` and `chapter17.03-calendar` and we will be ready to send OAuth 2 requests.


```shell
curl -i -X POST \
http://localhost:9000/oauth2/token \
-H 'Content-Type: application/x-www-form-urlencoded' \
-d 'grant_type=client_credentials&client_id=jbcp-calendar&&client_secret=secret&scope=events.read'
```

Now we will take the `access_token` and use that token to initiate additional requests to the server with the following format:

```shell
curl -k -i http://localhost:8080/events/  \
-H "Authorization: Bearer <access_token>"
```

Now that we have our OAuth 2 server ready to issue `access_tokens` for clients, we now can create a microservices client to interact with our system.




