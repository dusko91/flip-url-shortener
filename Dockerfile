FROM eclipse-temurin:25.0.1_8-jre-noble@sha256:75fede17046e48a8188735f380dc8d71195c135dbb1ed97854a01e419f680aae as builder
WORKDIR extracted
ADD ./build/libs/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

# Base image for Java runtime
FROM eclipse-temurin:25.0.1_8-jre-noble@sha256:75fede17046e48a8188735f380dc8d71195c135dbb1ed97854a01e419f680aae
WORKDIR application
# for regular released dependencies
COPY --from=builder extracted/dependencies/ ./
# for everything under org/springframework/boot/loader
COPY --from=builder extracted/spring-boot-loader/ ./
COPY --from=builder extracted/snapshot-dependencies/ ./
# for application classes and resources
COPY --from=builder extracted/application/ ./
# Make port 8080 available to the outside
EXPOSE 8080
VOLUME /tmp
# Command for running the .jar file
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]