# chapter17.03-calendar-client #

Execute the below command using Gradle from the project directory:

```shell
./gradlew bootRun
```

Alternatively, if you're using Maven, execute the following command from the project directory:

```shell
./mvnw spring-boot:run
```


We need to ensure that the `chapter17.03-calendar` and `chapter17.00-authorization-server` applications are running and ready to take OAuth 2 requests from clients.

We can then start the `chapter17.03-calendar-client application`.

It will expose a RESTful endpoint that will call our resource server to access the configured events located at `/events` on the remote resource, and will return the following result by running http://localhost:8888/.

```json
[
{
"id":100,
"summary":"Birthday Party",
"description":"This is going to be a great birthday",
"dateWhen":"2023-07-03T18:30:00.000+00:00"
},
{
"id":101,
"summary":"Conference Call",
"description":"Call with the client",
"dateWhen":"2023-12-23T12:00:00.000+00:00"
},
{
"id":102,
"summary":"Vacation",
"description":"Paragliding in Greece",
"dateWhen":"2023-09-14T09:30:00.000+00:00"
}
]
```

