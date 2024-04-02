# README #

### Build GraalVM native images ###

Not Done


### Sections ###

mvn clean package -Pnative
java -Dspring.aot.enabled=true \
-agentlib:native-image-agent=config-output-dir=/Users/Badr.NassLahsen/Library/CloudStorage/OneDrive-CyberArkLtd/Spring-Security-Fourth-Edition/Chapter19/chapter19.01-calendar/src/main/resources/META-INF/native-image \
-jar target/chapter19.02-calendar-0.0.1-SNAPSHOT.jar


#### 19.00-calendar ####

Build GraalVM native images using Gradle:

```sh
../.gradle bootBuildImage
```


#### 15.01-calendar ####


