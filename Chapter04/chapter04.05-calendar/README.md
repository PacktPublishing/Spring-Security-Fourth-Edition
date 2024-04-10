# chapter04.05-calendar #

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

Try creating another user with the password `user1`. 
Use the H2 console to compare the new user's password and observe that they are different.
![img.png](docs/img2.png)
