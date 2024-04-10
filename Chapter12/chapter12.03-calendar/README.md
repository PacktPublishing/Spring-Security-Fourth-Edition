# chapter12.03-calendar #

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

With all of these configurations in place, we can start up the site again and test out the custom ACL permission. Based on the sample data we have configured, here is what should happen when the various available users click on categories:

| Username/password       | Birthday party event | Conference call event | Other events |
|-------------------------|----------------------|-----------------------|--------------|
| user2@example.com/user2 | Allowed via READ     | Denied                | Denied       |
| admin1@example.com/admin1| Denied               | Allowed via ADMIN_READ| Denied       |
| user1@example.com/user1 | Denied               | Denied                | Denied       |

