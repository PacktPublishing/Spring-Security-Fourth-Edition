# Spring Security - Fourth Edition

<a href="https://www.packtpub.com/product/spring-security-fourth-edition/9781835460504"><img src="https://content.packt.com/B21757/cover_image_small.jpg" alt="" height="256px" align="right"></a>

This is the code repository for [Spring Security - Fourth Edition](https://www.packtpub.com/product/spring-security-fourth-edition/9781835460504), published by Packt.

**Effectively secure your web apps, RESTful services, cloud apps, and microservice architectures**

## What is this book about?
Knowing that experienced hackers are constantly on the prowl to attack your apps can make security one of the most challenging concerns of creating an app. The complexity of properly securing an app is compounded when you must also integrate this factor with legacy code, new technologies, and other frameworks. This book will help you easily secure your Java apps with Spring Security, a trusted and highly customizable authentication and access control framework.
	
This book covers the following exciting features:
* Understand common security vulnerabilities and how to resolve them
* Implement authentication and authorization and learn how to map users to roles
* Get to grips with the security challenges of RESTful web services and microservices
* Configure Spring Security to use Spring Data for authentication
* Integrate Spring Security with Spring Boot, Spring Data, and web applications
* Protect against common vulnerabilities like XSS, CSRF, and Clickjacking

If you feel this book is for you, get your [copy](https://www.amazon.com/dp/183546050X) today!

<a href="https://www.packtpub.com/?utm_source=github&utm_medium=banner&utm_campaign=GitHubBanner"><img src="https://raw.githubusercontent.com/PacktPublishing/GitHub/master/GitHub.png" 
alt="https://www.packtpub.com/" border="5" /></a>


## Instructions and Navigations
All of the code is organized into folders. For example, Chapter02.

The code will look like the following:
```
//src/main/java/com/packtpub/springsecurity/configuration/
SecurityConfig.java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http,
       PersistentTokenRepository persistentTokenRepository,
RememberMeServices rememberMeServices) throws Exception {
    http.authorizeHttpRequests( authz -> authz
                .requestMatchers("/webjars/**").permitAll()
…
    // Remember Me
    http.rememberMe(httpSecurityRememberMeConfigurer -> 
httpSecurityRememberMeConfigurer
          .key("jbcpCalendar")
          .rememberMeServices(rememberMeServices)
          .tokenRepository(persistentTokenRepository));
    return http.build();
}
```

**Following is what you need for this book:**

If you’re a Java web developer or an architect with fundamental knowledge of Java 17/21, web services, and the Spring Framework, this book is for you. No previous experience with Spring Security is needed to get started with this book.

With the following software and hardware list you can run all code files present in the book (Chapter 1-19).

## Software and Hardware List

| Chapter  | Software required                                                          | OS required                      |
| -------- | ---------------------------------------------------------------------------| ---------------------------------|
| 1-19     | IntelliJ IDEA and Eclipse are both popular choices forSpring development   | Windows, macOS, or Linux         |
| 1-19     | JDK versions: 17 or 21                                                     | Windows, macOS, or Linux         |
| 1-19     | Spring- Security 6.                                                        | Windows, macOS, or Linux         |
| 1-19     | Spring- Boot 3.                                                            | Windows, macOS, or Linux         |
| 1-19     | Thymeleaf 6.                                                               | Windows, macOS, or Linux         |

## JBCP Calendar Application

* [Pact Publishing Book Home](https://www.packtpub.com/application-development/spring-security-fourth-edition)
* Jim Bob CP Calendar Application for [Spring Security 6](https://docs.spring.io/spring-security/reference/index.html)
* The code tested with [Java 17](https://openjdk.org/projects/jdk/17/) And [Java 21](https://openjdk.java.net/projects/jdk/21/)
* This code supports both [Gradle](http://gradle.org) and [Maven](https://maven.apache.org/)
* [Thymeleaf](https://www.thymeleaf.org/) has been used as the view templating engine

### Chapters

***
1. [Anatomy of an Unsafe Application](Chapter01/README.md)
***
2. [Getting Started with Spring Security](Chapter02/README.md)
***
3. [Custom Authentication](Chapter03/README.md)
***
4. [JDBC-Based Authentication](Chapter04/README.md)
***
5. [Authentication with Spring-Data](Chapter05/README.md)
***
6. [LDAP Directory Services](Chapter06/README.md)
***
7. [Remember-Me Services](Chapter07/README.md)
***
8. [Client Certificate Authentication with TLS](Chapter08/README.md)
***
9. [Opening up to OAuth 2](Chapter09/README.md)
***
10. [SAML 2 Support](Chapter10/README.md)
***
11. [Fine-grained Access Control](Chapter11/README.md)
***
12. [Access Control Lists](Chapter12/README.md)
***
13. [Custom Authorization](Chapter13/README.md)
***
14. [Session Management](Chapter14/README.md)
***
15. [Additional Spring Security Features](Chapter15/README.md)
***
16. [Migration to Spring Security 6](Chapter16/README.md)
***
17. [Microservice Security with OAuth 2 and JSON Web Tokens](Chapter17/README.md)
***
18. [Single Sign-On with the Central Authentication Service](Chapter18/README.md)
***
19. [Build GraalVM native images](Chapter19/README.md)
***

## Related products <Other books you may enjoy>
* Modern API Development with Spring 6 and Spring Boot 3 [[Packt]](https://www.packtpub.com/product/modern-api-development-with-spring-6-and-spring-boot-3-second-edition/9781804613276) [[Amazon]](https://www.amazon.com/dp/1804613274)

* Learning Spring Boot 3.0 [[Packt]](https://www.packtpub.com/product/learning-spring-boot-30-third-edition/9781803233307) [[Amazon]](https://www.amazon.com/dp/1803233303)

## Get to Know the Author
**Badr Nasslahsen**
is a lead security and cloud architect with over 17 years of experience. He holds an executive master&rsquo;s degree from Ecole Centrale Paris and an engineering degree from Telecom SudParis. He is an Oracle Certified Java SE 11 Professional, CISSP, TOGAF, CKA, and Scrum master.
Badr has extensive experience in public cloud providers: AWS, Azure, GCP, Oracle, and IBM. He is also the author of the springdoc-openapi project.
