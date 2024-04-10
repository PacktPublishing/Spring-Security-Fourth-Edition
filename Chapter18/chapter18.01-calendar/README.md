# chapter18.01-calendar #

Execute the below command using Gradle from the project directory:

```shell
./gradlew bootRun
```

Alternatively, if you're using Maven, execute the following command from the project directory:

```shell
./mvnw spring-boot:run
```

To test the application, open a web browser and navigate to:
[https://localhost:8443/](https://localhost:8443/)
![img.png](docs/img.png)

Select All Events, which will redirect you to the CAS server. 

You can then log in using the username `casuser` and the password `Mellon`.
![img.png](docs/img_1.png)

Upon successful authentication, you will be redirected back to the JBCP calendar application.
![img.png](docs/img_2.png)




