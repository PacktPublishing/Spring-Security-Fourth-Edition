# chapter18.00-calendar #

Import the JBCP Calendar application certificate inside the JRE keystore of your, by running the following command from the following location `chapter18.00-calendar/src/main/resources/keys`:( In general keystore password `changeit`)
```shell
keytool -importkeystore -srckeystore tomcat.jks -destkeystore $JDK_HOME/jre/lib/security/cacerts -deststoretype JKS
```

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







